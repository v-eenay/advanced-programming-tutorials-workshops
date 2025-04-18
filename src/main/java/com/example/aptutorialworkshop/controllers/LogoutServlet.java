package com.example.aptutorialworkshop.controllers;

import com.example.aptutorialworkshop.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

/**
 * LogoutServlet
 *
 * This servlet handles user logout functionality. It invalidates the user's session
 * and redirects to the login page.
 *
 * Usage:
 * 1. Add a logout link in your dashboard pages:
 *    <a href="${pageContext.request.contextPath}/LogoutServlet">Logout</a>
 *
 * 2. When the user clicks the link, this servlet will:
 *    - Invalidate their session using the AuthService
 *    - Redirect them to the login page
 */
@WebServlet(name = "LogoutServlet", value = "/LogoutServlet")
public class LogoutServlet extends HttpServlet {

    /**
     * Handles GET requests to the LogoutServlet
     *
     * This method invalidates the user's session using the AuthService.
     * After logout, it redirects to the login page with a success message.
     *
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user is actually logged in
        HttpSession session = request.getSession(false);
        boolean wasLoggedIn = (session != null && session.getAttribute("user") != null);

        // Invalidate the session using the AuthService
        AuthService.logout(request);

        // Note: Cookie implementation will be added later
        // When implementing cookies, you would add code here to delete any authentication cookies

        // Redirect to the login page with a success message if the user was logged in
        if (wasLoggedIn) {
            response.sendRedirect("LoginServlet?message=You+have+been+successfully+logged+out");
        } else {
            // If no active session existed, just redirect to login page
            response.sendRedirect("LoginServlet");
        }
    }

    /**
     * Handles POST requests to the LogoutServlet
     *
     * This method delegates to doGet to handle the logout process.
     *
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
