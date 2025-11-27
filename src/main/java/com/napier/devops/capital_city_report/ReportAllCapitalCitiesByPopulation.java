package com.napier.devops.capital_city_report;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

/**
 * Report 7:
 * All the capital cities in the world organised by largest population to smallest.
 * Columns:
 *  - Name
 *  - Country
 *  - Population
 */
public class ReportAllCapitalCitiesByPopulation {

    private static final Logger LOGGER =
            Logger.getLogger(ReportAllCapitalCitiesByPopulation.class.getName());

    public static void generateReport(Connection con) {
        try {
            Statement stmt = con.createStatement();

            String sql = """
                    SELECT
                        city.Name      AS Name,
                        country.Name   AS Country,
                        city.Population AS Population
                    FROM city
                    JOIN country ON city.ID = country.Capital
                    ORDER BY city.Population DESC;
                    """;

            ResultSet rset = stmt.executeQuery(sql);

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

            String header = "\n--- All Capital Cities in the World by Population ---";
            LOGGER.info(header);

            String output = table.render();
            LOGGER.info(output);
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error generating AllCapitalCitiesByPopulation report", e);
        }
    }
}
