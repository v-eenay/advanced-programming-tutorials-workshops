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
 * For session management implementation:
 * 1. This servlet should be protected by a filter that checks if the user is authenticated
 * 2. If not authenticated, redirect to the login page
 *
 * Example filter implementation:
 * ```java
 * @WebFilter("/UserDashboardServlet")
 * public class UserAuthFilter implements Filter {
 *     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
 *             throws IOException, ServletException {
 *         HttpServletRequest httpRequest = (HttpServletRequest) request;
 *         HttpServletResponse httpResponse = (HttpServletResponse) response;
 *         HttpSession session = httpRequest.getSession(false);
 *
 *         boolean isLoggedIn = (session != null && session.getAttribute("user") != null);
 *
 *         if (isLoggedIn) {
 *             chain.doFilter(request, response);
 *         } else {
 *             httpResponse.sendRedirect(httpRequest.getContextPath() + "/LoginServlet");
 *         }
 *     }
 * }
 * ```
 */
@WebServlet(name = "UserDashboardServlet", value = "/UserDashboardServlet")
public class UserDashboardServlet extends HttpServlet {
    /**
     * Handles GET requests to the UserDashboardServlet
     *
     * This method should display the user dashboard to authenticated users.
     * Currently, it redirects to the login page since there's no session management.
     *
     * For session management implementation:
     * ```java
     * HttpSession session = request.getSession(false);
     * if (session != null && session.getAttribute("user") != null) {
     *     UserModel user = (UserModel) session.getAttribute("user");
     *     // User is authenticated
     *     request.setAttribute("user", user);
     *
     *     // Convert image bytes to Base64 for display in JSP
     *     if (user.getImage() != null && user.getImage().length > 0) {
     *         String base64Image = Base64.getEncoder().encodeToString(user.getImage());
     *         request.setAttribute("base64Image", base64Image);
     *     }
     *
     *     // Forward to dashboard
     *     request.getRequestDispatcher("/WEB-INF/views/user-dashboard.jsp").forward(request, response);
     *     return;
     * }
     * // If not authenticated, redirect to login
     * response.sendRedirect("LoginServlet");
     * ```
     *
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // For direct access to the dashboard, redirect to login
        // In a real application with sessions, you would check for authentication here
        response.sendRedirect("LoginServlet");
    }

    /**
     * Handles POST requests to the UserDashboardServlet
     *
     * This method currently delegates to doGet, but could be expanded to handle
     * user-specific form submissions and actions.
     *
     * For session management implementation:
     * You would first verify that the user is authenticated, then process
     * the specific user action requested.
     *
     * @param request The HTTP request object
     * @param response The HTTP response object
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // For POST requests, just call doGet to handle the same way
        doGet(request, response);
    }
}