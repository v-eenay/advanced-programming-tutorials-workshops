<%--
  Login Page

  This JSP file displays the login form and handles login error messages.

  For session management implementation:
  - After successful login, the servlet will create a session and store user information
  - The session can be accessed in JSP using the session implicit object
  - Example: ${sessionScope.user.name} to display the user's name

  For cookie implementation:
  - Add a "Remember Me" checkbox to the form
  - The servlet will create a cookie if this is checked
  - The cookie can be used for auto-login on subsequent visits
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login - Advanced Programming and Technologies</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</head>
<body>
    <header>
        <h1>Advanced Programming and Technologies</h1>
        <p>Itahari International College</p>
    </header>

    <div class="container">
        <div class="form-card">
            <h2>Login</h2>
            <% if(request.getAttribute("errorMessage") != null) { %>
                <div class="error-alert">
                    <%= request.getAttribute("errorMessage") %>
                </div>
            <% } %>
            <% if(request.getAttribute("successMessage") != null) { %>
                <div class="success-alert">
                    <%= request.getAttribute("successMessage") %>
                </div>
            <% } %>
            <form id="loginForm" action="LoginServlet" method="post" onsubmit="return validateForm('loginForm')">
                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" required>
                    <div class="error-message">Please enter a valid email address</div>
                </div>

                <div class="form-group">
                    <label for="password">Password:</label>
                    <div style="position: relative;">
                        <input type="password" id="password" name="password" required>
                        <i class="fas fa-eye toggle-password" id="togglePassword" onclick="togglePassword('password', 'togglePassword')" style="position: absolute; right: 10px; top: 14px; cursor: pointer;"></i>
                    </div>
                    <div class="error-message">Password is required</div>
                </div>

                <%-- Remember Me checkbox for cookie implementation --%>
                <div class="form-group remember-me">
                    <label class="checkbox-container">
                        <input type="checkbox" name="remember-me" id="remember-me">
                        <span class="checkmark"></span>
                        Remember me on this device
                    </label>
                </div>

                <button type="submit" class="btn">Login</button>
            </form>

            <div class="links">
                <p>Don't have an account? <a href="./RegisterServlet">Register here</a></p>
                <p><a href="${pageContext.request.contextPath}/index.jsp">Back to Home</a></p>
            </div>
        </div>
    </div>

    <footer>
        <p>Module Leader: Binay Koirala | Module Tutor: Sujan Subedi</p>
        <p>&copy; 2025 Itahari International College</p>
    </footer>
</body>
</html>
