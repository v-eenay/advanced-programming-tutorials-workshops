<%--
  Registration Page

  This JSP file displays the registration form and handles registration error messages.
  It includes form validation and image preview functionality.

  For session management implementation:
  - After successful registration, the servlet could create a session for the new user
  - Alternatively, it could redirect to the login page as it does now

  For security enhancement:
  - Consider adding CSRF protection with a token
  - Add more client-side validation for password strength, etc.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register - Advanced Programming and Technologies</title>
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
            <h2>Register</h2>
            <% if(request.getAttribute("errorMessage") != null) { %>
                <div class="error-alert">
                    <%= request.getAttribute("errorMessage") %>
                </div>
            <% } %>
            <form id="registerForm" action="RegisterServlet" method="post" enctype="multipart/form-data" onsubmit="return validateForm('registerForm')">
                <div class="form-group">
                    <label for="name">Full Name:</label>
                    <input type="text" id="name" name="name" required>
                    <div class="error-message">Please enter your full name</div>
                </div>

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

                <div class="form-group">
                    <label for="confirm-password">Confirm Password:</label>
                    <div style="position: relative;">
                        <input type="password" id="confirm-password" name="confirm-password" required>
                        <i class="fas fa-eye toggle-password" id="toggleConfirmPassword" onclick="togglePassword('confirm-password', 'toggleConfirmPassword')" style="position: absolute; right: 10px; top: 14px; cursor: pointer;"></i>
                    </div>
                    <div class="error-message">Passwords do not match</div>
                </div>

                <div class="form-group">
                    <label for="role">Role:</label>
                    <select id="role" name="role" required>
                        <option value="user">Student</option>
                        <option value="admin">Administrator</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="image">Profile Picture:</label>
                    <input type="file" id="image" name="image" accept="image/*" onchange="previewImage(event)">
                    <div class="image-preview-container">
                        <img id="imagePreview" class="image-preview" src="${pageContext.request.contextPath}/assets/images/default-profile.svg" alt="Image Preview" style="display: block;" />
                        <p class="preview-text">Upload an image to change the default profile picture</p>
                    </div>
                </div>

                <button type="submit" class="btn">Register</button>
            </form>

            <div class="links">
                <p>Already have an account? <a href="./LoginServlet">Login here</a></p>
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
