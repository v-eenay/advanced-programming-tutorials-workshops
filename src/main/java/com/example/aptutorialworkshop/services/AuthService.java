package com.example.aptutorialworkshop.services;

import com.example.aptutorialworkshop.dao.UserDAO;
import com.example.aptutorialworkshop.models.UserModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * AuthService Class
 *
 * Provides authentication, session management, and user operations.
 * Handles user login, registration, session creation/validation, and logout.
 * Uses BCrypt for secure password verification.
 */
public class AuthService {

    /**
     * Register a new user
     *
     * Creates a user with the provided information and registers in database.
     * Password is automatically hashed using BCrypt.
     *
     * @param name User's full name
     * @param email User's email address
     * @param password Plain text password (will be hashed)
     * @param role User's role ("admin" or "user")
     * @param image User's profile picture as byte array
     * @return Generated user ID if successful, -1 otherwise
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
     * Retrieves user by email and verifies password using BCrypt.
     *
     * @param email User's email address
     * @param password User's plain text password
     * @return Complete UserModel if authenticated, null otherwise
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
     * Gets complete user record from database using ID.
     * Used after registration to retrieve the newly created user.
     *
     * @param id User ID to look up
     * @return Complete UserModel if found, null otherwise
     */
    public static UserModel getUserById(int id) {
        return UserDAO.getUserById(id);
    }

    /**
     * Check if user is authenticated
     *
     * Verifies if a valid user object exists in the current session.
     *
     * @param request HTTP request object
     * @return true if authenticated, false otherwise
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
     * Check if user has admin role
     *
     * Verifies if the authenticated user has admin privileges.
     *
     * @param request HTTP request object
     * @return true if user is admin, false otherwise
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
     * Create user session
     *
     * Creates/uses a session and stores user object with timeout.
     *
     * @param request HTTP request object
     * @param user Authenticated user object
     * @param timeoutSeconds Session timeout in seconds
     */
    public static void createUserSession(HttpServletRequest request, UserModel user, int timeoutSeconds) {
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setMaxInactiveInterval(timeoutSeconds);
    }

    /**
     * Get current user
     *
     * Retrieves authenticated user from session.
     *
     * @param request HTTP request object
     * @return User object or null if not authenticated
     */
    public static UserModel getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (UserModel) session.getAttribute("user");
    }

    /**
     * Logout user
     *
     * Invalidates the current session.
     *
     * @param request HTTP request object
     */
    public static void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
