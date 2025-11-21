package com.napier.devops;

import com.napier.devops.country_report.*;
import com.napier.devops.country_report.Country;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class App {

    private Connection con = null;

    /**
     * Connect to MySQL database with retry logic.
     */
    public void connect(String location, int delay)
    {
        // Load JDBC driver
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;

        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database... attempt " + (i + 1));

            try
            {
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
            }
            catch (SQLException sqle)
            {
                System.out.println("Failed to connect to database attempt " + (i + 1));
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println("Thread interrupted while waiting to connect.");
                Thread.currentThread().interrupt();
            }
        }

        // If still not connected after all attempts, exit
        if (con == null)
        {
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

        // Non-interactive mode (Docker/CI) – use defaults, no Scanner
        if (System.console() == null && args.length >= 2) {


            String continent = "Asia";
            String region = "Southeast Asia";
            int nWorld = 10;
            int nContinent = 5;
            int nRegion = 5;

            System.out.println("\n=== Report 1: All countries in the world by population (largest to smallest) ===");
            ReportAllCountriesByPopulation.generateReport(app.con);

            System.out.println("\n=== Report 2: All countries in continent '" + continent + "' by population ===");
            ReportCountriesByContinent.generateReport(app.con, continent);

            System.out.println("\n=== Report 3: All countries in region '" + region + "' by population ===");
            ReportCountriesByRegion.generateReport(app.con, region);

            System.out.println("\n=== Report 4: Top " + nWorld + " populated countries in the world ===");
            ReportTopNCountriesWorld.generateReport(app.con, nWorld);

            System.out.println("\n=== Report 5: Top " + nContinent + " populated countries in continent '" + continent + "' ===");
            ReportTopNCountriesContinent.generateReport(app.con, continent, nContinent);

            System.out.println("\n=== Report 6: Top " + nRegion + " populated countries in region '" + region + "' ===");
            ReportTopNCountriesRegion.generateReport(app.con, region, nRegion);
        }
        else {
            // Interactive run (IntelliJ) – Scanner but no menu
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter N for Top N countries in the WORLD: ");
            int nWorld = scanner.nextInt();
            scanner.nextLine(); // consume newline

            System.out.print("Enter Continent Name: ");
            String continent = scanner.nextLine();

            System.out.print("Enter N for Top N countries in this CONTINENT: ");
            int nContinent = scanner.nextInt();
            scanner.nextLine(); // consume newline

            System.out.print("Enter Region Name: ");
            String region = scanner.nextLine();

            System.out.print("Enter N for Top N countries in this REGION: ");
            int nRegion = scanner.nextInt();
            scanner.nextLine(); // consume newline

            // Run reports
            System.out.println("\n=== Report 1: All countries in the world by population (largest to smallest) ===");
            ReportAllCountriesByPopulation.generateReport(app.con);

            System.out.println("\n=== Report 2: All countries in continent '" + continent + "' by population ===");
            ReportCountriesByContinent.generateReport(app.con, continent);

            System.out.println("\n=== Report 3: All countries in region '" + region + "' by population ===");
            ReportCountriesByRegion.generateReport(app.con, region);

            System.out.println("\n=== Report 4: Top " + nWorld + " populated countries in the world ===");
            ReportTopNCountriesWorld.generateReport(app.con, nWorld);

            System.out.println("\n=== Report 5: Top " + nContinent + " populated countries in continent '" + continent + "' ===");
            ReportTopNCountriesContinent.generateReport(app.con, continent, nContinent);

            System.out.println("\n=== Report 6: Top " + nRegion + " populated countries in region '" + region + "' ===");
            ReportTopNCountriesRegion.generateReport(app.con, region, nRegion);

            scanner.close();
        }

        app.disconnect();
    }

    public static void printCountries(ArrayList<Country> countries)
    {
        // Check countries is not null
        if (countries == null)
        {
            System.out.println("No countries");
            return;
        }

        // Print header
        System.out.println(String.format(
                "%-30s %-15s %-25s %-15s",
                "Country Name", "Continent", "Region", "Population"));

        // Loop over all countries in the list
        for (Country c : countries)
        {
            if (c == null)
                continue;

            String countryString = String.format(
                    "%-30s %-15s %-25s %-15d",
                    c.getName(), c.getContinent(), c.getRegion(), c.getPopulation()
            );

            System.out.println(countryString);
        }
    }

    public Connection getConnection() {
        return con;
    }
}
