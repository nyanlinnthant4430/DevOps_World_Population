package com.napier.devops;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Main application entry point.
 * Can connect to either LOCALHOST or DOCKER DATABASE depending on args.
 */
public class App {

    private Connection con = null;

    /**
     * Connect to MySQL for BOTH modes:
     *
     * 1️⃣ Local mode (IntelliJ)
     *      - No args
     *      - Connects to: localhost:33080
     *
     * 2️⃣ Docker / CI mode
     *      - args: host port-delay
     *      - Example:
     *          db:3306 30000
     */
    public void connect(String location, int delay) {
        int retries = 10;

        while (retries > 0) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Wait (only used in Docker/CI)
                Thread.sleep(delay);

                con = DriverManager.getConnection(
                        "jdbc:mysql://" + location + "/world?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                        "root",
                        "example"
                );

                System.out.println("Connected to database successfully: " + location);
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
            System.out.println("Failed to connect to database at: " + location);
            System.exit(-1);
        }
    }

    /**
     * Backwards-compatible LOCALHOST connect()
     * → Calls the unified method with built-in settings.
     */
    public void connect() {
        connect("localhost:33080", 0);
    }

    /**
     * Disconnect from MySQL database.
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

    /**
     * Getter for database connection.
     */
    public Connection getConnection() {
        return con;
    }

    /**
     * Runs population reports.
     */
    private void runAllPopulationReports() {
        System.out.println("\n===== 1. Population of each Continent =====");
        new ReportContinent().generateReport(con);

        System.out.println("\n===== 2. Population of each Region =====");
        new ReportRegion().generateReport(con);

        System.out.println("\n===== 3. Population of each Country =====");
        new ReportCountry().generateReport(con);

        System.out.println("\nAll world population reports have been generated.");
    }

    /**
     * Application entry point.
     */
    public static void main(String[] args) {
        App app = new App();

        // MODE 1: Local IntelliJ run (no args)
        if (args.length < 2) {
            app.connect();             // localhost:33080
        }
        // MODE 2: Docker / CI run
        else {
            String host = args[0];     // example: db:3306
            int delay = Integer.parseInt(args[1]); // example: 30000
            app.connect(host, delay);
        }

        try {
            app.runAllPopulationReports();
        } catch (Exception e) {
            System.out.println("Error running application: " + e.getMessage());
        } finally {
            app.disconnect();
        }
    }
}