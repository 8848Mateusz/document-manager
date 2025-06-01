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
    <div style="position: relative; display: inline-block; background: rgba(0,0,0,0.8); padding: 30px 60px; border-radius: 8px;">
        <div class="loader-close" style="position: absolute; top: 10px; right: 15px; font-size: 30px; cursor: pointer;"
             onclick="cancelLoading()">&times;</div>
        <div class="loader"></div>
        <p>Proszę czekać, trwa wczytywanie danych...</p>
    </div>
</div>
<script src="<c:url value='/resources/js/scriptsDashBoard.js'/>"></script>
</body>
</html>