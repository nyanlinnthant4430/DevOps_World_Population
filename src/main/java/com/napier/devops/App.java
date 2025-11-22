package com.napier.devops;

import com.napier.devops.country_report.*;
import com.napier.devops.city_report.*;
import com.napier.devops.FeatureCity_report.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
                Thread.sleep(delay);
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
        connect("localhost:33060", 0);
    }

    /** Get connection */
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

    /** Country printing helper */
    public static void printCountries(java.util.ArrayList<Country> countries) {
        if (countries == null || countries.isEmpty()) {
            System.out.println("No countries to display");
            return;
        }
        System.out.printf("%-30s %-15s %-25s %-15s%n", "Country Name", "Continent", "Region", "Population");
        for (Country c : countries) {
            if (c == null) continue;
            System.out.printf("%-30s %-15s %-25s %-15d%n", c.getName(), c.getContinent(), c.getRegion(), c.getPopulation());
        }
    }

    /** City printing helper */
    public static void printCities(java.util.ArrayList<City> cities) {
        if (cities == null || cities.isEmpty()) {
            System.out.println("No cities to display");
            return;
        }
        System.out.printf("%-30s %-25s %-20s %-15s%n", "City Name", "Country", "District", "Population");
        for (City c : cities) {
            if (c == null) continue;
            System.out.printf("%-30s %-25s %-20s %-15d%n", c.getName(), c.getCountryName(), c.getDistrict(), c.getPopulation());
        }
    }

    /** FeatureCity printing helper */
    public static void printFeatureCities(java.util.ArrayList<City> cities) {
        if (cities == null || cities.isEmpty()) {
            System.out.println("No FeatureCities to display");
            return;
        }
        System.out.printf("%-30s %-25s %-20s %-15s%n", "City Name", "Country", "District", "Population");
        for (City c : cities) {
            if (c == null) continue;
            System.out.printf("%-30s %-25s %-20s %-15d%n", c.getName(), c.getCountryName(), c.getDistrict(), c.getPopulation());
        }
    }

    public static void main(String[] args) {
        App app = new App();

        // Determine connection mode
        if (args.length < 2) {
            app.connect(); // localhost:33060
        } else {
            String host = args[0];
            int delay = Integer.parseInt(args[1]);
            app.connect(host, delay);
        }

        boolean nonInteractive = (System.console() == null && args.length >= 2);

        // Default values
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

        // ------------------------------
        // COUNTRY REPORTS
        // ------------------------------
        System.out.println("\n=== COUNTRY REPORTS ===");
        ReportAllCountriesByPopulation.generateReport(app.con);
        ReportCountriesByContinent.generateReport(app.con, continent);
        ReportCountriesByRegion.generateReport(app.con, region);
        ReportTopNCountriesWorld.generateReport(app.con, nWorld);
        ReportTopNCountriesContinent.generateReport(app.con, continent, nContinent);
        ReportTopNCountriesRegion.generateReport(app.con, region, nRegion);

        // ------------------------------
        // CITY REPORTS
        // ------------------------------
        System.out.println("\n=== CITY REPORTS ===");
        ReportAllCitiesByPopulation.generateReport(app.con);
        ReportCitiesByContinent.generateReport(app.con, continent);
        ReportCitiesByRegion.generateReport(app.con, region);
        ReportTopCitiesWorld.generateReport(app.con, nWorld);
        ReportTopCitiesContinent.generateReport(app.con, continent, nContinent);
        ReportTopCitiesRegion.generateReport(app.con, region, nRegion);

        // ------------------------------
        // FEATURECITY REPORTS
        // ------------------------------
        System.out.println("\n=== FEATURECITY REPORTS ===");
        if (!nonInteractive && scanner != null) {
            System.out.print("\nEnter Country for FEATURECITY report: ");
            String featureCountry = scanner.nextLine();
            ReportFeatureCityByCountry.generateReport(app.con, featureCountry);
        } else {
            ReportAllFeatureCities.generateReport(app.con); // Docker/CI mode
        }

        if (scanner != null) scanner.close();
        app.disconnect();
    }
}
