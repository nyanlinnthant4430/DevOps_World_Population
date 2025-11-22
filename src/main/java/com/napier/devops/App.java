package com.napier.devops;
import com.napier.devops.city_report.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;
public class App {
    private Connection con = null;
    /**
     * Connect to MySQL database with retry logic.
     * Works for both:
     * - "localhost:33060" (IntelliJ/local)
     * - "db:3306" (Docker/CI)
     */
    public void connect(String location, int delay) {
        int retries = 10;
        while (retries > 0) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
// delay mainly used in Docker/CI
                Thread.sleep(delay);
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
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {}
            }
        }
        if (con == null) {
            System.out.println("FATAL ERROR: Cannot connect to MySQL at " + location);
            System.exit(-1);
        }
    }
    /** Convenience: local IntelliJ run → localhost:33060, no delay */
    public void connect() {
        connect("localhost:33060", 0);
    }
    /** For tests if needed */
    public Connection getConnection() {
        return con;
    }
    /** Disconnect safely */
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
    public static void main(String[] args) {
        App app = new App();
// MODE 1: local run (no args) → localhost
        if (args.length < 2) {
            app.connect(); // localhost:33060
        }
// MODE 2: Docker / CI → db host + delay
        else {
            String host = args[0]; // e.g. "db:3306"
            int delay = Integer.parseInt(args[1]); // e.g. 30000
            app.connect(host, delay);
        }
// NON-INTERACTIVE (Docker/CI) → no Scanner, just first report
        if (System.console() == null && args.length >= 2) {
            System.out.println("Running in non-interactive (Docker) mode...");
            System.out.println("\n=== 1. All cities in the WORLD by population ===");
            ReportAllCitiesByPopulation.generateReport(app.con);
            app.disconnect();
            return;
        }
// INTERACTIVE (IntelliJ/local)
// 1️⃣ First report: no scanner, run immediately
        System.out.println("\n=== 1. All cities in the WORLD by population ===");
        ReportAllCitiesByPopulation.generateReport(app.con);
// 2️⃣–🔟 Use Scanner for all inputs
        Scanner scanner = new Scanner(System.in);
// 2. All cities in a continent
        System.out.print("\nEnter Continent Name for ALL cities by population: ");
        String continentAll = scanner.nextLine();
        System.out.println("\n=== 2. All cities in continent '" + continentAll + "' by population ===");
        ReportCitiesByContinent.generateReport(app.con, continentAll);
// 3. All cities in a region
        System.out.print("\nEnter Region Name for ALL cities by population: ");
        String regionAll = scanner.nextLine();
        System.out.println("\n=== 3. All cities in region '" + regionAll + "' by population ===");
        ReportCitiesByRegion.generateReport(app.con, regionAll);
// 4. All cities in a country
        System.out.print("\nEnter Country Name for ALL cities by population: ");
        String countryAll = scanner.nextLine();
        System.out.println("\n=== 4. All cities in country '" + countryAll + "' by population ===");
        ReportCitiesByCountry.generateReport(app.con, countryAll);
// 5. All cities in a district
        System.out.print("\nEnter District Name for ALL cities by population: ");
        String districtAll = scanner.nextLine();
        System.out.println("\n=== 5. All cities in district '" + districtAll + "' by population ===");
        ReportCitiesByDistrict.generateReport(app.con, districtAll);
// 6. Top N cities in the world
        System.out.print("\nEnter N for TOP N cities in the WORLD: ");
        int nWorld = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.println("\n=== 6. Top " + nWorld + " cities in the WORLD by population ===");
        ReportTopNCitiesWorld.generateReport(app.con, nWorld);
// 7. Top N cities in a continent
        System.out.print("\nEnter Continent for TOP N cities: ");
        String topContinent = scanner.nextLine();
        System.out.print("Enter N for TOP N cities in continent '" + topContinent + "': ");
        int nContinent = scanner.nextInt();
        scanner.nextLine();
        System.out.println("\n=== 7. Top " + nContinent + " cities in continent '" + topContinent + "' ===");
        ReportTopNCitiesContinent.generateReport(app.con, topContinent, nContinent);
// 8. Top N cities in a region
        System.out.print("\nEnter Region for TOP N cities: ");
        String topRegion = scanner.nextLine();
        System.out.print("Enter N for TOP N cities in region '" + topRegion + "': ");
        int nRegion = scanner.nextInt();
        scanner.nextLine();
        System.out.println("\n=== 8. Top " + nRegion + " cities in region '" + topRegion + "' ===");
        ReportTopNCitiesRegion.generateReport(app.con, topRegion, nRegion);
// 9. Top N cities in a country
        System.out.print("\nEnter Country for TOP N cities: ");
        String topCountry = scanner.nextLine();
        System.out.print("Enter N for TOP N cities in country '" + topCountry + "': ");
        int nCountry = scanner.nextInt();
        scanner.nextLine();
        System.out.println("\n=== 9. Top " + nCountry + " cities in country '" + topCountry + "' ===");
        ReportTopNCitiesCountry.generateReport(app.con, topCountry, nCountry);
// 10. Top N cities in a district
        System.out.print("\nEnter District for TOP N cities: ");
        String topDistrict = scanner.nextLine();
        System.out.print("Enter N for TOP N cities in district '" + topDistrict + "': ");
        int nDistrict = scanner.nextInt();
        scanner.nextLine();
        System.out.println("\n=== 10. Top " + nDistrict + " cities in district '" + topDistrict + "' ===");
        ReportTopNCitiesDistrict.generateReport(app.con, topDistrict, nDistrict);
        scanner.close();
        app.disconnect();
    }
}