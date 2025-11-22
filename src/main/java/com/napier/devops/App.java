package com.napier.devops;

import com.napier.devops.city_report.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class App {

    // DB connection
    private Connection con = null;

    public static void printCities(ArrayList<City> cities) {
        // similar to printCountries but using City getters
    }


    /**
     * Connect to MySQL database with retry logic.
     * Example:
     *  - location: "db:3306" for Docker, "localhost:33060" for local
     *  - delay: milliseconds to wait between retries (e.g. 30000 in CI)
     */
    public void connect(String location, int delay) {
        // Load JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;

        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database... attempt " + (i + 1));

            try {
                // Wait before trying (if delay > 0)
                Thread.sleep(delay);

                // Connect to *world* database on the given host:port
                con = DriverManager.getConnection(
                        "jdbc:mysql://" + location +
                                "/world?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                        "root",
                        "example"
                );

                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + (i + 1));
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted while waiting to connect.");
                Thread.currentThread().interrupt();
            }
        }

        // If still not connected after all attempts, exit
        if (con == null) {
            System.out.println("FATAL ERROR: Could not connect to database after " + retries + " attempts");
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

    /**
     * Getter used by reports/tests.
     * I provide both getCon() and getConnection() so your existing code still compiles.
     */
    public Connection getCon() {
        return con;
    }

    public Connection getConnection() {
        return con;
    }

    public static void main(String[] args) {
        App app = new App();

        // LOCAL RUN (IntelliJ) – no args → connect to localhost
        if (args.length < 2) {
            app.connect("localhost:33060", 0);
        } else {
            // DOCKER / CI RUN: e.g. "db:3306 30000"
            String host = args[0];
            int delay = Integer.parseInt(args[1]);
            app.connect(host, delay);
        }

        // Non-interactive mode (Docker/CI) – no Scanner prompts
        if (System.console() == null && args.length >= 2) {
            System.out.println("Running in non-interactive mode (e.g., CI/CD).");
            // Run a simple report only (no user input)
            ReportAllCapitalCitiesByPopulation.generateReport(app.getCon());
        }
        // Interactive mode – ask user and run all 6 reports
        else {
            app.runAllCapitalCityReportsInteractive(app.getCon());
        }

        app.disconnect();
    }

    /**
     * Runs all 6 capital city reports sequentially (no menu).
     * Asks user for continent/region and N where needed.
     */
    private void runAllCapitalCityReportsInteractive(Connection con) {
        Scanner scanner = new Scanner(System.in);

        // 1. All capital cities in the world by population
        System.out.println("\n===== 1. All capital cities in the WORLD by population =====");
        ReportAllCapitalCitiesByPopulation.generateReport(con);

        // 2. All capital cities in a continent by population
        System.out.println("\n===== 2. All capital cities in a CONTINENT by population =====");
        System.out.print("Enter continent name (e.g., Asia, Europe): ");
        String continentAll = scanner.nextLine().trim();
        ReportCapitalCitiesByContinent.generateReport(con, continentAll);

        // 3. All capital cities in a region by population
        System.out.println("\n===== 3. All capital cities in a REGION by population =====");
        System.out.print("Enter region name (e.g., Eastern Asia, Western Europe): ");
        String regionAll = scanner.nextLine().trim();
        ReportCapitalCitiesByRegion.generateReport(con, regionAll);

        // 4. Top N capital cities in the world
        System.out.println("\n===== 4. Top N capital cities in the WORLD by population =====");
        int nWorld = askForPositiveInt(scanner, "Enter N for top capital cities in the WORLD: ");
        ReportTopNCapitalCitiesWorld.generateReport(con, nWorld);

        // 5. Top N capital cities in a continent
        System.out.println("\n===== 5. Top N capital cities in a CONTINENT by population =====");
        System.out.print("Enter continent name (e.g., Asia, Europe): ");
        String continentTop = scanner.nextLine().trim();
        int nContinent = askForPositiveInt(scanner, "Enter N for top capital cities in this CONTINENT: ");
        ReportTopNCapitalCitiesContinent.generateReport(con, continentTop, nContinent);

        // 6. Top N capital cities in a region
        System.out.println("\n===== 6. Top N capital cities in a REGION by population =====");
        System.out.print("Enter region name (e.g., Eastern Asia, Western Europe): ");
        String regionTop = scanner.nextLine().trim();
        int nRegion = askForPositiveInt(scanner, "Enter N for top capital cities in this REGION: ");
        ReportTopNCapitalCitiesRegion.generateReport(con, regionTop, nRegion);

        System.out.println("\nAll 6 capital city reports have been generated.");

        scanner.close();
    }

    /**
     * Helper to repeatedly ask for a positive integer.
     */
    private int askForPositiveInt(Scanner scanner, String prompt) {
        int value = -1;
        while (value <= 0) {
            System.out.print(prompt);
            while (!scanner.hasNextInt()) {
                System.out.print("Please enter a valid number: ");
                scanner.next(); // discard invalid token
            }
            value = scanner.nextInt();
            scanner.nextLine(); // consume newline
            if (value <= 0) {
                System.out.println("N must be a positive integer.");
            }
        }
        return value;
    }
}