<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
  <title>Reset Password</title>
  <link rel="stylesheet" href="<c:url value='/resources/css/styleForgotPassword.css'/>"/>
</head>
<body>
<div class="wrapper">
  <c:if test="${not empty error}">
    <div class="alert-message">${error}</div>
  </c:if>
  <h2>Reset your password</h2>

  <form action="/resetPassword" method="post">
    <input type="hidden" name="token" value="${token}" />

    <div class="input-field">
      <input type="password" name="password" id="password" required />
      <label for="password">Enter new password</label>
    </div>

    <button type="submit">Reset Password</button>
  </form>

  <div class="register">
    <p>Go back to <a href="/login"><b>Login</b></a></p>
  </div>
</div>

<script src="<c:url value='/resources/js/scriptsForgotPassword.js'/>"></script>
</body>
</html>