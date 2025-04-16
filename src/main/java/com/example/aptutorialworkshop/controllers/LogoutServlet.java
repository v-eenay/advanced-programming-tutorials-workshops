package com.example.aptutorialworkshop.controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

/**
 * LogoutServlet
 * 
 * This servlet handles user logout functionality. It invalidates the user's session
 * and removes any authentication cookies.
 * 
 * This is a sample implementation that you can use when implementing session management.
 * Currently, it's not used in the application since there's no session management yet.
 * 
 * Usage:
 * 1. Add a logout link in your dashboard pages:
 *    <a href="${pageContext.request.contextPath}/LogoutServlet">Logout</a>
 * 
 * 2. When the user clicks the link, this servlet will:
 *    - Invalidate their session
 *    - Remove any authentication cookies
 *    - Redirect them to the login page
 */
@WebServlet(name = "LogoutServlet", value = "/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    
    /**
     * Handles GET requests to the LogoutServlet
     * 
     * This method invalidates the user's session and removes any authentication cookies.
     * 
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the current session without creating a new one if none exists
        HttpSession session = request.getSession(false);
        
        // Invalidate the session if it exists
        if (session != null) {
            session.invalidate();
        }
        
        // Remove any authentication cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("user_email".equals(cookie.getName())) {
                    // Set the cookie's max age to 0 to delete it
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }
        
        // Redirect to the login page
        response.sendRedirect("LoginServlet");
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
