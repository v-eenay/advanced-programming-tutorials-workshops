package com.example.aptutorialworkshop.filters;

/**
 * AuthenticationFilter
 *
 * This class should be implemented as a filter to check if a user is authenticated
 * before allowing access to protected resources. When implemented, it will intercept
 * requests to protected URLs and redirect unauthenticated users to the login page.
 *
 * Implementation Guidelines:
 * 1. Make this class implement the Filter interface
 * 2. Add necessary imports (jakarta.servlet.*, jakarta.servlet.http.*, etc.)
 * 3. Add the @WebFilter annotation with appropriate URL patterns to protect
 *    (e.g., "/UserDashboardServlet", "/AdminDashboardServlet", etc.)
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
public class AuthenticationFilter {
    // Implement this class as a Filter
}
