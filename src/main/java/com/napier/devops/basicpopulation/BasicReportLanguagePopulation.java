package com.napier.devops.basicpopulation;

import de.vandermeer.asciitable.AsciiTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BasicReportLanguagePopulation {

    private static final Logger LOGGER =
            Logger.getLogger(BasicReportLanguagePopulation.class.getName());

    public static void generateReport(Connection con) {
        if (con == null) {
            LOGGER.warning("No database connection.");
            return;
        }

        try {
            long worldPopulation = 0;

            // Get world population
            try (Statement stmt = con.createStatement();
                 ResultSet rset = stmt.executeQuery(
                         "SELECT SUM(Population) AS Population FROM country")) {

                if (rset.next()) {
                    worldPopulation = rset.getLong("Population");
                }
            }

            if (worldPopulation == 0) {
                LOGGER.warning("World population could not be calculated.");
                return;
            }

            // Query five selected languages
            String sql = """
                    SELECT cl.Language,
                           SUM(c.Population * cl.Percentage / 100) AS Speakers
                    FROM countrylanguage cl
                    JOIN country c ON cl.CountryCode = c.Code
                    WHERE cl.Language IN ('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic')
                    GROUP BY cl.Language
                    ORDER BY Speakers DESC;
                    """;

            try (PreparedStatement stmt = con.prepareStatement(sql);
                 ResultSet rset = stmt.executeQuery()) {

                AsciiTable table = new AsciiTable();
                table.addRule();
                table.addRow("Language", "Speakers", "% of World Population");
                table.addRule();

                while (rset.next()) {
                    LanguagePopulation lp = new LanguagePopulation();

                    lp.setLanguage(rset.getString("Language"));
                    lp.setSpeakers(rset.getLong("Speakers"));
                    lp.setPercentageOfWorld(
                            (rset.getLong("Speakers") * 100.0) / worldPopulation
                    );

                    table.addRow(
                            lp.getLanguage(),
                            lp.getSpeakers(),
                            String.format("%.2f%%", lp.getPercentageOfWorld())
                    );
                    table.addRule();
                }

                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info(table.render());
                }
            }
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error generating language population report", e);
        }
    }
}
