package com.example.aptutorialworkshop.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * AuthenticationFilter
 *
 * This class should be implemented as a filter to check if a user is authenticated
 * before allowing access to protected resources. When implemented, it will intercept
 * requests to protected URLs and redirect unauthenticated users to the login page.
 *
 * Implementation Guidelines:
 * 1. Uncomment the @WebFilter annotation with appropriate URL patterns to protect
 * 2. Implement the doFilter method to check for authentication
 *
 * In the doFilter method, you should:
 * 1. Cast the request and response to HttpServletRequest and HttpServletResponse
 * 2. Get the current session (without creating a new one if none exists)
 * 3. Check if the user is authenticated by looking for a user object in the session
 * 4. Get the requested URL and check if it's a public resource (login page, register page, etc.)
 * 5. If the user is authenticated or accessing a public resource, allow the request to proceed
 * 6. Otherwise, redirect to the login page
 *
 * Example URL patterns to protect:
 * - "/UserDashboardServlet"
 * - "/AdminDashboardServlet"
 * - "/WEB-INF/views/user-dashboard.jsp"
 * - "/WEB-INF/views/admin-dashboard.jsp"
 *
 * Example public resources to exclude from authentication:
 * - "login.jsp"
 * - "register.jsp"
 * - "LoginServlet"
 * - "RegisterServlet"
 * - "/assets/" (for CSS, JS, images, etc.)
 */
// @WebFilter(urlPatterns = {"/UserDashboardServlet", "/AdminDashboardServlet", "/WEB-INF/views/user-dashboard.jsp", "/WEB-INF/views/admin-dashboard.jsp"})
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // TODO: Implement authentication logic here

        // For now, just pass the request along the filter chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup code
    }
}
