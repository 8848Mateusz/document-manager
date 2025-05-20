document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");

    const emailError = document.getElementById("email-error");
    const passwordError = document.getElementById("password-error");

    form.addEventListener("submit", function (e) {
        let valid = true;

        // Reset
        emailError.textContent = "";
        passwordError.textContent = "";
        emailInput.classList.remove("invalid");
        passwordInput.classList.remove("invalid");

        // Email check
        if (!emailInput.value) {
            emailError.textContent = "Email jest wymagany";
            emailInput.classList.add("invalid");
            valid = false;
        } else if (!emailInput.value.includes("@")) {
            emailError.textContent = "Niepoprawny format email";
            emailInput.classList.add("invalid");
            valid = false;
        }

        // Password check
        if (!passwordInput.value) {
            passwordError.textContent = "Hasło jest wymagane";
            passwordInput.classList.add("invalid");
            valid = false;
        }

        if (!valid) {
            e.preventDefault();
        }
    });
});