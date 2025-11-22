package com.napier.devops;

import com.napier.devops.country_report.*;
import com.napier.devops.city_report.*;

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
    public void connect(String location, int delay) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;

        for (int i = 0; i < retries; i++) {
            System.out.println("Connecting to database... attempt " + (i + 1));

            try {
                Thread.sleep(delay);

                con = DriverManager.getConnection(
                        "jdbc:mysql://" + location +
                                "/world?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                        "root",
                        "example"
                );

                System.out.println("Successfully connected");
                break;

            } catch (SQLException sqle) {
                System.out.println("Failed attempt " + (i + 1) + ": " + sqle.getMessage());
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }

        if (con == null) {
            System.out.println("FATAL ERROR: Could not connect after " + retries + " attempts");
            System.exit(-1);
        }
    }

    /**
     * Disconnect
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

    // COUNTRY PRINT
    public static void printCountries(ArrayList<Country> countries) {
        if (countries == null) {
            System.out.println("No countries");
            return;
        }

        System.out.println(String.format(
                "%-30s %-15s %-25s %-15s",
                "Country Name", "Continent", "Region", "Population"));

        for (Country c : countries) {
            if (c == null) continue;
            System.out.println(String.format(
                    "%-30s %-15s %-25s %-15d",
                    c.getName(), c.getContinent(), c.getRegion(), c.getPopulation()
            ));
        }
    }

    // CITY PRINT
    public static void printCities(ArrayList<City> cities) {
        if (cities == null) {
            System.out.println("No cities");
            return;
        }

        System.out.println(String.format(
                "%-30s %-25s %-20s %-15s",
                "City Name", "Country", "District", "Population"));

        for (City c : cities) {
            if (c == null) continue;
            System.out.println(String.format(
                    "%-30s %-25s %-20s %-15d",
                    c.getName(), c.getCountryName(), c.getDistrict(), c.getPopulation()
            ));
        }
    }

    public Connection getConnection() {
        return con;
    }

    public static void main(String[] args) {
        App app = new App();

        // Local run
        if (args.length < 2) {
            app.connect("localhost:33060", 0);
        }
        else {
            app.connect(args[0], Integer.parseInt(args[1]));
        }

        // Non-interactive (Docker/CI)
        boolean nonInteractive = (System.console() == null && args.length >= 2);

        String continent = "Asia";
        String region = "Southeast Asia";

        int nWorld = 10;
        int nContinent = 5;
        int nRegion = 5;

        if (!nonInteractive) {
            Scanner sc = new Scanner(System.in);

            System.out.print("Enter N for Top N countries in WORLD: ");
            nWorld = sc.nextInt(); sc.nextLine();

            System.out.print("Enter Continent: ");
            continent = sc.nextLine();

            System.out.print("Enter N for Top N countries in CONTINENT: ");
            nContinent = sc.nextInt(); sc.nextLine();

            System.out.print("Enter Region: ");
            region = sc.nextLine();

            System.out.print("Enter N for Top N countries in REGION: ");
            nRegion = sc.nextInt(); sc.nextLine();

            sc.close();
        }

        // ------------------------------
        // COUNTRY REPORTS
        // ------------------------------
        System.out.println("\n=== COUNTRY REPORTS ===");

        System.out.println("\n1. All countries in the world:");
        ReportAllCountriesByPopulation.generateReport(app.con);

        System.out.println("\n2. All countries in continent '" + continent + "':");
        ReportCountriesByContinent.generateReport(app.con, continent);

        System.out.println("\n3. All countries in region '" + region + "':");
        ReportCountriesByRegion.generateReport(app.con, region);

        System.out.println("\n4. Top " + nWorld + " countries in the world:");
        ReportTopNCountriesWorld.generateReport(app.con, nWorld);

        System.out.println("\n5. Top " + nContinent + " countries in continent '" + continent + "':");
        ReportTopNCountriesContinent.generateReport(app.con, continent, nContinent);

        System.out.println("\n6. Top " + nRegion + " countries in region '" + region + "':");
        ReportTopNCountriesRegion.generateReport(app.con, region, nRegion);

        // ------------------------------
        // CITY REPORTS
        // ------------------------------
        System.out.println("\n=== CITY REPORTS ===");

        System.out.println("\n1. All cities in the world:");
        ReportAllCitiesByPopulation.generateReport(app.con);

        System.out.println("\n2. All cities in continent '" + continent + "':");
        ReportCitiesByContinent.generateReport(app.con, continent);

        System.out.println("\n3. All cities in region '" + region + "':");
        ReportCitiesByRegion.generateReport(app.con, region);

        System.out.println("\n4. Top " + nWorld + " cities in the world:");
        ReportTopCitiesWorld.generateReport(app.con, nWorld);

        System.out.println("\n5. Top " + nContinent + " cities in continent '" + continent + "':");
        ReportTopCitiesContinent.generateReport(app.con, continent, nContinent);

        System.out.println("\n6. Top " + nRegion + " cities in region '" + region + "':");
        ReportTopCitiesRegion.generateReport(app.con, region, nRegion);

        app.disconnect();
    }
}
