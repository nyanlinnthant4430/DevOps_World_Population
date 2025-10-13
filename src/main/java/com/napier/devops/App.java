package com.napier.devops;

import de.vandermeer.asciitable.AsciiTable;
import java.sql.*;

public class App {

    private Connection con = null;

    public static void main(String[] args) {
        App app = new App();
        app.connect();

        // Run all six reports
        app.allCountriesByPopulation();
        app.countriesByContinent("Asia");
        app.countriesByRegion("Caribbean");
        app.topNCountriesInWorld(10);
        app.topNCountriesInContinent("Europe", 10);
        app.topNCountriesInRegion("Western Europe", 5);

        app.disconnect();
    }

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 20;
        for (int i = 0; i < retries; i++) {
            try {
                Thread.sleep(5000); // wait for DB container
                con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/world?allowPublicKeyRetrieval=true&useSSL=false",
                        "root", "example");
                System.out.println("Connected to database");
                break;
            } catch (Exception e) {
                System.out.println("Connection attempt " + (i+1) + " failed");
            }
        }
    }

    public void disconnect() {
        try {
            if (con != null) con.close();
            System.out.println("Disconnected from database");
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    // ================== Reports ==================

    public void allCountriesByPopulation() {
        System.out.println("\n=== All Countries in the World ===");
        String sql = "SELECT Name, Continent, Region, Population FROM country ORDER BY Population DESC";
        executeAndPrint(sql);
    }

    public void countriesByContinent(String continent) {
        System.out.println("\n=== Countries in Continent: " + continent + " ===");
        String sql = "SELECT Name, Continent, Region, Population FROM country WHERE Continent = '" + continent + "' ORDER BY Population DESC";
        executeAndPrint(sql);
    }

    public void countriesByRegion(String region) {
        System.out.println("\n=== Countries in Region: " + region + " ===");
        String sql = "SELECT Name, Continent, Region, Population FROM country WHERE Region = '" + region + "' ORDER BY Population DESC";
        executeAndPrint(sql);
    }

    public void topNCountriesInWorld(int n) {
        System.out.println("\n=== Top " + n + " Countries in the World ===");
        String sql = "SELECT Name, Continent, Region, Population FROM country ORDER BY Population DESC LIMIT " + n;
        executeAndPrint(sql);
    }

    public void topNCountriesInContinent(String continent, int n) {
        System.out.println("\n=== Top " + n + " Countries in Continent: " + continent + " ===");
        String sql = "SELECT Name, Continent, Region, Population FROM country WHERE Continent = '" + continent + "' ORDER BY Population DESC LIMIT " + n;
        executeAndPrint(sql);
    }

    public void topNCountriesInRegion(String region, int n) {
        System.out.println("\n=== Top " + n + " Countries in Region: " + region + " ===");
        String sql = "SELECT Name, Continent, Region, Population FROM country WHERE Region = '" + region + "' ORDER BY Population DESC LIMIT " + n;
        executeAndPrint(sql);
    }

    // ================== Helper ==================

    private void executeAndPrint(String sql) {
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            printAsciiTable(rs);
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }

    private void printAsciiTable(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();

        AsciiTable table = new AsciiTable();

        // Header
        String[] headers = new String[cols];
        for (int i = 1; i <= cols; i++) headers[i-1] = meta.getColumnName(i);
        table.addRule();
        table.addRow((Object[]) headers);
        table.addRule();

        // Rows
        while (rs.next()) {
            Object[] row = new Object[cols];
            for (int i = 1; i <= cols; i++) row[i-1] = rs.getString(i);
            table.addRow(row);
            table.addRule();
        }

        System.out.println(table.render());
    }
}
