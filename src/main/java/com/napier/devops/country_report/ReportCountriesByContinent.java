package com.napier.devops.country_report;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.sql.*;
import java.util.LinkedList;

/**
 * Generates a report displaying all countries within a specific continent organized by population.
 * The report retrieves data from the database and displays it in descending order of population.
 */
public class ReportCountriesByContinent {

    /**
     * Generates and displays a report of countries in a specified continent sorted by population in descending order.
     * Uses a prepared statement to filter countries by continent and formats the output as an ASCII table.
     *
     * @param con the database connection to use for querying data
     * @param continent the name of the continent to filter countries by
     */
    public static void generateReport(Connection con, String continent) {
        try {
            // Create a prepared statement to prevent SQL injection and filter by continent
            PreparedStatement pstmt = con.prepareStatement(
                    "SELECT Name, Region, Population FROM country WHERE Continent = ? ORDER BY Population DESC;"
            );

            // Set the continent parameter in the prepared statement
            pstmt.setString(1, continent);

            // Execute the query and retrieve results
            ResultSet rset = pstmt.executeQuery();

            // Create a list to store Country objects
            LinkedList<Country> countries = new LinkedList<>();

            // Iterate through result set and create Country objects
            while (rset.next()) {
                countries.add(new Country(
                        rset.getString("Name"),
                        continent,
                        rset.getString("Region"),
                        rset.getInt("Population")
                ));
            }

            // Create ASCII table for formatted output
            AsciiTable table = new AsciiTable();

            // Add top border
            table.addRule();

            // Add header row with column names
            table.addRow("Name", "Region", "Population");
            table.addRule();

            // Add each country as a row in the table
            for (Country c : countries) {
                table.addRow(c.getName(), c.getRegion(), c.getPopulation());
                table.addRule();
            }

            // Set text alignment to center for all cells
            table.setTextAlignment(TextAlignment.CENTER);

            // Display the report title with continent name and rendered table
            System.out.println("\n--- Countries in " + continent + " by Population ---");
            System.out.println(table.render());
        } catch (Exception e) {
            // Print error message if query or table generation fails
            System.out.println("Error: " + e.getMessage());
        }
    }
}