package com.example.aptutorialworkshop.services;

import com.example.aptutorialworkshop.dao.UserDAO;
import com.example.aptutorialworkshop.models.UserModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * AuthService Class
 *
 * This service class provides authentication and user management functionality.
 * It acts as an intermediary between the controllers and the data access layer,
 * encapsulating the business logic related to user authentication and registration.
 *
 * For session management implementation:
 * - Use the login method to authenticate users and create sessions
 * - Use the getUserById method to retrieve user information for session validation
 * - Consider adding methods for session validation and user authorization
 */
public class AuthService {

    /**
     * Register a new user
     *
     * This method creates a new UserModel object with the provided information
     * and passes it to the UserDAO for registration in the database.
     * The password is automatically hashed by the UserModel.setPassword method.
     *
     * @param name The user's full name
     * @param email The user's email address
     * @param password The user's plain text password (will be hashed)
     * @param role The user's role ("admin" or "user")
     * @param image The user's profile picture as a byte array
     * @return The generated user ID if registration is successful, -1 otherwise
     */
    public static int register(String name, String email, String password, String role, byte[] image){
        // Create a new UserModel object
        UserModel user = new UserModel();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password); // Password will be hashed by UserModel.setPassword
        user.setRole(UserModel.Role.valueOf(role));
        user.setImage(image);

        // Register the user and return the generated ID
        return UserDAO.registerUser(user);
    }

    /**
     * Authenticate a user
     *
     * This method retrieves a user by email and verifies the provided password
     * against the stored hash using BCrypt.
     *
     * @param email The user's email address
     * @param password The user's plain text password
     * @return The complete UserModel if authentication is successful, null otherwise
     */
    public static UserModel login(String email, String password){
        // Get the user by email
        UserModel user = UserDAO.getUserByEmail(email);

        // If user exists and password matches the hash
        if (user != null && user.verifyPassword(password)) {
            return user;
        }

        // Authentication failed
        return null;
    }

    /**
     * Retrieve a user by ID
     *
     * This method retrieves a complete user record from the database using the user ID.
     *
     * @param id The user ID to look up
     * @return The complete UserModel if found, null otherwise
     *
     * For session implementation:
     * This method is useful for:
     * 1. Refreshing user data during a session
     * 2. Validating that a user ID stored in a session still exists in the database
     * 3. Retrieving user details when only the ID is available (e.g., from a session)
     */
    public static UserModel getUserById(int id) {
        return UserDAO.getUserById(id);
    }

    /**
     * Validate if a user is authenticated in the current session
     *
     * This method checks if there is a valid user object in the session.
     *
     * @param request The HTTP request object
     * @return true if the user is authenticated, false otherwise
     */
    public static boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        UserModel user = (UserModel) session.getAttribute("user");
        return user != null;
    }

    /**
     * Check if the authenticated user has admin role
     *
     * This method checks if there is a valid user object in the session
     * and if that user has the admin role.
     *
     * @param request The HTTP request object
     * @return true if the user is an admin, false otherwise
     */
    public static boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        UserModel user = (UserModel) session.getAttribute("user");
        return user != null && user.getRole() == UserModel.Role.admin;
    }

    /**
     * Create a session for an authenticated user
     *
     * This method creates a new session (or uses an existing one) and stores
     * the user object in it. It also sets the session timeout.
     *
     * @param request The HTTP request object
     * @param user The authenticated user object
     * @param timeoutSeconds The session timeout in seconds (default: 30 minutes)
     */
    public static void createUserSession(HttpServletRequest request, UserModel user, int timeoutSeconds) {
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setMaxInactiveInterval(timeoutSeconds);
    }

    /**
     * Get the currently authenticated user from the session
     *
     * @param request The HTTP request object
     * @return The authenticated user object, or null if not authenticated
     */
    public static UserModel getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (UserModel) session.getAttribute("user");
    }

    /**
     * Invalidate the current user session (logout)
     *
     * @param request The HTTP request object
     */
    public static void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
