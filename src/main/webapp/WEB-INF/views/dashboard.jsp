<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <meta name="description" content="" />
    <meta name="author" content="" />
    <title></title>
    <!-- Favicon-->
    <link rel="icon" href="<c:url value='/resources/images/favicon.ico'/>"/>
    <!-- Core theme CSS (includes Bootstrap)-->
    <link rel="stylesheet" href="<c:url value='/resources/css/stylesDashBoard.css'/>"/>
</head>
<body>
    <!-- Page content wrapper-->
    <div id="page-content-wrapper">
        <!-- Top navigation-->
        <nav class="navbar navbar-expand-lg navbar-light bg-light border-bottom">
            <div class="container-fluid">
                <ul>
                    <a class="text-dark text-decoration-none" href="https://www.kopiarkibialystok.pl/" target="_blank"> <p><b>Biuro Serwis Paweł Tworek-Kujawski</b></p></a>
                </ul>
                    <ul class="navbar-nav ms-auto ">
                        <span class="" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-haspopup="" aria-expanded="false"><b>${fullName}</b></span>
                            <div class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                                <a class="dropdown-item" href="/home">Wybór sekcji</a>
                                    <div class="dropdown-divider"></div>
                                    <a class="dropdown-item" href="/logout">Wyloguj</a>
                            </div>
                    </ul>
                </div>
        </nav>
        <!-- Page content-->
        <div class="container-fluid">
            <h1 class="mt-4">Simple Sidebar</h1>

        </div>
    </div>
<!-- Bootstrap core JS-->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- Core theme JS-->
<script src="<c:url value='/resources/js/scriptsDashBoard.js'/>"></script>
</body>
</html>