document.getElementById('togglePassword').addEventListener('click', function() {
    const password = document.getElementById('password');
    const icon = this.querySelector('i');
    if (password.type === 'password') {
        password.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        password.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
});

function validateForm() {
    const firstname = document.getElementById('firstName').value.trim();
    const lastname = document.getElementById('lastName').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const termsChecked = document.getElementById('termsCheck').checked;

    // Basic validation
    if (!firstname || !lastname || !email || !password || !confirmPassword) {
        Swal.fire('Error', 'All fields are required!', 'error');
        return false;
    }

    if (password !== confirmPassword) {
        Swal.fire('Error', 'Passwords do not match!', 'error');
        return false;
    }

    if (password.length < 8 || !/\d/.test(password)) {
        Swal.fire('Error', 'Password must be at least 8 characters with at least 1 number', 'error');
        return false;
    }

    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        Swal.fire('Error', 'Please enter a valid email address', 'error');
        return false;
    }

    if (!termsChecked) {
        Swal.fire('Error', 'You must accept the terms and conditions', 'error');
        return false;
    }

    return true;
}

function register() {
    if (!validateForm()) return;

    const firstname = document.getElementById('firstName').value.trim();
    const lastname = document.getElementById('lastName').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;

    $.ajax({
        method: 'POST',
        contentType: 'application/json',
        url: 'http://localhost:8080/api/v1/user/register',
        async: true,
        data: JSON.stringify({
            "email": email,
            "name": `${firstname} ${lastname}`, // Combine first and last name
            "password": password,
            "role": "admin"
        }),
        success: function(response) {
       if(response.code ===  201){
           localStorage.setItem("token", response.data.token);

           Swal.fire({
               icon: 'success',
               title: 'Registration Successful!',
               text: 'Your account has been created successfully.',
               showConfirmButton: true,
               confirmButtonText: 'Continue',
           }).then(() => {
               window.location.href = 'login.html';
           });
       }
        },
        error: function(xhr) {
            let errorMessage = "Registration failed. Please try again.";
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
                if (Array.isArray(errorMessage)) {
                    errorMessage = errorMessage.join("<br>");
                }
            }
            Swal.fire({
                icon: 'error',
                title: 'Registration Failed',
                html: errorMessage,
                showConfirmButton: true
            });
        }
    });
}