package com.napier.devops.capital_city_report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

/**
 * Report 8:
 * All the capital cities in a continent organised by largest population to smallest.
 * Columns:
 *  - Name
 *  - Country
 *  - Population
 */
public class ReportCapitalCitiesByContinent {

    private static final Logger LOGGER =
            Logger.getLogger(ReportCapitalCitiesByContinent.class.getName());

    public static void generateReport(Connection con, String continent) {
        try {
            String sql = """
                    SELECT
                        city.Name      AS Name,
                        country.Name   AS Country,
                        city.Population AS Population
                    FROM city
                    JOIN country ON city.ID = country.Capital
                    WHERE country.Continent = ?
                    ORDER BY city.Population DESC;
                    """;

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, continent);
            ResultSet rset = pstmt.executeQuery();

            LinkedList<City> capitals = new LinkedList<>();

            while (rset.next()) {
                City c = new City();
                c.setName(rset.getString("Name"));
                c.setCountry(rset.getString("Country"));
                c.setPopulation(rset.getInt("Population"));
                capitals.add(c);
            }

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("Name", "Country", "Population");
            table.addRule();

            for (City c : capitals) {
                table.addRow(
                        c.getName(),
                        c.getCountryName(),
                        String.format("%,d", c.getPopulation())
                );
                table.addRule();
            }

            table.setTextAlignment(TextAlignment.CENTER);

            String header = String.format(
                    "\n--- Capital Cities in Continent: %s (by Population) ---",
                    continent
            );
            LOGGER.info(header);

            String output = table.render();
            LOGGER.info(output);
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error generating CapitalCitiesByContinent report", e);
        }
    }
}
