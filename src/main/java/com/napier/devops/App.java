package com.napier.devops;

import com.napier.devops.capital_city_report.*;
import com.napier.devops.FeatureCity_report.*;
import com.napier.devops.basicpopulation.*;
import com.napier.devops.country_report.*;
import com.napier.devops.feature_policymaker.*;

import com.napier.devops.country_report.Country;
import com.napier.devops.capital_city_report.City;

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
public class App {
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
    public void connect(String location, int delay) {
        int retries = 10;

        while (retries > 0) {
            try {
                // Load JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Optional initial delay (mainly for Docker/CI)
                if (delay > 0) {
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
            } catch (Exception e) {
                System.out.println("Waiting for database to be ready... (" + retries + " retries left)");
                retries--;

                if (retries == 0) {
                    System.out.println("FATAL ERROR: Could not connect to database at " + location);
                    System.exit(-1);
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * Convenience for local development:
     * connects to localhost:33060 with no delay.
     */
    public void connect() {
        connect("localhost:33060", 0);
    }

    /**
     * Disconnects from the database safely.
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
     * Getter for reports/tests.
     */
    public Connection getConnection() {
        return con;
    }

    /**
     * Backwards compatible alias (if any old code uses getCon()).
     */
    public Connection getCon() {
        return con;
    }

    // ----------------------------------------------------------
    //  PRINT HELPERS
    // ----------------------------------------------------------

    public static void printCountries(ArrayList<Country> countries) {
        if (countries == null) {
            System.out.println("No countries");
            return;
        }

        System.out.println(String.format(
                "%-30s %-15s %-25s %-15s",
                "Country Name", "Continent", "Region", "Population"));

        for (Country c : countries) {
            if (c == null)
                continue;

            String countryString = String.format(
                    "%-30s %-15s %-25s %-15d",
                    c.getName(), c.getContinent(), c.getRegion(), c.getPopulation()
            );

            System.out.println(countryString);
        }
    }

    public static void printCities(ArrayList<City> cities) {
        if (cities == null) {
            System.out.println("No cities");
            return;
        }

        System.out.println(String.format(
                "%-30s %-25s %-25s %-15s",
                "City Name", "Country", "District", "Population"));

        for (City c : cities) {
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
    //  COUNTRY REPORTS
    // ----------------------------------------------------------

    private void runCountryReportsInteractive(Scanner scanner)
    {
        System.out.println("\n===== COUNTRY REPORTS =====");

        // 1. All countries in the world by population
        System.out.println("\n===== 1. All countries in the WORLD by population =====");
        ReportAllCountriesByPopulation.generateReport(con);

        // 2. All countries in a continent by population
        System.out.println("\n===== 2. All countries in a CONTINENT by population =====");
        System.out.print("Enter continent name (e.g., Asia, Europe): ");
        String continentAll = scanner.nextLine().trim();
        ReportCountriesByContinent.generateReport(con, continentAll);

        // 3. All countries in a region by population
        System.out.println("\n===== 3. All countries in a REGION by population =====");
        System.out.print("Enter region name (e.g., Eastern Asia, Western Europe): ");
        String regionAll = scanner.nextLine().trim();
        ReportCountriesByRegion.generateReport(con, regionAll);

        // 4. Top N countries in the world
        System.out.println("\n===== 4. Top N countries in the WORLD by population =====");
        int nWorld = askForPositiveInt(scanner, "Enter N for top countries in the WORLD: ");
        ReportTopNCountriesWorld.generateReport(con, nWorld);

        // 5. Top N countries in a continent
        System.out.println("\n===== 5. Top N countries in a CONTINENT by population =====");
        System.out.print("Enter continent name (e.g., Asia, Europe): ");
        String continentTop = scanner.nextLine().trim();
        int nContinent = askForPositiveInt(scanner, "Enter N for top countries in this CONTINENT: ");
        ReportTopNCountriesContinent.generateReport(con, continentTop, nContinent);

        // 6. Top N countries in a region
        System.out.println("\n===== 6. Top N countries in a REGION by population =====");
        System.out.print("Enter region name (e.g., Eastern Asia, Western Europe): ");
        String regionTop = scanner.nextLine().trim();
        int nRegion = askForPositiveInt(scanner, "Enter N for top countries in this REGION: ");
        ReportTopNCountriesRegion.generateReport(con, regionTop, nRegion);

        System.out.println("\nAll 6 country reports have been generated.");
    }




    private void runCountryReportsNonInteractive() {
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

//     ----------------------------------------------------------
//      CAPITAL CITY REPORTS
//     ----------------------------------------------------------

    private void runCapitalCityReportsInteractive(Scanner scanner) {
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

    private void runCapitalCityReportsNonInteractive() {
        System.out.println("\n===== CAPITAL CITY REPORTS (Non-interactive) =====");
        // Minimal subset for CI – no input required
        ReportAllCapitalCitiesByPopulation.generateReport(con);
    }


//
//     ----------------------------------------------------------
//      CITY REPORTS (FeatureCity_report)
//     ----------------------------------------------------------

    private void runCityReportsInteractive(Scanner scanner) {
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

    private void runCityReportsNonInteractive() {
        System.out.println("\n===== CITY REPORTS (Non-interactive) =====");
        // Simple world-level report for CI
        FeatureReportAllCitiesByPopulation.generateReport(con);
    }

//     ----------------------------------------------------------
//      BASIC POPULATION REPORTS
//     ----------------------------------------------------------

    private void runBasicPopulationReportsInteractive(Scanner scanner) {
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

    private void runBasicPopulationReportsNonInteractive() {
        System.out.println("\n===== BASIC POPULATION REPORTS (Non-interactive) =====");

        // You can change these defaults to any valid values in your world database
        String continent = "Asia";
        String region = "Southeast Asia";
        String country = "Myanmar";
        String district = "Yangon";
        String city = "Yangon";

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

    private void runPolicyMakerReports() {
        System.out.println("\n===== POLICYMAKER POPULATION REPORTS =====");

        System.out.println("\n===== 1. Population of each Continent =====");
        ReportPopulationByContinent.generateReport(con);

        System.out.println("\n===== 2. Population of each Region =====");
        ReportPopulationByRegion.generateReport(con);

        System.out.println("\n===== 3. Population of each Country =====");
        ReportPopulationByCountry.generateReport(con);

        System.out.println("\nAll world population reports for policymakers have been generated.");
    }

    private void runPolicyMakerReportsNonInteractive() {
        // Same as interactive – there is no user input here
        runPolicyMakerReports();
    }

    // ----------------------------------------------------------
    //  INTERACTIVE VS NON-INTERACTIVE WRAPPERS
    // ----------------------------------------------------------

    /**
     * Runs all report families with user input (no menu, just sequential questions).
     */
    private void runAllReportsInteractive(Scanner scanner) {
        runCountryReportsInteractive(scanner);
        runCapitalCityReportsInteractive(scanner);
        runCityReportsInteractive(scanner);
        runBasicPopulationReportsInteractive(scanner);
        runPolicyMakerReports();
    }

    /**
     * Runs a CI-friendly subset of all reports with no user input.
     */
    private void runAllReportsNonInteractive() {
        runCountryReportsNonInteractive();
        runCapitalCityReportsNonInteractive();
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

    // ----------------------------------------------------------
    //  MAIN
    // ----------------------------------------------------------

    /**
     * Application entry point.
     * Behaviour:
     *  - Local run (IntelliJ): no args → connect to localhost:33060 and run all reports interactively.
     *  - Docker / CI: args[0] = host:port, args[1] = delayMs → non-interactive subset of reports.
     */
    public static void main(String[] args) {
        App app = new App();

        // Decide how to connect
        if (args.length < 2) {
            // Local
            app.connect();
        } else {
            // Docker / CI
            String host = args[0];
            int delay = Integer.parseInt(args[1]);
            app.connect(host, delay);
        }

        try {
            // Non-interactive mode (Docker/CI) – no console attached
            if (System.console() == null && args.length >= 2) {
                System.out.println("Running in non-interactive mode (Docker/CI)...");
                app.runAllReportsNonInteractive();
            } else {
                // Interactive mode
                Scanner scanner = new Scanner(System.in);
                app.runAllReportsInteractive(scanner);
                scanner.close();
            }
        } catch (Exception e) {
            System.out.println("Error running application: " + e.getMessage());
        } finally {
            app.disconnect();
        }
    }
}

