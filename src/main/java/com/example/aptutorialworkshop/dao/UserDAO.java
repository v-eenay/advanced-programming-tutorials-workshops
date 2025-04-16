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
 * This class handles all database operations related to the User entity.
 * It provides methods for user registration, authentication, and retrieval.
 *
 * The DAO pattern separates the data access logic from the business logic,
 * making the code more maintainable and testable.
 *
 * When implementing session management and filters:
 * 1. Use the loginUser method for authentication in the login process
 * 2. Use the getUserById method to retrieve user details when needed
 * 3. Consider adding methods for updating user information
 */
public class UserDAO {
    // SQL query to insert a new user into the database
    public static final String INSERT_USER = "INSERT INTO users(name,email,password,role, profile_picture) VALUES(?,?,?,?, ?)";

    // SQL query to select a user by email and password for authentication
    public static final String SELECT_USER_BY_EMAIL_PASSWORD = "SELECT * FROM users WHERE email = ? AND password = ?";

    // SQL query to select a user by ID
    public static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";

    /**
     * Register a new user in the database
     *
     * This method inserts a new user record into the database and returns the generated user ID.
     * It's used during the registration process.
     *
     * @param user The UserModel object containing the user information to be registered
     * @return The generated user ID if registration is successful, -1 otherwise
     *
     * For session implementation:
     * After successful registration, you might want to:
     * 1. Create a session for the newly registered user
     * 2. Set session attributes with user information
     * 3. Redirect to the appropriate dashboard based on user role
     */
    public static int registerUser(UserModel user) {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_USER, PreparedStatement.RETURN_GENERATED_KEYS);) {
            // Set parameters for the prepared statement
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword()); // In production, this should be hashed
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
     * This method checks if the provided email and password match a user in the database.
     * If authentication is successful, it returns the complete user object.
     *
     * @param user A UserModel object containing at least the email and password to check
     * @return The complete UserModel if authentication is successful, null otherwise
     *
     * For session implementation:
     * After successful authentication, you should:
     * 1. Create a session for the user
     * 2. Store the user object or user ID in the session
     * 3. Optionally create a remember-me cookie if the user selected that option
     * 4. Redirect to the appropriate dashboard based on user role
     *
     * For security enhancement:
     * In production, this method should be updated to use password hashing:
     * 1. Retrieve the hashed password from the database using only the email
     * 2. Use a password hashing library to verify the provided password against the hash
     */
    public static UserModel loginUser(UserModel user) {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_EMAIL_PASSWORD);) {
            // Set parameters for the prepared statement
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword()); // In production, use password hashing

            // Execute the query
            ResultSet rs = ps.executeQuery();

            // If a matching user is found, create and return a UserModel object
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
            System.err.println("Error authenticating user: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null; // Return null if authentication fails
    }

    /**
     * Retrieve a user by their ID
     *
     * This method fetches a complete user record from the database using the user ID.
     * It's useful for retrieving user information when only the ID is available.
     *
     * @param id The user ID to look up
     * @return The complete UserModel if found, null otherwise
     *
     * For session implementation:
     * This method is particularly useful when:
     * 1. Storing only the user ID in the session for security reasons
     * 2. Retrieving the full user object when needed using the ID from the session
     * 3. Refreshing user data from the database during a session
     *
     * Example usage with sessions:
     * ```java
     * // In a servlet that needs user information
     * HttpSession session = request.getSession(false);
     * if (session != null) {
     *     Integer userId = (Integer) session.getAttribute("userId");
     *     if (userId != null) {
     *         UserModel user = UserDAO.getUserById(userId);
     *         // Use the user object
     *     }
     * }
     * ```
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
