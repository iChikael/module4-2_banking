function addEventDelete() {
    $('.delete').on('click', function () {
        let customerId = $(this).data('id');
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
                    type: 'DELETE',
                    url: 'http://localhost:3300/customers/' + customerId,

                    // type: 'PATCH',
                    // url: 'http://localhost:3300/customers/' + customerId,
                    // data: {
                    //     delete: true
                    // }
                })
                    .done(() => {
                        $('#tr_' + customerId).remove();
                        Swal.fire({
                            position: 'top-end',
                            icon: 'success',
                            title: 'Customer has been deleted',
                            showConfirmButton: false,
                            timer: 1500
                        })
                    })
                    .fail(() => {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: 'Something went wrong!',
                        })
                    });
            }
        })
    })
}