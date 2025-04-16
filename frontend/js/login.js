function login() {
    const loginName = document.getElementById('loginName').value;
    const loginPassword = document.getElementById('loginPassword').value;

    console.log(loginName, loginPassword);
    if (loginName === '' || loginPassword === '') {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'All fields are required!',
        });
        return;
    }

    $.ajax({
        url: 'http://localhost:8080/api/v1/auth/authenticate',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            email: loginName,
            password: loginPassword
        }),
        success: function(response) {
            console.log("Login successful");
            localStorage.setItem("token", response.data.token);
            //swal fire
            Swal.fire({
                icon:'success',
                title: 'Login Successful!',
                showConfirmButton: true,
                confirmButtonText: 'Close',
            }).then((result) => {
                if (result.isConfirmed) {
                    if(response.data.role === "admin"){
                        window.location.href = "../../../Springboot_final_project/frontend/html/AdminDashboard/adminDashboard.html";
                    }
                    if(response.data.role === "user"){
                        window.location.href = "../../../Springboot_final_project/frontend/booking.html";
                    }
                }
            });
        },
        error: function(xhr, status, error) {
            let errorMessage = "Login failed. Please try again.";
            if (xhr.status === 401) {
                errorMessage = "Invalid email or password. Please try again.";
            } else if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }

            Swal.fire({
                icon: 'error',
                title: 'Login Failed',
                text: errorMessage,
            });
        }
    });
}