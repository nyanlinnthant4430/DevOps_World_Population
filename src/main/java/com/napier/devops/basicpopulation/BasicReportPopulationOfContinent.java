package com.napier.devops.basicpopulation;

import de.vandermeer.asciitable.AsciiTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BasicReportPopulationOfContinent
{
    public static void generateReport(Connection con, String continent)
    {
        String sql = """
                SELECT Continent, SUM(Population) AS Population
                FROM country
                WHERE Continent = ?
                GROUP BY Continent;
                """;

        try (PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, continent);
            ResultSet rset = stmt.executeQuery();

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("Continent", "Population");
            table.addRule();

            if (rset.next())
            {
                long population = rset.getLong("Population");
                table.addRow(rset.getString("Continent"), population);
            }
            else
            {
                table.addRow(continent, "No data");
            }
            table.addRule();

            System.out.println(table.render());
        }
        catch (Exception e)
        {
            System.out.println("Error generating continent population report: " + e.getMessage());
        }
    }
}
