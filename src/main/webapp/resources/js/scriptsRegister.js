document.addEventListener('DOMContentLoaded', function () {
    const password = document.getElementById('password');
    const confirm = document.getElementById('confirmPassword');
    const error = document.getElementById('passwordError');

    function validatePasswords() {
        if (confirm.value && confirm.value !== password.value) {
            confirm.setCustomValidity("Hasła nie są zgodne");
            error.style.display = 'block';
        } else {
            confirm.setCustomValidity("");
            error.style.display = 'none';
        }
    }

    // Sprawdź tylko po opuszczeniu pola confirm
    confirm.addEventListener('blur', validatePasswords);
    password.addEventListener('blur', () => {
        // Jeśli użytkownik wróci do hasła i coś zmieni, sprawdź ponownie
        if (confirm.value) {
            validatePasswords();
        }
    });
});