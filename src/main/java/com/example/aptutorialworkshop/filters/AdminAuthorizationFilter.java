package com.example.aptutorialworkshop.filters;

import com.example.aptutorialworkshop.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * AdminAuthorizationFilter
 *
 * This filter checks if a user has admin role before allowing access to admin resources.
 * It intercepts requests to admin URLs and redirects unauthorized users to the login page.
 *
 * The filter uses the AuthService to check if the user is authenticated and has the admin role.
 */
@WebFilter(urlPatterns = {"/AdminDashboardServlet", "/WEB-INF/views/admin-dashboard.jsp"})
public class AdminAuthorizationFilter implements Filter {

    /**
     * Initializes the filter
     *
     * @param config The filter configuration
     * @throws ServletException If a servlet-specific error occurs
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        // Initialization code, if needed
    }

    /**
     * Filters requests to admin resources
     *
     * This method checks if the user is authenticated and has the admin role using the AuthService.
     * If the user is an admin, the request is allowed to proceed.
     * If not, the user is redirected to the login page.
     *
     * @param request The servlet request
     * @param response The servlet response
     * @param chain The filter chain
     * @throws IOException If an I/O error occurs
     * @throws ServletException If a servlet-specific error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Check if the user is authenticated and has admin role using the AuthService
        boolean isAdmin = AuthService.isAuthenticated(httpRequest) && AuthService.isAdmin(httpRequest);

        // If the user is an admin, allow the request
        if (isAdmin) {
            chain.doFilter(request, response);
        } else {
            // Otherwise, redirect to the login page
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/LoginServlet");
        }
    }

    /**
     * Cleans up resources used by the filter
     */
    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}
