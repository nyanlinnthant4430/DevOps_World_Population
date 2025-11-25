package com.napier.devops.country_report;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.sql.*;
import java.util.LinkedList;

/**
 * Generates a report displaying all countries in the world organized by population.
 * The report retrieves data from the database and displays it in descending order of population.
 */
public class ReportAllCountriesByPopulation {

    /**
     * Generates and displays a report of all countries sorted by population in descending order.
     * Retrieves country data from the database, creates Country objects, and formats the output as an ASCII table.
     *
     * @param con the database connection to use for querying data
     */
    public static void generateReport(Connection con) {
        try {
            // Create a statement object for executing SQL queries
            Statement stmt = con.createStatement();

            // Correct query: join to city to get capital name
            ResultSet rset = stmt.executeQuery(
                    "SELECT country.Code, " +
                            "country.Name, " +
                            "country.Continent, " +
                            "country.Region, " +
                            "country.Population, " +
                            "city.Name AS Capital " +
                            "FROM country " +
                            "LEFT JOIN city ON country.Capital = city.ID " +
                            "ORDER BY country.Population DESC;"
            );

            // Create a list to store Country objects
            LinkedList<Country> countries = new LinkedList<>();

            // Iterate through result set and create Country objects
//            while (rset.next()) {
//                countries.add(new Country(
//                        rset.getString("Code"),
//                        rset.getString("Name"),
//                        rset.getString("Continent"),
//                        rset.getString("Region"),
//                        rset.getInt("Population"),
//                        rset.getString("Capital")
//                ));
//            }

            while (rset.next()) {
                String capital = rset.getString("Capital");
                if (capital == null || capital.isBlank())
                    capital = "N/A";
                else
                    capital = capital.replace("\n", " ");

                countries.add(new Country(
                        rset.getString("Code"),
                        rset.getString("Name"),
                        rset.getString("Continent"),
                        rset.getString("Region"),
                        rset.getInt("Population"),
                        capital
                ));
            }

            // Create ASCII table for formatted output
            AsciiTable table = new AsciiTable();

            // Add top border
            table.addRule();

            // Add header row with column names
            table.addRow("Code", "Name", "Continent", "Region", "Population", "Capital");
            table.addRule();

            // Add each country as a row in the table
            for (Country c : countries) {
                table.addRow(
                        c.getCode(),
                        c.getName(),
                        c.getContinent(),
                        c.getRegion(),
                        c.getPopulation(),
                        c.getCapital()
                );
                table.addRule();
            }

            // Set text alignment to center for all cells
            table.setTextAlignment(TextAlignment.CENTER);

            // Display the report title and rendered table
            System.out.println("\n--- All Countries in the World by Population ---");
            System.out.println(table.render());
        } catch (Exception e) {
            // Print error message if query or table generation fails
            System.out.println("Error: " + e.getMessage());
        }
    }
}
