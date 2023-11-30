$(document).ready(() => {
    changePageAndSize();
});

changePageAndSize = () => {
    $('#pageSizeSelect').change(evt => {
        window.location.replace(`/?pageSize=${evt.target.value}&page=1`);
    });
}

$(document).ready(function () {
      $(".btn-delete").on("click", function (e) {
        e.preventDefault();
        link = $(this);

        nameSurname = link.attr("nameSurname");
        $("#yesBtn").attr("href", link.attr("href"));
        $("#confirmText").html("Вы уверены, что хотите удалить медицинские данные клиента \<strong\>" + nameSurname + "\<\/strong\>?");
        $("#confirmModal").modal();
      });
    });
