package com.napier.devops.basicpopulation;

import de.vandermeer.asciitable.AsciiTable;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class BasicReportWorldPopulation
{
    public static void generateReport(Connection con)
    {
        try (Statement stmt = con.createStatement();
             ResultSet rset = stmt.executeQuery("SELECT SUM(Population) AS Population FROM country"))
        {
            Population p = new Population();

            if (rset.next())
            {
                p.setName("World");
                p.setTotalPopulation(rset.getLong("Population"));
            }

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("World Population");
            table.addRule();
            table.addRow(p.getTotalPopulation());
            table.addRule();

            System.out.println(table.render());
        }
        catch (Exception e)
        {
            System.out.println("Error generating world population report: " + e.getMessage());
        }
    }
}
