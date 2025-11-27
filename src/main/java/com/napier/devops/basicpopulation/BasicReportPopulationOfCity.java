package com.napier.devops.basicpopulation;

import de.vandermeer.asciitable.AsciiTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BasicReportPopulationOfCity {

    private static final Logger LOGGER =
            Logger.getLogger(BasicReportPopulationOfCity.class.getName());

    public static void generateReport(Connection con, String city) {

        String sql = """
                SELECT Name, Population
                FROM city
                WHERE Name = ?
                """;

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, city);
            ResultSet rset = stmt.executeQuery();

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("City", "Population");
            table.addRule();

            if (rset.next()) {
                Population p = new Population();
                p.setName(rset.getString("Name"));
                p.setTotalPopulation(rset.getLong("Population"));

                table.addRow(p.getName(), p.getTotalPopulation());
            }
            else {
                table.addRow(city, "No data");
            }

            table.addRule();

            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info(table.render());
            }
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error generating city population report", e);
        }
    }
}
