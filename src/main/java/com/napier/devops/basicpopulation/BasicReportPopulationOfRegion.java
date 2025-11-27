package com.napier.devops.basicpopulation;

import de.vandermeer.asciitable.AsciiTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BasicReportPopulationOfRegion {

    private static final Logger LOGGER =
            Logger.getLogger(BasicReportPopulationOfRegion.class.getName());

    public static void generateReport(Connection con, String region) {

        String sql = """
                SELECT Region, SUM(Population) AS Population
                FROM country
                WHERE Region = ?
                GROUP BY Region;
                """;

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, region);
            ResultSet rset = stmt.executeQuery();

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("Region", "Population");
            table.addRule();

            if (rset.next()) {
                Population p = new Population();
                p.setName(rset.getString("Region"));
                p.setTotalPopulation(rset.getLong("Population"));

                table.addRow(p.getName(), p.getTotalPopulation());
            }
            else {
                table.addRow(region, "No data");
            }

            table.addRule();

            // Avoid GuardLogStatement: build string separately
            String output = table.render();
            LOGGER.info(output);
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error generating region population report", e);
        }
    }
}
