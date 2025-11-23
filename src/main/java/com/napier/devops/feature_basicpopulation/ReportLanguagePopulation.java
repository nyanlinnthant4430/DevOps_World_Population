package com.napier.devops.feature_basicpopulation;

import de.vandermeer.asciitable.AsciiTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Shows the number of people who speak selected languages
 * (Chinese, English, Hindi, Spanish, Arabic)
 * ordered from greatest number to smallest,
 * including the percentage of the world population.
 */
public class ReportLanguagePopulation
{
    public static void generateReport(Connection con)
    {
        if (con == null)
        {
            System.out.println("No database connection.");
            return;
        }

        try
        {
            // 1) Get world population
            long worldPopulation = 0;
            try (Statement stmt = con.createStatement();
                 ResultSet rset = stmt.executeQuery("SELECT SUM(Population) AS Population FROM country"))
            {
                if (rset.next())
                {
                    worldPopulation = rset.getLong("Population");
                }
            }

            if (worldPopulation == 0)
            {
                System.out.println("World population could not be calculated.");
                return;
            }

            // 2) Get speakers for the 5 target languages
            String sql = """
                    SELECT cl.Language,
                           SUM(c.Population * cl.Percentage / 100) AS Speakers
                    FROM countrylanguage cl
                    JOIN country c ON cl.CountryCode = c.Code
                    WHERE cl.Language IN ('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic')
                    GROUP BY cl.Language
                    ORDER BY Speakers DESC;
                    """;

            try (PreparedStatement stmt = con.prepareStatement(sql);
                 ResultSet rset = stmt.executeQuery())
            {
                AsciiTable table = new AsciiTable();
                table.addRule();
                table.addRow("Language", "Speakers", "% of World Population");
                table.addRule();

                while (rset.next())
                {
                    String language = rset.getString("Language");
                    long speakers   = rset.getLong("Speakers");
                    double percent  = (speakers * 100.0) / worldPopulation;

                    table.addRow(language, speakers, String.format("%.2f%%", percent));
                    table.addRule();
                }

                System.out.println(table.render());
            }
        }
        catch (Exception e)
        {
            System.out.println("Error generating language population report: " + e.getMessage());
        }
    }
}
