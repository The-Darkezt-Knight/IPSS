document.addEventListener('DOMContentLoaded', async ()=> {

        const activeSurveyBtn = document.getElementById('active-survey-button');

        activeSurveyBtn.addEventListener('click', ()=> {
            localStorage.removeItem('editRecordId');
            window.location.href = 'form.html';
        })

        await loadPendingRecords();

        async function loadPendingRecords() {
            if (typeof getPendingRecords !== 'function') return;

            try {
                const records = await getPendingRecords();
                const tbody = document.querySelector('table tbody');
                if (!tbody) return;

                tbody.innerHTML = '';

                if (records.length === 0) {
                    tbody.innerHTML = `<tr><td colspan="5" class="px-lg py-md text-center text-on-surface-variant">No pending records found.</td></tr>`;
                    return;
                }

                records.forEach(record => {
                    const statusColor = record.syncStatus === 'failed' ? 'text-error' : 'text-secondary-fixed-dim';
                    const statusIcon = record.syncStatus === 'failed' ? '<span class="material-symbols-outlined text-[16px]" data-icon="error_outline">error_outline</span> Failed' : '<span class="w-2 h-2 rounded-full bg-secondary"></span> Queued';

                    const clientName = `${record.firstName || ''} ${record.lastName || ''}`.trim() || 'Unknown Client';
                    const dateCreated = record.dateCreated ? new Date(record.dateCreated).toLocaleString() : 'N/A';
                    const recordType = 'Registration'; // Default, update if there are multiple types

                    const tr = document.createElement('tr');
                    tr.innerHTML = `
                        <td class="px-lg py-md font-bold text-primary">${clientName}</td>
                        <td class="px-lg py-md">${dateCreated}</td>
                        <td class="px-lg py-md">${recordType}</td>
                        <td class="px-lg py-md">
                            <span class="flex items-center gap-xs ${statusColor} font-bold">
                                ${statusIcon}
                            </span>
                        </td>
                        <td class="px-lg py-md text-right flex justify-end gap-sm">
                            <button class="text-primary hover:underline font-bold sync-single-btn" data-id="${record.localId}">Sync Now</button>
                            <button class="text-on-surface-variant hover:text-primary edit-record-btn" data-id="${record.localId}"><span class="material-symbols-outlined text-[20px]" data-icon="edit">edit</span></button>
                        </td>
                    `;
                    tbody.appendChild(tr);
                });

                // Attach event listeners to new buttons
                document.querySelectorAll('.edit-record-btn').forEach(btn => {
                    btn.addEventListener('click', (e) => {
                        const localId = e.currentTarget.getAttribute('data-id');
                        localStorage.setItem('editRecordId', localId);
                        window.location.href = 'form.html';
                    });
                });

                document.querySelectorAll('.sync-single-btn').forEach(btn => {
                    btn.addEventListener('click', async (e) => {
                        if (typeof syncPendingRecords === 'function') {
                            const originalText = e.currentTarget.innerText;
                            e.currentTarget.innerText = "Syncing...";
                            e.currentTarget.disabled = true;
                            try {
                                await syncPendingRecords();
                                await loadPendingRecords();
                            } catch (err) {
                                console.error('Error during individual sync:', err);
                            } finally {
                                if (e.currentTarget) {
                                    e.currentTarget.innerText = originalText;
                                    e.currentTarget.disabled = false;
                                }
                            }
                        }
                    });
                });

            } catch (err) {
                console.error("Failed to load pending records:", err);
            }
        }

        const syncAllBtn = document.getElementById('syncAllBtn');
        if (syncAllBtn) {
            syncAllBtn.addEventListener('click', async function() {
                const btn = this;
                const progressContainer = document.getElementById('progressContainer');
                const progressBar = document.getElementById('syncProgress');
                const progressLabel = document.getElementById('progressLabel');

                btn.disabled = true;
                btn.classList.add('opacity-50', 'cursor-not-allowed');
                progressContainer.classList.remove('hidden');

                progressBar.style.width = '50%';
                progressLabel.innerText = "Syncing...";

                if (typeof syncPendingRecords === 'function') {
                    try {
                        await syncPendingRecords();
                    } catch (err) {
                        console.error('Error during sync:', err);
                    }
                }

                progressLabel.innerText = "Sync Complete!";
                progressBar.style.backgroundColor = "#4caf50";
                progressBar.style.width = '100%';
                setTimeout(() => {
                    alert("Data successfully synchronized with the central server.");
                    location.reload();
                }, 1000);
            });
        }
})