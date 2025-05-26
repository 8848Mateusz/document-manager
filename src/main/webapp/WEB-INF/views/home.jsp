<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Witaj</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/styleHome.css'/>"/>
</head>
<body>
<div class="overlay">
    <h2>Witaj, ${fullName}!</h2>
    <p>Wybierz, co chcesz zrobić:</p>

    <a href="/dashboard">🧾 Przeterminowane płatności</a>

</div>
</body>
</html>