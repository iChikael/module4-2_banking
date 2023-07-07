function addEventTransfer() {
    $('.transfer').on('click', function () {
        customerId = $(this).data('id');
        getCustomers(customerId);
        getCustomerById(customerId).then((data) => {
            console.log(data);
            if (data !== {}) {
                $('#fullNameSent').val(data.fullName);
                $('#balanceSent').val(data.balance);
                $('#transactionAmountSent').val('');
                $('#transactionAmountTotal').val('');
                $('#transactionAmountFee').val('');
                $('#mdTransfer').modal('show');
            }
            else {
                alert('Customer not found');
            }
        })
            .catch((error) => {
                console.log(error);
            });
    })
}


$(document).ready(function () {
    getCustomers();
});


$(document).ready(function () {
    var transactionAmountSent = $('#transactionAmountSent');
    var transactionAmountTotal = $('#transactionAmountTotal');
    var transactionAmountFee = $('#transactionAmountFee');

    transactionAmountSent.on('input', function () {
        var amountSent = parseFloat(transactionAmountSent.val());
        var fee = amountSent * 0.1;
        var total = amountSent + fee;

        transactionAmountFee.val(fee);
        transactionAmountTotal.val(total);
    });
});

function getCustomers(customerId) {
    $.ajax({
        url: 'http://localhost:3300/customers',
        type: 'GET',
        dataType: 'json',
        success: function (customers) {
            var recipientSelect = $('#recipientSelect');
            recipientSelect.empty();
            recipientSelect.append($('<option>').val('').text('Select recipient'));
            
            $.each(customers, function (index, customer) {
                if (customer.id !== customerId && customer.deleted === false) {
                    recipientSelect.append($('<option>').val(customer.id).text(customer.id + '-' + customer.fullName));
                }
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log('Error: ' + textStatus + ' ' + errorThrown);
        }
    });
}



$('#btnTransfer').on('click', () => {
    let recipientId = $('#recipientSelect').val();
    if (!recipientId) {
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Please select a recipient !!!'
          });
        return;
    }
    let balanceSent = +$('#balanceSent').val();
    let transactionAmountSent = +$('#transactionAmountSent').val();
    let transactionFee = transactionAmountSent * 0.1;
    let transactionTotal = transactionAmountSent + transactionFee;
    if (!transactionAmountSent || isNaN(transactionAmountSent) || transactionAmountSent <= 0) {
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: 'Transaction amount must be a positive number !!!'
        });
        return;
      }
    getCustomerById(recipientId)
        .then((recipient) => {
            let balanceAfterTransfer = balanceSent - transactionTotal;
            if (balanceAfterTransfer < 0) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Not enough amount to transfer !!!'
                  });
                  return;
            }
            getCustomerById(customerId)
                .then((sender) => {
                    let senderData = {
                        id: sender.id,
                        fullName: sender.fullName,
                        phone: sender.phone,
                        email: sender.email,
                        address: sender.address,
                        balance: balanceAfterTransfer
                    };
                    let recipientData = {
                        id: recipient.id,
                        fullName: recipient.fullName,
                        phone: recipient.phone,
                        email: recipient.email,
                        address: recipient.address,
                        balance: recipient.balance + transactionAmountSent
                    };
                    return Promise.all([
                        $.ajax({
                            type: 'POST',
                            headers: {
                                'accept': 'application/json',
                                'content-type': 'application/json'
                            },
                            url: 'http://localhost:3300/transfers',
                            data: JSON.stringify({
                                senderId: senderData.id,
                                senderFullname: senderData.fullName,
                                transactionAmount: transactionAmountSent,
                                recipientId: recipientId,
                                transactionFee: transactionFee,
                                recipientFullname: recipientData.fullName
                            })
                        }),
                        $.ajax({
                            type: 'PATCH',
                            headers: {
                                'accept': 'application/json',
                                'content-type': 'application/json'
                            },
                            url: 'http://localhost:3300/customers/' + senderData.id,
                            data: JSON.stringify(senderData)
                        }),
                        $.ajax({
                            type: 'PATCH',
                            headers: {
                                'accept': 'application/json',
                                'content-type': 'application/json'
                            },
                            url: 'http://localhost:3300/customers/' + recipientData.id,
                            data: JSON.stringify(recipientData)
                        })
                    ])
                        .then(([transferRes, senderRes, recipientRes]) => {
                            let senderStr = renderCustomer(senderData);
                            let recipientStr = renderCustomer(recipientData);
                            $('#tr_' + senderData.id).replaceWith(senderStr);
                            $('#tr_' + recipientData.id).replaceWith(recipientStr);

                            addEventEdit();
                            addEventDeposit();
                            addEventDelete();
                            addEventTransfer();
                            addEventWithdraw();

                            $('#mdTransfer').modal('hide');
                            Swal.fire({
                                position: 'top-end',
                                icon: 'success',
                                title: 'Transfer successful',
                                showConfirmButton: false,
                                timer: 1500
                            });
                        });
                })
        })
        .catch((error) => {
            console.log(error);
        });
});

