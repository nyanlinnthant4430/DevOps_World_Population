package com.napier.devops;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Manages database connections for the application.
 * Provides static methods to establish and close connections to the MySQL database.
 */
public class DatabaseConnection {
    // Static connection object shared across the application
    private static Connection con = null;

    /**
     * Establishes a connection to the MySQL database.
     * Uses JDBC driver to connect to the 'world' database running on the 'db' host.
     *
     * @return Connection object if successful, exits application if connection fails
     */
    public static Connection connect() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection to the database with specified parameters
            con = DriverManager.getConnection(
                    "jdbc:mysql://db:3306/world?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                    "root",
                    "example"
            );

            // Print confirmation message
            System.out.println("Connected to database successfully!");

            // Print confirmation messages
            System.out.println("Successfully connected to database.");
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
            System.exit(-1);
        }
        return con;
    }

    /**
     * Closes the database connection if it exists.
     * Should be called when the application is shutting down to release resources.
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

