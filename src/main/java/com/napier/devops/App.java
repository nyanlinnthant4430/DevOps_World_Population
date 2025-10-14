package com.napier.devops;

import com.napier.devops.country_report.*;
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

                System.out.println(" Connected to database successfully!");
                break; // stop retrying when connection succeeds

            } catch (Exception e) {
                System.out.println(" Waiting for database to be ready... (" + retries + " retries left)");
                retries--;
                try {
                    Thread.sleep(5000); // wait 5 seconds before next try
                } catch (InterruptedException ignored) {}
            }
        }

        // if connection still fails
        if (con == null) {
            System.out.println(" Failed to connect to database after multiple attempts.");
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
        app.connect();

        // Detect if running inside Docker (no interactive input)
        if (System.console() == null) {
            System.out.println("Running in non-interactive mode (e.g., CI/CD).");

            // Run all country reports automatically
            ReportAllCountriesByPopulation.generateReport(app.con);

        } else {
            // Interactive mode (when running locally)
            Menu menu = new Menu();
            menu.showMenu(app.con);
        }

        app.disconnect();
    }
}
