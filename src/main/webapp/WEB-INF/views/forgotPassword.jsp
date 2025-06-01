<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Forgot Password</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/styleForgotPassword.css'/>"/>
</head>
<body>
<div class="wrapper">
    <c:if test="${not empty message}">
        <div class="alert-message" style="background-color: green;">${message}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert-message">${error}</div>
    </c:if>
    <h2>Nie pamiętasz hasła?</h2>

    <form action="/forgotPassword" method="post">
        <div class="input-field">
            <input type="email" name="email" id="email" required />
            <label for="email">Podaj swój adres e-mail</label>
        </div>

        <button type="submit">Wyślij link do resetu hasła</button>
    </form>
    <div class="register">
        <p>Pamiętasz swoje hasło? <a href="/login"><b>Zaloguj się</b></a></p>
    </div>
</div>

<script src="<c:url value='/resources/js/scriptsForgotPassword.js'/>"></script>
</body>
</html>