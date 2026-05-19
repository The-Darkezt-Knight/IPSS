document.addEventListener('DOMContentLoaded', ()=> {

        const activeSurveyBtn = document.getElementById('active-survey-button');

        activeSurveyBtn.addEventListener('click', ()=> {
            window.location.href = 'form.html';
        })

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