package com.example.aptutorialworkshop.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * AdminAuthorizationFilter
 *
 * This class should be implemented as a filter to check if a user has admin role
 * before allowing access to admin resources. When implemented, it will intercept
 * requests to admin URLs and redirect unauthorized users to the login page.
 *
 * Implementation Guidelines:
 * 1. Uncomment the @WebFilter annotation with appropriate URL patterns to protect
 * 2. Implement the doFilter method to check for admin authorization
 *
 * In the doFilter method, you should:
 * 1. Cast the request and response to HttpServletRequest and HttpServletResponse
 * 2. Get the current session (without creating a new one if none exists)
 * 3. Check if the user is authenticated and has the admin role
 * 4. If the user is an admin, allow the request to proceed
 * 5. Otherwise, redirect to the login page
 *
 * Example URL patterns to protect:
 * - "/AdminDashboardServlet"
 * - "/WEB-INF/views/admin-dashboard.jsp"
 * - Any other admin-specific resources
 *
 * To check if a user has admin role, you would:
 * 1. Get the user object from the session
 * 2. Check if the user's role is UserModel.Role.admin
 */
// @WebFilter(urlPatterns = {"/AdminDashboardServlet", "/WEB-INF/views/admin-dashboard.jsp"})
public class AdminAuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // TODO: Implement admin authorization logic here

        // For now, just pass the request along the filter chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup code
    }
}
