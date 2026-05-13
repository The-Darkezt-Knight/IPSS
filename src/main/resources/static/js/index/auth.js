document.addEventListener('DOMContentLoaded', ()=> {

    const submitBtn = document.getElementById('submit');

    submitBtn.addEventListener('click', (e)=> {
        const govEmail = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        e.preventDefault();


        fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type' : 'application/json'
        },
        body: JSON.stringify({
            govtEmail : govEmail,
            password : password
        })
        })
        .then(
            res => {
                if(!res.ok) {
                    alert('Authentication error');
                    throw new Error('Authentication error');
                }

                return res.json();
            }
        )
        .then(data => {
            localStorage.setItem('token', data.token);
            localStorage.setItem('role', data.role);
            alert('Authentication successful');

            if(localStorage.getItem('role') === 'ROLE_ADMIN') {
                console.log('admin');
                window.location.href = 'admin.html';
            }

            if(localStorage.getItem('role') === 'ROLE_SUPERADMIN') {
                console.log('super admin');
                window.location.href = '/view/superadmin.html';
            }

            if(localStorage.getItem('role') === 'ROLE_DIRECTOR') {
                console.log('director');
                window.location.href = 'director.html';
            }

            if(localStorage.getItem('role') === 'ROLE_SURVEYOR') {
                console.log('surveyor');
                window.location.href = 'surveyor.html';
            }
        })
        .catch(err => {
            console.log(err);
        })
    })

})