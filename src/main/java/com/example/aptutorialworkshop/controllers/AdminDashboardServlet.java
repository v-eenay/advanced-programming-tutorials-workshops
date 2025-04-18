package com.example.aptutorialworkshop.controllers;

import com.example.aptutorialworkshop.models.UserModel;
import com.example.aptutorialworkshop.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Base64;

/**
 * AdminDashboardServlet
 *
 * This servlet handles the admin dashboard functionality. It's responsible for
 * displaying the admin dashboard and processing admin-specific actions.
 *
 * Session management implementation:
 * 1. Checks if the user is authenticated using AuthService
 * 2. Verifies that the user has the admin role
 * 3. If not authenticated or not an admin, redirects to the login page
 */
@WebServlet(name = "AdminDashboardServlet", value = "/AdminDashboardServlet")
public class AdminDashboardServlet extends HttpServlet {
    /**
     * Handles GET requests to the AdminDashboardServlet
     *
     * This method checks if the user is authenticated and has admin role.
     * If so, it displays the admin dashboard. Otherwise, it redirects to the login page.
     *
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user is authenticated and is an admin
        if (AuthService.isAuthenticated(request) && AuthService.isAdmin(request)) {
            // Get the user from the session
            UserModel user = AuthService.getCurrentUser(request);

            // User is authenticated and is an admin
            request.setAttribute("user", user);

            // Convert image bytes to Base64 for display in JSP
            if (user.getImage() != null && user.getImage().length > 0) {
                String base64Image = Base64.getEncoder().encodeToString(user.getImage());
                request.setAttribute("base64Image", base64Image);
            }

            // Forward to dashboard
            request.getRequestDispatcher("/WEB-INF/views/admin-dashboard.jsp").forward(request, response);
        } else {
            // If not authenticated or not an admin, redirect to login
            response.sendRedirect("LoginServlet");
        }
    }

    /**
     * Handles POST requests to the AdminDashboardServlet
     *
     * This method verifies that the user is authenticated and has admin role,
     * then processes any admin-specific form submissions or actions.
     *
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user is authenticated and is an admin
        if (!AuthService.isAuthenticated(request) || !AuthService.isAdmin(request)) {
            // If not authenticated or not an admin, redirect to login
            response.sendRedirect("LoginServlet");
            return;
        }

        // Process any admin-specific form submissions here
        // For now, just display the dashboard
        doGet(request, response);
    }
}