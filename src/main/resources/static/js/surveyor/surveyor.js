document.addEventListener('DOMContentLoaded', ()=> {

        const activeSurveyBtn = document.getElementById('active-survey-button');

        activeSurveyBtn.addEventListener('click', ()=> {
            window.location.href = 'form.html';
        })

        document.getElementById('syncAllBtn').addEventListener('click', function() {
            const btn = this;
            const progressContainer = document.getElementById('progressContainer');
            const progressBar = document.getElementById('syncProgress');
            const progressLabel = document.getElementById('progressLabel');

            btn.disabled = true;
            btn.classList.add('opacity-50', 'cursor-not-allowed');
            progressContainer.classList.remove('hidden');
            
            let progress = 0;
            const interval = setInterval(() => {
                progress += Math.floor(Math.random() * 15) + 5;
                if (progress >= 100) {
                    progress = 100;
                    clearInterval(interval);
                    progressLabel.innerText = "Sync Complete!";
                    progressBar.style.backgroundColor = "#4caf50";
                    setTimeout(() => {
                        alert("Data successfully synchronized with the central server.");
                        location.reload();
                    }, 1000);
                }
                progressBar.style.width = progress + '%';
                if(progress < 100) {
                    progressLabel.innerText = "Syncing: " + progress + "%";
                }
            }, 600);
        });
})