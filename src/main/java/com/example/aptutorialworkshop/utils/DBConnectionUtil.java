package com.example.aptutorialworkshop.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database Connection Utility
 *
 * This class provides database connection functionality using properties
 * defined in application.properties file.
 *
 * When implementing session management, this utility class will be used by:
 * 1. The DAO classes to establish database connections for user authentication
 * 2. Session validation processes that need to verify user information
 * 3. Any database operations performed during a user's session
 */
public class DBConnectionUtil {
    private static final String URL;
    private static final String USER;
    private static final String PASS;
    private static final int MIN_CONNECTIONS;
    private static final int MAX_CONNECTIONS;
    private static final int CONNECTION_TIMEOUT;

    // Static initialization block to load properties once when the class is loaded
    static {
        try (InputStream is = DBConnectionUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (is == null) {
                throw new RuntimeException("application.properties file not found in classpath");
            }

            Properties prop = new Properties();
            prop.load(is);

            // Database connection properties
            URL = prop.getProperty("db.url");
            USER = prop.getProperty("db.username");
            PASS = prop.getProperty("db.password");
            String driver = prop.getProperty("db.driver");

            // Connection pool properties (with defaults if not specified)
            MIN_CONNECTIONS = Integer.parseInt(prop.getProperty("db.min_connections", "5"));
            MAX_CONNECTIONS = Integer.parseInt(prop.getProperty("db.max_connections", "20"));
            CONNECTION_TIMEOUT = Integer.parseInt(prop.getProperty("db.connection_timeout", "30000"));

            // Load the JDBC driver
            Class.forName(driver);

            System.out.println("Database connection properties loaded successfully");
        } catch (IOException | ClassNotFoundException | NumberFormatException e) {
            System.err.println("Error loading database properties: " + e.getMessage());
            throw new RuntimeException("Failed to load database properties", e);
        }
    }

    /**
     * Get a database connection
     *
     * @return A connection to the database
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /**
     * Get the minimum number of connections for the connection pool
     *
     * @return Minimum number of connections
     */
    public static int getMinConnections() {
        return MIN_CONNECTIONS;
    }

    /**
     * Get the maximum number of connections for the connection pool
     *
     * @return Maximum number of connections
     */
    public static int getMaxConnections() {
        return MAX_CONNECTIONS;
    }

    /**
     * Get the connection timeout in milliseconds
     *
     * @return Connection timeout
     */
    public static int getConnectionTimeout() {
        return CONNECTION_TIMEOUT;
    }
}
