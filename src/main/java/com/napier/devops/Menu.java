//package com.napier.devops;
//
//import com.napier.devops.city_report.*;
//
//import java.sql.Connection;
//import java.util.Scanner;
//
//public class Menu {
//
//    public void show(Connection con) {
//        Scanner scanner = new Scanner(System.in);
//        int choice;
//
//        do {
//            System.out.println("\n===== CAPITAL CITY REPORT MENU =====");
//            System.out.println("1. All capital cities in the world by population");
//            System.out.println("2. All capital cities in a continent by population");
//            System.out.println("3. All capital cities in a region by population");
//            System.out.println("4. Top N capital cities in the world by population");
//            System.out.println("5. Top N capital cities in a continent by population");
//            System.out.println("6. Top N capital cities in a region by population");
//            System.out.println("0. Exit");
//            System.out.print("Enter your choice: ");
//
//            while (!scanner.hasNextInt()) {
//                System.out.println("Invalid input! Please enter a number between 0–6.");
//                scanner.next(); // discard invalid input
//            }
//
//            choice = scanner.nextInt();
//            scanner.nextLine(); // clear newline
//
//            switch (choice) {
//                case 1 -> ReportAllCapitalCitiesByPopulation.generateReport(con);
//                case 2 -> {
//                    System.out.print("Enter continent name: ");
//                    String continent = scanner.nextLine();
//                    ReportCapitalCitiesByContinent.generateReport(con, continent);
//                }
//                case 3 -> {
//                    System.out.print("Enter region name: ");
//                    String region = scanner.nextLine();
//                    ReportCapitalCitiesByRegion.generateReport(con, region);
//                }
//                case 4 -> {
//                    System.out.print("Enter N: ");
//                    int n = scanner.nextInt();
//                    ReportTopNCapitalCitiesWorld.generateReport(con, n);
//                }
//                case 5 -> {
//                    System.out.print("Enter continent name: ");
//                    String continent = scanner.nextLine();
//                    System.out.print("Enter N: ");
//                    int n = scanner.nextInt();
//                    ReportTopNCapitalCitiesContinent.generateReport(con, continent, n);
//                }
//                case 6 -> {
//                    System.out.print("Enter region name: ");
//                    String region = scanner.nextLine();
//                    System.out.print("Enter N: ");
//                    int n = scanner.nextInt();
//                    ReportTopNCapitalCitiesRegion.generateReport(con, region, n);
//                }
//                case 0 -> System.out.println("Exiting... Goodbye!");
//                default -> System.out.println("Invalid choice. Please try again.");
//            }
//
//        } while (choice != 0);
//    }
//}
