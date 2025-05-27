document.addEventListener("DOMContentLoaded", function () {
    const selectAll = document.getElementById("select-all");
    const checkboxes = document.querySelectorAll(".row-check");

    const selected = JSON.parse(localStorage.getItem("selectedInvoices")) || [];

    checkboxes.forEach(cb => {
        const filename = cb.dataset.filename;
        if (selected.includes(filename)) cb.checked = true;

        cb.addEventListener("change", () => {
            const updated = Array.from(document.querySelectorAll(".row-check"))
                .filter(c => c.checked)
                .map(c => c.dataset.filename);

            localStorage.setItem("selectedInvoices", JSON.stringify(updated));
        });
    });

    if (selectAll) {
        selectAll.addEventListener("change", () => {
            checkboxes.forEach(cb => {
                cb.checked = selectAll.checked;
                cb.dispatchEvent(new Event("change"));
            });
        });

        selectAll.checked = Array.from(checkboxes).every(cb => cb.checked);
    }
});


// Modal logic

function openInvoiceModal(invoiceNumber) {
    if (!invoiceNumber) return;

    document.getElementById("modalInvoiceNumber").textContent = invoiceNumber;
    document.getElementById("invoiceModal").classList.add("show");
    document.body.style.overflow = 'hidden'; // ⛔ blokuj scroll tła
    fetchNotes(invoiceNumber);
}

function closeInvoiceModal() {
    document.getElementById("invoiceModal").style.display = "none";
    location.reload(); // <--- automatyczne odświeżenie strony
}


// Add comment

function addComment() {
    const invoiceNumber = document.getElementById('modalInvoiceNumber').innerText.trim();
    const comment = document.getElementById('newComment').value.trim();

    if (!comment) {
        alert("Komentarz nie może być pusty.");
        return;
    }

    fetch('/api/invoice-interaction/comment', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ invoiceNumber, comment })
    }).then(() => {
        document.getElementById('newComment').value = '';
        return fetchNotes(invoiceNumber);
    });
}


// Add phone contact

function incrementCallCounter() {
    const invoiceNumber = document.getElementById('modalInvoiceNumber').innerText.trim();

    fetch('/api/invoice-interaction/phone', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ invoiceNumber })
    }).then(() => fetchNotes(invoiceNumber));
}


// Load data from DB and render

function fetchNotes(invoiceNumber) {
    return fetch(`/api/invoice-interaction/history?invoiceNumber=${encodeURIComponent(invoiceNumber)}`)
        .then(res => res.json())
        .then(data => {
            const history = document.getElementById('commentHistory');
            const calls = document.getElementById('callHistory');
            let callCount = 0;

            history.innerHTML = '';
            calls.innerHTML = '';

            data.forEach(note => {
                if (note.type === "comment") {
                    const li = document.createElement('li');
                    li.innerHTML = `💬 ${note.value} <small>(${note.timestamp})</small>`;
                    li.classList.add("fade-in");
                    history.appendChild(li);
                } else if (note.type === "phone") {
                    callCount++;
                    const li = document.createElement('li');
                    li.innerHTML = `📞 Telefon <small>(${note.timestamp})</small>`;
                    li.classList.add("fade-in");
                    calls.appendChild(li);
                }
            });

            document.getElementById('callCount').textContent = callCount;
        });
}

// Pokazuje menu
function toggleUserMenu() {
    const menu = document.getElementById("userMenu");
    menu.classList.toggle("show");
}

// Ukrywa menu po kliknięciu poza nim
document.addEventListener("click", function(event) {
    const toggle = document.querySelector(".user-toggle");
    const menu = document.getElementById("userMenu");

    // Jeśli kliknięcie było poza menu i poza przyciskiem toggle
    if (!menu.contains(event.target) && !toggle.contains(event.target)) {
        menu.classList.remove("show");
    }
});