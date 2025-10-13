package com.napier.devops.city_report;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Manages database connections for the application.
 * Provides static methods to establish and close connections to the MySQL database.
 * Configuration is handled through Docker Compose environment variables.
 */
public class DatabaseConnection {
    // Static connection object shared across the application
    private static Connection con = null;

    /**
     * Establishes a connection to the MySQL database.
     * Uses configuration provided by Docker Compose.
     *
     * @return Connection object if successful, exits application if connection fails
     */
    public static Connection connect() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection using Docker environment configuration
            con = DriverManager.getConnection(
                    "jdbc:mysql://db:3306/world?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                    "root",
                    "example"
            );

            // Print confirmation message
            System.out.println("Connected to database successfully!");
        } catch (Exception e) {
            // Print error and terminate if connection fails
            System.out.println("Database connection failed: " + e.getMessage());
            System.exit(-1);
        }
        return con;
    }

    /**
     * Closes the active database connection if it exists.
     * Should be called when the application shuts down.
     */
    public static void disconnect() {
        try {
            if (con != null) {
                con.close();
                System.out.println("Disconnected from database.");
            }
        } catch (Exception e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
