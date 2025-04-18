<%--
  User Dashboard Page

  This JSP file displays the user dashboard with user information and user-specific functionality.
  It shows dynamic user information (name, email, ID) and static content for demonstration.

  For session management implementation:
  - This page should be protected by a filter that checks for authentication
  - User information should be retrieved from the session
  - Example: UserModel user = (UserModel) session.getAttribute("user");

  For logout functionality:
  - Add a logout link that calls a LogoutServlet
  - The LogoutServlet would invalidate the session and redirect to the login page
  - Example: session.invalidate();
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.aptutorialworkshop.models.UserModel" %>
<html>
<head>
    <title>Student Dashboard - Advanced Programming and Technologies</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
</head>
<body>
    <header>
        <h1>Student Dashboard</h1>
        <p>Advanced Programming and Technologies - Itahari International College</p>
    </header>

    <div class="container clearfix">
        <div class="sidebar">
            <h2>Student Menu</h2>
            <div class="menu-item"><a href="#">Dashboard</a></div>
            <div class="menu-item"><a href="#">My Courses</a></div>
            <div class="menu-item"><a href="#">Profile Settings</a></div>
            <div class="menu-item"><a href="${pageContext.request.contextPath}/LogoutServlet">Logout</a></div>
        </div>

        <div class="main-content">
            <div class="card">
                <h2>Welcome, <span><%= ((UserModel)request.getAttribute("user")).getName() %></span></h2>
                <div class="user-profile">
                    <div class="profile-image">
                        <img src="data:image/jpeg;base64,${base64Image}" alt="Profile Picture" onerror="this.src='${pageContext.request.contextPath}/assets/images/default-profile.svg'" width="120" height="120">
                    </div>
                    <div class="profile-details">
                        <p><strong>Email:</strong> <span><%= ((UserModel)request.getAttribute("user")).getEmail() %></span></p>
                        <p><strong>Role:</strong> Student</p>
                        <p><strong>User ID:</strong> <span><%= ((UserModel)request.getAttribute("user")).getId() %></span></p>
                        <p><strong>Module Leader:</strong> Binay Koirala | <strong>Module Tutor:</strong> Sujan Subedi</p>
                    </div>
                </div>
            </div>

            <div class="card">
                <h2>My Courses</h2>

                <div class="course-card">
                    <h3>Advanced Programming and Technologies</h3>
                    <p>Instructor: Binay Koirala</p>
                    <div class="progress-container">
                        <div class="progress-bar" style="width: 75%;">75%</div>
                    </div>
                    <a href="#" class="btn">View Course</a>
                </div>

                <div class="course-card">
                    <h3>Database Management Systems</h3>
                    <p>Instructor: Sujan Subedi</p>
                    <div class="progress-container">
                        <div class="progress-bar" style="width: 60%;">60%</div>
                    </div>
                    <a href="#" class="btn">View Course</a>
                </div>
            </div>

            <div class="card">
                <h2>Account Information</h2>
                <div class="account-actions">
                    <a href="#" class="btn">Edit Profile</a>
                    <a href="#" class="btn">Change Password</a>
                    <a href="#" class="btn">Update Profile Picture</a>
                </div>
            </div>
        </div>
    </div>

    <footer>
        <p>&copy; 2025 Itahari International College - Advanced Programming and Technologies</p>
        <p>Module Leader: Binay Koirala | Module Tutor: Sujan Subedi</p>
    </footer>
</body>
</html>
