function openDB() {
    return new Promise((resolve, reject) => {
        try {
            const request = indexedDB.open("dti-surveyor", 1);

            request.onupgradeneeded = (event) => {
                try {
                    const db = event.target.result;
                    if (!db.objectStoreNames.contains("clients")) {
                        const store = db.createObjectStore("clients", { keyPath: "localId" });
                        store.createIndex("syncStatus", "syncStatus", { unique: false });
                        store.createIndex("dateCreated", "dateCreated", { unique: false });
                    }
                } catch (err) {
                    // Handle upgrade errors
                    console.error("IDB upgrade error:", err);
                }
            };

            request.onsuccess = (event) => {
                resolve(event.target.result);
            };

            request.onerror = (event) => {
                reject(event.target.error || new Error("Unknown IDB open error"));
            };
        } catch (err) {
            reject(err);
        }
    });
}

async function saveClient(formData) {
    try {
        let deviceId = "unknown";
        if (typeof localStorage !== 'undefined') {
            deviceId = localStorage.getItem("deviceId");
            if (!deviceId) {
                deviceId = crypto.randomUUID();
                localStorage.setItem("deviceId", deviceId);
            }
        } else {
            // fallback if localStorage not available (e.g. in service worker context accidentally)
            deviceId = crypto.randomUUID();
        }

        const isUpdate = !!formData.localId;
        const localId = formData.localId || crypto.randomUUID();
        const dateNow = new Date().toISOString();

        const db = await openDB();

        // Preserve dateCreated if this is an update
        let dateCreated = dateNow;
        if (isUpdate) {
            try {
                const existingRecord = await getRecordById(localId);
                if (existingRecord && existingRecord.dateCreated) {
                    dateCreated = existingRecord.dateCreated;
                }
            } catch (err) {
                console.error("Failed to fetch existing record for update:", err);
            }
        }

        const record = {
            ...formData,
            localId,
            serverId: null,
            syncStatus: "pending",
            dateCreated,
            dateUpdated: dateNow,
            deviceId
        };

        return new Promise((resolve, reject) => {
            try {
                const transaction = db.transaction(["clients"], "readwrite");
                const store = transaction.objectStore("clients");
                const request = store.put(record);

                request.onsuccess = () => {
                    resolve(record);
                    if (typeof navigator !== 'undefined' && navigator.onLine) {
                        syncPendingRecords().catch(err => console.error("Auto sync failed:", err));
                    }
                };

                request.onerror = (event) => {
                    reject(event.target.error || new Error("Failed to save client"));
                };
            } catch (err) {
                reject(err);
            }
        });
    } catch (err) {
        return Promise.reject(err);
    }
}

async function syncPendingRecords() {
    let db;
    try {
        db = await openDB();
    } catch (err) {
        console.error("Failed to open DB during sync:", err);
        return;
    }

    return new Promise((resolve, reject) => {
        try {
            const transaction = db.transaction(["clients"], "readonly");
            const store = transaction.objectStore("clients");
            const index = store.index("syncStatus");
            const request = index.getAll("pending");

            request.onsuccess = async (event) => {
                try {
                    const records = event.target.result;
                    if (!records || records.length === 0) {
                        resolve();
                        return;
                    }

                    const syncPromises = records.map(async (record) => {
                        try {
                            const payload = { ...record };

                            const response = await fetch('/api/clients', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                body: JSON.stringify(payload)
                            });

                            if (response.ok) {
                                const result = await response.json();
                                await updateRecord(db, record.localId, {
                                    syncStatus: "synced",
                                    serverId: result.id || result.serverId || null,
                                    dateUpdated: new Date().toISOString()
                                });
                            } else {
                                await updateRecord(db, record.localId, {
                                    syncStatus: "failed",
                                    dateUpdated: new Date().toISOString()
                                });
                            }
                        } catch (error) {
                            try {
                                await updateRecord(db, record.localId, {
                                    syncStatus: "failed",
                                    dateUpdated: new Date().toISOString()
                                });
                            } catch (fallbackError) {
                                console.error("Failed to update record status to failed:", fallbackError);
                            }
                        }
                    });

                    await Promise.all(syncPromises);
                    resolve();
                } catch (err) {
                    console.error("Error during sync iteration:", err);
                    resolve(); // Resolve rather than reject to avoid unhandled promises in SW
                }
            };

            request.onerror = (event) => {
                console.error("Failed to fetch pending records:", event.target.error);
                resolve(); // Resolve to prevent uncaught exceptions
            };
        } catch (err) {
            console.error("Transaction error in syncPendingRecords:", err);
            resolve();
        }
    });
}

async function updateRecord(db, localId, patch) {
    return new Promise((resolve, reject) => {
        try {
            const transaction = db.transaction(["clients"], "readwrite");
            const store = transaction.objectStore("clients");
            const getRequest = store.get(localId);

            getRequest.onsuccess = (event) => {
                try {
                    const record = event.target.result;
                    if (!record) {
                        reject(new Error(`Record with localId ${localId} not found.`));
                        return;
                    }

                    const updatedRecord = { ...record, ...patch };
                    const putRequest = store.put(updatedRecord);

                    putRequest.onsuccess = () => {
                        resolve(updatedRecord);
                    };

                    putRequest.onerror = (e) => {
                        reject(e.target.error || new Error("Failed to put updated record"));
                    };
                } catch (err) {
                    reject(err);
                }
            };

            getRequest.onerror = (event) => {
                reject(event.target.error || new Error("Failed to get record for update"));
            };
        } catch (err) {
            reject(err);
        }
    });
}

async function retryFailed() {
    let db;
    try {
        db = await openDB();
    } catch (err) {
        console.error("Failed to open DB during retryFailed:", err);
        return;
    }

    return new Promise((resolve, reject) => {
        try {
            const transaction = db.transaction(["clients"], "readonly");
            const store = transaction.objectStore("clients");
            const index = store.index("syncStatus");
            const request = index.getAll("failed");

            request.onsuccess = async (event) => {
                try {
                    const records = event.target.result;
                    if (!records || records.length === 0) {
                        resolve();
                        return;
                    }

                    const updatePromises = records.map(async (record) => {
                        try {
                            await updateRecord(db, record.localId, { syncStatus: "pending" });
                        } catch (err) {
                            console.error("Failed to reset record to pending:", err);
                        }
                    });

                    await Promise.all(updatePromises);
                    await syncPendingRecords();
                    resolve();
                } catch (err) {
                    console.error("Error during retryFailed iteration:", err);
                    resolve();
                }
            };

            request.onerror = (event) => {
                console.error("Failed to fetch failed records:", event.target.error);
                resolve();
            };
        } catch (err) {
            console.error("Transaction error in retryFailed:", err);
            resolve();
        }
    });
}

async function getPendingRecords() {
    const db = await openDB();
    return new Promise((resolve, reject) => {
        try {
            const transaction = db.transaction(["clients"], "readonly");
            const store = transaction.objectStore("clients");
            const index = store.index("syncStatus");
            const requestPending = index.getAll("pending");

            requestPending.onsuccess = (eventPending) => {
                const requestFailed = index.getAll("failed");
                requestFailed.onsuccess = (eventFailed) => {
                    const records = [...(eventPending.target.result || []), ...(eventFailed.target.result || [])];
                    resolve(records);
                };
                requestFailed.onerror = (e) => reject(e.target.error);
            };
            requestPending.onerror = (e) => reject(e.target.error);
        } catch (err) {
            reject(err);
        }
    });
}

async function getRecordById(localId) {
    const db = await openDB();
    return new Promise((resolve, reject) => {
        try {
            const transaction = db.transaction(["clients"], "readonly");
            const store = transaction.objectStore("clients");
            const request = store.get(localId);

            request.onsuccess = (event) => {
                resolve(event.target.result);
            };
            request.onerror = (event) => {
                reject(event.target.error);
            }
        } catch (err) {
            reject(err);
        }
    });
}

// Make functions available to the Service Worker importScripts and common environments
if (typeof self !== "undefined") {
    self.openDB = openDB;
    self.saveClient = saveClient;
    self.syncPendingRecords = syncPendingRecords;
    self.updateRecord = updateRecord;
    self.retryFailed = retryFailed;
    self.getPendingRecords = getPendingRecords;
    self.getRecordById = getRecordById;
}
if (typeof exports !== "undefined") {
    exports.openDB = openDB;
    exports.saveClient = saveClient;
    exports.syncPendingRecords = syncPendingRecords;
    exports.updateRecord = updateRecord;
    exports.retryFailed = retryFailed;
    exports.getPendingRecords = getPendingRecords;
    exports.getRecordById = getRecordById;
}

// Register sync triggers
if (typeof window !== "undefined") {
    window.addEventListener("load", () => {
        if (navigator.onLine) {
            syncPendingRecords().catch(err => console.error(err));
        }

        if ("serviceWorker" in navigator) {
            navigator.serviceWorker.register("/service-worker.js")
            .then((registration) => {
                // Ignore errors related to SyncManager not being supported on all browsers
                if ("sync" in registration) {
                    registration.sync.register("sync-clients").catch((err) => {
                        console.error("Background Sync registration failed:", err);
                    });
                }
            })
            .catch((err) => {
                console.error("Service Worker registration failed:", err);
            });
        }
    });

    window.addEventListener("online", () => {
        syncPendingRecords().catch(err => console.error(err));
    });
}
