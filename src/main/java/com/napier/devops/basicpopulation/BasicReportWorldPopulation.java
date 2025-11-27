package com.napier.devops.basicpopulation;

import de.vandermeer.asciitable.AsciiTable;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BasicReportWorldPopulation {

    private static final Logger LOGGER =
            Logger.getLogger(BasicReportWorldPopulation.class.getName());

    public static void generateReport(Connection con) {
        try (Statement stmt = con.createStatement();
             ResultSet rset = stmt.executeQuery(
                     "SELECT SUM(Population) AS Population FROM country")) {

            Population p = new Population();

            if (rset.next()) {
                p.setName("World");
                p.setTotalPopulation(rset.getLong("Population"));
            }

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("World Population");
            table.addRule();
            table.addRow(p.getTotalPopulation());
            table.addRule();

            // Avoid GuardLogStatement: build string separately
            String output = table.render();
            LOGGER.info(output);
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error generating world population report", e);
        }
    }
}
