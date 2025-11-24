//////package com.napier.devops;
//////
//////import com.napier.devops.basicpopulation.*;
//////import com.napier.devops.country_report.*;
//////import com.napier.devops.city_report.*;
//////import com.napier.devops.FeatureCity_report.*;
//////import com.napier.devops.feature_policymaker.*;
//////
//////import java.sql.Connection;
//////import java.sql.DriverManager;
//////import java.util.Scanner;
//////
///////**
////// * Main application class combining:
////// *  - Basic population reports
////// *  - Country reports
////// *  - City reports
////// *  - Feature City reports
////// *  - Policymaker reports
////// */
//////public class App {
//////
//////    /** Active MySQL connection */
//////    private Connection con = null;
//////
//////    // -------------------------------------------------------------------
//////    // DATABASE CONNECTION
//////    // -------------------------------------------------------------------
//////    public void connect(String location, int delay) {
//////        int retries = 10;
//////
//////        while (retries > 0) {
//////            try {
//////                Class.forName("com.mysql.cj.jdbc.Driver");
//////
//////                if (delay > 0)
//////                    Thread.sleep(delay);
//////
//////                con = DriverManager.getConnection(
//////                        "jdbc:mysql://" + location +
//////                                "/world?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
//////                        "root",
//////                        "example"
//////                );
//////
//////                System.out.println("Connected to database at: " + location);
//////                break;
//////
//////            } catch (Exception e) {
//////                System.out.println("Waiting for database... (" + retries + " retries left)");
//////                retries--;
//////
//////                try { Thread.sleep(5000); }
//////                catch (InterruptedException ignored) {}
//////            }
//////        }
//////
//////        if (con == null) {
//////            System.out.println("FATAL ERROR: Could not connect to " + location);
//////            System.exit(-1);
//////        }
//////    }
//////
//////    /** Default for IntelliJ */
//////    public void connect() {
//////        connect("localhost:33060", 0);
//////    }
//////
//////    /** Access connection */
//////    public Connection getConnection() {
//////        return con;
//////    }
//////
//////    /** Disconnect safely */
//////    public void disconnect() {
//////        try {
//////            if (con != null) {
//////                con.close();
//////                System.out.println("Disconnected from database.");
//////            }
//////        } catch (Exception e) {
//////            System.out.println("Error closing connection: " + e.getMessage());
//////        }
//////    }
//////
//////    // -------------------------------------------------------------------
//////    // BASIC POPULATION REPORTS (interactive)
//////    // -------------------------------------------------------------------
//////    public void runBasicPopulationReports() {
//////        if (con == null) {
//////            System.out.println("No DB connection.");
//////            return;
//////        }
//////
//////        Scanner scanner = new Scanner(System.in);
//////
//////        System.out.println("\n===== WORLD POPULATION =====");
//////        BasicReportWorldPopulation.generateReport(con);
//////
//////        System.out.print("\nEnter Continent: ");
//////        String continent = scanner.nextLine().trim();
//////        BasicReportPopulationOfContinent.generateReport(con, continent);
//////
//////        System.out.print("\nEnter Region: ");
//////        String region = scanner.nextLine().trim();
//////        BasicReportPopulationOfRegion.generateReport(con, region);
//////
//////        System.out.print("\nEnter Country: ");
//////        String country = scanner.nextLine().trim();
//////        BasicReportPopulationOfCountry.generateReport(con, country);
//////
//////        System.out.print("\nEnter District: ");
//////        String district = scanner.nextLine().trim();
//////        BasicReportPopulationOfDistrict.generateReport(con, district);
//////
//////        System.out.print("\nEnter City: ");
//////        String city = scanner.nextLine().trim();
//////        BasicReportPopulationOfCity.generateReport(con, city);
//////
//////        System.out.println("\n===== LANGUAGE POPULATION =====");
//////        BasicReportLanguagePopulation.generateReport(con);
//////    }
//////
//////    // -------------------------------------------------------------------
//////    // COUNTRY / CITY / FEATURECITY REPORTS (parameters)
//////    // -------------------------------------------------------------------
//////    public void runCountryCityReports(String continent, String region,
//////                                      int nWorld, int nContinent, int nRegion) {
//////
//////        System.out.println("\n=== COUNTRY REPORTS ===");
//////        ReportAllCountriesByPopulation.generateReport(con);
//////        ReportCountriesByContinent.generateReport(con, continent);
//////        ReportCountriesByRegion.generateReport(con, region);
//////        ReportTopNCountriesWorld.generateReport(con, nWorld);
//////        ReportTopNCountriesContinent.generateReport(con, continent, nContinent);
//////        ReportTopNCountriesRegion.generateReport(con, region, nRegion);
//////
//////        System.out.println("\n=== CITY REPORTS ===");
//////        ReportAllCitiesByPopulation.generateReport(con);
//////        ReportCitiesByContinent.generateReport(con, continent);
//////        ReportCitiesByRegion.generateReport(con, region);
//////        ReportTopCitiesWorld.generateReport(con, nWorld);
//////        ReportTopCitiesContinent.generateReport(con, continent, nContinent);
//////        ReportTopCitiesRegion.generateReport(con, region, nRegion);
//////
//////        System.out.println("\n=== FEATURE CITY REPORTS ===");
//////        ReportAllFeatureCities.generateReport(con);
//////    }
//////
//////    // -------------------------------------------------------------------
//////    // FEATURE POLICYMAKER REPORTS
//////    // -------------------------------------------------------------------
//////    public void runFeaturePolicymakerReports() {
//////        System.out.println("\n=== FEATURE POLICYMAKER REPORTS ===");
//////        ReportPopulationByContinent.generateReport(con);
//////        ReportPopulationByRegion.generateReport(con);
//////        ReportPopulationByCountry.generateReport(con);
//////    }
//////
//////    // -------------------------------------------------------------------
//////    // MAIN ENTRY POINT
//////    // -------------------------------------------------------------------
//////    public static void main(String[] args) {
//////        App app = new App();
//////
//////        boolean nonInteractive = (System.console() == null && args.length >= 2);
//////
//////        // Connect
//////        if (args.length < 2)
//////            app.connect();
//////        else
//////            app.connect(args[0], Integer.parseInt(args[1]));
//////
//////        // Default parameters
//////        String continent = "Asia";
//////        String region = "Southeast Asia";
//////        int nWorld = 10, nContinent = 5, nRegion = 5;
//////
//////        Scanner scanner = null;
//////
//////        // Choose interactive or non-interactive
//////        if (!nonInteractive) {
//////            scanner = new Scanner(System.in);
//////
//////            System.out.print("Enter N (Top N world countries): ");
//////            nWorld = scanner.nextInt(); scanner.nextLine();
//////
//////            System.out.print("Enter Continent: ");
//////            continent = scanner.nextLine();
//////
//////            System.out.print("Enter N (Top N continent countries): ");
//////            nContinent = scanner.nextInt(); scanner.nextLine();
//////
//////            System.out.print("Enter Region: ");
//////            region = scanner.nextLine();
//////
//////            System.out.print("Enter N (Top N region countries): ");
//////            nRegion = scanner.nextInt(); scanner.nextLine();
//////
//////            System.out.println("\n--- Running BASIC POPULATION REPORTS ---");
//////            app.runBasicPopulationReports();
//////        } else {
//////            System.out.println("Running in Docker/CI mode (basic reports only).");
//////
//////            System.out.println("\n===== WORLD POPULATION =====");
//////            BasicReportWorldPopulation.generateReport(app.getConnection());
//////
//////            System.out.println("\n===== LANGUAGE POPULATION =====");
//////            BasicReportLanguagePopulation.generateReport(app.getConnection());
//////        }
//////
//////        // Run full detailed reports
//////        System.out.println("\n--- Running COUNTRY / CITY REPORTS ---");
//////        app.runCountryCityReports(continent, region, nWorld, nContinent, nRegion);
//////
//////        System.out.println("\n--- Running POLICYMAKER REPORTS ---");
//////        app.runFeaturePolicymakerReports();
//////
//////        if (scanner != null) scanner.close();
//////        app.disconnect();
//////    }
//////}
////// === Unified Combined App.java ===
////// This file merges all previous App classes (Option A)
////// One main(), one connect(), one interactive menu running all modules.
////
//////package com.napier.devops;
//////
//////import java.sql.*;
//////import java.util.*;
//////
//////// Import ALL report modules
//////import com.napier.devops.city_report.*;
//////import com.napier.devops.FeatureCity_report.*;
//////import com.napier.devops.basicpopulation.*;
//////import com.napier.devops.country_report.*;
//////import com.napier.devops.feature_policymaker.*;
//////
//////public class App {
//////
//////    // ==========================================================
//////    //  SINGLE SHARED DATABASE CONNECTION
//////    // ==========================================================
//////    private Connection con = null;
//////
//////    public Connection getConnection() { return con; }
//////    public Connection getCon() { return con; }
//////
//////
//////    // ==========================================================
//////    //  UNIFIED CONNECT METHOD
//////    // ==========================================================
//////    public void connect(String location, int delay) {
//////        int retries = 10;
//////        while (retries > 0) {
//////            try {
//////                Class.forName("com.mysql.cj.jdbc.Driver");
//////                Thread.sleep(delay);
//////
//////                con = DriverManager.getConnection(
//////                        "jdbc:mysql://" + location + "/world?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
//////                        "root",
//////                        "example"
//////                );
//////
//////                System.out.println("Connected to database successfully at: " + location);
//////                break;
//////            } catch (Exception e) {
//////                System.out.println("Waiting for database to be ready... (" + retries + " retries left)");
//////                retries--;
//////                try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
//////            }
//////        }
//////
//////        if (con == null) {
//////            System.out.println("FATAL ERROR: Cannot connect to MySQL at " + location);
//////            System.exit(-1);
//////        }
//////    }
//////
//////    // Default localhost connect
//////    public void connect() { connect("localhost:33060", 10000); }
//////
//////
//////    // ==========================================================
//////    //  UNIFIED DISCONNECT
//////    // ==========================================================
//////    public void disconnect() {
//////        try {
//////            if (con != null) {
//////                con.close();
//////                System.out.println("Disconnected from database.");
//////            }
//////        } catch (Exception e) {
//////            System.out.println("Error closing connection: " + e.getMessage());
//////        }
//////    }
//////
//////
//////    // ==========================================================
//////    //  MENU – USER CHOOSES MODULE
//////    // ==========================================================
//////    private void runMenu() {
//////        Scanner s = new Scanner(System.in);
//////
//////        while (true) {
//////            System.out.println("\n========= MAIN MENU =========");
//////            System.out.println("1. Capital City Reports");
//////            System.out.println("2. City Reports (All Cities)");
//////            System.out.println("3. Basic Population Reports");
//////            System.out.println("4. Country Reports");
//////            System.out.println("5. Policymaker World Population Reports");
//////            System.out.println("0. Exit");
//////            System.out.print("Choose option: ");
//////
//////            int choice = -1;
//////            try { choice = Integer.parseInt(s.nextLine()); } catch (Exception ignored) {}
//////
//////            switch (choice) {
//////                case 1: runCapitalCityReports(); break;
//////                case 2: runAllCitiesReports(); break;
//////                case 3: runBasicPopulationReports(); break;
//////                case 4: runCountryReports(); break;
//////                case 5: runPolicyMakerReports(); break;
//////                case 0: return;
//////                default: System.out.println("Invalid choice.");
//////            }
//////        }
//////    }
//////
//////
//////    // ==========================================================
//////    //  MODULE 1: CAPITAL CITY REPORTS
//////    // ==========================================================
//////    private void runCapitalCityReports() {
//////        Scanner sc = new Scanner(System.in);
//////
//////        System.out.println("\n===== CAPITAL CITY REPORTS =====");
//////        ReportAllCapitalCitiesByPopulation.generateReport(con);
//////
//////        System.out.print("Enter continent: ");
//////        ReportCapitalCitiesByContinent.generateReport(con, sc.nextLine());
//////
//////        System.out.print("Enter region: ");
//////        ReportCapitalCitiesByRegion.generateReport(con, sc.nextLine());
//////    }
//////
//////
//////    // ==========================================================
//////    //  MODULE 2: CITY REPORTS
//////    // ==========================================================
//////    private void runAllCitiesReports() {
//////        Scanner sc = new Scanner(System.in);
//////
//////        System.out.println("\n===== ALL CITIES IN WORLD BY POP =====");
//////        FeatureReportAllCitiesByPopulation.generateReport(con);
//////
//////        System.out.print("Enter continent: ");
//////        FeatureReportCitiesByContinent.generateReport(con, sc.nextLine());
//////
//////        System.out.print("Enter region: ");
//////        FeatureReportCitiesByRegion.generateReport(con, sc.nextLine());
//////    }
//////
//////
//////    // ==========================================================
//////    //  MODULE 3: BASIC POPULATION
//////    // ==========================================================
//////    private void runBasicPopulationReports() {
//////        Scanner sc = new Scanner(System.in);
//////
//////        BasicReportWorldPopulation.generateReport(con);
//////
//////        System.out.print("Enter Continent: ");
//////        BasicReportPopulationOfContinent.generateReport(con, sc.nextLine());
//////
//////        System.out.print("Enter Region: ");
//////        BasicReportPopulationOfRegion.generateReport(con, sc.nextLine());
//////
//////        System.out.print("Enter Country: ");
//////        BasicReportPopulationOfCountry.generateReport(con, sc.nextLine());
//////
//////        System.out.print("Enter District: ");
//////        BasicReportPopulationOfDistrict.generateReport(con, sc.nextLine());
//////
//////        System.out.print("Enter City: ");
//////        BasicReportPopulationOfCity.generateReport(con, sc.nextLine());
//////
//////        BasicReportLanguagePopulation.generateReport(con);
//////    }
//////
//////
//////    // ==========================================================
//////    //  MODULE 4: COUNTRY REPORTS
//////    // ==========================================================
//////    private void runCountryReports() {
//////        Scanner sc = new Scanner(System.in);
//////
//////        ReportAllCountriesByPopulation.generateReport(con);
//////
//////        System.out.print("Enter Continent: ");
//////        ReportCountriesByContinent.generateReport(con, sc.nextLine());
//////
//////        System.out.print("Enter Region: ");
//////        ReportCountriesByRegion.generateReport(con, sc.nextLine());
//////    }
//////
//////
//////    // ==========================================================
//////    //  MODULE 5: POLICYMAKER REPORTS
//////    // ==========================================================
//////    private void runPolicyMakerReports() {
//////        ReportPopulationByContinent.generateReport(con);
//////        ReportPopulationByRegion.generateReport(con);
//////        ReportPopulationByCountry.generateReport(con);
//////    }
//////
//////
//////    // ==========================================================
//////    //  MAIN
//////    // ==========================================================
//////    public static void main(String[] args) {
//////        App app = new App();
//////
//////        if (args.length < 2)
//////            app.connect();
//////        else
//////            app.connect(args[0], Integer.parseInt(args[1]));
//////
//////        try {
//////            app.runMenu();
//////        } catch (Exception e) {
//////            System.out.println("Error running application: " + e.getMessage());
//////        } finally {
//////            app.disconnect();
//////        }
//////    }
//////}
////// === Unified Combined App.java (Rebuilt using your model classes) ===
////// Uses: City, Country, FeatureCity, DatabaseConnection
////// One main(), one connect(), unified menu, consistent models
//////
//package com.napier.devops;
//
//import java.sql.*;
//import java.util.*;
//
//// Import ALL model + report modules
//import com.napier.devops.city_report.*;
//import com.napier.devops.FeatureCity_report.*;
//import com.napier.devops.basicpopulation.*;
//import com.napier.devops.country_report.*;
//import com.napier.devops.feature_policymaker.*;
//
//public class App {
//
//    // ==========================================================
//    //  SINGLE DB CONNECTION (no duplicate connection systems)
//    // ==========================================================
//    private Connection con = null;
//
//    public Connection getConnection() { return con; }
//
//    // Unified connect() using your schema format
//    public void connect(String location, int delay) {
//        int retries = 10;
//
//        while (retries > 0) {
//            try {
//                Class.forName("com.mysql.cj.jdbc.Driver");
//                Thread.sleep(delay);
//
//                con = DriverManager.getConnection(
//                        "jdbc:mysql://" + location + "/world?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
//                        "root",
//                        "example"
//                );
//
//                System.out.println("Connected to database: " + location);
//                break;
//
//            } catch (Exception e) {
//                System.out.println("Database connection failed. Retries left: " + (retries - 1));
//                retries--;
//                try { Thread.sleep(4000); } catch (InterruptedException ignored) {}
//            }
//        }
//
//        if (con == null) {
//            throw new RuntimeException("FATAL ERROR: Could not connect to DB at: " + location);
//        }
//    }
//
//
//    public void connect() { connect("localhost:33060", 10000); }
//
//    // Disconnect
//    public void disconnect() {
//        try {
//            if (con != null) {
//                con.close();
//                System.out.println("Disconnected from DB.");
//            }
//        } catch (Exception e) {
//            System.out.println("Error disconnecting: " + e.getMessage());
//        }
//    }
//
//    // ==========================================================
//    // HELPER: PRINT CITY LIST
//    // ==========================================================
//    public static void printCities(ArrayList<com.napier.devops.city_report.City> cities)
//    {
//        if (cities == null) {
//            System.out.println("No cities (null list).");
//            return;
//        }
//        if (cities.isEmpty()) {
//            System.out.println("No cities available.");
//            return;
//        }
//
//        System.out.println("\n=== CITY LIST ===");
//        for (com.napier.devops.city_report.City c : cities) {
//            if (c == null) {
//                System.out.println("Null city entry.");
//                continue;
//            }
//            System.out.printf("%-30s %-20s %-15s %-20s %10d\n",
//                    c.getName(),
//                    c.getCountry(),
//                    c.getContinent(),
//                    c.getRegion(),
//                    c.getPopulation()
//            );
//        }
//    }
//
//    // ==========================================================
//    // HELPER: PRINT COUNTRY LIST
//    // ==========================================================
//    public static void printCountries(ArrayList<com.napier.devops.country_report.Country> countries)
//    {
//        if (countries == null) {
//            System.out.println("No countries (null list).");
//            return;
//        }
//        if (countries.isEmpty()) {
//            System.out.println("No countries available.");
//            return;
//        }
//
//        System.out.println("\n=== COUNTRY LIST ===");
//        for (com.napier.devops.country_report.Country c : countries) {
//            if (c == null) {
//                System.out.println("Null country entry.");
//                continue;
//            }
//            System.out.printf("%-30s %-15s %-20s %10d\n",
//                    c.getName(),
//                    c.getContinent(),
//                    c.getRegion(),
//                    c.getPopulation()
//            );
//        }
//    }
//
//    // ==========================================================
//    //  MAIN MENU
//    // ==========================================================
//    public void runMenu() {
//       Scanner s = new Scanner(System.in);
//
//        while (true) {
//            System.out.println("=========== MAIN MENU ===========");
//            System.out.println("1. Capital City Reports");
//            System.out.println("2. City Reports");
//            System.out.println("3. Basic Population Reports");
//            System.out.println("4. Country Reports");
//            System.out.println("5. Policymaker Reports");
//            System.out.println("0. Exit");
//            System.out.print("Select option: ");
//
//            int choice;
//            try { choice = Integer.parseInt(s.nextLine()); }
//            catch (Exception e) { continue; }
//
//            switch (choice) {
//                case 1 -> runCapitalCityReports();
//                case 2 -> runCityReports();
//                case 3 -> runBasicPopulationReports();
//                case 4 -> runCountryReports();
//                case 5 -> runPolicymakerReports();
//                case 0 -> { return; }
//                default -> System.out.println("Invalid option.");
//            }
//        }
//    }
//
//    // ==========================================================
//    //  CAPITAL CITY REPORTS
//    // ==========================================================
//    public void runCapitalCityReports() {
//        Scanner sc = new Scanner(System.in);
//
//        System.out.println("\n--- CAPITAL CITIES REPORT ---");
//        ReportAllCapitalCitiesByPopulation.generateReport(con);
//
//        System.out.print("Enter Continent: ");
//        ReportCapitalCitiesByContinent.generateReport(con, sc.nextLine());
//
//        System.out.print("Enter Region: ");
//        ReportCapitalCitiesByRegion.generateReport(con, sc.nextLine());
//    }
//
//    // ==========================================================
//    //  CITY REPORTS (FeatureCity uses your getters)
//    // ==========================================================
//    public void runCityReports() {
//        Scanner sc = new Scanner(System.in);
//
//        System.out.println("\n--- ALL CITIES BY POPULATION ---");
//        FeatureReportAllCitiesByPopulation.generateReport(con);
//
//        System.out.print("Enter Continent: ");
//        FeatureReportCitiesByContinent.generateReport(con, sc.nextLine());
//
//        System.out.print("Enter Region: ");
//        FeatureReportCitiesByRegion.generateReport(con, sc.nextLine());
//    }
//
//    // ==========================================================
//    //  BASIC POPULATION (uses your model formats)
//    // ==========================================================
//    public void runBasicPopulationReports() {
//        Scanner sc = new Scanner(System.in);
//
//        BasicReportWorldPopulation.generateReport(con);
//
//        System.out.print("Continent: ");
//        BasicReportPopulationOfContinent.generateReport(con, sc.nextLine());
//
//        System.out.print("Region: ");
//        BasicReportPopulationOfRegion.generateReport(con, sc.nextLine());
//
//        System.out.print("Country: ");
//        BasicReportPopulationOfCountry.generateReport(con, sc.nextLine());
//
//        System.out.print("District: ");
//        BasicReportPopulationOfDistrict.generateReport(con, sc.nextLine());
//
//        System.out.print("City: ");
//        BasicReportPopulationOfCity.generateReport(con, sc.nextLine());
//
//        BasicReportLanguagePopulation.generateReport(con);
//    }
//
//    // ==========================================================
//    //  COUNTRY REPORTS
//    // ==========================================================
//    public void runCountryReports() {
//        Scanner sc = new Scanner(System.in);
//
//        ReportAllCountriesByPopulation.generateReport(con);
//
//        System.out.print("Continent: ");
//        ReportCountriesByContinent.generateReport(con, sc.nextLine());
//
//        System.out.print("Region: ");
//        ReportCountriesByRegion.generateReport(con, sc.nextLine());
//    }
//
//    // ==========================================================
//    //  POLICYMAKER POPULATION REPORTS
//    // ==========================================================
//    public void runPolicymakerReports() {
//        ReportPopulationByContinent.generateReport(con);
//        ReportPopulationByRegion.generateReport(con);
//        ReportPopulationByCountry.generateReport(con);
//    }
//
//    // ==========================================================
//    //  MAIN
//    // ==========================================================
//    public static void main(String[] args) {
//        App app = new App();
//
//        if (args.length < 2) app.connect();
//        else app.connect(args[0], Integer.parseInt(args[1]));
//
//        try { app.runMenu(); }
//        finally { app.disconnect(); }
//    }
//}
////
////package com.napier.devops;
////
////import java.sql.*;
////import java.util.*;
////
////// Import ALL model + report modules
////import com.napier.devops.city_report.*;
////import com.napier.devops.FeatureCity_report.*;
////import com.napier.devops.basicpopulation.*;
////import com.napier.devops.country_report.*;
////import com.napier.devops.feature_policymaker.*;
////
////public class App {
////
////    // ==========================================================
////    //  SINGLE DB CONNECTION (no duplicate connection systems)
////    // ==========================================================
////    private Connection con = null;
////
////    public Connection getConnection() { return con; }
////
////    // Unified connect() using your schema format
////    public void connect(String location, int delay) {
////        int retries = 10;
////
////        while (retries > 0) {
////            try {
////                Class.forName("com.mysql.cj.jdbc.Driver");
////                Thread.sleep(delay);
////
////                con = DriverManager.getConnection(
////                        "jdbc:mysql://" + location + "/world?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
////                        "root",
////                        "example"
////                );
////
////                System.out.println("Connected to database: " + location);
////                break;
////
////            } catch (Exception e) {
////                System.out.println("Database connection failed. Retries left: " + (retries - 1));
////                retries--;
////                try { Thread.sleep(4000); } catch (InterruptedException ignored) {}
////            }
////        }
////
////        if (con == null) {
////            throw new RuntimeException("FATAL ERROR: Could not connect to DB at: " + location);
////        }
////    }
////
////    // Default local connection (IntelliJ)
////    public void connect() { connect("localhost:33060", 10000); }
////
////    // Disconnect
////    public void disconnect() {
////        try {
////            if (con != null) {
////                con.close();
////                System.out.println("Disconnected from DB.");
////            }
////        } catch (Exception e) {
////            System.out.println("Error disconnecting: " + e.getMessage());
////        }
////    }
////
////    // ==========================================================
////    // HELPER: PRINT CITY LIST
////    // ==========================================================
////    public static void printCities(ArrayList<com.napier.devops.city_report.City> cities)
////    {
////        if (cities == null) {
////            System.out.println("No cities (null list).");
////            return;
////        }
////        if (cities.isEmpty()) {
////            System.out.println("No cities available.");
////            return;
////        }
////
////        System.out.println("\n=== CITY LIST ===");
////        for (com.napier.devops.city_report.City c : cities) {
////            if (c == null) {
////                System.out.println("Null city entry.");
////                continue;
////            }
////            System.out.printf("%-30s %-20s %-15s %-20s %10d\n",
////                    c.getName(),
////                    c.getCountry(),
////                    c.getContinent(),
////                    c.getRegion(),
////                    c.getPopulation()
////            );
////        }
////    }
////
////    // ==========================================================
////    // HELPER: PRINT COUNTRY LIST
////    // ==========================================================
////    public static void printCountries(ArrayList<com.napier.devops.country_report.Country> countries)
////    {
////        if (countries == null) {
////            System.out.println("No countries (null list).");
////            return;
////        }
////        if (countries.isEmpty()) {
////            System.out.println("No countries available.");
////            return;
////        }
////
////        System.out.println("\n=== COUNTRY LIST ===");
////        for (com.napier.devops.country_report.Country c : countries) {
////            if (c == null) {
////                System.out.println("Null country entry.");
////                continue;
////            }
////            System.out.printf("%-30s %-15s %-20s %10d\n",
////                    c.getName(),
////                    c.getContinent(),
////                    c.getRegion(),
////                    c.getPopulation()
////            );
////        }
////    }
////
////    // ==========================================================
////    //  CAPITAL CITY REPORTS (NO INPUT)
////    // ==========================================================
////    public void runCapitalCityReports() {
////        System.out.println("\n--- CAPITAL CITIES REPORTS ---");
////
////        // 1. All capital cities
////        ReportAllCapitalCitiesByPopulation.generateReport(con);
////
////        // 2. Capital cities in a sample continent
////        String continent = "Asia";
////        System.out.println("\nCapital cities in continent: " + continent);
////        ReportCapitalCitiesByContinent.generateReport(con, continent);
////
////        // 3. Capital cities in a sample region
////        String region = "Southeast Asia";
////        System.out.println("\nCapital cities in region: " + region);
////        ReportCapitalCitiesByRegion.generateReport(con, region);
////    }
////
////    // ==========================================================
////    //  CITY REPORTS (FeatureCity, NO INPUT)
////    // ==========================================================
////    public void runCityReports() {
////        System.out.println("\n--- CITY REPORTS (FeatureCity) ---");
////
////        // 1. All cities
////        FeatureReportAllCitiesByPopulation.generateReport(con);
////
////        // 2. Cities in a sample continent
////        String continent = "Asia";
////        System.out.println("\nCities in continent: " + continent);
////        FeatureReportCitiesByContinent.generateReport(con, continent);
////
////        // 3. Cities in a sample region
////        String region = "Southeast Asia";
////        System.out.println("\nCities in region: " + region);
////        FeatureReportCitiesByRegion.generateReport(con, region);
////    }
////
////    // ==========================================================
////    //  BASIC POPULATION (NO INPUT)
////    // ==========================================================
////    public void runBasicPopulationReports() {
////        System.out.println("\n--- BASIC POPULATION REPORTS ---");
////
////        // 1. World population
////        BasicReportWorldPopulation.generateReport(con);
////
////        // Default sample values
////        String continent = "Asia";
////        String region    = "Southeast Asia";
////        String country   = "Myanmar";
////        String district  = "Yangon";
////        String city      = "Rangoon (Yangon)";
////
////        // 2. Continent
////        System.out.println("\nContinent: " + continent);
////        BasicReportPopulationOfContinent.generateReport(con, continent);
////
////        // 3. Region
////        System.out.println("\nRegion: " + region);
////        BasicReportPopulationOfRegion.generateReport(con, region);
////
////        // 4. Country
////        System.out.println("\nCountry: " + country);
////        BasicReportPopulationOfCountry.generateReport(con, country);
////
////        // 5. District
////        System.out.println("\nDistrict: " + district);
////        BasicReportPopulationOfDistrict.generateReport(con, district);
////
////        // 6. City
////        System.out.println("\nCity: " + city);
////        BasicReportPopulationOfCity.generateReport(con, city);
////
////        // 7. Languages (Chinese, English, Hindi, Spanish, Arabic)
////        System.out.println("\nSelected language populations:");
////        BasicReportLanguagePopulation.generateReport(con);
////    }
////
////    // ==========================================================
////    //  COUNTRY REPORTS (NO INPUT)
////    // ==========================================================
////    public void runCountryReports() {
////        System.out.println("\n--- COUNTRY REPORTS ---");
////
////        // 1. All countries
////        ReportAllCountriesByPopulation.generateReport(con);
////
////        String continent = "Asia";
////        String region    = "Southeast Asia";
////
////        // 2. Countries in continent
////        System.out.println("\nCountries in continent: " + continent);
////        ReportCountriesByContinent.generateReport(con, continent);
////
////        // 3. Countries in region
////        System.out.println("\nCountries in region: " + region);
////        ReportCountriesByRegion.generateReport(con, region);
////    }
////
////    // ==========================================================
////    //  POLICYMAKER POPULATION REPORTS (NO INPUT)
////    // ==========================================================
////    public void runPolicymakerReports() {
////        System.out.println("\n--- POLICYMAKER POPULATION REPORTS ---");
////        ReportPopulationByContinent.generateReport(con);
////        ReportPopulationByRegion.generateReport(con);
////        ReportPopulationByCountry.generateReport(con);
////    }
////
////    // ==========================================================
////    //  MAIN
////    // ==========================================================
////    public static void main(String[] args) {
////        App app = new App();
////
////        // Local or Docker/CI connection
////        if (args.length < 2) {
////            app.connect();
////        } else {
////            app.connect(args[0], Integer.parseInt(args[1]));
////        }
////
////        try {
////            // NO MENU, NO INPUT – just run all report groups
////            app.runCapitalCityReports();
////            app.runCityReports();
////            app.runBasicPopulationReports();
////            app.runCountryReports();
////            app.runPolicymakerReports();
////        } finally {
////            app.disconnect();
////        }
////    }
////}
//
package com.napier.devops;

import com.napier.devops.city_report.*;
import com.napier.devops.FeatureCity_report.*;
import com.napier.devops.basicpopulation.*;
import com.napier.devops.country_report.*;
import com.napier.devops.feature_policymaker.*;

import com.napier.devops.country_report.Country;
import com.napier.devops.city_report.City;

import java.sql.Connection;
import java.sql.DriverManager;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Unified application that runs:
 *  - Capital city reports
 *  - Country reports
 *  - City reports (feature)
 *  - Basic population reports
 *  - Policymaker reports

 *  Single App.java, single connection, no menu.
 */
public class App
{
    /**
     * Active database connection to the MySQL {@code world} database.
     */
    private Connection con = null;

    // ----------------------------------------------------------
    //  CONNECTION HANDLING
    // ----------------------------------------------------------

    /**
     * Connects to the MySQL database with retry logic.

     * Examples:
     *  - Local run (IntelliJ): connect("localhost:33060", 0)
     *  - Docker / CI:         connect("db:3306", 30000)
     *
     * @param location host:port
     * @param delay    delay in milliseconds before first attempt (useful in CI)
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

                // Establish connection
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
     * Convenience for local development:
     * connects to localhost:33060 with no delay.
     */
    public void connect()
    {
        connect("localhost:33060", 0);
    }

    /**
     * Disconnects from the database safely.
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
     * Getter for reports/tests.
     */
    public Connection getConnection()
    {
        return con;
    }

    /**
     * Backwards compatible alias (if any old code uses getCon()).
     */
    public Connection getCon()
    {
        return con;
    }

    // ----------------------------------------------------------
    //  PRINT HELPERS
    // ----------------------------------------------------------

    public static void printCountries(ArrayList<Country> countries)
    {
        if (countries == null)
        {
            System.out.println("No countries");
            return;
        }

        System.out.println(String.format(
                "%-30s %-15s %-25s %-15s",
                "Country Name", "Continent", "Region", "Population"));

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

    public static void printCities(ArrayList<City> cities)
    {
        if (cities == null)
        {
            System.out.println("No cities");
            return;
        }

        System.out.println(String.format(
                "%-30s %-25s %-25s %-15s",
                "City Name", "Country", "District", "Population"));

        for (City c : cities)
        {
            if (c == null)
                continue;

            String cityString = String.format(
                    "%-30s %-25s %-25s %-15d",
                    c.getName(), c.getCountryName(), c.getDistrict(), c.getPopulation()
            );

            System.out.println(cityString);
        }
    }

    // ----------------------------------------------------------
    //  CAPITAL CITY REPORTS
    // ----------------------------------------------------------

    private void runCapitalCityReportsInteractive(Scanner scanner)
    {
        System.out.println("\n===== CAPITAL CITY REPORTS =====");

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
    }

    private void runCapitalCityReportsNonInteractive()
    {
        System.out.println("\n===== CAPITAL CITY REPORTS (Non-interactive) =====");
        // Minimal subset for CI – no input required
        ReportAllCapitalCitiesByPopulation.generateReport(con);
    }

    // ----------------------------------------------------------
    //  COUNTRY REPORTS
    // ----------------------------------------------------------

    private void runCountryReportsInteractive(Scanner scanner)
    {
        System.out.println("\n===== COUNTRY REPORTS =====");

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

        System.out.println("\n=== Report 1: All countries in the world by population (largest to smallest) ===");
        ReportAllCountriesByPopulation.generateReport(con);

        System.out.println("\n=== Report 2: All countries in continent '" + continent + "' by population ===");
        ReportCountriesByContinent.generateReport(con, continent);

        System.out.println("\n=== Report 3: All countries in region '" + region + "' by population ===");
        ReportCountriesByRegion.generateReport(con, region);

        System.out.println("\n=== Report 4: Top " + nWorld + " populated countries in the world ===");
        ReportTopNCountriesWorld.generateReport(con, nWorld);

        System.out.println("\n=== Report 5: Top " + nContinent + " populated countries in continent '" + continent + "' ===");
        ReportTopNCountriesContinent.generateReport(con, continent, nContinent);

        System.out.println("\n=== Report 6: Top " + nRegion + " populated countries in region '" + region + "' ===");
        ReportTopNCountriesRegion.generateReport(con, region, nRegion);
    }

    private void runCountryReportsNonInteractive()
    {
        System.out.println("\n===== COUNTRY REPORTS (Non-interactive) =====");

        String continent = "Asia";
        String region = "Southeast Asia";
        int nWorld = 10;
        int nContinent = 5;
        int nRegion = 5;

        System.out.println("\n=== Report 1: All countries in the world by population (largest to smallest) ===");
        ReportAllCountriesByPopulation.generateReport(con);

        System.out.println("\n=== Report 2: All countries in continent '" + continent + "' by population ===");
        ReportCountriesByContinent.generateReport(con, continent);

        System.out.println("\n=== Report 3: All countries in region '" + region + "' by population ===");
        ReportCountriesByRegion.generateReport(con, region);

        System.out.println("\n=== Report 4: Top " + nWorld + " populated countries in the world ===");
        ReportTopNCountriesWorld.generateReport(con, nWorld);

        System.out.println("\n=== Report 5: Top " + nContinent + " populated countries in continent '" + continent + "' ===");
        ReportTopNCountriesContinent.generateReport(con, continent, nContinent);

        System.out.println("\n=== Report 6: Top " + nRegion + " populated countries in region '" + region + "' ===");
        ReportTopNCountriesRegion.generateReport(con, region, nRegion);
    }

    // ----------------------------------------------------------
    //  CITY REPORTS (FeatureCity_report)
    // ----------------------------------------------------------

    private void runCityReportsInteractive(Scanner scanner)
    {
        System.out.println("\n===== CITY REPORTS =====");

        // 1. All cities in the world
        System.out.println("\n=== 1. All cities in the WORLD by population ===");
        FeatureReportAllCitiesByPopulation.generateReport(con);

        // 2. All cities in a continent
        System.out.print("\nEnter Continent Name for ALL cities by population: ");
        String continentAll = scanner.nextLine();
        System.out.println("\n=== 2. All cities in continent '" + continentAll + "' by population ===");
        FeatureReportCitiesByContinent.generateReport(con, continentAll);

        // 3. All cities in a region
        System.out.print("\nEnter Region Name for ALL cities by population: ");
        String regionAll = scanner.nextLine();
        System.out.println("\n=== 3. All cities in region '" + regionAll + "' by population ===");
        FeatureReportCitiesByRegion.generateReport(con, regionAll);

        // 4. All cities in a country
        System.out.print("\nEnter Country Name for ALL cities by population: ");
        String countryAll = scanner.nextLine();
        System.out.println("\n=== 4. All cities in country '" + countryAll + "' by population ===");
        FeatureReportCitiesByCountry.generateReport(con, countryAll);

        // 5. All cities in a district
        System.out.print("\nEnter District Name for ALL cities by population: ");
        String districtAll = scanner.nextLine();
        System.out.println("\n=== 5. All cities in district '" + districtAll + "' by population ===");
        FeatureReportCitiesByDistrict.generateReport(con, districtAll);

        // 6. Top N cities in the world
        System.out.print("\nEnter N for TOP N cities in the WORLD: ");
        int nWorld = scanner.nextInt();
        scanner.nextLine();
        System.out.println("\n=== 6. Top " + nWorld + " cities in the WORLD by population ===");
        FeatureReportTopNCitiesWorld.generateReport(con, nWorld);

        // 7. Top N cities in a continent
        System.out.print("\nEnter Continent for TOP N cities: ");
        String topContinent = scanner.nextLine();
        System.out.print("Enter N for TOP N cities in continent '" + topContinent + "': ");
        int nContinent = scanner.nextInt();
        scanner.nextLine();
        System.out.println("\n=== 7. Top " + nContinent + " cities in continent '" + topContinent + "' ===");
        FeatureReportTopNCitiesContinent.generateReport(con, topContinent, nContinent);

        // 8. Top N cities in a region
        System.out.print("\nEnter Region for TOP N cities: ");
        String topRegion = scanner.nextLine();
        System.out.print("Enter N for TOP N cities in region '" + topRegion + "': ");
        int nRegion = scanner.nextInt();
        scanner.nextLine();
        System.out.println("\n=== 8. Top " + nRegion + " cities in region '" + topRegion + "' ===");
        FeatureReportTopNCitiesRegion.generateReport(con, topRegion, nRegion);

        // 9. Top N cities in a country
        System.out.print("\nEnter Country for TOP N cities: ");
        String topCountry = scanner.nextLine();
        System.out.print("Enter N for TOP N cities in country '" + topCountry + "': ");
        int nCountry = scanner.nextInt();
        scanner.nextLine();
        System.out.println("\n=== 9. Top " + nCountry + " cities in country '" + topCountry + "' ===");
        FeatureReportTopNCitiesCountry.generateReport(con, topCountry, nCountry);

        // 10. Top N cities in a district
        System.out.print("\nEnter District for TOP N cities: ");
        String topDistrict = scanner.nextLine();
        System.out.print("Enter N for TOP N cities in district '" + topDistrict + "': ");
        int nDistrict = scanner.nextInt();
        scanner.nextLine();
        System.out.println("\n=== 10. Top " + nDistrict + " cities in district '" + topDistrict + "' ===");
        FeatureReportTopNCitiesDistrict.generateReport(con, topDistrict, nDistrict);
    }

    private void runCityReportsNonInteractive()
    {
        System.out.println("\n===== CITY REPORTS (Non-interactive) =====");
        // Simple world-level report for CI
        FeatureReportAllCitiesByPopulation.generateReport(con);
    }

    // ----------------------------------------------------------
    //  BASIC POPULATION REPORTS
    // ----------------------------------------------------------

    private void runBasicPopulationReportsInteractive(Scanner scanner)
    {
        System.out.println("\n===== BASIC POPULATION REPORTS =====");

        // 1. World population (no input required)
        System.out.println("\n===== 1. Population of the WORLD =====");
        BasicReportWorldPopulation.generateReport(con);

        // 2. Continent population
        System.out.print("\nEnter Continent name: ");
        String continent = scanner.nextLine().trim();
        System.out.println("\n===== 2. Population of continent '" + continent + "' =====");
        BasicReportPopulationOfContinent.generateReport(con, continent);

        // 3. Region population
        System.out.print("\nEnter Region name: ");
        String region = scanner.nextLine().trim();
        System.out.println("\n===== 3. Population of region '" + region + "' =====");
        BasicReportPopulationOfRegion.generateReport(con, region);

        // 4. Country population
        System.out.print("\nEnter Country name: ");
        String country = scanner.nextLine().trim();
        System.out.println("\n===== 4. Population of country '" + country + "' =====");
        BasicReportPopulationOfCountry.generateReport(con, country);

        // 5. District population
        System.out.print("\nEnter District name: ");
        String district = scanner.nextLine().trim();
        System.out.println("\n===== 5. Population of district '" + district + "' =====");
        BasicReportPopulationOfDistrict.generateReport(con, district);

        // 6. City population
        System.out.print("\nEnter City name: ");
        String city = scanner.nextLine().trim();
        System.out.println("\n===== 6. Population of city '" + city + "' =====");
        BasicReportPopulationOfCity.generateReport(con, city);

        // 7. Language populations (fixed set)
        System.out.println("\n===== 7. Population by Selected Languages =====");
        BasicReportLanguagePopulation.generateReport(con);
    }

    private void runBasicPopulationReportsNonInteractive()
    {
        System.out.println("\n===== BASIC POPULATION REPORTS (Non-interactive) =====");

        // You can change these defaults to any valid values in your world database
        String continent = "Asia";
        String region    = "Southeast Asia";
        String country   = "Myanmar";
        String district  = "Yangon";
        String city      = "Yangon";

        // 1. World
        System.out.println("\n===== 1. Population of the WORLD =====");
        BasicReportWorldPopulation.generateReport(con);

        // 2. Continent
        System.out.println("\n===== 2. Population of continent '" + continent + "' =====");
        BasicReportPopulationOfContinent.generateReport(con, continent);

        // 3. Region
        System.out.println("\n===== 3. Population of region '" + region + "' =====");
        BasicReportPopulationOfRegion.generateReport(con, region);

        // 4. Country
        System.out.println("\n===== 4. Population of country '" + country + "' =====");
        BasicReportPopulationOfCountry.generateReport(con, country);

        // 5. District
        System.out.println("\n===== 5. Population of district '" + district + "' =====");
        BasicReportPopulationOfDistrict.generateReport(con, district);

        // 6. City
        System.out.println("\n===== 6. Population of city '" + city + "' =====");
        BasicReportPopulationOfCity.generateReport(con, city);

        // 7. Languages
        System.out.println("\n===== 7. Population by Selected Languages =====");
        BasicReportLanguagePopulation.generateReport(con);
    }


    // ----------------------------------------------------------
    //  POLICYMAKER REPORTS
    // ----------------------------------------------------------

    private void runPolicyMakerReports()
    {
        System.out.println("\n===== POLICYMAKER POPULATION REPORTS =====");

        System.out.println("\n===== 1. Population of each Continent =====");
        ReportPopulationByContinent.generateReport(con);

        System.out.println("\n===== 2. Population of each Region =====");
        ReportPopulationByRegion.generateReport(con);

        System.out.println("\n===== 3. Population of each Country =====");
        ReportPopulationByCountry.generateReport(con);

        System.out.println("\nAll world population reports for policymakers have been generated.");
    }

    private void runPolicyMakerReportsNonInteractive()
    {
        // Same as interactive – there is no user input here
        runPolicyMakerReports();
    }

    // ----------------------------------------------------------
    //  INTERACTIVE VS NON-INTERACTIVE WRAPPERS
    // ----------------------------------------------------------

    /**
     * Runs all report families with user input (no menu, just sequential questions).
     */
    private void runAllReportsInteractive(Scanner scanner)
    {
        runCapitalCityReportsInteractive(scanner);
        runCountryReportsInteractive(scanner);
        runCityReportsInteractive(scanner);
        runBasicPopulationReportsInteractive(scanner);
        runPolicyMakerReports();
    }

    /**
     * Runs a CI-friendly subset of all reports with no user input.
     */
    private void runAllReportsNonInteractive()
    {
        runCapitalCityReportsNonInteractive();
        runCountryReportsNonInteractive();
        runCityReportsNonInteractive();
        runBasicPopulationReportsNonInteractive();
        runPolicyMakerReportsNonInteractive();
    }

    // ----------------------------------------------------------
    //  SMALL INPUT HELPER
    // ----------------------------------------------------------

    /**
     * Helper to repeatedly ask for a positive integer.
     */
    private int askForPositiveInt(Scanner scanner, String prompt)
    {
        int value = -1;
        while (value <= 0)
        {
            System.out.print(prompt);
            while (!scanner.hasNextInt())
            {
                System.out.print("Please enter a valid number: ");
                scanner.next(); // discard invalid token
            }
            value = scanner.nextInt();
            scanner.nextLine(); // consume newline
            if (value <= 0)
            {
                System.out.println("N must be a positive integer.");
            }
        }
        return value;
    }

    // ----------------------------------------------------------
    //  MAIN
    // ----------------------------------------------------------

    /**
     * Application entry point.

     * Behaviour:
     *  - Local run (IntelliJ): no args → connect to localhost:33060 and run all reports interactively.
     *  - Docker / CI: args[0] = host:port, args[1] = delayMs → non-interactive subset of reports.
     */
    public static void main(String[] args)
    {
        App app = new App();

        // Decide how to connect
        if (args.length < 2)
        {
            // Local
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
            // Non-interactive mode (Docker/CI) – no console attached
            if (System.console() == null && args.length >= 2)
            {
                System.out.println("Running in non-interactive mode (Docker/CI)...");
                app.runAllReportsNonInteractive();
            }
            else
            {
                // Interactive mode
                Scanner scanner = new Scanner(System.in);
                app.runAllReportsInteractive(scanner);
                scanner.close();
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

