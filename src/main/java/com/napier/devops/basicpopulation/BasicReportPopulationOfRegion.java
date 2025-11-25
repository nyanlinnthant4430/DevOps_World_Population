package com.napier.devops.basicpopulation;

import com.napier.devops.basicpopulation.Population;
import de.vandermeer.asciitable.AsciiTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BasicReportPopulationOfRegion
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
                Population p = new Population();
                p.setName(rset.getString("Region"));
                p.setTotalPopulation(rset.getLong("Population"));

                table.addRow(p.getName(), p.getTotalPopulation());
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
