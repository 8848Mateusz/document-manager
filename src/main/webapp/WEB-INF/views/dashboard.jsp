<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Dashboard</title>

    <link rel="icon" href="<c:url value='/resources/images/favicon.ico'/>"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/stylesDashBoard.css'/>"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
<div id="page-content-wrapper">
    <nav class="navbar navbar-expand-lg navbar-light bg-light border-bottom">
        <div class="container-fluid d-flex justify-content-between align-items-center">
            <a></a>
            <div class="user-dropdown position-relative">
                <span class="user-toggle" onclick="toggleUserMenu()">
                    <b>${fullName}</b> <i class="fa fa-caret-down"></i>
                </span>
                <ul id="userMenu" class="user-menu">
                    <li><a href="/home">Wybór sekcji</a></li>
                    <li><a href="/logout">Wyloguj</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="dashboard-container">
        <h2>Przeterminowane płatności</h2>

        <form method="get" action="/dashboard/load" style="display: flex; align-items: center; gap: 10px; flex-wrap: wrap;">
            <label for="from">Data od:</label>
            <input type="date" id="from" name="from" value="${from}">
            <label for="to">do:</label>
            <input type="date" id="to" name="to" value="${to}">
            <button type="submit" class="btn-filter">Filtruj</button>
            <a href="/dashboard/load" class="btn-filter" style="text-decoration: none; text-align: center;">Wyczyść</a>
        </form>
        <br>

        <div class="status-bar">
            <span>
                Ostatnia aktualizacja: <strong>${aktualizacja}</strong>
                <i class="fa fa-sync-alt refresh-icon" onclick="refreshDashboard()" title="Odśwież dane"></i>
            </span>
            <span>Liczba pozycji: <strong>${invoiceCount}</strong></span>
            <span>Błędy: <strong>${errorCount}</strong></span>
            <span>Łączna kwota do zapłaty: <strong>${totalToPay}</strong></span>
        </div>

        <div class="action-buttons">
            <button class="btn-action" onclick="sendNotifications()">Wyślij powiadomienia</button>
        </div>

        <table class="invoice-table">
            <thead>
            <tr>
                <th><input type="checkbox" id="select-all"></th>
                <th>Kontrahent</th>
                <th>Numer faktury</th>
                <th>Data wystawienia
                    <i class="sort-icon fa fa-sort" data-column-index="3" data-sort-order="asc"></i>
                </th>
                <th>Termin płatności
                    <i class="sort-icon fa fa-sort" data-column-index="4" data-sort-order="asc"></i>
                </th>
                <th>Kwota brutto</th>
                <th>Kwota do zapłaty</th>
                <th><i class="fa fa-comment text-blue" title="Komentarze"></i></th>
                <th><i class="fa fa-envelope-open text-red" title="Status wiadomości"></i></th>
                <th><i class="fa fa-phone text-green" title="Kontakt telefoniczny"></i></th>
                <th>Szczegóły</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="invoice" items="${invoices}">
                <tr data-invoice-number="${invoice.invoiceNumber}">
                    <td><input type="checkbox" class="row-check" data-filename="${invoice.filename}"></td>
                    <td class="${empty invoice.contractor ? 'error-cell' : ''}">
                            ${invoice.contractor}
                    </td>
                    <td class="${empty invoice.invoiceNumber ? 'error-cell' : ''}">
                            ${invoice.invoiceNumber}
                    </td>
                    <td class="${empty invoice.formattedInvoiceIssueDate ? 'error-cell' : ''}">
                            ${invoice.formattedInvoiceIssueDate}
                    </td>
                    <td class="${empty invoice.formattedPaymentDate ? 'error-cell' : ''}">
                            ${invoice.formattedPaymentDate}
                    </td>
                    <td>${invoice.formattedGrossAmount}</td>
                    <td>${invoice.formattedAmountDue}</td>
                    <td><span class="comment-count">${commentCounts[invoice.invoiceNumber] != null ? commentCounts[invoice.invoiceNumber] : 0}</span></td>
                    <td>0</td>
                    <td><span class="phone-count">${invoice.phoneCalls}</span></td>
                    <td>
                        <a class="btn-details" onclick="openInvoiceModal('${invoice.invoiceNumber}')">Wyświetl</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- Modal -->
    <div id="invoiceModal" class="modal-overlay">
        <div class="modal-background" onclick="closeInvoiceModal()"></div>
        <div class="modal-window">
            <button class="modal-close" onclick="closeInvoiceModal()">&times;</button>
            <h3 id="modalInvoiceNumberTitle">Faktura: <span id="modalInvoiceNumber"></span></h3>
            <div class="modal-section">
                <label>Rozmowy telefoniczne:</label>
                <button onclick="incrementCallCounter()" class="btn-round">+1</button>
                <span id="callCount">0</span>
            </div>
            <div class="modal-section">
                <label for="newComment">Dodaj komentarz:</label>
                <textarea id="newComment" rows="3" placeholder="Wpisz komentarz..."></textarea>
                <button onclick="addComment()" class="btn-primary">Dodaj</button>
            </div>
            <div class="modal-section">
                <h4>Historia komentarzy:</h4>
                <ul id="commentHistory" class="history-list"></ul>
                <h4>Historia kontaktów telefonicznych:</h4>
                <ul id="callHistory" class="history-list"></ul>
            </div>
        </div>
    </div>
</div>

<!-- Loader -->
<div id="loadingModal" style="display:none; position: fixed; top: 0; left: 0;
     width: 100%; height: 100%; background: rgba(0,0,0,0.6);
     z-index: 9999; color: white; font-size: 18px; text-align: center;
     padding-top: 20%;">
    <div>
        <div class="loader"></div>
        <p>Proszę czekać, trwa wczytywanie danych...</p>
    </div>
</div>

<script src="<c:url value='/resources/js/scriptsDashBoard.js'/>"></script>
</body>
</html>