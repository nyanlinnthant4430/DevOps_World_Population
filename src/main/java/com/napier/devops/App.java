package com.napier.devops;

import com.napier.devops.country_report.*;
import com.napier.devops.city_report.*;
import com.napier.devops.FeatureCity_report.*;
import com.napier.devops.feature_policymaker.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class App {

    private Connection con = null;

    /**
     * Connect to MySQL database with retry logic.
     * Works for local IntelliJ or Docker/CI.
     */
    public void connect(String location, int delay) {
        int retries = 10;

        while (retries > 0) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Thread.sleep(delay); // only used in Docker/CI
                con = DriverManager.getConnection(
                        "jdbc:mysql://" + location + "/world?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                        "root",
                        "example"
                );
                System.out.println("Connected to database successfully at: " + location);
                break;
            } catch (Exception e) {
                System.out.println("Waiting for database... (" + retries + " retries left)");
                retries--;
                try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
            }
        }

        if (con == null) {
            System.out.println("FATAL ERROR: Cannot connect to MySQL at " + location);
            System.exit(-1);
        }
    }

    /** Convenience for local IntelliJ run */
    public void connect() {
        connect("localhost:33080", 0);
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

    /** Get connection */
    public Connection getConnection() {
        return con;
    }

    // -----------------------------
    // COUNTRY/CITY/FEATURECITY REPORTS
    // -----------------------------
    public void runCountryCityReports(String continent, String region, int nWorld, int nContinent, int nRegion) {
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

        System.out.println("\n=== FEATURECITY REPORTS ===");
        ReportAllFeatureCities.generateReport(con);
    }

    // -----------------------------
    // FEATURE POLICYMAKER REPORTS
    // -----------------------------
    public void runFeaturePolicymakerReports() {
        System.out.println("\n=== FEATURE POLICYMAKER REPORTS ===");
        ReportPopulationByContinent.generateReport(con);
        ReportPopulationByRegion.generateReport(con);
        ReportPopulationByCountry.generateReport(con);
    }

    // -----------------------------
    // MAIN
    // -----------------------------
    public static void main(String[] args) {
        App app = new App();

        boolean nonInteractive = (System.console() == null && args.length >= 2);

        // Connect to database
        if (args.length < 2) {
            app.connect(); // localhost:33080
        } else {
            String host = args[0];
            int delay = Integer.parseInt(args[1]);
            app.connect(host, delay);
        }

        // Default parameters
        String continent = "Asia";
        String region = "Southeast Asia";
        int nWorld = 10, nContinent = 5, nRegion = 5;

        Scanner scanner = null;
        if (!nonInteractive) {
            scanner = new Scanner(System.in);
            System.out.print("Enter N for Top N countries in WORLD: "); nWorld = scanner.nextInt(); scanner.nextLine();
            System.out.print("Enter Continent: "); continent = scanner.nextLine();
            System.out.print("Enter N for Top N countries in CONTINENT: "); nContinent = scanner.nextInt(); scanner.nextLine();
            System.out.print("Enter Region: "); region = scanner.nextLine();
            System.out.print("Enter N for Top N countries in REGION: "); nRegion = scanner.nextInt(); scanner.nextLine();
        }

        // Run reports
        try {
            app.runCountryCityReports(continent, region, nWorld, nContinent, nRegion);
            app.runFeaturePolicymakerReports();
        } catch (Exception e) {
            System.out.println("Error running reports: " + e.getMessage());
        } finally {
            if (scanner != null) scanner.close();
            app.disconnect();
        }
    }
}
