    package com.napier.devops;

    import java.sql.Connection;
    import java.util.Scanner;
    import com.napier.devops.FeatureCity_report.*;

    public class Menu {
        public static void showMenu(Connection con) {
            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("\n=== City Population Reports ===");
                System.out.println("1. All cities in the world by population");
                System.out.println("2. All cities in a continent by population");
                System.out.println("3. All cities in a region by population");
                System.out.println("4. All cities in a country by population");
                System.out.println("5. All cities in a district by population");
                System.out.println("6. Top N populated cities in the world");
                System.out.println("7. Top N populated cities in a continent");
                System.out.println("8. Top N populated cities in a region");
                System.out.println("9. Top N populated cities in a country");
                System.out.println("10. Top N populated cities in a district");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> FeatureReportAllCitiesByPopulation.generateReport(con);
                    case 2 -> {
                        System.out.print("Enter Continent Name: ");
                        String continent = scanner.nextLine();
                        FeatureReportCitiesByContinent.generateReport(con, continent);
                    }
                    case 3 -> {
                        System.out.print("Enter Region Name: ");
                        String region = scanner.nextLine();
                        FeatureReportCitiesByRegion.generateReport(con, region);
                    }
                    case 4 -> {
                        System.out.print("Enter Country Name: ");
                        String country = scanner.nextLine();
                        FeatureReportCitiesByCountry.generateReport(con, country);
                    }
                    case 5 -> {
                        System.out.print("Enter District Name: ");
                        String district = scanner.nextLine();
                        FeatureReportCitiesByDistrict.generateReport(con, district);
                    }
                    case 6 -> {
                        System.out.print("Enter N: ");
                        int n = scanner.nextInt();
                        FeatureReportTopNCitiesWorld.generateReport(con, n);
                    }
                    case 7 -> {
                        System.out.print("Enter Continent Name: ");
                        String continent = scanner.nextLine();
                        System.out.print("Enter N: ");
                        int n = scanner.nextInt();
                        FeatureReportTopNCitiesContinent.generateReport(con, continent, n);
                    }
                    case 8 -> {
                        System.out.print("Enter Region Name: ");
                        String region = scanner.nextLine();
                        System.out.print("Enter N: ");
                        int n = scanner.nextInt();
                        FeatureReportTopNCitiesRegion.generateReport(con, region, n);
                    }
                    case 9 -> {
                        System.out.print("Enter Country Name: ");
                        String country = scanner.nextLine();
                        System.out.print("Enter N: ");
                        int n = scanner.nextInt();
                        FeatureReportTopNCitiesCountry.generateReport(con, country, n);
                    }
                    case 10 -> {
                        System.out.print("Enter District Name: ");
                        String district = scanner.nextLine();
                        System.out.print("Enter N: ");
                        int n = scanner.nextInt();
                        FeatureReportTopNCitiesDistrict.generateReport(con, district, n);
                    }
                    case 0 -> System.out.println("Exiting...");
                    default -> System.out.println("Invalid choice! Try again.");
                }
            } while (choice != 0);

            scanner.close();
        }
    }
