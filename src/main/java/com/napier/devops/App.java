//package com.napier.devops;
//
//import com.napier.devops.basicpopulation.*;
//import com.napier.devops.country_report.*;
//import com.napier.devops.city_report.*;
//import com.napier.devops.FeatureCity_report.*;
//import com.napier.devops.feature_policymaker.*;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.util.Scanner;
//
///**
// * Main application class combining:
// *  - Basic population reports
// *  - Country reports
// *  - City reports
// *  - Feature City reports
// *  - Policymaker reports
// */
//public class App {
//
//    /** Active MySQL connection */
//    private Connection con = null;
//
//    // -------------------------------------------------------------------
//    // DATABASE CONNECTION
//    // -------------------------------------------------------------------
//    public void connect(String location, int delay) {
//        int retries = 10;
//
//        while (retries > 0) {
//            try {
//                Class.forName("com.mysql.cj.jdbc.Driver");
//
//                if (delay > 0)
//                    Thread.sleep(delay);
//
//                con = DriverManager.getConnection(
//                        "jdbc:mysql://" + location +
//                                "/world?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
//                        "root",
//                        "example"
//                );
//
//                System.out.println("Connected to database at: " + location);
//                break;
//
//            } catch (Exception e) {
//                System.out.println("Waiting for database... (" + retries + " retries left)");
//                retries--;
//
//                try { Thread.sleep(5000); }
//                catch (InterruptedException ignored) {}
//            }
//        }
//
//        if (con == null) {
//            System.out.println("FATAL ERROR: Could not connect to " + location);
//            System.exit(-1);
//        }
//    }
//
//    /** Default for IntelliJ */
//    public void connect() {
//        connect("localhost:33060", 0);
//    }
//
//    /** Access connection */
//    public Connection getConnection() {
//        return con;
//    }
//
//    /** Disconnect safely */
//    public void disconnect() {
//        try {
//            if (con != null) {
//                con.close();
//                System.out.println("Disconnected from database.");
//            }
//        } catch (Exception e) {
//            System.out.println("Error closing connection: " + e.getMessage());
//        }
//    }
//
//    // -------------------------------------------------------------------
//    // BASIC POPULATION REPORTS (interactive)
//    // -------------------------------------------------------------------
//    public void runBasicPopulationReports() {
//        if (con == null) {
//            System.out.println("No DB connection.");
//            return;
//        }
//
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("\n===== WORLD POPULATION =====");
//        BasicReportWorldPopulation.generateReport(con);
//
//        System.out.print("\nEnter Continent: ");
//        String continent = scanner.nextLine().trim();
//        BasicReportPopulationOfContinent.generateReport(con, continent);
//
//        System.out.print("\nEnter Region: ");
//        String region = scanner.nextLine().trim();
//        BasicReportPopulationOfRegion.generateReport(con, region);
//
//        System.out.print("\nEnter Country: ");
//        String country = scanner.nextLine().trim();
//        BasicReportPopulationOfCountry.generateReport(con, country);
//
//        System.out.print("\nEnter District: ");
//        String district = scanner.nextLine().trim();
//        BasicReportPopulationOfDistrict.generateReport(con, district);
//
//        System.out.print("\nEnter City: ");
//        String city = scanner.nextLine().trim();
//        BasicReportPopulationOfCity.generateReport(con, city);
//
//        System.out.println("\n===== LANGUAGE POPULATION =====");
//        BasicReportLanguagePopulation.generateReport(con);
//    }
//
//    // -------------------------------------------------------------------
//    // COUNTRY / CITY / FEATURECITY REPORTS (parameters)
//    // -------------------------------------------------------------------
//    public void runCountryCityReports(String continent, String region,
//                                      int nWorld, int nContinent, int nRegion) {
//
//        System.out.println("\n=== COUNTRY REPORTS ===");
//        ReportAllCountriesByPopulation.generateReport(con);
//        ReportCountriesByContinent.generateReport(con, continent);
//        ReportCountriesByRegion.generateReport(con, region);
//        ReportTopNCountriesWorld.generateReport(con, nWorld);
//        ReportTopNCountriesContinent.generateReport(con, continent, nContinent);
//        ReportTopNCountriesRegion.generateReport(con, region, nRegion);
//
//        System.out.println("\n=== CITY REPORTS ===");
//        ReportAllCitiesByPopulation.generateReport(con);
//        ReportCitiesByContinent.generateReport(con, continent);
//        ReportCitiesByRegion.generateReport(con, region);
//        ReportTopCitiesWorld.generateReport(con, nWorld);
//        ReportTopCitiesContinent.generateReport(con, continent, nContinent);
//        ReportTopCitiesRegion.generateReport(con, region, nRegion);
//
//        System.out.println("\n=== FEATURE CITY REPORTS ===");
//        ReportAllFeatureCities.generateReport(con);
//    }
//
//    // -------------------------------------------------------------------
//    // FEATURE POLICYMAKER REPORTS
//    // -------------------------------------------------------------------
//    public void runFeaturePolicymakerReports() {
//        System.out.println("\n=== FEATURE POLICYMAKER REPORTS ===");
//        ReportPopulationByContinent.generateReport(con);
//        ReportPopulationByRegion.generateReport(con);
//        ReportPopulationByCountry.generateReport(con);
//    }
//
//    // -------------------------------------------------------------------
//    // MAIN ENTRY POINT
//    // -------------------------------------------------------------------
//    public static void main(String[] args) {
//        App app = new App();
//
//        boolean nonInteractive = (System.console() == null && args.length >= 2);
//
//        // Connect
//        if (args.length < 2)
//            app.connect();
//        else
//            app.connect(args[0], Integer.parseInt(args[1]));
//
//        // Default parameters
//        String continent = "Asia";
//        String region = "Southeast Asia";
//        int nWorld = 10, nContinent = 5, nRegion = 5;
//
//        Scanner scanner = null;
//
//        // Choose interactive or non-interactive
//        if (!nonInteractive) {
//            scanner = new Scanner(System.in);
//
//            System.out.print("Enter N (Top N world countries): ");
//            nWorld = scanner.nextInt(); scanner.nextLine();
//
//            System.out.print("Enter Continent: ");
//            continent = scanner.nextLine();
//
//            System.out.print("Enter N (Top N continent countries): ");
//            nContinent = scanner.nextInt(); scanner.nextLine();
//
//            System.out.print("Enter Region: ");
//            region = scanner.nextLine();
//
//            System.out.print("Enter N (Top N region countries): ");
//            nRegion = scanner.nextInt(); scanner.nextLine();
//
//            System.out.println("\n--- Running BASIC POPULATION REPORTS ---");
//            app.runBasicPopulationReports();
//        } else {
//            System.out.println("Running in Docker/CI mode (basic reports only).");
//
//            System.out.println("\n===== WORLD POPULATION =====");
//            BasicReportWorldPopulation.generateReport(app.getConnection());
//
//            System.out.println("\n===== LANGUAGE POPULATION =====");
//            BasicReportLanguagePopulation.generateReport(app.getConnection());
//        }
//
//        // Run full detailed reports
//        System.out.println("\n--- Running COUNTRY / CITY REPORTS ---");
//        app.runCountryCityReports(continent, region, nWorld, nContinent, nRegion);
//
//        System.out.println("\n--- Running POLICYMAKER REPORTS ---");
//        app.runFeaturePolicymakerReports();
//
//        if (scanner != null) scanner.close();
//        app.disconnect();
//    }
//}
// === Unified Combined App.java ===
// This file merges all previous App classes (Option A)
// One main(), one connect(), one interactive menu running all modules.

package com.napier.devops;

import java.sql.*;
import java.util.*;

// Import ALL report modules
import com.napier.devops.city_report.*;
import com.napier.devops.FeatureCity_report.*;
import com.napier.devops.basicpopulation.*;
import com.napier.devops.country_report.*;
import com.napier.devops.feature_policymaker.*;

public class App {

    // ==========================================================
    //  SINGLE SHARED DATABASE CONNECTION
    // ==========================================================
    private Connection con = null;

    public Connection getConnection() { return con; }
    public Connection getCon() { return con; }


    // ==========================================================
    //  UNIFIED CONNECT METHOD
    // ==========================================================
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
                System.out.println("Waiting for database to be ready... (" + retries + " retries left)");
                retries--;
                try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
            }
        }

        if (con == null) {
            System.out.println("FATAL ERROR: Cannot connect to MySQL at " + location);
            System.exit(-1);
        }
    }

    // Default localhost connect
    public void connect() { connect("localhost:33060", 10000); }


    // ==========================================================
    //  UNIFIED DISCONNECT
    // ==========================================================
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


    // ==========================================================
    //  MENU – USER CHOOSES MODULE
    // ==========================================================
    private void runMenu() {
        Scanner s = new Scanner(System.in);

        while (true) {
            System.out.println("\n========= MAIN MENU =========");
            System.out.println("1. Capital City Reports");
            System.out.println("2. City Reports (All Cities)");
            System.out.println("3. Basic Population Reports");
            System.out.println("4. Country Reports");
            System.out.println("5. Policymaker World Population Reports");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            int choice = -1;
            try { choice = Integer.parseInt(s.nextLine()); } catch (Exception ignored) {}

            switch (choice) {
                case 1: runCapitalCityReports(); break;
                case 2: runAllCitiesReports(); break;
                case 3: runBasicPopulationReports(); break;
                case 4: runCountryReports(); break;
                case 5: runPolicyMakerReports(); break;
                case 0: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }


    // ==========================================================
    //  MODULE 1: CAPITAL CITY REPORTS
    // ==========================================================
    private void runCapitalCityReports() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n===== CAPITAL CITY REPORTS =====");
        ReportAllCapitalCitiesByPopulation.generateReport(con);

        System.out.print("Enter continent: ");
        ReportCapitalCitiesByContinent.generateReport(con, sc.nextLine());

        System.out.print("Enter region: ");
        ReportCapitalCitiesByRegion.generateReport(con, sc.nextLine());
    }


    // ==========================================================
    //  MODULE 2: CITY REPORTS
    // ==========================================================
    private void runAllCitiesReports() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n===== ALL CITIES IN WORLD BY POP =====");
        FeatureReportAllCitiesByPopulation.generateReport(con);

        System.out.print("Enter continent: ");
        FeatureReportCitiesByContinent.generateReport(con, sc.nextLine());

        System.out.print("Enter region: ");
        FeatureReportCitiesByRegion.generateReport(con, sc.nextLine());
    }


    // ==========================================================
    //  MODULE 3: BASIC POPULATION
    // ==========================================================
    private void runBasicPopulationReports() {
        Scanner sc = new Scanner(System.in);

        BasicReportWorldPopulation.generateReport(con);

        System.out.print("Enter Continent: ");
        BasicReportPopulationOfContinent.generateReport(con, sc.nextLine());

        System.out.print("Enter Region: ");
        BasicReportPopulationOfRegion.generateReport(con, sc.nextLine());

        System.out.print("Enter Country: ");
        BasicReportPopulationOfCountry.generateReport(con, sc.nextLine());

        System.out.print("Enter District: ");
        BasicReportPopulationOfDistrict.generateReport(con, sc.nextLine());

        System.out.print("Enter City: ");
        BasicReportPopulationOfCity.generateReport(con, sc.nextLine());

        BasicReportLanguagePopulation.generateReport(con);
    }


    // ==========================================================
    //  MODULE 4: COUNTRY REPORTS
    // ==========================================================
    private void runCountryReports() {
        Scanner sc = new Scanner(System.in);

        ReportAllCountriesByPopulation.generateReport(con);

        System.out.print("Enter Continent: ");
        ReportCountriesByContinent.generateReport(con, sc.nextLine());

        System.out.print("Enter Region: ");
        ReportCountriesByRegion.generateReport(con, sc.nextLine());
    }


    // ==========================================================
    //  MODULE 5: POLICYMAKER REPORTS
    // ==========================================================
    private void runPolicyMakerReports() {
        ReportPopulationByContinent.generateReport(con);
        ReportPopulationByRegion.generateReport(con);
        ReportPopulationByCountry.generateReport(con);
    }


    // ==========================================================
    //  MAIN
    // ==========================================================
    public static void main(String[] args) {
        App app = new App();

        if (args.length < 2)
            app.connect();
        else
            app.connect(args[0], Integer.parseInt(args[1]));

        try {
            app.runMenu();
        } catch (Exception e) {
            System.out.println("Error running application: " + e.getMessage());
        } finally {
            app.disconnect();
        }
    }
}
