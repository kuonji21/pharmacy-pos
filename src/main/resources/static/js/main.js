/**
 * Main JavaScript file for the Pharmacy POS application
 */
document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Initialize popovers
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });

    // Flash messages auto-hide
    setTimeout(function() {
        $('.alert-dismissible').fadeOut('slow');
    }, 5000);

    // Current time display
    updateClock();
    setInterval(updateClock, 1000);

    // Initialize datepickers
    initializeDatepickers();

    // Initialize datatables
    initializeDataTables();

    // Initialize product search autocomplete
    initializeAutocomplete();

    // Handle dynamic form elements
    setupDynamicForms();

    // Handle sales form calculations
    setupSalesCalculations();
});

/**
 * Updates the clock display with the current time
 */
function updateClock() {
    const clockElement = document.getElementById('liveClock');
    if (clockElement) {
        const now = new Date();
        const hours = now.getHours().toString().padStart(2, '0');
        const minutes = now.getMinutes().toString().padStart(2, '0');
        const seconds = now.getSeconds().toString().padStart(2, '0');
        clockElement.textContent = `${hours}:${minutes}:${seconds}`;
    }
}

/**
 * Initializes date picker fields
 */
function initializeDatepickers() {
    $('.datepicker').datepicker({
        format: 'yyyy-mm-dd',
        autoclose: true,
        todayHighlight: true,
        clearBtn: true
    });
}

/**
 * Initializes DataTables for enhanced table functionality
 */
function initializeDataTables() {
    $('.datatable').DataTable({
        responsive: true,
        language: {
            search: "_INPUT_",
            searchPlaceholder: "Search...",
            lengthMenu: "Show _MENU_ entries",
            info: "Showing _START_ to _END_ of _TOTAL_ entries",
            infoEmpty: "Showing 0 to 0 of 0 entries",
            infoFiltered: "(filtered from _MAX_ total entries)"
        },
        dom: '<"top"lf>rt<"bottom"ip><"clear">',
        lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]]
    });
}

/**
 * Initializes autocomplete for product search
 */
function initializeAutocomplete() {
    $('.product-search').autocomplete({
        source: function(request, response) {
            $.ajax({
                url: "/products/search",
                dataType: "json",
                data: {
                    term: request.term
                },
                success: function(data) {
                    response(data);
                }
            });
        },
        minLength: 2,
        select: function(event, ui) {
            // Auto-fill product details when selected
            if (ui.item) {
                const productId = ui.item.id;
                fetchProductDetails(productId);
            }
        }
    });
}

/**
 * Fetches product details by ID
 * @param {number} productId - The product ID to fetch details for
 */
function fetchProductDetails(productId) {
    $.ajax({
        url: `/products/${productId}/details`,
        method: 'GET',
        dataType: 'json',
        success: function(data) {
            if (data) {
                $('#productCode').val(data.productCode);
                $('#productName').val(data.productName);
                $('#price').val(data.price);
                $('#availableQty').text(data.qty);
                $('#qtyInput').attr('max', data.qty);
            }
        },
        error: function(xhr, status, error) {
            console.error("Error fetching product details:", error);
        }
    });
}

/**
 * Sets up dynamic form handling for adding multiple items
 */
function setupDynamicForms() {
    // Add new item row in forms
    $('#addItemRow').on('click', function(e) {
        e.preventDefault();
        const template = $('#itemRowTemplate').html();
        $('#itemsContainer').append(template);
        
        // Reinitialize components for the new row
        initializeDatepickers();
        initializeAutocomplete();
        
        // Update row numbers
        updateRowNumbers();
    });
    
    // Remove item row
    $(document).on('click', '.remove-row', function(e) {
        e.preventDefault();
        $(this).closest('.item-row').remove();
        updateRowNumbers();
        updateTotals();
    });
    
    function updateRowNumbers() {
        $('.item-row').each(function(index) {
            $(this).find('.row-number').text(index + 1);
        });
    }
}

/**
 * Sets up calculation functionality for sales forms
 */
function setupSalesCalculations() {
    // Calculate line totals when quantity or price changes
    $(document).on('change', '.qty-input, .price-input, .discount-input', function() {
        updateLineTotals($(this).closest('.item-row'));
        updateTotals();
    });
    
    // Generate random invoice number
    $('#generateInvoice').on('click', function(e) {
        e.preventDefault();
        const invoicePrefix = 'INV';
        const randomNum = Math.floor(100000 + Math.random() * 900000);
        const date = new Date().toISOString().slice(0, 10).replace(/-/g, '');
        $('#invoiceNumber').val(`${invoicePrefix}-${date}-${randomNum}`);
    });
}

/**
 * Updates totals for a specific row in a form
 * @param {Object} row - The row jQuery element to update totals for
 */
function updateLineTotals(row) {
    const qty = parseFloat(row.find('.qty-input').val()) || 0;
    const price = parseFloat(row.find('.price-input').val()) || 0;
    const discount = parseFloat(row.find('.discount-input').val()) || 0;
    
    const subtotal = qty * price;
    const discountAmount = (subtotal * discount) / 100;
    const total = subtotal - discountAmount;
    
    row.find('.subtotal').text(subtotal.toFixed(2));
    row.find('.total').text(total.toFixed(2));
    row.find('.total-input').val(total.toFixed(2));
}

/**
 * Updates the grand total for all items in a form
 */
function updateTotals() {
    let grandTotal = 0;
    let totalItems = 0;
    
    $('.item-row').each(function() {
        const qty = parseFloat($(this).find('.qty-input').val()) || 0;
        const total = parseFloat($(this).find('.total-input').val()) || 0;
        
        totalItems += qty;
        grandTotal += total;
    });
    
    $('#totalItems').text(totalItems);
    $('#grandTotal').text(grandTotal.toFixed(2));
    $('#grandTotalInput').val(grandTotal.toFixed(2));
}

/**
 * Format currency values
 * @param {number} value - The numeric value to format as currency
 * @return {string} The formatted currency string
 */
function formatCurrency(value) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
        minimumFractionDigits: 2
    }).format(value);
}

/**
 * Format date values to localized format
 * @param {string} dateString - The date string to format
 * @return {string} The formatted date string
 */
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString();
}

/**
 * Confirms an action with a dialog
 * @param {string} message - The confirmation message to display
 * @param {function} callback - The function to call if confirmed
 */
function confirmAction(message, callback) {
    if (confirm(message)) {
        callback();
    }
}

/**
 * Show a notification message
 * @param {string} message - The message to display
 * @param {string} type - The type of message (success, error, warning, info)
 */
function showNotification(message, type = 'info') {
    const alertClass = `alert-${type}`;
    const alertHtml = `
        <div class="alert ${alertClass} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    `;
    
    const alertContainer = $('#alertContainer');
    if (alertContainer.length) {
        alertContainer.append(alertHtml);
    } else {
        $('body').prepend(`<div id="alertContainer" class="alert-container">${alertHtml}</div>`);
    }
    
    // Auto-dismiss after 5 seconds
    setTimeout(function() {
        $('.alert').alert('close');
    }, 5000);
}