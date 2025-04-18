package com.example.aptutorialworkshop.dao;

import com.example.aptutorialworkshop.models.UserModel;
import com.example.aptutorialworkshop.utils.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserDAO (Data Access Object) Class
 *
 * Handles database operations for users including registration, authentication, and retrieval.
 * Supports BCrypt password hashing and session management.
 */
public class UserDAO {
    // SQL query to insert a new user into the database
    public static final String INSERT_USER = "INSERT INTO users(name,email,password,role, profile_picture) VALUES(?,?,?,?, ?)";

    // SQL query to select a user by email and password for authentication
    public static final String SELECT_USER_BY_EMAIL_PASSWORD = "SELECT * FROM users WHERE email = ? AND password = ?";

    // SQL query to select a user by ID
    public static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";

    // SQL query to select a user by email
    public static final String SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";

    /**
     * Register a new user
     *
     * Inserts user record into database with BCrypt hashed password.
     *
     * @param user UserModel with registration information
     * @return Generated user ID if successful, -1 otherwise
     */
    public static int registerUser(UserModel user) {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_USER, PreparedStatement.RETURN_GENERATED_KEYS);) {
            // Set parameters for the prepared statement
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword()); // Password is already hashed by UserModel
            ps.setString(4, user.getRole().name());
            ps.setBytes(5, user.getImage());

            // Execute the insert statement
            int rows = ps.executeUpdate();

            // If insertion was successful, get the generated user ID
            if (rows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            // Log the exception details for debugging
            System.err.println("Error registering user: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return -1; // Return -1 to indicate registration failure
    }

    /**
     * Authenticate a user by email and password
     *
     * Legacy method kept for compatibility. Authentication now uses getUserByEmail
     * with BCrypt verification in AuthService.
     *
     * @param user UserModel with email and password
     * @return UserModel if found, null otherwise
     * @deprecated Use getUserByEmail with BCrypt verification instead
     */
    @Deprecated
    public static UserModel loginUser(UserModel user) {
        // This method is kept for backward compatibility
        // Authentication is now handled by retrieving the user by email and then verifying the password
        return getUserByEmail(user.getEmail());
    }

    /**
     * Get user by email
     *
     * Retrieves user record by email for authentication with BCrypt.
     *
     * @param email Email address to look up
     * @return Complete UserModel if found, null otherwise
     */
    public static UserModel getUserByEmail(String email) {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_EMAIL);) {
            // Set the email parameter
            ps.setString(1, email);

            // Execute the query
            ResultSet rs = ps.executeQuery();

            // If the user is found, create and return a UserModel object
            if (rs.next()) {
                UserModel userFromDB = new UserModel();
                userFromDB.setId(rs.getInt("id"));
                userFromDB.setName(rs.getString("name"));
                userFromDB.setEmail(rs.getString("email"));
                userFromDB.setPassword(rs.getString("password"));
                userFromDB.setRole(UserModel.Role.valueOf(rs.getString("role")));
                userFromDB.setImage(rs.getBytes("profile_picture"));
                return userFromDB;
            }
        } catch (SQLException e) {
            // Log the exception details for debugging
            System.err.println("Error retrieving user by email: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null; // Return null if user not found
    }

    /**
     * Get user by ID
     *
     * Retrieves complete user record using ID.
     * Used after registration and for session management.
     *
     * @param id User ID to look up
     * @return Complete UserModel if found, null otherwise
     */
    public static UserModel getUserById(int id) {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_ID);) {
            // Set the user ID parameter
            ps.setInt(1, id);

            // Execute the query
            ResultSet rs = ps.executeQuery();

            // If the user is found, create and return a UserModel object
            if (rs.next()) {
                UserModel userFromDB = new UserModel();
                userFromDB.setId(rs.getInt("id"));
                userFromDB.setName(rs.getString("name"));
                userFromDB.setEmail(rs.getString("email"));
                userFromDB.setPassword(rs.getString("password"));
                userFromDB.setRole(UserModel.Role.valueOf(rs.getString("role")));
                userFromDB.setImage(rs.getBytes("profile_picture"));
                return userFromDB;
            }
        } catch (SQLException e) {
            // Log the exception details for debugging
            System.err.println("Error retrieving user by ID: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null; // Return null if user not found
    }

    /**
     * Filter Implementation Guide
     *
     * To implement authentication and authorization filters:
     *
     * 1. Create an AuthenticationFilter class:
     *    ```java
     *    @WebFilter(urlPatterns = {"/*"})  // Apply to all URLs
     *    public class AuthenticationFilter implements Filter {
     *        @Override
     *        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
     *                throws IOException, ServletException {
     *            HttpServletRequest httpRequest = (HttpServletRequest) request;
     *            HttpServletResponse httpResponse = (HttpServletResponse) response;
     *            HttpSession session = httpRequest.getSession(false);
     *
     *            // Get the requested URL
     *            String requestURI = httpRequest.getRequestURI();
     *
     *            // Check if the user is accessing a public resource
     *            boolean isPublicResource = requestURI.contains("login.jsp") ||
     *                                      requestURI.contains("register.jsp") ||
     *                                      requestURI.contains("LoginServlet") ||
     *                                      requestURI.contains("RegisterServlet") ||
     *                                      requestURI.contains("/assets/");
     *
     *            // If it's not a public resource and no session exists, redirect to login
     *            if (!isPublicResource && (session == null || session.getAttribute("user") == null)) {
     *                httpResponse.sendRedirect(httpRequest.getContextPath() + "/LoginServlet");
     *            } else {
     *                // Continue the filter chain
     *                chain.doFilter(request, response);
     *            }
     *        }
     *
     *        // Other filter methods (init, destroy)
     *    }
     *    ```
     *
     * 2. Create a RoleBasedAuthorizationFilter for role-specific access:
     *    ```java
     *    @WebFilter(urlPatterns = {"/AdminDashboardServlet", "/admin/*"})  // Apply to admin URLs
     *    public class AdminAuthorizationFilter implements Filter {
     *        @Override
     *        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
     *                throws IOException, ServletException {
     *            HttpServletRequest httpRequest = (HttpServletRequest) request;
     *            HttpServletResponse httpResponse = (HttpServletResponse) response;
     *            HttpSession session = httpRequest.getSession(false);
     *
     *            boolean isAuthorized = false;
     *
     *            if (session != null && session.getAttribute("user") != null) {
     *                UserModel user = (UserModel) session.getAttribute("user");
     *                if (user.getRole() == UserModel.Role.admin) {
     *                    isAuthorized = true;
     *                }
     *            }
     *
     *            if (isAuthorized) {
     *                chain.doFilter(request, response);
     *            } else {
     *                httpResponse.sendRedirect(httpRequest.getContextPath() + "/LoginServlet");
     *            }
     *        }
     *
     *        // Other filter methods (init, destroy)
     *    }
     *    ```
     */
}
