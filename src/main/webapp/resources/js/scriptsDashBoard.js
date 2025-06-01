document.addEventListener("DOMContentLoaded", function () {
    const selectAll = document.getElementById("select-all");
    const checkboxes = document.querySelectorAll(".row-check");
    const loader = document.getElementById("loadingModal");

    // Checkboxy
    const selected = JSON.parse(localStorage.getItem("selectedInvoices")) || [];
    checkboxes.forEach(cb => {
        const filename = cb.dataset.filename;
        if (selected.includes(filename)) cb.checked = true;

        cb.addEventListener("change", () => {
            const updated = Array.from(checkboxes)
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

    // Obsługa filtra
    const filterForm = document.querySelector("form[action='/dashboard/load']");
    if (filterForm) {
        filterForm.addEventListener("submit", () => {
            loader.style.display = "block";
        });
    }

    // Obsługa przycisku wyczyść
    const clearBtn = document.querySelector("a[href='/dashboard/load'].btn-filter");
    if (clearBtn) {
        clearBtn.addEventListener("click", function (event) {
            event.preventDefault();
            loader.style.display = "block";
            setTimeout(() => {
                window.location.href = clearBtn.href;
            }, 300);
        });
    }

    // Obsługa refresh
    const refreshBtn = document.querySelector(".refresh-icon");
    if (refreshBtn) {
        refreshBtn.addEventListener("click", function () {
            refreshDashboard();
        });
    }
});

// Modal logic
function openInvoiceModal(invoiceNumber) {
    if (!invoiceNumber) return;
    document.getElementById("modalInvoiceNumber").textContent = invoiceNumber;
    document.getElementById("newComment").value = '';
    document.getElementById("invoiceModal").classList.add("show");
    document.body.style.overflow = 'hidden';
    fetchNotes(invoiceNumber).then(() => {
        refreshCountsInTable(invoiceNumber);
    });
}

function closeInvoiceModal() {
    document.getElementById("invoiceModal").classList.remove("show");
    document.body.style.overflow = '';
    const invoiceNumber = document.getElementById('modalInvoiceNumber').textContent.trim();
    refreshCountsInTable(invoiceNumber);
}

// AJAX dodanie komentarza
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
        fetchNotes(invoiceNumber).then(() => {
            refreshCountsInTable(invoiceNumber);
        });
    });
}

// AJAX dodanie telefonu
function incrementCallCounter() {
    const invoiceNumber = document.getElementById('modalInvoiceNumber').innerText.trim();
    fetch('/api/invoice-interaction/phone', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ invoiceNumber })
    }).then(() => {
        fetchNotes(invoiceNumber).then(() => {
            refreshCountsInTable(invoiceNumber);
        });
    });
}

// Historia komentarzy i telefonów
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
                const li = document.createElement('li');
                if (note.type === "comment") {
                    li.innerHTML = `💬 ${note.value} <small>(${note.timestamp})</small>`;
                    history.appendChild(li);
                } else if (note.type === "phone") {
                    callCount++;
                    li.innerHTML = `📞 Telefon <small>(${note.timestamp})</small>`;
                    calls.appendChild(li);
                }
            });
            document.getElementById('callCount').textContent = callCount;
        });
}

// Refresh dashboard
function refreshDashboard() {
    const icon = document.querySelector('.refresh-icon');
    icon.classList.add('spin');
    document.getElementById('loadingModal').style.display = 'block';
    setTimeout(() => {
        window.location.href = "/dashboard/load";  // 🔄 uwzględnia /load
    }, 500);
}

// Odświeżanie liczników komentarzy i telefonów
function refreshCountsInTable(invoiceNumber) {
    fetch(`/api/invoice-interaction/counts?invoiceNumber=${encodeURIComponent(invoiceNumber)}`)
        .then(res => res.json())
        .then(data => {
            const row = document.querySelector(`tr[data-invoice-number="${invoiceNumber}"]`);
            if (row) {
                const commentCell = row.querySelector('.comment-count');
                const phoneCell = row.querySelector('.phone-count');
                if (commentCell && data.comments !== undefined) {
                    commentCell.textContent = data.comments;
                }
                if (phoneCell && data.phoneCalls !== undefined) {
                    phoneCell.textContent = data.phoneCalls;
                }
            }
        });
}

// Menu użytkownika
function toggleUserMenu() {
    const menu = document.getElementById("userMenu");
    menu.classList.toggle("show");
}

document.addEventListener("click", function (event) {
    const toggle = document.querySelector(".user-toggle");
    const menu = document.getElementById("userMenu");
    if (!menu.contains(event.target) && !toggle.contains(event.target)) {
        menu.classList.remove("show");
    }
});

// Obsługa kliknięcia w "przeterminowane płatności"
const btnPrzeterminowane = document.getElementById("btnPrzeterminowane");
if (btnPrzeterminowane) {
    btnPrzeterminowane.addEventListener("click", function(event) {
        event.preventDefault();
        document.getElementById('loadingModal').style.display = "block";
        setTimeout(() => {
            window.location.href = btnPrzeterminowane.href;
        }, 300);
    });
}