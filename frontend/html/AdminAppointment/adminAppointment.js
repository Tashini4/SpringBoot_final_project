// Global variables
let appointments = [];
let stylists = [];
let currentPage = 1;
const itemsPerPage = 10;
let currentWeekStart = moment().startOf('week');
let currentFilters = {};
let refreshInterval = null;

$(document).ready(function() {
    initializeDatePickers();
    loadStylists();
    loadAppointments();
    setupEventListeners();

    // Start auto-refresh every 30 seconds
    startAutoRefresh();

    // Clear interval when leaving the page
    $(window).on('beforeunload', function() {
        clearInterval(refreshInterval);
    });
});

function startAutoRefresh() {
    refreshInterval = setInterval(function() {
        loadAppointments(currentFilters);
    }, 30000);
}

function initializeDatePickers() {
    flatpickr(".date-range", {
        mode: "range",
        dateFormat: "Y-m-d",
        defaultDate: [moment().subtract(7, 'days').format('YYYY-MM-DD'), moment().format('YYYY-MM-DD')]
    });
}

function loadStylists() {
    $.ajax({
        url: 'http://localhost:8080/api/v1/stylists/getStylist',
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem("token")
        },
        beforeSend: function() {
            $('#stylistFilter').prop('disabled', true);
        },
        success: function(response) {
            if (response.code === "00") {
                stylists = response.data;
                const stylistFilter = $('#stylistFilter');
                stylistFilter.empty().append('<option value="">All Stylists</option>');
                stylists.forEach(stylist => {
                    stylistFilter.append(`<option value="${stylist.stylistId}">${stylist.fullName}</option>`);
                });
            }
        },
        error: function(error) {
            console.error('Error loading stylists:', error);
            showError('Failed to load stylists');
        },
        complete: function() {
            $('#stylistFilter').prop('disabled', false);
        }
    });
}

function loadAppointments(filters = {}) {
    currentFilters = filters;
    showLoading(true);

    $.ajax({
        url: 'http://localhost:8080/api/v1/appointment/getAppointment',
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem("token")
        },
        success: function(response) {
            if (response.code === "00") {
                appointments = response.data;
                updateUI();

                // Check for new appointments
                checkForNewAppointments();
            } else {
                showError(response.message || 'Failed to load appointments');
            }
        },
        error: function(error) {
            console.error('Error loading appointments:', error);
            showError('Failed to load appointments. Please try again.');
        },
        complete: function() {
            showLoading(false);
        }
    });
}

function updateUI() {
    updateStatsCards();
    renderAppointmentsTable();
    renderCalendarView();
}

function checkForNewAppointments() {
    if (appointments.length === 0) return;

    const latestAppointment = appointments[appointments.length - 1];
    const appointmentDate = moment(latestAppointment.appointmentDate + ' ' + latestAppointment.appointmentTime);
    const now = moment();

    // If appointment was created within the last minute
    if (now.diff(appointmentDate, 'seconds') < 60) {
        showNewAppointmentNotification(latestAppointment);
    }
}

function showNewAppointmentNotification(appointment) {
    const serviceName = appointment.serviceDTO?.serviceName || 'Service';
    const clientName = appointment.user?.name || 'Client';

    // Using Toastr for notification
    toastr.success(`
        <strong>New Appointment</strong><br>
        ${clientName} - ${serviceName}<br>
        ${appointment.appointmentDate} at ${appointment.appointmentTime}
    `, '', {
        timeOut: 5000,
        extendedTimeOut: 2000,
        closeButton: true,
        progressBar: true,
        positionClass: "toast-bottom-right",
        escapeHtml: false
    });
}

function updateStatsCards() {
    const stats = {
        total: appointments.length,
        confirmed: appointments.filter(a => a.appointmentStatus === 'confirmed').length,
        pending: appointments.filter(a => a.appointmentStatus === 'pending').length,
        cancelled: appointments.filter(a => a.appointmentStatus === 'cancelled').length,
        completed: appointments.filter(a => a.appointmentStatus === 'completed').length
    };

    $('#statsCards').html(`
        <div class="col-md-3">
            <div class="card bg-primary text-white">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="text-uppercase mb-0">Total</h6>
                            <h2 class="mb-0">${stats.total}</h2>
                        </div>
                        <div class="bg-white text-primary rounded-circle p-3">
                            <i class="fas fa-calendar fa-2x"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card bg-success text-white">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="text-uppercase mb-0">Confirmed</h6>
                            <h2 class="mb-0">${stats.confirmed}</h2>
                        </div>
                        <div class="bg-white text-success rounded-circle p-3">
                            <i class="fas fa-check-circle fa-2x"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card bg-warning text-white">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="text-uppercase mb-0">Pending</h6>
                            <h2 class="mb-0">${stats.pending}</h2>
                        </div>
                        <div class="bg-white text-warning rounded-circle p-3">
                            <i class="fas fa-clock fa-2x"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card bg-danger text-white">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="text-uppercase mb-0">Cancelled</h6>
                            <h2 class="mb-0">${stats.cancelled}</h2>
                        </div>
                        <div class="bg-white text-danger rounded-circle p-3">
                            <i class="fas fa-times-circle fa-2x"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `);
}

// [Rest of your existing functions like renderAppointmentsTable, filterAppointments, etc.]
// Make sure they're included in your code as shown in previous examples

function setupEventListeners() {
    // View toggle
    $('#listViewBtn').click(function() {
        $(this).addClass('active');
        $('#calendarViewBtn').removeClass('active');
        $('#listView').removeClass('d-none');
        $('#calendarView').addClass('d-none');
    });

    $('#calendarViewBtn').click(function() {
        $(this).addClass('active');
        $('#listViewBtn').removeClass('active');
        $('#calendarView').removeClass('d-none');
        $('#listView').addClass('d-none');
    });

    // Pagination
    $(document).on('click', '.page-link', function(e) {
        e.preventDefault();
        const page = $(this).data('page');
        if (page) {
            renderAppointmentsTable(page);
        }
    });

    // Calendar navigation
    $('#prevWeek').click(function() {
        currentWeekStart.subtract(7, 'days');
        renderCalendarView();
    });

    $('#nextWeek').click(function() {
        currentWeekStart.add(7, 'days');
        renderCalendarView();
    });

    $('#todayBtn').click(function() {
        currentWeekStart = moment().startOf('week');
        renderCalendarView();
    });

    // Apply filters
    $('#applyFilters').click(function() {
        const status = $('#statusFilter').val();
        const dateRange = $('#dateRangeFilter').val();
        const stylistId = $('#stylistFilter').val();

        const filters = {};
        if (status) filters.status = status;
        if (stylistId) filters.stylistId = stylistId;

        if (dateRange) {
            const dates = dateRange.split(' to ');
            filters.startDate = moment(dates[0]).startOf('day');
            filters.endDate = moment(dates[1] || dates[0]).endOf('day');
        }

        loadAppointments(filters);
    });

    // Search
    $('#searchInput').on('input', function() {
        currentFilters.search = $(this).val();
        renderAppointmentsTable(1);
    });

    // Manual refresh
    $('#refreshBtn').click(function() {
        $(this).html('<i class="fas fa-sync-alt fa-spin"></i>');
        loadAppointments(currentFilters);
        setTimeout(() => {
            $(this).html('<i class="fas fa-sync-alt"></i> Refresh');
        }, 1000);
    });

    // View appointment details
    $(document).on('click', '.view-appointment, .appointment-event', function() {
        const appointmentId = $(this).data('id');
        showAppointmentDetails(appointmentId);
    });

    // Edit appointment
    $(document).on('click', '.edit-appointment', function() {
        const appointmentId = $(this).data('id');
        editAppointment(appointmentId);
    });

    // Save changes
    $('#saveChangesBtn').click(function() {
        saveAppointmentChanges();
    });
}

function showLoading(show) {
    if (show) {
        $('#appointmentTableBody').html(`
            <tr>
                <td colspan="9" class="text-center py-4">
                    <i class="fas fa-spinner fa-spin fa-2x"></i>
                    <p class="mt-2">Loading appointments...</p>
                </td>
            </tr>
        `);
    }
}

function showError(message) {
    toastr.error(message, 'Error', {
        timeOut: 5000,
        positionClass: "toast-top-right"
    });
}