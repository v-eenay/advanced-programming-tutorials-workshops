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
 * For session management implementation:
 * 1. After successful authentication, create a session for the user
 * 2. Store user information in the session
 * 3. Use filters to protect resources based on authentication status
 *
 * For cookie implementation:
 * 1. Add a "Remember Me" checkbox to the login form
 * 2. If checked, create a persistent cookie with the user's email
 * 3. Check for this cookie on subsequent visits to auto-login the user
 */
@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    /**
     * Handles GET requests to the LoginServlet
     *
     * This method displays the login form to the user. It also checks for success messages
     * from the registration process to display them to the user.
     *
     * For session management implementation:
     * You could check if the user is already logged in here and redirect them
     * to their dashboard instead of showing the login form.
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
     * For cookie implementation:
     * You could check for a remember-me cookie and auto-login the user:
     * ```java
     * Cookie[] cookies = request.getCookies();
     * if (cookies != null) {
     *     for (Cookie cookie : cookies) {
     *         if ("user_email".equals(cookie.getName())) {
     *             String email = cookie.getValue();
     *             // Try to auto-login with this email
     *             // You would need a secure way to authenticate without a password
     *         }
     *     }
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
     * authenticates the user, and forwards them to the appropriate dashboard if successful.
     *
     * For session management implementation:
     * After successful authentication, you should:
     * ```java
     * // Create a session
     * HttpSession session = request.getSession();
     * // Store user information
     * session.setAttribute("user", user);
     * // Set session timeout (in seconds)
     * session.setMaxInactiveInterval(1800); // 30 minutes
     * ```
     *
     * For cookie implementation:
     * If the user selects "Remember Me":
     * ```java
     * String rememberMe = request.getParameter("remember-me");
     * if (rememberMe != null && rememberMe.equals("on")) {
     *     Cookie userCookie = new Cookie("user_email", email);
     *     userCookie.setMaxAge(60*60*24*30); // 30 days
     *     userCookie.setPath("/");
     *     response.addCookie(userCookie);
     * }
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
                // Login successful

                // For session implementation, you would add this code:
                // HttpSession session = request.getSession();
                // session.setAttribute("user", user);
                // session.setMaxInactiveInterval(1800); // 30 minutes

                // For cookie implementation, you would add this code:
                // String rememberMe = request.getParameter("remember-me");
                // if (rememberMe != null && rememberMe.equals("on")) {
                //     Cookie userCookie = new Cookie("user_email", email);
                //     userCookie.setMaxAge(60*60*24*30); // 30 days
                //     userCookie.setPath("/");
                //     response.addCookie(userCookie);
                // }

                // Pass user to dashboard as request attribute
                request.setAttribute("user", user);

                // Convert image bytes to Base64 for display in JSP
                if (user.getImage() != null && user.getImage().length > 0) {
                    String base64Image = Base64.getEncoder().encodeToString(user.getImage());
                    request.setAttribute("base64Image", base64Image);
                }

                // Forward to appropriate dashboard based on user role
                if (user.getRole() == UserModel.Role.admin) {
                    request.getRequestDispatcher("/WEB-INF/views/admin-dashboard.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher("/WEB-INF/views/user-dashboard.jsp").forward(request, response);
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