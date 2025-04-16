// Base API URL
const API_BASE_URL = '/api/v1/user';

// Get authorization header with token
function getAuthHeader() {
    const token = localStorage.getItem('authToken');
    return {
        'Authorization': 'Bearer ' + token,
        'Content-Type': 'application/json'
    };
}

// Save/Register User
function saveUser(userData) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: API_BASE_URL + '/register',
            type: 'POST',
            headers: getAuthHeader(),
            data: JSON.stringify(userData),
            success: function(response) {
                resolve(response);
            },
            error: function(xhr) {
                reject(xhr.responseJSON || { message: 'An error occurred' });
            }
        });
    });
}

// Update User
function updateUser(userData) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: API_BASE_URL + '/update',
            type: 'PUT',
            headers: getAuthHeader(),
            data: JSON.stringify(userData),
            success: function(response) {
                resolve(response);
            },
            error: function(xhr) {
                reject(xhr.responseJSON || { message: 'An error occurred' });
            }
        });
    });
}

// Delete User
function deleteUser(email) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: API_BASE_URL + '/delete?email=' + encodeURIComponent(email),
            type: 'DELETE',
            headers: getAuthHeader(),
            success: function(response) {
                resolve(response);
            },
            error: function(xhr) {
                reject(xhr.responseJSON || { message: 'An error occurred' });
            }
        });
    });
}

// Get All Users
function getAllUsers() {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: API_BASE_URL + '/all',
            type: 'GET',
            headers: getAuthHeader(),
            success: function(response) {
                resolve(response);
            },
            error: function(xhr) {
                reject(xhr.responseJSON || { message: 'An error occurred' });
            }
        });
    });
}

// Get User by Email
function getUserByEmail(email) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: API_BASE_URL + '/getUser?email=' + encodeURIComponent(email),
            type: 'GET',
            headers: getAuthHeader(),
            success: function(response) {
                resolve(response);
            },
            error: function(xhr) {
                reject(xhr.responseJSON || { message: 'An error occurred' });
            }
        });
    });
}

// Populate users table
function populateUsersTable(users) {
    const tbody = $('#usersTable tbody');
    tbody.empty();

    users.forEach(user => {
        // Format join date
        const joinDate = new Date(user.joinDate).toISOString().split('T')[0];

        // Determine badge color based on role
        let badgeClass = 'bg-secondary';
        if (user.role === 'Admin') badgeClass = 'bg-success';
        else if (user.role === 'Stylist') badgeClass = 'bg-info';
        else if (user.role === 'Receptionist') badgeClass = 'bg-warning text-dark';

        const row = `
            <tr data-email="${user.email}">
                <td>${user.uid.substring(0, 8)}</td>
                <td>
                    <img src="https://ui-avatars.com/api/?name=${encodeURIComponent(user.name)}&background=random" 
                         class="user-avatar me-2">
                    ${user.name}
                </td>
                <td>${user.email}</td>
                <td>${user.primary_phone_number || 'N/A'}</td>
                <td><span class="badge ${badgeClass}">${user.role}</span></td>
                <td>${joinDate}</td>
                <td class="action-buttons">
                    <button class="btn btn-sm btn-outline-primary edit-user" data-email="${user.email}">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-danger delete-user" data-email="${user.email}">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            </tr>
        `;
        tbody.append(row);
    });
}

// Populate edit form
function populateEditForm(user) {
    $('#editEmail').val(user.email);
    $('#editName').val(user.name);
    $('#editPhone').val(user.primary_phone_number);
    $('#editRole').val(user.role);
    // Store original email for reference
    $('#editUserForm').data('original-email', user.email);
}

// Initialize the page
$(document).ready(function() {
    // Load all users on page load
    loadUsers();

    // Search functionality
    $('#searchInput').on('input', function() {
        const searchTerm = $(this).val().toLowerCase();
        $('#usersTable tbody tr').each(function() {
            const rowText = $(this).text().toLowerCase();
            $(this).toggle(rowText.includes(searchTerm));
        });
    });

    // Add user form submission
    $('#saveUserBtn').click(async function() {
        const userData = {
            email: $('#addEmail').val(),
            name: $('#addName').val(),
            password: $('#addPassword').val(),
            role: $('#addRole').val(),
            primary_phone_number: $('#addPhone').val(),
            joinDate: $('#addJoinDate').val() || new Date().toISOString().split('T')[0]
        };

        try {
            const response = await saveUser(userData);
            showAlert('User added successfully!', 'success');
            $('#addUserModal').modal('hide');
            $('#addUserForm')[0].reset();
            loadUsers();
        } catch (error) {
            showAlert('Error adding user: ' + error.message, 'danger');
        }
    });

    // Edit user button click
    $(document).on('click', '.edit-user', async function() {
        const email = $(this).data('email');
        try {
            const response = await getUserByEmail(email);
            populateEditForm(response.data);
            $('#editUserModal').modal('show');
        } catch (error) {
            showAlert('Error loading user: ' + error.message, 'danger');
        }
    });

    // Update user form submission
    $('#editUserForm').submit(async function(e) {
        e.preventDefault();

        const userData = {
            email: $('#editEmail').val(),
            name: $('#editName').val(),
            primary_phone_number: $('#editPhone').val(),
            role: $('#editRole').val(),
            // Include original email for backend reference
            originalEmail: $(this).data('original-email')
        };

        try {
            const response = await updateUser(userData);
            showAlert('User updated successfully!', 'success');
            $('#editUserModal').modal('hide');
            loadUsers();
        } catch (error) {
            showAlert('Error updating user: ' + error.message, 'danger');
        }
    });

    // Delete user button click
    $(document).on('click', '.delete-user', function() {
        const email = $(this).data('email');
        if (confirm('Are you sure you want to delete this user?')) {
            deleteUser(email)
                .then(() => {
                    showAlert('User deleted successfully', 'success');
                    loadUsers();
                })
                .catch(error => {
                    showAlert('Error deleting user: ' + error.message, 'danger');
                });
        }
    });
});

// Load users from server
function loadUsers() {
    getAllUsers()
        .then(response => {
            populateUsersTable(response.data);
        })
        .catch(error => {
            showAlert('Error loading users: ' + error.message, 'danger');
        });
}

// Show alert message
function showAlert(message, type) {
    const alert = $(`
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    `);
    $('#alertsContainer').append(alert);
    setTimeout(() => alert.alert('close'), 5000);
}
