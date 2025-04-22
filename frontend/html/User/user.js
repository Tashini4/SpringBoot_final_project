$(document).ready(function() {
    // Load users when page loads
    loadUsers();

    // Search functionality
    $('input[type="text"]').on('keyup', function() {
        const searchTerm = $(this).val().toLowerCase();
        $('#userTableBody tr').filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(searchTerm) > -1);
        });
    });
});

function loadUsers() {
    const token = localStorage.getItem('token');

    if (!token) {
        console.error('No token found');
        return;
    }

    $.ajax({
        url: 'http://localhost:8080/api/v1/user/getUsers',
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        success: function(response) {
            if (response.code === 200) {
                populateUserTable(response.data);
            } else {
                console.error('Error loading users:', response.message);
            }
        },
        error: function(xhr, status, error) {
            console.error('AJAX Error:', error);
            if (xhr.status === 401) {
                // Token expired or invalid
                window.location.href = '../../login.html';
            }
        }
    });
}

function populateUserTable(users) {
    const tableBody = $('#userTableBody');
    tableBody.empty();

    if (users.length === 0) {
        tableBody.append('<tr><td colspan="7" class="text-center">No users found</td></tr>');
        return;
    }

    users.forEach((user, index) => {
        const joinDate = new Date(user.joinDate).toLocaleDateString();

        const row = `
            <tr>
                <td>${index + 1}</td>
                <td>
                    <div class="d-flex align-items-center">
                        <div class="ms-3">
                            <p class="fw-bold mb-0">${user.name}</p>
                        </div>
                    </div>
                </td>
                <td>${user.email}</td>
                <td>${user.primary_phone_number || 'N/A'}</td>
                <td>
                    <span class="badge ${getRoleBadgeClass(user.role)}">${user.role}</span>
                </td>
                <td>${joinDate}</td>
                <td class="action-buttons">
                    <button class="btn btn-sm btn-outline-primary me-1" onclick="editUser('${user.email}')">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-danger" onclick="deleteUser('${user.email}')">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            </tr>
        `;
        tableBody.append(row);
    });
}

function getRoleBadgeClass(role) {
    switch (role.toLowerCase()) {
        case 'admin':
            return 'bg-primary';
        case 'user':
            return 'bg-success';
        default:
            return 'bg-secondary';
    }
}

function editUser(email) {
    // Implement edit functionality
    console.log('Edit user:', email);
    // You can redirect to an edit page or show a modal
}

function deleteUser(email) {
    const token = localStorage.getItem('token');

    Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: `http://localhost:8080/api/v1/user/deleteUser?email=${email}`,
                method: 'DELETE',
                headers: {
                    'Authorization': 'Bearer ' + token
                },
                success: function(response) {
                    if (response.code === 200) {
                        Swal.fire(
                            'Deleted!',
                            'User has been deleted.',
                            'success'
                        ).then(() => {
                            loadUsers(); // Refresh the table
                        });
                    } else {
                        Swal.fire(
                            'Error!',
                            response.message || 'Failed to delete user',
                            'error'
                        );
                    }
                },
                error: function(xhr) {
                    Swal.fire(
                        'Error!',
                        xhr.responseJSON?.message || 'Failed to delete user',
                        'error'
                    );
                }
            });
        }
    });
}