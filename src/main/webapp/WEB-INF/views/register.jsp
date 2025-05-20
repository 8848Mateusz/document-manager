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
        <form action="/register" method="post" role="form" >
        <h2>Create Account</h2>
        <div class="input-field">
            <input type="text" id="fullName" name="fullName" placeholder=" " required>
            <label>Full name</label>
        </div>
        <div class="input-field">
            <input type="email" id="email" name="email" placeholder=" " required>
            <div class="error-message">Email format is incorrect</div>
            <label>E-mail</label>
        </div>
        <div class="input-field">
            <input type="password" id="password" name="password" placeholder=" " required />
            <label for="password">Password</label>
        </div>
        <div class="input-field">
            <input type="password" id="confirmPassword" name="confirmPassword" placeholder=" " required />
            <label for="confirmPassword">Confirm password</label>
            <div class="error-message" id="passwordError">Both passwords must be the same</div>
        </div>
        <button type="submit">Create</button>
        <div class="register">
            <p>Already have an account? <a href="<c:url value='/login'/>"><b>Login</b></a></p>
        </div>
    </form>
</div>
<script src="<c:url value='/resources/js/scriptsRegister.js'/>"></script>
</body>
</html>
