<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>




<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title></title>
  <link rel="stylesheet" href="<c:url value='/resources/css/styleLogin.css'/>"/>
</head>
<body>
<div class="wrapper">
  <c:if test="${loginError}">
    <div class="alert-message">Nieprawidłowy e-mail lub hasło</div>
  </c:if>
  <c:if test="${logoutSuccess}">
    <div class="alert-message">Zostałeś wylogowany</div>
  </c:if>
  <form action="/login" method="post">
    <h2>Login</h2>
    <div class="input-field">
      <input type="text"  id="email" name="email" autocomplete="off" required>
      <label>Enter your email</label>
      <div class="error-msg" id="email-error"></div>
    </div>
    <div class="input-field">
      <input type="password" id="password" name="password" autocomplete="off" required>
      <label>Enter your password</label>
      <div class="error-msg" id="password-error"></div>
    </div>
    <div class="forget">
      <label></label>
      <a href="/forgotPassword">Forgot your password?</a>
    </div>
    <button type="submit">Log In</button>
    <div class="register">
      <p>Don't have an account? <a href="<c:url value='/register'/>"><b>Register</b></a></p>
    </div>
  </form>
</div>
<script src="<c:url value='/resources/js/scriptsLogin.js'/>"></script>
</body>
</html>