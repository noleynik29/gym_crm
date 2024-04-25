var toggle  = document.querySelector("#toggle");
var content = document.querySelector("#content");

toggle.addEventListener("click", function() {
  content.classList.toggle("is-shown");
});