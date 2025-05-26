function toggleUserMenu() {
    const menu = document.getElementById("userMenu");
    if (menu) {
        menu.classList.toggle("show");
    }
}

window.addEventListener("click", function (e) {
    if (!e.target.closest(".user-dropdown")) {
        const menu = document.getElementById("userMenu");
        if (menu) {
            menu.classList.remove("show");
        }
    }
});