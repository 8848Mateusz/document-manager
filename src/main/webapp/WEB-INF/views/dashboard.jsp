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

        <div class="filter-section">
            <label for="from">Data od:</label>
            <input type="date" id="from" name="from">
            <label for="to">do:</label>
            <input type="date" id="to" name="to">
            <button class="btn-filter">Filtruj</button>
        </div>

        <div class="status-bar">
            <span>Ostatnia aktualizacja: 9 maja 2025, 14:57</span>
            <span>Błędy: <b>10</b></span>
            <span>Łączna kwota netto do zapłaty: <strong>602,70 zł</strong></span>
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
                <th></th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td><input type="checkbox" class="row-check"></td>
                <td>1523</td>
                <td>3n Solutions Sp. z o.o.</td>
                <td>2504/5/FS</td>
                <td>24 kwietnia 2025</td>
                <td>602.70 zł</td>
                <td>602.70 zł</td>
                <td>Nie</td>
                <td>0</td>
                <td><button class="btn-details">Szczegóły</button></td>
            </tr>
            <!-- więcej dynamicznych wierszy -->
            </tbody>
        </table>
    </div>
</div>
<script src="<c:url value='/resources/js/scriptsDashBoard.js'/>"></script>
</body>
</html>