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

        name = link.attr("name");
        $("#yesBtn").attr("href", link.attr("href"));
        $("#confirmText").html("Вы уверены, что хотите удалить список посещений \<strong\>" + name + "\<\/strong\>?");
        $("#confirmModal").modal();
      });
    });