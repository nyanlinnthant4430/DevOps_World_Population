package com.napier.devops.feature_policymaker;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static Connection con = null;

    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                    "jdbc:mysql://db:3306/world?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                    "root",
                    "example"
            );

            System.out.println("Connected to database successfully!");
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());

            // ❌ REMOVE System.exit(-1)
            // ✔ REPLACE with exception so JUnit can catch it
            throw new RuntimeException("Database connection failed: " + e.getMessage());
        }
        return con;
    }

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
