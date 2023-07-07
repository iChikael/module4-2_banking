

function getAllHistory() {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:3300/transfers'
    })
        .done((data) => {
            let tbody = $('#tbHistory tbody');
            tbody.empty();
            let totalFeesAmount = 0;
            $.each(data, (index, item) => {
                let str = renderTransfer(item);
                tbody.prepend(str);
                totalFeesAmount += parseFloat(item.transactionFee);
            });
            let totalRow = renderHistory(totalFeesAmount.toFixed(2));
            tfoothistory.empty();
            tfoothistory.prepend(totalRow);
        })
        .fail((error) => {
            console.error(error);
        })
}

let tbodyhistory = $('#tbHistory tbody');
let tfoothistory = $('#tbHistory tfoot');


function renderTransfer(obj) {
    return `
        <tr>
            <td>${obj.id}</td>
            <td>${obj.senderFullname}</td>
            <td>${obj.recipientFullname}</td>
            <td>${obj.transactionAmount}</td>
            <td>${obj.transactionFee}</td>
        </tr>
    `;
}

function renderHistory(totalFeesAmount) {
    return `
    <tr>
                                <td colspan="3"></td>
                                <td colspan="1">Total Fee:</td>
                                <td colspan="1" th:text="">${totalFeesAmount}</td>
    </tr>
    `
}

$('#btnHistory').on('click', function () {
    getAllHistory();
    $('#mdHistory').modal('show');
});

