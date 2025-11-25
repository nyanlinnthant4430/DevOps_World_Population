package com.napier.devops.country_report;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.sql.*;
import java.util.LinkedList;

/**
 * Generates a report displaying all countries in a continent
 * with columns:
 * Code, Name, Continent, Region, Population, Capital
 * ordered by population (descending).
 */
public class ReportCountriesByContinent
{
    /**
     * Generates and displays a report of countries in a specified continent
     * sorted by population in descending order.
     *
     * @param con       the database connection
     * @param continent the continent to filter by
     */
    public static void generateReport(Connection con, String continent)
    {
        try
        {
            PreparedStatement pstmt = con.prepareStatement("""
                    SELECT
                        country.Code,
                        country.Name,
                        country.Continent,
                        country.Region,
                        country.Population,
                        city.Name AS Capital
                    FROM country
                    LEFT JOIN city ON country.Capital = city.ID
                    WHERE country.Continent = ?
                    ORDER BY country.Population DESC;
                    """);
            pstmt.setString(1, continent);

            ResultSet rset = pstmt.executeQuery();

            LinkedList<Country> countries = new LinkedList<>();

            while (rset.next())
            {
                countries.add(new Country(
                        rset.getString("Code"),
                        rset.getString("Name"),
                        rset.getString("Continent"),
                        rset.getString("Region"),
                        rset.getInt("Population"),
                        rset.getString("Capital")
                ));
            }

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("Code", "Name", "Continent", "Region", "Population", "Capital");
            table.addRule();

            for (Country c : countries)
            {
                table.addRow(
                        c.getCode(),
                        c.getName(),
                        c.getContinent(),
                        c.getRegion(),
                        c.getPopulation(),
                        c.getCapital()
                );
                table.addRule();
            }

            table.setTextAlignment(TextAlignment.CENTER);

            System.out.println("\n--- Countries in " + continent + " by Population ---");
            System.out.println(table.render());
        }
        catch (Exception e)
        {
            System.out.println("Error generating CountriesByContinent report: " + e.getMessage());
        }
    }
}
