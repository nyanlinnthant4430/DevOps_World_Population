package com.napier.devops.feature_basicpopulation;

import de.vandermeer.asciitable.AsciiTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReportPopulationOfRegion
{
    public static void generateReport(Connection con, String region)
    {
        String sql = """
                SELECT Region, SUM(Population) AS Population
                FROM country
                WHERE Region = ?
                GROUP BY Region;
                """;

        try (PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, region);
            ResultSet rset = stmt.executeQuery();

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("Region", "Population");
            table.addRule();

            if (rset.next())
            {
                long population = rset.getLong("Population");
                table.addRow(rset.getString("Region"), population);
            }
            else
            {
                table.addRow(region, "No data");
            }
            table.addRule();

            System.out.println(table.render());
        }
        catch (Exception e)
        {
            System.out.println("Error generating region population report: " + e.getMessage());
        }
    }
}
