<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Dashboard</title>

    <link rel="icon" href="<c:url value='/resources/images/favicon.ico'/>"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/stylesDashBoard.css'/>"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
<div id="page-content-wrapper">
    <!-- NAV -->
    <nav class="navbar navbar-expand-lg navbar-light bg-light border-bottom">
        <div class="container-fluid d-flex justify-content-between align-items-center">
            <a></a>

            <div class="user-dropdown position-relative">
                <span class="user-toggle" onclick="toggleUserMenu()"><b>${fullName}</b> <i class="fa fa-caret-down"></i></span>
                <ul id="userMenu" class="user-menu">
                    <li><a href="/home">Wybór sekcji</a></li>
                    <li><a href="/logout">Wyloguj</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- CONTENT -->
    <div class="dashboard-container">
        <h2>Przeterminowane płatności</h2>

        <form method="get" action="/dashboard" style="display: flex; align-items: center; gap: 10px; flex-wrap: wrap;">
            <label for="from">Data od:</label>
            <input type="date" id="from" name="from" value="${from}">

            <label for="to">do:</label>
            <input type="date" id="to" name="to" value="${to}">

            <button type="submit" class="btn-filter">Filtruj</button>

            <a href="/dashboard" class="btn-filter" style="text-decoration: none; text-align: center;">Wyczyść</a>
        </form>
        <br>

        <div class="status-bar">
            <span>Ostatnia aktualizacja: 9 maja 2025, 14:57</span>
            <span>Błędy: <b>${errorCount}</b></span>
            <span>Łączna kwota netto do zapłaty: <strong>${totalToPay} zł</strong></span>
        </div>

        <div class="action-buttons">
            <button class="btn-action" onclick="sendNotifications()">Wyślij powiadomienia</button>
        </div>

        <table class="invoice-table">
            <thead>
            <tr>
                <th><input type="checkbox" id="select-all"></th>
                <th>#ID</th>
                <th>Kontrahent</th>
                <th>Numer faktury</th>
                <th>Termin płatności</th>
                <th>Kwota brutto</th>
                <th>Kwota do zapłaty</th>
                <th>Rozliczono</th>
                <th><i class="fa fa-envelope-open text-red" title="Status wiadomości"></i></th>
                <th><i class="fa fa-phone text-green" style="margin-left: 8px;" title="Kontakt telefoniczny"></i></th>
                <th>Szczegóły</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="invoice" items="${invoices}">
                <tr>
                    <td><input type="checkbox" class="row-check" data-filename="${invoice.filename}"></td>
                    <td>0</td>

                    <td>
                        <c:if test="${empty invoice.contractor}">
                            <i class="fa fa-exclamation-triangle icon-warning" title="Brak kontrahenta"></i>
                        </c:if>
                            ${invoice.contractor}
                    </td>

                    <td>
                        <c:if test="${empty invoice.invoiceNumber}">
                            <i class="fa fa-exclamation-triangle icon-warning" title="Brak numeru faktury"></i>
                        </c:if>
                            ${invoice.invoiceNumber}
                    </td>

                    <td>
                        <c:if test="${empty invoice.paymentDate}">
                            <i class="fa fa-exclamation-triangle icon-warning" title="Brak daty płatności"></i>
                        </c:if>
                            ${invoice.formattedPaymentDate}
                    </td>

                    <td>
                        <c:if test="${empty invoice.grossAmount}">
                            <i class="fa fa-exclamation-triangle icon-warning" title="Brak kwoty brutto"></i>
                        </c:if>
                            ${invoice.formattedGrossAmount}
                    </td>

                    <td>
                        <c:if test="${empty invoice.grossAmount}">
                            <i class="fa fa-exclamation-triangle icon-warning" title="Brak kwoty brutto"></i>
                        </c:if>
                            ${invoice.formattedGrossAmount}
                    </td>

                    <td>
                        <c:choose>
                            <c:when test="${invoice.settled}">Tak</c:when>
                            <c:otherwise>Nie</c:otherwise>
                        </c:choose>
                    </td>

                    <td>0</td>

                    <td><span>${invoice.phoneCalls}</span></td>

                    <td>
                        <a class="btn-details" onclick="openInvoiceModal('${invoice.invoiceNumber}')">Wyświetl</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <!-- MODAL: Szczegóły faktury -->
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


</div>
</div>

<script src="<c:url value='/resources/js/scriptsDashBoard.js'/>"></script>
</body>
</html>