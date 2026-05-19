importScripts('./offlineSync.js');

self.addEventListener('install', (event) => {
    self.skipWaiting();
});

self.addEventListener('activate', (event) => {
    event.waitUntil(clients.claim());
});

self.addEventListener('sync', (event) => {
    if (event.tag === 'sync-clients') {
        event.waitUntil(syncPendingRecords());
    }
});