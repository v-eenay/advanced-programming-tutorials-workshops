package com.example.aptutorialworkshop.models;

import java.io.Serializable;

/**
 * UserModel Class
 *
 * This class represents a user in the system. It contains all the user attributes
 * such as id, name, email, password, role, and profile image.
 *
 * The class implements Serializable to support session management, allowing
 * the user object to be stored in the session and retrieved across requests.
 *
 * In a production environment, consider the following enhancements:
 * 1. Password hashing: Store hashed passwords instead of plain text
 * 2. Input validation: Add validation for email format, password strength, etc.
 * 3. Additional user attributes: Add more user information as needed
 */
public class UserModel implements Serializable {

    /**
     * Role Enumeration
     *
     * Defines the possible roles a user can have in the system:
     * - admin: Has administrative privileges
     * - user: Regular user with limited access
     *
     * When implementing session management, the role can be used for authorization:
     * 1. Store the user's role in the session after login
     * 2. Check the role before allowing access to protected resources
     * 3. Use role-based filters to restrict access to certain pages
     */
    public enum Role{admin, user}

    // Unique identifier for the user
    private int id;

    // User's full name
    private String name;

    // User's email address (used for login)
    private String email;

    // User's password (should be hashed in production)
    private String password;

    // User's role (admin or regular user)
    private Role role;

    // User's profile picture stored as byte array
    private byte[] image;

    /**
     * Default constructor
     * Required for JavaBean specification and session serialization
     */
    public UserModel() {
    }

    /**
     * Get the user's ID
     *
     * @return The user's unique identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Set the user's ID
     *
     * @param id The user's unique identifier
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the user's name
     *
     * @return The user's full name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the user's name
     *
     * @param name The user's full name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the user's email
     *
     * @return The user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the user's email
     *
     * @param email The user's email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the user's password
     *
     * @return The user's password (should be hashed in production)
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the user's password
     *
     * @param password The user's password
     *
     * Note: In a production environment, this should hash the password
     * before storing it. Example implementation with BCrypt:
     *
     * this.password = BCrypt.hashpw(password, BCrypt.gensalt());
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the user's role
     *
     * @return The user's role (admin or user)
     */
    public Role getRole() {
        return role;
    }

    /**
     * Set the user's role
     *
     * @param role The user's role
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Get the user's profile image
     *
     * @return The user's profile image as a byte array
     */
    public byte[] getImage() {
        return image;
    }

    /**
     * Set the user's profile image
     *
     * @param image The user's profile image as a byte array
     */
    public void setImage(byte[] image) {
        this.image = image;
    }

    /**
     * Session Management Implementation Guide
     *
     * This model class is designed to work with session management. Here's how to implement it:
     *
     * 1. Creating a session and storing user information:
     *    After successful login in LoginServlet:
     *    ```java
     *    HttpSession session = request.getSession();
     *    session.setAttribute("user", userObject);
     *    // Optionally set session timeout (in seconds)
     *    session.setMaxInactiveInterval(1800); // 30 minutes
     *    ```
     *
     * 2. Retrieving user information from session:
     *    In any servlet or JSP:
     *    ```java
     *    HttpSession session = request.getSession(false); // Don't create a new session if none exists
     *    if (session != null) {
     *        UserModel user = (UserModel) session.getAttribute("user");
     *        if (user != null) {
     *            // User is logged in, use user information
     *        }
     *    }
     *    ```
     *
     * 3. Implementing logout:
     *    ```java
     *    HttpSession session = request.getSession(false);
     *    if (session != null) {
     *        session.invalidate(); // Removes all session attributes
     *    }
     *    ```
     *
     * 4. For cookies implementation:
     *    - Create a remember-me cookie:
     *      ```java
     *      Cookie userCookie = new Cookie("user_email", user.getEmail());
     *      userCookie.setMaxAge(60*60*24*30); // 30 days
     *      userCookie.setPath("/");
     *      response.addCookie(userCookie);
     *      ```
     *
     *    - Read cookies for auto-login:
     *      ```java
     *      Cookie[] cookies = request.getCookies();
     *      if (cookies != null) {
     *          for (Cookie cookie : cookies) {
     *              if ("user_email".equals(cookie.getName())) {
     *                  String email = cookie.getValue();
     *                  // Auto-login logic
     *              }
     *          }
     *      }
     *      ```
     */
}
