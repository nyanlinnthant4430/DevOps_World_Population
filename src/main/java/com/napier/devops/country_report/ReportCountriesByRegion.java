package com.napier.devops.country_report;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.sql.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generates a report displaying all countries in a region
 * with columns:
 * Code, Name, Continent, Region, Population, Capital
 * ordered by population (descending).
 */
public class ReportCountriesByRegion {

    private static final Logger LOGGER =
            Logger.getLogger(ReportCountriesByRegion.class.getName());

    /**
     * Generates and displays a report of countries in a specified region
     * sorted by population in descending order.
     *
     * @param con    the database connection
     * @param region the region to filter by
     */
    public static void generateReport(Connection con, String region) {
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
                    WHERE country.Region = ?
                    ORDER BY country.Population DESC;
                    """);
            pstmt.setString(1, region);

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

            LOGGER.log(Level.INFO,
                    "\n--- Countries in Region: {0} by Population ---", region);
            String output = table.render();
            LOGGER.info(output);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error generating CountriesByRegion report", e);
        }
    }
}
