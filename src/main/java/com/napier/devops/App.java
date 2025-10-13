package com.napier.devops;

import com.napier.devops.city_report.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class App {

    private Connection con = null;

    /**
     * Connect to MySQL database
     */
    public void connect() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            con = DriverManager.getConnection(
                    "jdbc:mysql://db:3306/world?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                    "root",
                    "example"
            );

            System.out.println("Connected to database successfully!");
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Disconnect from MySQL database
     */
    public void disconnect() {
        try {
            if (con != null) {
                con.close();
                System.out.println("Disconnected from database.");
            }
        } catch (Exception e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        App app = new App();

        // Connect to the database
        app.connect();

        // Show the menu
        Menu menu = new Menu();
        menu.show(app.con);

        // Disconnect from the database
        app.disconnect();
    }
}
