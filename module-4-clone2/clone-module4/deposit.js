$('#btnDeposit').on('click', () => {
    getCustomerById(customerId).then((data) => {
        let customer = data;
        let currentBalance = customer.balance;
        let transactionAmount = +$('#transactionAmountDep').val();
        let newBalance = currentBalance + transactionAmount;
        customer.balance = newBalance;
         if (isNaN(transactionAmount) || transactionAmount < 0) {
            Swal.fire({
              icon: 'error',
              title: 'Oops...',
              text: 'Transaction amount must be a positive number !!!'
            });
            return;
          }

        $.ajax({
            type: 'PATCH',
            headers: {
                'accept': 'application/json',
                'content-type': 'application/json'
            },
            url: 'http://localhost:3300/customers/' + customer.id,
            data: JSON.stringify(customer)
        })
            .done((data) => {
                let str = renderCustomer(data);
                $('#tr_' + customerId).replaceWith(str);

                addEventEdit();
                addEventDeposit();
                addEventDelete();
                addEventTransfer();
                addEventWithdraw();

                $('#mdDeposit').modal('hide');
                Swal.fire({
                    position: 'top-end',
                    icon: 'success',
                    title: 'Deposit successful',
                    showConfirmButton: false,
                    timer: 1500
                });
            })
            .fail((jqXHR, textStatus, errorThrown) => {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Something went wrong!',
                });
            });

        let transfer = {
            customerId,
            transactionAmount,
        }

        $.ajax({
            type: 'POST',
            headers: {
                'accept': 'application/json',
                'content-type': 'application/json'
            },
            url: 'http://localhost:3300/deposits',
            data: JSON.stringify(deposit)
        });
    })
        .catch((error) => {
            console.log(error);
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Something went wrong!',
            });
        });
});


function addEventDeposit() {
    $('.deposit').on('click', function () {
        customerId = $(this).data('id');
        getCustomerById(customerId).then((data) => {

            if (data !== {}) {
                $('#fullNameDep').val(data.fullName);
                $('#emailDep').val(data.email);
                $('#balanceDep').val(data.balance);

                $('#mdDeposit').modal('show');
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

