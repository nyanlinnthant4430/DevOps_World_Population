package com.napier.devops.country_report;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.sql.*;
import java.util.LinkedList;

/**
 * Generates a report displaying the top N most populated countries within a specific region.
 * The report retrieves data from the database and displays it in descending order of population.
 */
public class ReportTopNCountriesRegion {

    /**
     * Generates and displays a report of the top N countries in a specified region sorted by population in descending order.
     * Uses a prepared statement to filter countries by region and limit the number of results.
     * Formats the output as an ASCII table.
     *
     * @param con the database connection to use for querying data
     * @param region the name of the region to filter countries by
     * @param n the number of top countries to retrieve and display
     */
    public static void generateReport(Connection con, String region, int n) {
        try {
            // Create a prepared statement to prevent SQL injection, filter by region and limit results
            PreparedStatement pstmt = con.prepareStatement(
                    "SELECT Name, Population FROM country WHERE Region = ? ORDER BY Population DESC LIMIT ?;"
            );

            // Set the region parameter in the prepared statement
            pstmt.setString(1, region);

            // Set the limit parameter to retrieve only top N countries
            pstmt.setInt(2, n);

            // Execute the query and retrieve results
            ResultSet rset = pstmt.executeQuery();

            // Create a list to store Country objects
            LinkedList<Country> countries = new LinkedList<>();

            // Iterate through result set and create Country objects
            // Note: Continent is set to empty string as it's not retrieved in this query
            while (rset.next()) {
                countries.add(new Country(
                        rset.getString("Name"),
                        "",
                        region,
                        rset.getInt("Population")
                ));
            }

            // Create ASCII table for formatted output
            AsciiTable table = new AsciiTable();

            // Add top border
            table.addRule();

            // Add header row with column names
            table.addRow("Name", "Population");
            table.addRule();

            // Add each country as a row in the table
            for (Country c : countries) {
                table.addRow(c.getName(), c.getPopulation());
                table.addRule();
            }

            // Set text alignment to center for all cells
            table.setTextAlignment(TextAlignment.CENTER);

            // Display the report title with number of countries and region name, then render table
            System.out.println("\n--- Top " + n + " Countries in " + region + " ---");
            System.out.println(table.render());
        } catch (Exception e) {
            // Print error message if query or table generation fails
            System.out.println("Error: " + e.getMessage());
        }
    }
}