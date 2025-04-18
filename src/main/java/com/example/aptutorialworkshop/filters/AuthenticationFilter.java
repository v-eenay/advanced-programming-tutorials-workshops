package com.example.aptutorialworkshop.filters;

import com.example.aptutorialworkshop.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * AuthenticationFilter
 *
 * This filter checks if a user is authenticated before allowing access to protected resources.
 * It intercepts requests to protected URLs and redirects unauthenticated users to the login page.
 *
 * The filter uses the AuthService to check if the user is authenticated.
 * Public resources like login and registration pages are excluded from authentication checks.
 */
@WebFilter(urlPatterns = {"/UserDashboardServlet", "/AdminDashboardServlet", "/WEB-INF/views/user-dashboard.jsp", "/WEB-INF/views/admin-dashboard.jsp"})
public class AuthenticationFilter implements Filter {

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
     * Filters requests to protected resources
     *
     * This method checks if the user is authenticated using the AuthService.
     * If the user is authenticated or accessing a public resource, the request is allowed to proceed.
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

        // Check if the user is authenticated using the AuthService
        boolean isAuthenticated = AuthService.isAuthenticated(httpRequest);

        // Get the requested URL
        String requestURI = httpRequest.getRequestURI();

        // Check if the user is accessing a public resource
        boolean isPublicResource = requestURI.contains("login.jsp") ||
                                  requestURI.contains("register.jsp") ||
                                  requestURI.contains("LoginServlet") ||
                                  requestURI.contains("RegisterServlet") ||
                                  requestURI.contains("/assets/");

        // If the user is authenticated or accessing a public resource, allow the request
        if (isAuthenticated || isPublicResource) {
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
