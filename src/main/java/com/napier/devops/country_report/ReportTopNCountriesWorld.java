package com.napier.devops.country_report;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.sql.*;
import java.util.LinkedList;

/**
 * Generates a report displaying the top N most populated countries in the world.
 * The report retrieves data from the database and displays it in descending order of population.
 */
public class ReportTopNCountriesWorld {

    /**
     * Generates and displays a report of the top N countries in the world sorted by population in descending order.
     * Uses a prepared statement to limit the number of results.
     * Formats the output as an ASCII table.
     *
     * @param con the database connection to use for querying data
     * @param n the number of top countries to retrieve and display
     */
    public void generateReport(Connection con, int n) {
        try {
            // Create a prepared statement to retrieve top N countries and prevent SQL injection
            PreparedStatement pstmt = con.prepareStatement(
                    "SELECT Name, Continent, Region, Population FROM country ORDER BY Population DESC LIMIT ?;"
            );

            // Set the limit parameter to retrieve only top N countries
            pstmt.setInt(1, n);

            // Execute the query and retrieve results
            ResultSet rset = pstmt.executeQuery();

            // Create a list to store Country objects
            LinkedList<Country> countries = new LinkedList<>();

            // Iterate through result set and create Country objects
            while (rset.next()) {
                countries.add(new Country(
                        rset.getString("Name"),
                        rset.getString("Continent"),
                        rset.getString("Region"),
                        rset.getInt("Population")
                ));
            }

            // Create ASCII table for formatted output
            AsciiTable table = new AsciiTable();

            // Add top border
            table.addRule();

            // Add header row with column names
            table.addRow("Name", "Continent", "Region", "Population");
            table.addRule();

            // Add each country as a row in the table
            for (Country c : countries) {
                table.addRow(c.getName(), c.getContinent(), c.getRegion(), c.getPopulation());
                table.addRule();
            }

            // Set text alignment to center for all cells
            table.setTextAlignment(TextAlignment.CENTER);

            // Display the report title with number of countries, then render table
            System.out.println("\n--- Top " + n + " Countries in the World ---");
            System.out.println(table.render());
        } catch (Exception e) {
            // Print error message if query or table generation fails
            System.out.println("Error: " + e.getMessage());
        }
    }
}