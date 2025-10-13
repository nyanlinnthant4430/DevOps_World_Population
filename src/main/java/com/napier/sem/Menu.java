package com.napier.sem;

import java.sql.Connection;
import java.util.Scanner;
import com.napier.sem.country_report.*;

public class Menu {
    public static void showMenu(Connection con) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Population Report Menu ===");
            System.out.println("1. All countries in the world by population (largest to smallest)");
            System.out.println("2. All countries in a continent by population");
            System.out.println("3. All countries in a region by population");
            System.out.println("4. Top N populated countries in the world");
            System.out.println("5. Top N populated countries in a continent");
            System.out.println("6. Top N populated countries in a region");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    new ReportAllCountriesByPopulation().generateReport(con);
                    break;

                case 2:
                    System.out.print("Enter Continent Name: ");
                    String continent = scanner.nextLine();
                    new ReportCountriesByContinent().generateReport(con, continent);
                    break;

                case 3:
                    System.out.print("Enter Region Name: ");
                    String region = scanner.nextLine();
                    new ReportCountriesByRegion().generateReport(con, region);
                    break;

                case 4:
                    System.out.print("Enter N (number of top countries): ");
                    int nWorld = scanner.nextInt();
                    new ReportTopNCountriesWorld().generateReport(con, nWorld);
                    break;

                case 5:
                    System.out.print("Enter Continent Name: ");
                    String continentTop = scanner.nextLine();
                    System.out.print("Enter N (number of top countries): ");
                    int nContinent = scanner.nextInt();
                    new ReportTopNCountriesContinent().generateReport(con, continentTop, nContinent);
                    break;

                case 6:
                    System.out.print("Enter Region Name: ");
                    String regionTop = scanner.nextLine();
                    System.out.print("Enter N (number of top countries): ");
                    int nRegion = scanner.nextInt();
                    new ReportTopNCountriesRegion().generateReport(con, regionTop, nRegion);
                    break;

                case 0:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice! Try again.");
            }

        } while (choice != 0);

        scanner.close();
    }
}
