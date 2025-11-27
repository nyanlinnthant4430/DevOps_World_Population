package com.napier.devops.basicpopulation;

import de.vandermeer.asciitable.AsciiTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BasicReportPopulationOfContinent {

    private static final Logger LOGGER =
            Logger.getLogger(BasicReportPopulationOfContinent.class.getName());

    public static void generateReport(Connection con, String continent) {

        String sql = """
                SELECT Continent, SUM(Population) AS Population
                FROM country
                WHERE Continent = ?
                GROUP BY Continent;
                """;

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, continent);
            ResultSet rset = stmt.executeQuery();

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("Continent", "Population");
            table.addRule();

            if (rset.next()) {
                Population p = new Population();
                p.setName(rset.getString("Continent"));
                p.setTotalPopulation(rset.getLong("Population"));

                table.addRow(p.getName(), p.getTotalPopulation());
            }
            else {
                table.addRow(continent, "No data");
            }

            table.addRule();

            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info(table.render());
            }
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error generating continent population report", e);
        }
    }
}
