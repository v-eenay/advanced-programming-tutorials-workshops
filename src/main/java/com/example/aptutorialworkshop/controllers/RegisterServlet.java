package com.example.aptutorialworkshop.controllers;
import com.example.aptutorialworkshop.models.UserModel;
import com.example.aptutorialworkshop.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

/**
 * RegisterServlet
 *
 * This servlet handles user registration functionality. It processes both GET requests
 * to display the registration form and POST requests to process form submissions.
 *
 * The servlet is annotated with @MultipartConfig to handle file uploads (profile pictures).
 *
 * Session management implementation:
 * 1. Checks if user is already logged in before showing registration form
 * 2. After successful registration, creates a session for the user
 * 3. Redirects to the appropriate dashboard based on user role
 *
 * Password security:
 * - Passwords are automatically hashed using BCrypt in the UserModel class
 */
@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 5, // 5MB
        maxRequestSize = 1024 * 1024 * 20 // 20MB - Maximum size of the entire request
)
public class RegisterServlet extends HttpServlet {
    /**
     * Handles GET requests to the RegisterServlet
     *
     * This method checks if the user is already logged in and redirects them to the appropriate
     * dashboard. If not, it displays the registration form.
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

        // User is not logged in, show the registration form
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    /**
     * Handles POST requests to the RegisterServlet
     *
     * This method processes the registration form submission, validates the input,
     * registers the user if the input is valid, and creates a session for the new user.
     * The password is automatically hashed by the UserModel class.
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
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirm-password");
            String role = request.getParameter("role");

            // Validate input - Name
            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Name is required");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                return;
            }

            // Validate input - Email
            if (email == null || email.trim().isEmpty() || !email.contains("@")) {
                request.setAttribute("errorMessage", "Valid email is required");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                return;
            }

            // Validate input - Password
            if (password == null || password.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Password is required");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                return;
            }

            // Validate input - Password confirmation
            if (!password.equals(confirmPassword)) {
                request.setAttribute("errorMessage", "Passwords do not match");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                return;
            }

            // Process profile image upload
            Part imagePart = request.getPart("image");
            byte[] imageBytes = null;

            if (imagePart != null && imagePart.getSize() > 0) {
                imageBytes = imagePart.getInputStream().readAllBytes();
            }

            // Register user through the AuthService
            int userID = AuthService.register(name, email, password, role, imageBytes);

            if (userID != -1) {
                // Registration successful - Get the user and create a session
                UserModel user = AuthService.getUserById(userID);

                // Create a session for the new user
                AuthService.createUserSession(request, user, 1800); // 30 minutes timeout

                // Redirect to the appropriate dashboard based on user role
                if (user.getRole() == UserModel.Role.admin) {
                    response.sendRedirect("AdminDashboardServlet");
                } else {
                    response.sendRedirect("UserDashboardServlet");
                }
            } else {
                // Registration failed
                request.setAttribute("errorMessage", "Registration failed. Email may already be in use.");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // Handle any exceptions
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }
}