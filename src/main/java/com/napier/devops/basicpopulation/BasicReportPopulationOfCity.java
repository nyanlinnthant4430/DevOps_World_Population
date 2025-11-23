package com.napier.devops.basicpopulation;

import de.vandermeer.asciitable.AsciiTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BasicReportPopulationOfCity
{
    public static void generateReport(Connection con, String city)
    {
        String sql = """
                SELECT Name, Population
                FROM city
                WHERE Name = ?;
                """;

        try (PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, city);
            ResultSet rset = stmt.executeQuery();

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("City", "Population");
            table.addRule();

            if (rset.next())
            {
                table.addRow(rset.getString("Name"), rset.getLong("Population"));
            }
            else
            {
                table.addRow(city, "No data");
            }
            table.addRule();

            System.out.println(table.render());
        }
        catch (Exception e)
        {
            System.out.println("Error generating city population report: " + e.getMessage());
        }
    }
}
