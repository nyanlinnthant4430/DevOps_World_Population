package com.napier.devops.basicpopulation;

import de.vandermeer.asciitable.AsciiTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BasicReportPopulationOfDistrict
{
    public static void generateReport(Connection con, String district)
    {
        String sql = """
                SELECT District, SUM(Population) AS Population
                FROM city
                WHERE District = ?
                GROUP BY District;
                """;

        try (PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, district);
            ResultSet rset = stmt.executeQuery();

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("District", "Population");
            table.addRule();

            if (rset.next())
            {
                Population p = new Population();
                p.setName(rset.getString("District"));
                p.setTotalPopulation(rset.getLong("Population"));

                table.addRow(p.getName(), p.getTotalPopulation());
            }
            else
            {
                table.addRow(district, "No data");
            }

            table.addRule();
            System.out.println(table.render());
        }
        catch (Exception e)
        {
            System.out.println("Error generating district population report: " + e.getMessage());
        }
    }
}
