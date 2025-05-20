<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>




<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title></title>
  <link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>"/>
</head>
<body>
<div class="wrapper">
  <form action="/index" method="post">
    <h2>Login</h2>
    <div class="input-field">
      <input type="text" required>
      <label>Enter your email</label>
    </div>
    <div class="input-field">
      <input type="password" required>
      <label>Enter your password</label>
    </div>
    <div class="forget">
      <label></label>
      <a href="#">Forgot password?</a>
    </div>
    <button type="submit">Log In</button>
    <div class="register">
      <p>Don't have an account? <a href="<c:url value='/register'/>"><b>Register</b></a></p>
    </div>
  </form>
</div>
</body>
</html>