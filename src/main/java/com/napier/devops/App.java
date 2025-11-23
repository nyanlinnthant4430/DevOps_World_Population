package com.napier.devops;

import com.napier.devops.feature_basicpopulation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

/**
 * Main application class for running core population reports.
 * <p>
 * Provides access to:
 * <ul>
 *     <li>The population of the world.</li>
 *     <li>The population of a continent.</li>
 *     <li>The population of a region.</li>
 *     <li>The population of a country.</li>
 *     <li>The population of a district.</li>
 *     <li>The population of a city.</li>
 *     <li>The number of people who speak selected languages
 *         (Chinese, English, Hindi, Spanish, Arabic),
 *         including their percentage of the world population.</li>
 *
 * </ul>
 */
public class App
{
    /**
     * Active database connection to the MySQL {@code world} database.
     */
    private Connection con = null;

    /**
     * Connects to the MySQL database with retry logic.
     *
     * <p>Two typical usages:</p>
     * <ul>
     *     <li>Local run (IntelliJ): {@code connect("localhost:33080", 0)}</li>
     *     <li>Docker / CI run:      {@code connect("db:3306", 30000)}</li>
     * </ul>
     *
     * @param location host and port in the form {@code host:port}, for example {@code "localhost:33080"}.
     * @param delay    delay in milliseconds before the first attempt (useful in CI/Docker).
     */
    public void connect(String location, int delay)
    {
        int retries = 10;

        while (retries > 0)
        {
            try
            {
                // Load JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Optional initial delay (mainly for Docker/CI)
                if (delay > 0)
                {
                    Thread.sleep(delay);
                }

                // Establish connection to world database
                con = DriverManager.getConnection(
                        "jdbc:mysql://" + location +
                                "/world?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                        "root",
                        "example"
                );

                System.out.println("Connected to database successfully at: " + location);
                break;
            }
            catch (Exception e)
            {
                System.out.println("Waiting for database to be ready... (" + retries + " retries left)");
                retries--;

                if (retries == 0)
                {
                    System.out.println("FATAL ERROR: Could not connect to database at " + location);
                    System.exit(-1);
                }

                try
                {
                    Thread.sleep(5000);
                }
                catch (InterruptedException ignored)
                {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * Convenience method for local development.
     * <p>
     * Connects to {@code localhost:33080} with no additional delay.
     */
    public void connect()
    {
        connect("localhost:33060", 0);
    }

    /**
     * Returns the active database connection.
     *
     * @return the current {@link Connection}, or {@code null} if not connected.
     */
    public Connection getConnection()
    {
        return con;
    }

    /**
     * Safely disconnects from the MySQL database.
     */
    public void disconnect()
    {
        try
        {
            if (con != null)
            {
                con.close();
                System.out.println("Disconnected from database.");
            }
        }
        catch (Exception e)
        {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    /**
     * Runs the basic population reports interactively.
     * <p>
     * This method:
     * <ol>
     *     <li>Displays the population of the world.</li>
     *     <li>Prompts for a continent and displays its population.</li>
     *     <li>Prompts for a region and displays its population.</li>
     *     <li>Prompts for a country and displays its population.</li>
     *     <li>Prompts for a district and displays its population.</li>
     *     <li>Prompts for a city and displays its population.</li>
     *     <li>Displays the population of selected languages and their
     *         percentage of the world population.</li>
     * </ol>
     */
    private void runBasicPopulationReports()
    {
        if (con == null)
        {
            System.out.println("No database connection – cannot run reports.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        // 1. World population (no input required)
        System.out.println("\n===== 1. Population of the WORLD =====");
        ReportWorldPopulation.generateReport(con);

        // 2. Continent population
        System.out.print("\nEnter Continent name: ");
        String continent = scanner.nextLine().trim();
        System.out.println("\n===== 2. Population of continent '" + continent + "' =====");
        ReportPopulationOfContinent.generateReport(con, continent);

        // 3. Region population
        System.out.print("\nEnter Region name: ");
        String region = scanner.nextLine().trim();
        System.out.println("\n===== 3. Population of region '" + region + "' =====");
        ReportPopulationOfRegion.generateReport(con, region);

        // 4. Country population
        System.out.print("\nEnter Country name: ");
        String country = scanner.nextLine().trim();
        System.out.println("\n===== 4. Population of country '" + country + "' =====");
        ReportPopulationOfCountry.generateReport(con, country);

        // 5. District population
        System.out.print("\nEnter District name: ");
        String district = scanner.nextLine().trim();
        System.out.println("\n===== 5. Population of district '" + district + "' =====");
        ReportPopulationOfDistrict.generateReport(con, district);

        // 6. City population
        System.out.print("\nEnter City name: ");
        String city = scanner.nextLine().trim();
        System.out.println("\n===== 6. Population of city '" + city + "' =====");
        ReportPopulationOfCity.generateReport(con, city);

        // 7. Language populations (fixed set: Chinese, English, Hindi, Spanish, Arabic)
        System.out.println("\n===== 7. Population by Selected Languages =====");
        ReportLanguagePopulation.generateReport(con);

        scanner.close();
    }

    /**
     * Application entry point.
     * <p>
     * Behaviour:
     * <ul>
     *     <li><b>Local run (IntelliJ)</b>: no command-line arguments –
     *         connects to {@code localhost:33080} and runs all interactive reports.</li>
     *     <li><b>Docker / CI run</b>: two arguments {@code host delayMs} –
     *         connects to the given host (for example {@code db:3306}) with the delay,
     *         then runs a non-interactive subset (world + languages).</li>
     * </ul>
     *
     * @param args optional command-line arguments {@code [host delayMs]} for Docker / CI.
     */
    public static void main(String[] args)
    {
        App app = new App();

        // Determine connection mode
        if (args.length < 2)
        {
            // Local development
            app.connect();
        }
        else
        {
            // Docker / CI
            String host = args[0];
            int delay = Integer.parseInt(args[1]);
            app.connect(host, delay);
        }

        try
        {
            // Non-interactive mode (e.g., Docker / CI) – no console attached
            if (System.console() == null && args.length >= 2)
            {
                System.out.println("Running in non-interactive mode (Docker/CI)...");
                System.out.println("\n===== Population of the WORLD =====");
                ReportWorldPopulation.generateReport(app.getConnection());

                System.out.println("\n===== Population by Selected Languages =====");
                ReportLanguagePopulation.generateReport(app.getConnection());
            }
            else
            {
                // Interactive mode – prompt user and run all reports
                app.runBasicPopulationReports();
            }
        }
        catch (Exception e)
        {
            System.out.println("Error running application: " + e.getMessage());
        }
        finally
        {
            app.disconnect();
        }
    }
}
