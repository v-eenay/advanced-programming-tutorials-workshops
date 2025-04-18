package com.example.aptutorialworkshop.controllers;
import com.example.aptutorialworkshop.models.UserModel;
import com.example.aptutorialworkshop.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.Base64;

/**
 * LoginServlet
 *
 * This servlet handles user authentication functionality. It processes both GET requests
 * to display the login form and POST requests to authenticate users.
 *
 * Session management implementation:
 * 1. After successful authentication, creates a session for the user
 * 2. Stores user information in the session
 * 3. Uses filters to protect resources based on authentication status
 *
 * Note: Cookie implementation has been removed as it will be implemented separately.
 */
@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    /**
     * Handles GET requests to the LoginServlet
     *
     * This method checks if the user is already logged in and redirects them to the appropriate
     * dashboard. If not, it displays the login form with any success messages from registration.
     *
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user is already logged in
        if (AuthService.isAuthenticated(request)) {
            // Get the user from the session
            UserModel user = AuthService.getCurrentUser(request);

            // Redirect to the appropriate dashboard based on user role
            if (user.getRole() == UserModel.Role.admin) {
                response.sendRedirect("AdminDashboardServlet");
            } else {
                response.sendRedirect("UserDashboardServlet");
            }
            return;
        }

        // User is not logged in, show the login form
        // Check if there's a success message from registration
        String message = request.getParameter("message");
        if (message != null && !message.isEmpty()) {
            request.setAttribute("successMessage", message);
        }

        // Forward to the login JSP page
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    /**
     * Handles POST requests to the LoginServlet
     *
     * This method processes the login form submission, validates the input,
     * authenticates the user, creates a session, and redirects them to the appropriate
     * dashboard if successful.
     *
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get form parameters
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            // Validate input - Email
            if (email == null || email.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Email is required");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }

            // Validate input - Password
            if (password == null || password.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Password is required");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }

            // Authenticate user through the AuthService
            UserModel user = AuthService.login(email, password);

            if (user != null) {
                // Login successful - Create a session for the user
                AuthService.createUserSession(request, user, 1800); // 30 minutes timeout

                // Convert image bytes to Base64 for display in JSP
                if (user.getImage() != null && user.getImage().length > 0) {
                    String base64Image = Base64.getEncoder().encodeToString(user.getImage());
                    request.setAttribute("base64Image", base64Image);
                }

                // Redirect to appropriate dashboard based on user role
                if (user.getRole() == UserModel.Role.admin) {
                    response.sendRedirect("AdminDashboardServlet");
                } else {
                    response.sendRedirect("UserDashboardServlet");
                }
            } else {
                // Login failed
                request.setAttribute("errorMessage", "Invalid email or password");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // Handle any exceptions
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}