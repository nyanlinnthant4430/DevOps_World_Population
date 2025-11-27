package com.napier.devops.country_report;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.sql.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generates a report displaying all countries in the world organized by population.
 * The report retrieves data from the database and displays it in descending order of population.
 */
public class ReportAllCountriesByPopulation {

    private static final Logger LOGGER =
            Logger.getLogger(ReportAllCountriesByPopulation.class.getName());

    /**
     * Generates and displays a report of all countries sorted by population in descending order.
     * Retrieves country data from the database, creates Country objects, and formats the output as an ASCII table.
     *
     * @param con the database connection to use for querying data
     */
    public static void generateReport(Connection con) {
        try {
            Statement stmt = con.createStatement();

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

            LinkedList<Country> countries = new LinkedList<>();

            while (rset.next()) {
                String capital = rset.getString("Capital");
                if (capital == null || capital.isBlank()) {
                    capital = "N/A";
                } else {
                    capital = capital.replace("\n", " ");
                }

                countries.add(new Country(
                        rset.getString("Code"),
                        rset.getString("Name"),
                        rset.getString("Continent"),
                        rset.getString("Region"),
                        rset.getInt("Population"),
                        capital
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

            LOGGER.info("\n--- All Countries in the World by Population ---");
            String output = table.render();
            LOGGER.info(output);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error generating AllCountriesByPopulation report", e);
        }
    }
}
