const toggler = document.querySelector(".user_icon_trigger");

window.addEventListener("click", event => {
    if (event.target.classList.contains("user_icon_trigger")) {
        document.body.classList.toggle("show-nav");
    } else {
        document.body.classList.remove("show-nav");
    }
});


