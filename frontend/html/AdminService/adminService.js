function saveService() {

    const token = localStorage.getItem('token');
    const decodedToken = jwt_decode(token);
    const userEmail = decodedToken.email;

    const formData = new FormData();
    const image = $('#serviceImages')[0].files[0];
    if (image) {
        formData.append('file', image);
    }
    const salonDT0 = {
        serviceName: $("#serviceName").val(),
        servicePrice: $("#servicePrice").val(),
        serviceDescription: $("#serviceDescription").val(),
        duration: $("#duration").val()

    }
    console.log(salonDT0)
    console.log("oooooooooooooooooooooooooooooooooooo")
    formData.append('salonDTO', new Blob([JSON.stringify(salonDT0)], {type: 'application/json '}));

    $.ajax({
        url: 'http://localhost:8080/api/v1/service/saveService',
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
                title: 'Service saved successfully!',
                showConfirmButton: false,
                timer: 1500
            }).then(() => {
                window.location.href = 'adminService.html';
                loadServices();
            });
        },

        error: function (xhr, status, error) {
            let errorMsg = error;
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMsg = xhr.responseJSON.message;
            }
            Swal.fire({
                icon: 'error',
                title: 'Error saving service',
                text: errorMsg
            });
        }
    });
}
function loadServices() {
    const token = localStorage.getItem('token');

    $.ajax({
        url: 'http://localhost:8080/api/v1/service/getService',
        method: 'GET',
        headers: {
            Authorization: 'Bearer ' + token
        },
        success: function(response) {
            const tableBody = document.getElementById('servicesTableBody');
            tableBody.innerHTML = '';

            response.forEach(service => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${service.id}</td>
                    <td>${service.serviceName}</td>
                    <td>${service.serviceDescription || 'N/A'}</td>
                    <td>${service.duration} min</td>
                    <td>$${service.servicePrice.toFixed(2)}</td>
                    <td><span class="badge badge-primary">Active</span></td>
                    <td>
                        <button class="btn btn-sm edit-btn action-btn" onclick="editService(${service.id})">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-sm delete-btn action-btn" onclick="confirmDelete(${service.id})">
                            <i class="fas fa-trash-alt"></i>
                        </button>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        },
        error: function(xhr, status, error) {
            console.error('Error loading services:', error);
            Swal.fire({
                icon: 'error',
                title: 'Error loading services',
                text: 'Failed to fetch services. Please try again.'
            });
        }
    });
}
function handleImageUpload(input) {
    const previewContainer = document.getElementById('serviceImage');
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



