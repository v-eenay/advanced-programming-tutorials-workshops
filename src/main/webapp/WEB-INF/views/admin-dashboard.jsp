<%--
  Admin Dashboard Page

  This JSP file displays the admin dashboard with user information and admin-specific functionality.
  It shows dynamic user information (name, email, ID) and static content for demonstration.

  For session management implementation:
  - This page should be protected by a filter that checks for admin role
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
    <title>Admin Dashboard - Advanced Programming and Technologies</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
</head>
<body>
    <header>
        <h1>Admin Dashboard</h1>
        <p>Advanced Programming and Technologies - Itahari International College</p>
    </header>

    <div class="container clearfix">
        <div class="sidebar">
            <h2>Admin Menu</h2>
            <div class="menu-item"><a href="#">Dashboard</a></div>
            <div class="menu-item"><a href="#">Manage Users</a></div>
            <div class="menu-item"><a href="#">Profile Settings</a></div>
            <div class="menu-item"><a href="${pageContext.request.contextPath}/LogoutServlet">Logout</a></div>
        </div>

        <div class="main-content">
            <div class="card">
                <h2>Welcome, <span><%= ((UserModel)session.getAttribute("user")).getName() %></span></h2>
                <div class="admin-profile">
                    <div class="profile-image">
                        <img src="data:image/jpeg;base64,${base64Image}" alt="Profile Picture" onerror="this.src='${pageContext.request.contextPath}/assets/images/default-profile.svg'" width="120" height="120">
                    </div>
                    <div class="profile-details">
                        <p><strong>Email:</strong> <span><%= ((UserModel)session.getAttribute("user")).getEmail() %></span></p>
                        <p><strong>Role:</strong> Administrator</p>
                        <p><strong>User ID:</strong> <span><%= ((UserModel)session.getAttribute("user")).getId() %></span></p>
                        <p><strong>Module Leader:</strong> Binay Koirala | <strong>Module Tutor:</strong> Sujan Subedi</p>
                    </div>
                </div>
            </div>

            <div class="card">
                <h2>User Management</h2>
                <table>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Actions</th>
                    </tr>
                    <tr>
                        <td>1</td>
                        <td>John Doe</td>
                        <td>john@example.com</td>
                        <td>user</td>
                        <td>
                            <a href="#" class="btn-small">Edit</a>
                            <a href="#" class="btn-small btn-danger">Delete</a>
                        </td>
                    </tr>
                    <tr>
                        <td>2</td>
                        <td>Jane Smith</td>
                        <td>jane@example.com</td>
                        <td>user</td>
                        <td>
                            <a href="#" class="btn-small">Edit</a>
                            <a href="#" class="btn-small btn-danger">Delete</a>
                        </td>
                    </tr>
                    <tr>
                        <td>3</td>
                        <td>Admin User</td>
                        <td>admin@example.com</td>
                        <td>admin</td>
                        <td>
                            <a href="#" class="btn-small">Edit</a>
                            <a href="#" class="btn-small btn-danger">Delete</a>
                        </td>
                    </tr>
                </table>
            </div>

            <div class="card">
                <h2>Quick Actions</h2>
                <a href="#" class="btn">Add New User</a>
                <a href="#" class="btn">Export User Data</a>
                <a href="#" class="btn">System Settings</a>
            </div>
        </div>
    </div>

    <footer>
        <p>&copy; 2025 Itahari International College - Advanced Programming and Technologies</p>
        <p>Module Leader: Binay Koirala | Module Tutor: Sujan Subedi</p>
    </footer>
</body>
</html>
