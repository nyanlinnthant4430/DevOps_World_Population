package com.napier.devops.country_report;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.sql.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generates a report displaying the top N most populated countries
 * in a given continent with columns:
 * Code, Name, Continent, Region, Population, Capital
 */
public class ReportTopNCountriesContinent {

    private static final Logger LOGGER =
            Logger.getLogger(ReportTopNCountriesContinent.class.getName());

    /**
     * Generates and displays a report of the top N countries in a specified continent
     * sorted by population in descending order.
     *
     * @param con       the database connection
     * @param continent the continent to filter by
     * @param n         the number of countries to display
     */
    public static void generateReport(Connection con, String continent, int n) {
        try {
            PreparedStatement pstmt = con.prepareStatement("""
                    SELECT
                        country.Code,
                        country.Name,
                        country.Continent,
                        country.Region,
                        country.Population,
                        city.Name AS Capital
                    FROM country
                    LEFT JOIN city ON country.Capital = city.ID
                    WHERE country.Continent = ?
                    ORDER BY country.Population DESC
                    LIMIT ?
                    """);
            pstmt.setString(1, continent);
            pstmt.setInt(2, n);

            ResultSet rset = pstmt.executeQuery();

            LinkedList<Country> countries = new LinkedList<>();

            while (rset.next()) {
                countries.add(new Country(
                        rset.getString("Code"),
                        rset.getString("Name"),
                        rset.getString("Continent"),
                        rset.getString("Region"),
                        rset.getInt("Population"),
                        rset.getString("Capital")
                ));
            }

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("Code", "Name", "Continent", "Region", "Population", "Capital");
            table.addRule();

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

            table.setTextAlignment(TextAlignment.CENTER);

            // Build header string separately to avoid GuardLogStatement warning
            String header = String.format(
                    "\n--- Top %d Countries in %s ---", n, continent);
            LOGGER.info(header);

            String output = table.render();
            LOGGER.info(output);
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error generating TopNCountriesContinent report", e);
        }
    }
}
