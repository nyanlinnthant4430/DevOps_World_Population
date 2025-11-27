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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Unified application that runs:
 * - Capital city reports
 * - Country reports
 * - City reports (feature)
 * - Basic population reports
 * - Policymaker reports
 * Single App.java, single connection, no menu.
 */
public class App {

    /**
     * Logger instance for this class.
     */
    public static final Logger LOGGER = Logger.getLogger(App.class.getName());

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
     * - Local run (IntelliJ): connect("localhost:33060", 0)
     * - Docker / CI:         connect("db:3306", 30000)
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

                LOGGER.log(Level.INFO, "Connected to database successfully at: {0}", location);
                break;
            } catch (Exception e) {
                LOGGER.log(Level.WARNING,
                        "Waiting for database to be ready... ({0} retries left)", retries);
                retries--;

                if (retries == 0) {
                    LOGGER.log(Level.SEVERE,
                            "FATAL ERROR: Could not connect to database at {0}", location);
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
                LOGGER.info("Disconnected from database.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error closing connection", e);
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
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("No countries");
            }
            return;
        }

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info(String.format(
                    "%-30s %-15s %-25s %-15s",
                    "Country Name", "Continent", "Region", "Population"));

            for (Country c : countries) {
                if (c == null) {
                    continue;
                }

                String countryString = String.format(
                        "%-30s %-15s %-25s %-15d",
                        c.getName(), c.getContinent(), c.getRegion(), c.getPopulation()
                );
                LOGGER.info(countryString);
            }
        }
    }


    public static void printCities(ArrayList<City> cities) {
        if (cities == null) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("No cities");
            }
            return;
        }

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info(String.format(
                    "%-30s %-25s %-25s %-15s",
                    "City Name", "Country", "District", "Population"));

            for (City c : cities) {
                if (c == null) {
                    continue;
                }

                String cityString = String.format(
                        "%-30s %-25s %-25s %-15d",
                        c.getName(), c.getCountryName(), c.getDistrict(), c.getPopulation()
                );
                LOGGER.info(cityString);
            }
        }
    }


    // ----------------------------------------------------------
    //  COUNTRY REPORTS
    // ----------------------------------------------------------

    private void runCountryReportsInteractive(Scanner scanner) {
        LOGGER.info("\n===== COUNTRY REPORTS =====");

        // 1. All countries in the world by population
        LOGGER.info("\n===== 1. All countries in the WORLD by population =====");
        ReportAllCountriesByPopulation.generateReport(con);

        // 2. All countries in a continent by population
        LOGGER.info("\n===== 2. All countries in a CONTINENT by population =====");
        LOGGER.info("Enter continent name (e.g., Asia, Europe): ");
        String continentAll = scanner.nextLine().trim();
        ReportCountriesByContinent.generateReport(con, continentAll);

        // 3. All countries in a region by population
        LOGGER.info("\n===== 3. All countries in a REGION by population =====");
        LOGGER.info("Enter region name (e.g., Eastern Asia, Western Europe): ");
        String regionAll = scanner.nextLine().trim();
        ReportCountriesByRegion.generateReport(con, regionAll);

        // 4. Top N countries in the world
        LOGGER.info("\n===== 4. Top N countries in the WORLD by population =====");
        int nWorld = askForPositiveInt(scanner, "Enter N for top countries in the WORLD: ");
        ReportTopNCountriesWorld.generateReport(con, nWorld);

        // 5. Top N countries in a continent
        LOGGER.info("\n===== 5. Top N countries in a CONTINENT by population =====");
        LOGGER.info("Enter continent name (e.g., Asia, Europe): ");
        String continentTop = scanner.nextLine().trim();
        int nContinent = askForPositiveInt(scanner, "Enter N for top countries in this CONTINENT: ");
        ReportTopNCountriesContinent.generateReport(con, continentTop, nContinent);

        // 6. Top N countries in a region
        LOGGER.info("\n===== 6. Top N countries in a REGION by population =====");
        LOGGER.info("Enter region name (e.g., Eastern Asia, Western Europe): ");
        String regionTop = scanner.nextLine().trim();
        int nRegion = askForPositiveInt(scanner, "Enter N for top countries in this REGION: ");
        ReportTopNCountriesRegion.generateReport(con, regionTop, nRegion);

        LOGGER.info("\nAll 6 country reports have been generated.");
    }

    private void runCountryReportsNonInteractive() {
        String continent = "Asia";
        String region = "Southeast Asia";
        int nWorld = 10;
        int nContinent = 5;
        int nRegion = 5;

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("\n===== COUNTRY REPORTS (Non-interactive) =====");

            LOGGER.info("\n=== Report 1: All countries in the world by population (largest to smallest) ===");
            LOGGER.log(Level.INFO,
                    "\n=== Report 2: All countries in continent {0} by population ===",
                    continent);
            LOGGER.log(Level.INFO,
                    "\n=== Report 3: All countries in region {0} by population ===",
                    region);
            LOGGER.log(Level.INFO,
                    "\n=== Report 4: Top {0} populated countries in the world ===",
                    nWorld);
            LOGGER.log(Level.INFO,
                    "\n=== Report 5: Top {0} populated countries in continent {1} ===",
                    new Object[]{nContinent, continent});
            LOGGER.log(Level.INFO,
                    "\n=== Report 6: Top {0} populated countries in region {1} ===",
                    new Object[]{nRegion, region});
        }

        // actual report logic (no logging inside guard)
        ReportAllCountriesByPopulation.generateReport(con);
        ReportCountriesByContinent.generateReport(con, continent);
        ReportCountriesByRegion.generateReport(con, region);
        ReportTopNCountriesWorld.generateReport(con, nWorld);
        ReportTopNCountriesContinent.generateReport(con, continent, nContinent);
        ReportTopNCountriesRegion.generateReport(con, region, nRegion);
    }


    // ----------------------------------------------------------
    //  CAPITAL CITY REPORTS
    // ----------------------------------------------------------

    private void runCapitalCityReportsInteractive(Scanner scanner) {
        LOGGER.info("\n===== CAPITAL CITY REPORTS =====");

        // 1. All capital cities in the world by population
        LOGGER.info("\n===== 1. All capital cities in the WORLD by population =====");
        ReportAllCapitalCitiesByPopulation.generateReport(con);

        // 2. All capital cities in a continent by population
        LOGGER.info("\n===== 2. All capital cities in a CONTINENT by population =====");
        LOGGER.info("Enter continent name (e.g., Asia, Europe): ");
        String continentAll = scanner.nextLine().trim();
        ReportCapitalCitiesByContinent.generateReport(con, continentAll);

        // 3. All capital cities in a region by population
        LOGGER.info("\n===== 3. All capital cities in a REGION by population =====");
        LOGGER.info("Enter region name (e.g., Eastern Asia, Western Europe): ");
        String regionAll = scanner.nextLine().trim();
        ReportCapitalCitiesByRegion.generateReport(con, regionAll);

        // 4. Top N capital cities in the world
        LOGGER.info("\n===== 4. Top N capital cities in the WORLD by population =====");
        int nWorld = askForPositiveInt(scanner, "Enter N for top capital cities in the WORLD: ");
        ReportTopNCapitalCitiesWorld.generateReport(con, nWorld);

        // 5. Top N capital cities in a continent
        LOGGER.info("\n===== 5. Top N capital cities in a CONTINENT by population =====");
        LOGGER.info("Enter continent name (e.g., Asia, Europe): ");
        String continentTop = scanner.nextLine().trim();
        int nContinent = askForPositiveInt(scanner, "Enter N for top capital cities in this CONTINENT: ");
        ReportTopNCapitalCitiesContinent.generateReport(con, continentTop, nContinent);

        // 6. Top N capital cities in a region
        LOGGER.info("\n===== 6. Top N capital cities in a REGION by population =====");
        LOGGER.info("Enter region name (e.g., Eastern Asia, Western Europe): ");
        String regionTop = scanner.nextLine().trim();
        int nRegion = askForPositiveInt(scanner, "Enter N for top capital cities in this REGION: ");
        ReportTopNCapitalCitiesRegion.generateReport(con, regionTop, nRegion);

        LOGGER.info("\nAll 6 capital city reports have been generated.");
    }

    private void runCapitalCityReportsNonInteractive() {
        LOGGER.info("\n===== CAPITAL CITY REPORTS (Non-interactive) =====");
        // Minimal subset for CI – no input required
        ReportAllCapitalCitiesByPopulation.generateReport(con);
    }

    // ----------------------------------------------------------
    //  CITY REPORTS (FeatureCity_report)
    // ----------------------------------------------------------

    private void runCityReportsInteractive(Scanner scanner) {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("\n===== CITY REPORTS =====");
            LOGGER.info("\n=== 1. All cities in the WORLD by population ===");
        }
        FeatureReportAllCitiesByPopulation.generateReport(con);

        // 2. All cities in a continent
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("\nEnter Continent Name for ALL cities by population: ");
        }
        String continentAll = scanner.nextLine();
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO,
                    "\n=== 2. All cities in continent {0} by population ===",
                    continentAll);
        }
        FeatureReportCitiesByContinent.generateReport(con, continentAll);

        // 3. All cities in a region
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("\nEnter Region Name for ALL cities by population: ");
        }
        String regionAll = scanner.nextLine();
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO,
                    "\n=== 3. All cities in region {0} by population ===",
                    regionAll);
        }
        FeatureReportCitiesByRegion.generateReport(con, regionAll);

        // 4. All cities in a country
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("\nEnter Country Name for ALL cities by population: ");
        }
        String countryAll = scanner.nextLine();
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO,
                    "\n=== 4. All cities in country {0} by population ===",
                    countryAll);
        }
        FeatureReportCitiesByCountry.generateReport(con, countryAll);

        // 5. All cities in a district
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("\nEnter District Name for ALL cities by population: ");
        }
        String districtAll = scanner.nextLine();
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO,
                    "\n=== 5. All cities in district {0} by population ===",
                    districtAll);
        }
        FeatureReportCitiesByDistrict.generateReport(con, districtAll);

        // 6. Top N cities in the world
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("\nEnter N for TOP N cities in the WORLD: ");
        }
        int nWorld = scanner.nextInt();
        scanner.nextLine();
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO,
                    "\n=== 6. Top {0} cities in the WORLD by population ===",
                    nWorld);
        }
        FeatureReportTopNCitiesWorld.generateReport(con, nWorld);

        // 7. Top N cities in a continent
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("\nEnter Continent for TOP N cities: ");
        }
        String topContinent = scanner.nextLine();
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO,
                    "Enter N for TOP N cities in continent {0}:",
                    topContinent);
        }
        int nContinent = scanner.nextInt();
        scanner.nextLine();
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO,
                    "\n=== 7. Top {0} cities in continent {1} ===",
                    new Object[]{nContinent, topContinent});
        }
        FeatureReportTopNCitiesContinent.generateReport(con, topContinent, nContinent);

        // 8. Top N cities in a region
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("\nEnter Region for TOP N cities: ");
        }
        String topRegion = scanner.nextLine();
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO,
                    "Enter N for TOP N cities in region {0}:",
                    topRegion);
        }
        int nRegion = scanner.nextInt();
        scanner.nextLine();
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO,
                    "\n=== 8. Top {0} cities in region {1} ===",
                    new Object[]{nRegion, topRegion});
        }
        FeatureReportTopNCitiesRegion.generateReport(con, topRegion, nRegion);

        // 9. Top N cities in a country
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("\nEnter Country for TOP N cities: ");
        }
        String topCountry = scanner.nextLine();
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO,
                    "Enter N for TOP N cities in country {0}:",
                    topCountry);
        }
        int nCountry = scanner.nextInt();
        scanner.nextLine();
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO,
                    "\n=== 9. Top {0} cities in country {1} ===",
                    new Object[]{nCountry, topCountry});
        }
        FeatureReportTopNCitiesCountry.generateReport(con, topCountry, nCountry);

        // 10. Top N cities in a district
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("\nEnter District for TOP N cities: ");
        }
        String topDistrict = scanner.nextLine();
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO,
                    "Enter N for TOP N cities in district {0}:",
                    topDistrict);
        }
        int nDistrict = scanner.nextInt();
        scanner.nextLine();
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO,
                    "\n=== 10. Top {0} cities in district {1} ===",
                    new Object[]{nDistrict, topDistrict});
        }
        FeatureReportTopNCitiesDistrict.generateReport(con, topDistrict, nDistrict);
    }


    private void runCityReportsNonInteractive() {
        LOGGER.info("\n===== CITY REPORTS (Non-interactive) =====");
        // Simple world-level report for CI
        FeatureReportAllCitiesByPopulation.generateReport(con);
    }

    // ----------------------------------------------------------
    //  BASIC POPULATION REPORTS
    // ----------------------------------------------------------

    private void runBasicPopulationReportsInteractive(Scanner scanner) {
        LOGGER.info("\n===== BASIC POPULATION REPORTS =====");

        // 1. World population (no input required)
        LOGGER.info("\n===== 1. Population of the WORLD =====");
        BasicReportWorldPopulation.generateReport(con);

        // 2. Continent population
        LOGGER.info("\nEnter Continent name: ");
        String continent = scanner.nextLine().trim();
        LOGGER.log(Level.INFO,
                "\n===== 2. Population of continent {0} =====",
                continent);
        BasicReportPopulationOfContinent.generateReport(con, continent);

        // 3. Region population
        LOGGER.info("\nEnter Region name: ");
        String region = scanner.nextLine().trim();
        LOGGER.log(Level.INFO,
                "\n===== 3. Population of region {0} =====",
                region);
        BasicReportPopulationOfRegion.generateReport(con, region);

        // 4. Country population
        LOGGER.info("\nEnter Country name: ");
        String country = scanner.nextLine().trim();
        LOGGER.log(Level.INFO,
                "\n===== 4. Population of country {0} =====",
                country);
        BasicReportPopulationOfCountry.generateReport(con, country);

        // 5. District population
        LOGGER.info("\nEnter District name: ");
        String district = scanner.nextLine().trim();
        LOGGER.log(Level.INFO,
                "\n===== 5. Population of district {0} =====",
                district);
        BasicReportPopulationOfDistrict.generateReport(con, district);

        // 6. City population
        LOGGER.info("\nEnter City name: ");
        String city = scanner.nextLine().trim();
        LOGGER.log(Level.INFO,
                "\n===== 6. Population of city {0} =====",
                city);
        BasicReportPopulationOfCity.generateReport(con, city);

        // 7. Language populations (fixed set)
        LOGGER.info("\n===== 7. Population by Selected Languages =====");
        BasicReportLanguagePopulation.generateReport(con);
    }

    private void runBasicPopulationReportsNonInteractive() {
        LOGGER.info("\n===== BASIC POPULATION REPORTS (Non-interactive) =====");

        // You can change these defaults to any valid values in your world database
        String continent = "Asia";
        String region = "Southeast Asia";
        String country = "Myanmar";
        String district = "Yangon";
        String city = "Yangon";

        // 1. World
        LOGGER.info("\n===== 1. Population of the WORLD =====");
        BasicReportWorldPopulation.generateReport(con);

        // 2. Continent
        LOGGER.log(Level.INFO,
                "\n===== 2. Population of continent {0} =====",
                continent);
        BasicReportPopulationOfContinent.generateReport(con, continent);

        // 3. Region
        LOGGER.log(Level.INFO,
                "\n===== 3. Population of region {0} =====",
                region);
        BasicReportPopulationOfRegion.generateReport(con, region);

        // 4. Country
        LOGGER.log(Level.INFO,
                "\n===== 4. Population of country {0} =====",
                country);
        BasicReportPopulationOfCountry.generateReport(con, country);

        // 5. District
        LOGGER.log(Level.INFO,
                "\n===== 5. Population of district {0} =====",
                district);
        BasicReportPopulationOfDistrict.generateReport(con, district);

        // 6. City
        LOGGER.log(Level.INFO,
                "\n===== 6. Population of city {0} =====",
                city);
        BasicReportPopulationOfCity.generateReport(con, city);

        // 7. Languages
        LOGGER.info("\n===== 7. Population by Selected Languages =====");
        BasicReportLanguagePopulation.generateReport(con);
    }

    // ----------------------------------------------------------
    //  POLICYMAKER REPORTS
    // ----------------------------------------------------------

    private void runPolicyMakerReports() {
        LOGGER.info("\n===== POLICYMAKER POPULATION REPORTS =====");

        LOGGER.info("\n===== 1. Population of each Continent =====");
        ReportPopulationByContinent.generateReport(con);

        LOGGER.info("\n===== 2. Population of each Region =====");
        ReportPopulationByRegion.generateReport(con);

        LOGGER.info("\n===== 3. Population of each Country =====");
        ReportPopulationByCountry.generateReport(con);

        LOGGER.info("\nAll world population reports for policymakers have been generated.");
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
            LOGGER.info(prompt);
            while (!scanner.hasNextInt()) {
                LOGGER.warning("Please enter a valid number: ");
                scanner.next(); // discard invalid token
            }
            value = scanner.nextInt();
            scanner.nextLine(); // consume newline
            if (value <= 0) {
                LOGGER.warning("N must be a positive integer.");
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
     * - Local run (IntelliJ): no args → connect to localhost:33060 and run all reports interactively.
     * - Docker / CI: args[0] = host:port, args[1] = delayMs → non-interactive subset of reports.
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
                LOGGER.info("Running in non-interactive mode (Docker/CI)...");
                app.runAllReportsNonInteractive();
            } else {
                // Interactive mode
                Scanner scanner = new Scanner(System.in);
                app.runAllReportsInteractive(scanner);
                scanner.close();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error running application", e);
        } finally {
            app.disconnect();
        }
    }
}
