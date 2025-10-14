package com.napier.devops;

import com.napier.devops.city_report.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class App {

    public Connection con = null;

    /**
     * Connect to MySQL database with retry logic.
     */
    public void connect() {
        int retries = 10;
        while (retries > 0) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/world?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                        "root",
                        "example"
                );
                System.out.println("Connected to database successfully!");
                break;
            } catch (Exception e) {
                System.out.println("Waiting for database to be ready... (" + retries + " retries left)");
                retries--;
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {}
            }
        }
        if (con == null) {
            System.out.println("Failed to connect to database after multiple attempts.");
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
        app.connect();

        // Detect if running inside Docker (non-interactive)
        if (System.console() == null) {
            System.out.println("Running in non-interactive (Docker) mode...");
            ReportAllCitiesByPopulation.generateReport(app.con);
        } else {
            Menu.showMenu(app.con);
        }

        app.disconnect();
    }
}
