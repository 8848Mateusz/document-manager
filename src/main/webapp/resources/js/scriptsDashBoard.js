document.addEventListener("DOMContentLoaded", function () {
    const selectAll = document.getElementById("select-all");
    const checkboxes = document.querySelectorAll(".row-check");
    const loader = document.getElementById("loadingModal");

    // Zresetuj Local Storage (usunie wcześniejsze zaznaczenia)
    localStorage.removeItem("selectedInvoices");

    // Checkboxy - teraz puste przy starcie
    checkboxes.forEach(cb => {
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
    refreshEmailSentCount(invoiceNumber);
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
            const emails = document.getElementById('emailHistory');  // Dodaj selektor dla historii e-maili
            let callCount = 0;

            // Wyczyść poprzednią zawartość
            history.innerHTML = '';
            calls.innerHTML = '';
            emails.innerHTML = '';

            data.forEach(note => {
                const li = document.createElement('li');
                if (note.type === "comment") {
                    li.innerHTML = `💬 ${note.value} <small>(${note.timestamp}) — <b>${note.createdBy}</b></small>`;
                    history.appendChild(li);
                } else if (note.type === "phone") {
                    callCount++;
                    li.innerHTML = `📞 Telefon <small>(${note.timestamp}) — <b>${note.createdBy}</b></small>`;
                    calls.appendChild(li);
                } else if (note.type === "email") {
                    li.innerHTML = `📧 <small>(${note.timestamp}) — <b>${note.createdBy}</b></small>`;
                    emails.appendChild(li);
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
        window.location.href = "/dashboard/load";  //
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

function cancelLoading() {
    const loader = document.getElementById("loadingModal");
    if (loader) {
        loader.style.display = "none";
    }
    window.location.href = "/home";  // przekierowanie do strony głównej
}

function sortTableByDate(columnIndex, order) {
    const table = document.querySelector('.invoice-table tbody');
    const rows = Array.from(table.rows);

    const sortedRows = rows.sort((a, b) => {
        const aDate = parseDate(a.cells[columnIndex].innerText.trim());
        const bDate = parseDate(b.cells[columnIndex].innerText.trim());

        if (!aDate || !bDate) return 0; // Jeśli brak daty – traktuj równo

        return order === 'asc' ? aDate - bDate : bDate - aDate;
    });

    table.innerHTML = '';
    sortedRows.forEach(row => table.appendChild(row));
}

function parseDate(dateString) {
    if (!dateString || !dateString.includes('-')) return null;

    const parts = dateString.split("-");
    if (parts.length === 3) {
        const day = parseInt(parts[0], 10);
        const month = parseInt(parts[1], 10) - 1;
        const year = parseInt(parts[2], 10);
        return new Date(year, month, day);
    }
    return null;
}

document.querySelectorAll('.sort-icon').forEach(function(icon) {
    icon.addEventListener('click', function() {
        const columnIndex = parseInt(icon.getAttribute('data-column-index'), 10);
        let order = icon.getAttribute('data-sort-order');

        // Zmień sortowanie (asc ↔ desc)
        order = order === 'asc' ? 'desc' : 'asc';
        icon.setAttribute('data-sort-order', order);

        // Wykonaj sortowanie
        sortTableByDate(columnIndex, order);

        // Resetuj wszystkie ikony
        document.querySelectorAll('.sort-icon').forEach(function(i) {
            i.classList.remove('fa-sort-up', 'fa-sort-down');
            i.classList.add('fa-sort');
        });

        // Ustaw aktywną ikonę
        icon.classList.remove('fa-sort');
        icon.classList.add(order === 'asc' ? 'fa-sort-up' : 'fa-sort-down');
    });
});

function sendNotifications() {
    const selectedInvoices = Array.from(document.querySelectorAll('.row-check:checked'))
        .map(cb => cb.closest('tr').getAttribute('data-invoice-number'));

    if (selectedInvoices.length === 0) {
        alert("Nie wybrano żadnych faktur do wysłania powiadomień.");
        return;
    }

    showEmailSendingModal();  // Pokaż modal ładowania

    fetch('/dashboard/sendNotifications', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'invoiceNumbers=' + selectedInvoices.join('&invoiceNumbers=')
    })
        .then(response => response.json())
        .then(data => {
            hideEmailSendingModal();  // Ukryj modal ładowania

            const modal = document.getElementById('emailNotificationModal');
            const messageContainer = document.getElementById('emailNotificationMessage');

            let message = `<strong>Wysłano:</strong> ${data.successCount}/${data.totalCount}, 
                       <strong>brak e-maili:</strong> 
                       <span id="missingEmailCount" 
                             style="cursor:pointer; text-decoration:underline; color:blue;">
                             ${data.noEmailCount}
                       </span>`;

            if (data.noEmailCount > 0) {
                message += `
            <div id="missingEmailList" style="display:none; margin-top:10px; text-align:left;">
                <ul>`;
                data.missingEmails.forEach(contractor => {
                    message += `<li>${contractor}</li>`;
                });
                message += `</ul></div>`;
            }

            messageContainer.innerHTML = message;
            modal.style.display = 'flex';

            // Obsługa kliknięcia
            const missingEmailCount = document.getElementById('missingEmailCount');
            if (missingEmailCount) {
                missingEmailCount.addEventListener('click', () => {
                    const missingEmailList = document.getElementById('missingEmailList');
                    if (missingEmailList) {
                        missingEmailList.style.display =
                            missingEmailList.style.display === 'none' ? 'block' : 'none';
                    }
                });
            }
        })
        .catch(error => {
            hideEmailSendingModal();  // Ukryj modal ładowania nawet w przypadku błędu
            console.error('Błąd:', error);
            alert('Wystąpił błąd podczas wysyłania e-maili.');
        });
}

function openEmailNotificationModal(sentCount, totalCount, missingEmailCount) {
    const modal = document.getElementById('emailNotificationModal');
    const messageElement = document.getElementById('emailNotificationMessage');
    messageElement.innerText = `Wysłano: ${sentCount}/${totalCount}, brak e-maili: ${missingEmailCount}`;
    modal.style.display = 'flex';
}

function closeEmailNotificationModal() {
    const modal = document.getElementById('emailNotificationModal');
    modal.style.display = 'none';

    const selectedInvoices = Array.from(document.querySelectorAll('.row-check:checked'))
        .map(cb => cb.closest('tr').getAttribute('data-invoice-number'));

    selectedInvoices.forEach(invoiceNumber => {
        refreshEmailSentCount(invoiceNumber);
    });
}

function refreshEmailSentCount(invoiceNumber) {
    fetch(`/dashboard/emailSentCount?invoiceNumber=${encodeURIComponent(invoiceNumber)}`)
        .then(response => response.json())
        .then(count => {
            // Aktualizujemy licznik w tabeli (td)
            const row = document.querySelector(`tr[data-invoice-number="${invoiceNumber}"]`);
            if (row) {
                const emailSentCell = row.querySelector("td:nth-child(9)"); // Kolumna email_sent_count
                if (emailSentCell) {
                    emailSentCell.textContent = count;
                }
            }
        })
        .catch(error => {
            console.error("Błąd odświeżania licznika email:", error);
        });
}

let contractorSortOrder = 'asc';

function toggleSortOrder() {
    contractorSortOrder = contractorSortOrder === 'asc' ? 'desc' : 'asc';
    refreshDashboard();
}

function refreshDashboard() {
    const loader = document.getElementById("loadingModal");
    loader.style.display = "block";

    const url = `/dashboard/load`;
    window.location.href = url;
}

document.addEventListener("DOMContentLoaded", function() {
    const sortButton = document.getElementById("sortByContractorBtn");  // <-- ustaw ID przycisku
    const sortLoaderModal = document.getElementById("sortLoaderModal");

    if (sortButton && sortLoaderModal) {
        sortButton.addEventListener("click", function() {
            sortLoaderModal.style.display = "block";
        });
    }
});

function showSortLoadingModal() {
    const modal = document.getElementById("sortLoadingModal");
    if (modal) {
        modal.style.display = "block";
    }
}

function closeSortLoadingModal() {
    const modal = document.getElementById("sortLoadingModal");
    if (modal) {
        modal.style.display = "none";
    }
}

document.querySelectorAll(".email-count").forEach(cell => {
    cell.addEventListener("click", function() {
        const invoiceNumber = this.closest("tr").getAttribute("data-invoice-number");
        openEmailHistoryModal(invoiceNumber);
    });
});

function showEmailSendingModal() {
    document.getElementById('emailSendingModal').style.display = 'flex';
}

function hideEmailSendingModal() {
    document.getElementById('emailSendingModal').style.display = 'none';
}

function fetchEmailHistory(invoiceNumber) {
    return fetch(`/api/invoice-interaction/email-history?invoiceNumber=${encodeURIComponent(invoiceNumber)}`)
        .then(res => res.json())
        .then(data => {
            const emailHistory = document.getElementById('emailHistory');
            emailHistory.innerHTML = ''; // wyczyść listę

            data.forEach(entry => {
                const li = document.createElement('li');
                li.innerHTML = `✉️ <small>(${entry.timestamp}) — <b>${entry.createdBy}</b></small>`;
                emailHistory.appendChild(li);
            });
        });
}

function openInvoiceModal(invoiceNumber) {
    if (!invoiceNumber) return;
    document.getElementById("modalInvoiceNumber").textContent = invoiceNumber;
    document.getElementById("newComment").value = '';
    document.getElementById("invoiceModal").classList.add("show");
    document.body.style.overflow = 'hidden';

    fetchNotes(invoiceNumber).then(() => {
        refreshCountsInTable(invoiceNumber);
    });
    fetchEmailHistory(invoiceNumber); // Nowe!
}



