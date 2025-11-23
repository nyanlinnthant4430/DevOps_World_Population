package com.napier.devops.basicpopulation;

import de.vandermeer.asciitable.AsciiTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BasicReportPopulationOfCountry
{
    public static void generateReport(Connection con, String country)
    {
        String sql = """
                SELECT Name, Population
                FROM country
                WHERE Name = ?;
                """;

        try (PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, country);
            ResultSet rset = stmt.executeQuery();

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("Country", "Population");
            table.addRule();

            if (rset.next())
            {
                table.addRow(rset.getString("Name"), rset.getLong("Population"));
            }
            else
            {
                table.addRow(country, "No data");
            }
            table.addRule();

            System.out.println(table.render());
        }
        catch (Exception e)
        {
            System.out.println("Error generating country population report: " + e.getMessage());
        }
    }
}
