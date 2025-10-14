package com.napier.devops;

import java.sql.Connection;
import java.util.Scanner;

/**
 * Handles the interactive menu for generating population reports.
 */
public class Menu {

    /**
     * Displays the menu and handles user selections.
     *
     * @param con The database connection to use for reports.
     */
    public void showMenu(Connection con) {
        Scanner sc = new Scanner(System.in);
        int choice = -1;

        while (choice != 0) {
            System.out.println("\n========================================");
            System.out.println("   🌍 WORLD POPULATION REPORT MENU");
            System.out.println("========================================");
            System.out.println("1. Population of each Continent");
            System.out.println("2. Population of each Region");
            System.out.println("3. Population of each Country");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {
                case 1 -> {
                    ReportContinent continentReport = new ReportContinent();
                    continentReport.generateReport(con);
                }
                case 2 -> {
                    ReportRegion regionReport = new ReportRegion();
                    regionReport.generateReport(con);
                }
                case 3 -> {
                    ReportCountry countryReport = new ReportCountry();
                    countryReport.generateReport(con);
                }
                case 0 -> System.out.println("👋 Exiting application. Goodbye!");
                default -> System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }
}



