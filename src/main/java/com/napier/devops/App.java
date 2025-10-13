package com.napier.devops;

import com.napier.devops.city_report.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class App {

    private Connection con = null;

    /**
     * Connect to MySQL database with retry logic.
     */
    public void connect() {
        int retries = 10; // number of retries
        while (retries > 0) {
            try {
                // Load MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Establish connection
                con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/world?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                        "root",
                        "example"
                );

                System.out.println("✅ Connected to database successfully!");
                break; // stop retrying when connection succeeds

            } catch (Exception e) {
                System.out.println("⚠️ Waiting for database to be ready... (" + retries + " retries left)");
                retries--;
                try {
                    Thread.sleep(5000); // wait 5 seconds before next try
                } catch (InterruptedException ignored) {}
            }
        }

        // if connection still fails
        if (con == null) {
            System.out.println("❌ Failed to connect to database after multiple attempts.");
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
                System.out.println("🔌 Disconnected from database.");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error closing connection: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        App app = new App();

        // Connect to the database (waits until DB is ready)
        app.connect();

        // Show the main menu
        Menu menu = new Menu();
        menu.show(app.con);

        // Disconnect from database before exit
        app.disconnect();
    }
}
