package com.napier.devops.basicpopulation;

import de.vandermeer.asciitable.AsciiTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BasicReportPopulationOfCountry {

    private static final Logger LOGGER =
            Logger.getLogger(BasicReportPopulationOfCountry.class.getName());

    public static void generateReport(Connection con, String country) {

        String sql = """
                SELECT Name, Population
                FROM country
                WHERE Name = ?
                """;

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, country);
            ResultSet rset = stmt.executeQuery();

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("Country", "Population");
            table.addRule();

            if (rset.next()) {
                Population p = new Population();
                p.setName(rset.getString("Name"));
                p.setTotalPopulation(rset.getLong("Population"));

                table.addRow(p.getName(), p.getTotalPopulation());
            }
            else {
                table.addRow(country, "No data");
            }

            table.addRule();

            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info(table.render());
            }
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error generating country population report", e);
        }
    }
}
