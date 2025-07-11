<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title></title>
    <link rel="stylesheet" href="<c:url value='/resources/css/styleRegister.css'/>"/>
</head>
<body>
<div class="wrapper">
    <c:if test="${not empty errorMessage}">
        <div class="alert-message">${errorMessage}</div>
    </c:if>
        <form action="/register" method="post" role="form" >
        <h2>Utwórz konto</h2>
        <div class="input-field">
            <input type="text" id="fullName" name="fullName" placeholder=" " required>
            <label>Imię i nazwisko</label>
        </div>
        <div class="input-field">
            <input type="email" id="email" name="email" placeholder=" " required>
            <div class="error-message">Niepoprawny format adresu e-mail</div>
            <label>E-mail</label>
        </div>
        <div class="input-field">
            <input type="password" id="password" name="password" placeholder=" " required />
            <label for="password">Hasło</label>
        </div>
        <div class="input-field">
            <input type="password" id="confirmPassword" name="confirmPassword" placeholder=" " required />
            <label for="confirmPassword">Potwierdź hasło</label>
            <div class="error-message" id="passwordError">Oba hasła muszą być takie same</div>
        </div>
        <button type="submit">Utwórz konto</button>
        <div class="register">
            <p>Masz już konto? <a href="<c:url value='/login'/>"><b>Zaloguj się</b></a></p>
        </div>
    </form>
</div>
<script src="<c:url value='/resources/js/scriptsRegister.js'/>"></script>
</body>
</html>
