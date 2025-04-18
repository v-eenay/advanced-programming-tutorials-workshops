package com.example.aptutorialworkshop.controllers;
import com.example.aptutorialworkshop.models.UserModel;
import com.example.aptutorialworkshop.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.Base64;

/**
 * UserDashboardServlet
 *
 * This servlet handles the user dashboard functionality. It's responsible for
 * displaying the user dashboard and processing user-specific actions.
 *
 * Session management implementation:
 * 1. Checks if the user is authenticated using AuthService
 * 2. If authenticated, displays the dashboard with user information
 * 3. If not authenticated, redirects to the login page
 */
@WebServlet(name = "UserDashboardServlet", value = "/UserDashboardServlet")
public class UserDashboardServlet extends HttpServlet {
    /**
     * Handles GET requests to the UserDashboardServlet
     *
     * This method checks if the user is authenticated and displays the dashboard
     * if they are. If not, it redirects to the login page.
     *
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user is authenticated
        if (AuthService.isAuthenticated(request)) {
            // Get the user from the session
            UserModel user = AuthService.getCurrentUser(request);

            // Check if this is a regular user (not admin)
            if (user.getRole() != UserModel.Role.user) {
                // If admin, redirect to admin dashboard
                response.sendRedirect("AdminDashboardServlet");
                return;
            }

            // User is authenticated and has the correct role
            request.setAttribute("user", user);

            // Convert image bytes to Base64 for display in JSP
            if (user.getImage() != null && user.getImage().length > 0) {
                String base64Image = Base64.getEncoder().encodeToString(user.getImage());
                request.setAttribute("base64Image", base64Image);
            }

            // Forward to dashboard
            request.getRequestDispatcher("/WEB-INF/views/user-dashboard.jsp").forward(request, response);
        } else {
            // If not authenticated, redirect to login
            response.sendRedirect("LoginServlet");
        }
    }

    /**
     * Handles POST requests to the UserDashboardServlet
     *
     * This method verifies that the user is authenticated, then processes
     * any user-specific form submissions or actions.
     *
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user is authenticated
        if (!AuthService.isAuthenticated(request)) {
            // If not authenticated, redirect to login
            response.sendRedirect("LoginServlet");
            return;
        }

        // Process any user-specific form submissions here
        // For now, just display the dashboard
        doGet(request, response);
    }
}