package com.napier.devops;

import com.napier.devops.basicpopulation.*;
import com.napier.devops.country_report.*;
import com.napier.devops.city_report.*;
import com.napier.devops.FeatureCity_report.*;
import com.napier.devops.feature_policymaker.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

/**
 * Main application class combining:
 *  - Basic population reports
 *  - Country reports
 *  - City reports
 *  - Feature City reports
 *  - Policymaker reports
 */
public class App {

    /** Active MySQL connection */
    private Connection con = null;

    // -------------------------------------------------------------------
    // DATABASE CONNECTION
    // -------------------------------------------------------------------
    public void connect(String location, int delay) {
        int retries = 10;

        while (retries > 0) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                if (delay > 0)
                    Thread.sleep(delay);

                con = DriverManager.getConnection(
                        "jdbc:mysql://" + location +
                                "/world?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                        "root",
                        "example"
                );

                System.out.println("Connected to database at: " + location);
                break;

            } catch (Exception e) {
                System.out.println("Waiting for database... (" + retries + " retries left)");
                retries--;

                try { Thread.sleep(5000); }
                catch (InterruptedException ignored) {}
            }
        }

        if (con == null) {
            System.out.println("FATAL ERROR: Could not connect to " + location);
            System.exit(-1);
        }
    }

    /** Default for IntelliJ */
    public void connect() {
        connect("localhost:33060", 0);
    }

    /** Access connection */
    public Connection getConnection() {
        return con;
    }

    /** Disconnect safely */
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

    // -------------------------------------------------------------------
    // BASIC POPULATION REPORTS (interactive)
    // -------------------------------------------------------------------
    public void runBasicPopulationReports() {
        if (con == null) {
            System.out.println("No DB connection.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("\n===== WORLD POPULATION =====");
        BasicReportWorldPopulation.generateReport(con);

        System.out.print("\nEnter Continent: ");
        String continent = scanner.nextLine().trim();
        BasicReportPopulationOfContinent.generateReport(con, continent);

        System.out.print("\nEnter Region: ");
        String region = scanner.nextLine().trim();
        BasicReportPopulationOfRegion.generateReport(con, region);

        System.out.print("\nEnter Country: ");
        String country = scanner.nextLine().trim();
        BasicReportPopulationOfCountry.generateReport(con, country);

        System.out.print("\nEnter District: ");
        String district = scanner.nextLine().trim();
        BasicReportPopulationOfDistrict.generateReport(con, district);

        System.out.print("\nEnter City: ");
        String city = scanner.nextLine().trim();
        BasicReportPopulationOfCity.generateReport(con, city);

        System.out.println("\n===== LANGUAGE POPULATION =====");
        BasicReportLanguagePopulation.generateReport(con);
    }

    // -------------------------------------------------------------------
    // COUNTRY / CITY / FEATURECITY REPORTS (parameters)
    // -------------------------------------------------------------------
    public void runCountryCityReports(String continent, String region,
                                      int nWorld, int nContinent, int nRegion) {

        System.out.println("\n=== COUNTRY REPORTS ===");
        ReportAllCountriesByPopulation.generateReport(con);
        ReportCountriesByContinent.generateReport(con, continent);
        ReportCountriesByRegion.generateReport(con, region);
        ReportTopNCountriesWorld.generateReport(con, nWorld);
        ReportTopNCountriesContinent.generateReport(con, continent, nContinent);
        ReportTopNCountriesRegion.generateReport(con, region, nRegion);

        System.out.println("\n=== CITY REPORTS ===");
        ReportAllCitiesByPopulation.generateReport(con);
        ReportCitiesByContinent.generateReport(con, continent);
        ReportCitiesByRegion.generateReport(con, region);
        ReportTopCitiesWorld.generateReport(con, nWorld);
        ReportTopCitiesContinent.generateReport(con, continent, nContinent);
        ReportTopCitiesRegion.generateReport(con, region, nRegion);

        System.out.println("\n=== FEATURE CITY REPORTS ===");
        ReportAllFeatureCities.generateReport(con);
    }

    // -------------------------------------------------------------------
    // FEATURE POLICYMAKER REPORTS
    // -------------------------------------------------------------------
    public void runFeaturePolicymakerReports() {
        System.out.println("\n=== FEATURE POLICYMAKER REPORTS ===");
        ReportPopulationByContinent.generateReport(con);
        ReportPopulationByRegion.generateReport(con);
        ReportPopulationByCountry.generateReport(con);
    }

    // -------------------------------------------------------------------
    // MAIN ENTRY POINT
    // -------------------------------------------------------------------
    public static void main(String[] args) {
        App app = new App();

        boolean nonInteractive = (System.console() == null && args.length >= 2);

        // Connect
        if (args.length < 2)
            app.connect();
        else
            app.connect(args[0], Integer.parseInt(args[1]));

        // Default parameters
        String continent = "Asia";
        String region = "Southeast Asia";
        int nWorld = 10, nContinent = 5, nRegion = 5;

        Scanner scanner = null;

        // Choose interactive or non-interactive
        if (!nonInteractive) {
            scanner = new Scanner(System.in);

            System.out.print("Enter N (Top N world countries): ");
            nWorld = scanner.nextInt(); scanner.nextLine();

            System.out.print("Enter Continent: ");
            continent = scanner.nextLine();

            System.out.print("Enter N (Top N continent countries): ");
            nContinent = scanner.nextInt(); scanner.nextLine();

            System.out.print("Enter Region: ");
            region = scanner.nextLine();

            System.out.print("Enter N (Top N region countries): ");
            nRegion = scanner.nextInt(); scanner.nextLine();

            System.out.println("\n--- Running BASIC POPULATION REPORTS ---");
            app.runBasicPopulationReports();
        } else {
            System.out.println("Running in Docker/CI mode (basic reports only).");

            System.out.println("\n===== WORLD POPULATION =====");
            BasicReportWorldPopulation.generateReport(app.getConnection());

            System.out.println("\n===== LANGUAGE POPULATION =====");
            BasicReportLanguagePopulation.generateReport(app.getConnection());
        }

        // Run full detailed reports
        System.out.println("\n--- Running COUNTRY / CITY REPORTS ---");
        app.runCountryCityReports(continent, region, nWorld, nContinent, nRegion);

        System.out.println("\n--- Running POLICYMAKER REPORTS ---");
        app.runFeaturePolicymakerReports();

        if (scanner != null) scanner.close();
        app.disconnect();
    }
}
