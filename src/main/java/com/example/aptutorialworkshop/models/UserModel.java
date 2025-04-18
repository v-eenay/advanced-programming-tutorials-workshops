package com.example.aptutorialworkshop.models;

import java.io.Serializable;
import org.mindrot.jbcrypt.BCrypt;

/**
 * UserModel Class
 *
 * Represents a user with attributes like id, name, email, password, role, and profile image.
 * Implements Serializable to support session storage and retrieval.
 * Uses BCrypt for secure password hashing.
 */
public class UserModel implements Serializable {

    /**
     * Role Enumeration
     *
     * Defines user roles in the system:
     * - admin: Has administrative privileges
     * - user: Regular user with limited access
     */
    public enum Role{admin, user}

    // Unique identifier for the user
    private int id;

    // User's full name
    private String name;

    // User's email address (used for login)
    private String email;

    // User's password (BCrypt hashed)
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
     * This method hashes the password using BCrypt before storing it.
     * BCrypt automatically generates and includes a salt in the hash.
     *
     * @param password The user's plain text password to be hashed and stored
     */
    public void setPassword(String password) {
        // Check if the password is already hashed (starts with $2a$)
        if (password != null && !password.startsWith("$2a$")) {
            // Hash the password with BCrypt
            this.password = BCrypt.hashpw(password, BCrypt.gensalt(12));
        } else {
            // Password is already hashed or null
            this.password = password;
        }
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
     * Verify a plain text password against the stored hash
     *
     * This method uses BCrypt to check if the provided plain text password
     * matches the stored hashed password.
     *
     * @param plainTextPassword The plain text password to verify
     * @return true if the password matches, false otherwise
     */
    public boolean verifyPassword(String plainTextPassword) {
        if (this.password == null || plainTextPassword == null) {
            return false;
        }
        return BCrypt.checkpw(plainTextPassword, this.password);
    }


}
