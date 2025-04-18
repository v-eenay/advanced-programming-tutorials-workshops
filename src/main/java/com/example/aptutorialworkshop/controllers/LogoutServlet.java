package com.example.aptutorialworkshop.controllers;

import com.example.aptutorialworkshop.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

/**
 * LogoutServlet
 *
 * Handles user logout by invalidating the session.
 * Redirects to login page with success message.
 */
@WebServlet(name = "LogoutServlet", value = "/LogoutServlet")
public class LogoutServlet extends HttpServlet {

    /**
     * Handles GET requests
     *
     * Invalidates user session and redirects to login page with success message.
     *
     * @param request HTTP request
     * @param response HTTP response
     * @throws ServletException If servlet error occurs
     * @throws IOException If I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user is actually logged in
        HttpSession session = request.getSession(false);
        boolean wasLoggedIn = (session != null && session.getAttribute("user") != null);

        // Invalidate the session using the AuthService
        AuthService.logout(request);

        // Note: Cookie implementation will be added separately

        // Redirect to the login page with a success message if the user was logged in
        if (wasLoggedIn) {
            response.sendRedirect("LoginServlet?message=You+have+been+successfully+logged+out");
        } else {
            // If no active session existed, just redirect to login page
            response.sendRedirect("LoginServlet");
        }
    }

    /**
     * Handles POST requests
     *
     * Delegates to doGet for logout processing.
     *
     * @param request HTTP request
     * @param response HTTP response
     * @throws ServletException If servlet error occurs
     * @throws IOException If I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
