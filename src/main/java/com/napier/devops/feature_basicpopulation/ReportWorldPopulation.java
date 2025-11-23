package com.napier.devops.feature_basicpopulation;

import de.vandermeer.asciitable.AsciiTable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ReportWorldPopulation
{
    public static void generateReport(Connection con)
    {
        try (Statement stmt = con.createStatement();
             ResultSet rset = stmt.executeQuery(
                     "SELECT SUM(Population) AS Population FROM country"))
        {
            long population = 0;
            if (rset.next())
            {
                population = rset.getLong("Population");
            }

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("World Population");
            table.addRule();
            table.addRow(population);
            table.addRule();

            System.out.println(table.render());
        }
        catch (Exception e)
        {
            System.out.println("Error generating world population report: " + e.getMessage());
        }
    }
}
