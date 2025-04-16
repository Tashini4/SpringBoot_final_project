function saveStylist() {
    const token = localStorage.getItem('token');
    const decodedToken = jwt_decode(token);
    const userEmail = decodedToken.email;

    const formData = new FormData();
    const image = $('#stylistImage')[0].files[0];
    if (image) {
        formData.append('file', image);
    }
    const stylistDT0 = {
        fullName: $("#fullName").val(),
        email: $("#email").val(),
        phoneNumber: $("#phoneNumber").val(),
        specialization: $("#specialization").val(),
        availability: $("#availability").val()

    }
    console.log(stylistDT0)
    console.log("oooooooooooooooooooooooooooooooooooo")
    formData.append('stylistDTO', new Blob([JSON.stringify(stylistDT0)], {type: 'application/json '}));

    $.ajax({
        url: 'http://localhost:8080/api/v1/stylists/saveStylist',
        method: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        headers: {
            Authorization: 'Bearer ' + token
        },
        success: function (response) {
            console.log("tttttttttttttttttttttttttttttttttttttttt")
            Swal.fire({
                icon: 'success',
                title: 'Stylist saved successfully!',
                showConfirmButton: false,
                timer: 1500
            }).then(() => {
                window.location.href = 'stylist.html';
            });
        },
        error: function (xhr, status, error) {
            let errorMsg = error;
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMsg = xhr.responseJSON.message;
            }
            Swal.fire({
                icon: 'error',
                title: 'Error saving stylist',
                text: errorMsg
            });
        }
    });
}
function handleImageUpload(input) {
    const previewContainer = document.getElementById('stylistImage');
    previewContainer.innerHTML = '';

    if (input.files && input.files.length > 0) {
        for (let i = 0; i < input.files.length; i++) {
            const file = input.files[i];
            const reader = new FileReader();

            reader.onload = function(e) {
                const previewDiv = document.createElement('div');
                const img = document.createElement('img');
                img.src = e.target.result;
                img.className = 'image-preview';

                const removeBtn = document.createElement('span');
                removeBtn.className = 'remove-image';
                removeBtn.innerHTML = '&times;';
                removeBtn.onclick = function() {
                    previewDiv.remove();
                };

                img.appendChild(removeBtn);
                previewDiv.appendChild(img);
                previewContainer.appendChild(previewDiv);
            }

            reader.readAsDataURL(file);
        }
    }
}



