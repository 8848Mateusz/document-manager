<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Witaj</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/styleHome.css'/>"/>
</head>
<body>
<div class="overlay">
    <h2>Witaj, <c:out value="${fullName}" />!</h2>
    <p>Wybierz, co chcesz zrobić:</p>

    <a href="/dashboard/load" id="btnPrzeterminowane">🧾 Przeterminowane płatności</a>

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