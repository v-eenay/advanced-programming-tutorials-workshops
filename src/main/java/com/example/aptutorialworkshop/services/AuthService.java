package com.example.aptutorialworkshop.services;

import com.example.aptutorialworkshop.dao.UserDAO;
import com.example.aptutorialworkshop.models.UserModel;

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
     *
     * @param name The user's full name
     * @param email The user's email address
     * @param password The user's password (should be hashed in production)
     * @param role The user's role ("admin" or "user")
     * @param image The user's profile picture as a byte array
     * @return The generated user ID if registration is successful, -1 otherwise
     *
     * For session implementation:
     * After successful registration, you might want to create a session for the new user
     * or redirect them to the login page to authenticate with their new credentials.
     */
    public static int register(String name, String email, String password, String role, byte[] image){
        // Create a new UserModel object
        UserModel user = new UserModel();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password); // In production, this should be hashed
        user.setRole(UserModel.Role.valueOf(role));
        user.setImage(image);

        // Register the user and return the generated ID
        return UserDAO.registerUser(user);
    }

    /**
     * Authenticate a user
     *
     * This method creates a UserModel with the provided email and password
     * and passes it to the UserDAO for authentication against the database.
     *
     * @param email The user's email address
     * @param password The user's password
     * @return The complete UserModel if authentication is successful, null otherwise
     *
     * For session implementation:
     * After successful authentication:
     * 1. Create a session: HttpSession session = request.getSession();
     * 2. Store user information: session.setAttribute("user", authenticatedUser);
     * 3. Set session timeout: session.setMaxInactiveInterval(1800); // 30 minutes
     * 4. For remember-me functionality, create a cookie with the user's email
     */
    public static UserModel login(String email, String password){
        // Create a UserModel with the provided credentials
        UserModel user = new UserModel();
        user.setEmail(email);
        user.setPassword(password);

        // Authenticate the user and return the result
        return UserDAO.loginUser(user);
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
     * Session Management Implementation Guide
     *
     * To implement complete session management, consider adding these methods:
     *
     * 1. Create a method to validate sessions:
     *    ```java
     *    public static boolean validateSession(HttpServletRequest request) {
     *        HttpSession session = request.getSession(false);
     *        if (session == null) {
     *            return false;
     *        }
     *
     *        UserModel user = (UserModel) session.getAttribute("user");
     *        return user != null;
     *    }
     *    ```
     *
     * 2. Create a method to check user roles for authorization:
     *    ```java
     *    public static boolean isAdmin(HttpServletRequest request) {
     *        HttpSession session = request.getSession(false);
     *        if (session == null) {
     *            return false;
     *        }
     *
     *        UserModel user = (UserModel) session.getAttribute("user");
     *        return user != null && user.getRole() == UserModel.Role.admin;
     *    }
     *    ```
     *
     * 3. Create a method to handle logout:
     *    ```java
     *    public static void logout(HttpServletRequest request, HttpServletResponse response) {
     *        HttpSession session = request.getSession(false);
     *        if (session != null) {
     *            session.invalidate();
     *        }
     *
     *        // Also clear any authentication cookies
     *        Cookie[] cookies = request.getCookies();
     *        if (cookies != null) {
     *            for (Cookie cookie : cookies) {
     *                if ("user_email".equals(cookie.getName())) {
     *                    cookie.setMaxAge(0); // Delete the cookie
     *                    cookie.setPath("/");
     *                    response.addCookie(cookie);
     *                }
     *            }
     *        }
     *    }
     *    ```
     */
}
