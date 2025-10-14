package com.napier.devops;

import java.sql.Connection;

/**
 * Main application class responsible for initializing database connection
 * and launching the report menu for user interaction.
 */
public class App {
    public static void main(String[] args) {
        // Connect to the database
        Connection con = DatabaseConnection.connect();

        // Initialize report classes
        ReportContinent continentReport = new ReportContinent();
        ReportRegion regionReport = new ReportRegion();
        ReportCountry countryReport = new ReportCountry();

        // Application loop to handle user input
        while (true) {
            int choice = Menu.showMenu();

            switch (choice) {
                case 1 -> continentReport.generateReport(con);
                case 2 -> regionReport.generateReport(con);
                case 3 -> countryReport.generateReport(con);
                case 0 -> {
                    System.out.println("Exiting application...");
                    DatabaseConnection.disconnect();
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice, please try again.");
            }
        }
    }
}

