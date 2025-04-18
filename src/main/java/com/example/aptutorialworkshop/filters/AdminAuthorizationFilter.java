package com.example.aptutorialworkshop.filters;

/**
 * AdminAuthorizationFilter
 *
 * This class should be implemented as a filter to check if a user has admin role
 * before allowing access to admin resources. When implemented, it will intercept
 * requests to admin URLs and redirect unauthorized users to the login page.
 *
 * Implementation Guidelines:
 * 1. Make this class implement the Filter interface
 * 2. Add necessary imports (jakarta.servlet.*, jakarta.servlet.http.*, etc.)
 * 3. Add the @WebFilter annotation with appropriate URL patterns to protect
 *    (e.g., "/AdminDashboardServlet", "/WEB-INF/views/admin-dashboard.jsp")
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
public class AdminAuthorizationFilter {
    // Implement this class as a Filter
}
