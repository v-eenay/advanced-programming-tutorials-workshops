package com.example.aptutorialworkshop.controllers;
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
 * For session management implementation:
 * 1. After successful registration, you could create a session for the user
 * 2. Store user information in the session
 * 3. Redirect to the appropriate dashboard
 *
 * Currently, it redirects to the login page after successful registration.
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
     * This method displays the registration form to the user.
     *
     * For session management implementation:
     * You could check if the user is already logged in here and redirect them
     * to their dashboard instead of showing the registration form.
     *
     * Example:
     * ```java
     * HttpSession session = request.getSession(false);
     * if (session != null && session.getAttribute("user") != null) {
     *     UserModel user = (UserModel) session.getAttribute("user");
     *     if (user.getRole() == UserModel.Role.admin) {
     *         response.sendRedirect("AdminDashboardServlet");
     *     } else {
     *         response.sendRedirect("UserDashboardServlet");
     *     }
     *     return;
     * }
     * ```
     *
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to the registration JSP page
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    /**
     * Handles POST requests to the RegisterServlet
     *
     * This method processes the registration form submission, validates the input,
     * and registers the user if the input is valid.
     *
     * For session management implementation:
     * After successful registration, you could:
     * 1. Create a session: HttpSession session = request.getSession();
     * 2. Store user information: session.setAttribute("user", userObject);
     * 3. Set session timeout: session.setMaxInactiveInterval(1800); // 30 minutes
     * 4. Redirect directly to the dashboard instead of the login page
     *
     * For cookie implementation:
     * You could create a remember-me cookie after registration:
     * ```java
     * Cookie userCookie = new Cookie("user_email", email);
     * userCookie.setMaxAge(60*60*24*30); // 30 days
     * userCookie.setPath("/");
     * response.addCookie(userCookie);
     * ```
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
                // Registration successful
                // Redirect to login page with success message as a request parameter
                response.sendRedirect("LoginServlet?message=Registration+successful!+Please+login+with+your+credentials.");

                // For session implementation, you would do this instead:
                // UserModel user = AuthService.getUserById(userID);
                // HttpSession session = request.getSession();
                // session.setAttribute("user", user);
                // if (user.getRole() == UserModel.Role.admin) {
                //     response.sendRedirect("AdminDashboardServlet");
                // } else {
                //     response.sendRedirect("UserDashboardServlet");
                // }
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