package com.napier.devops.country_report;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.sql.*;
import java.util.LinkedList;

/**
 * Generates a report displaying the top N most populated countries
 * in a given region with columns:
 * Code, Name, Continent, Region, Population, Capital
 */
public class ReportTopNCountriesRegion
{
    /**
     * Generates and displays a report of the top N countries in a specified region
     * sorted by population in descending order.
     *
     * @param con    the database connection
     * @param region the region to filter by
     * @param n      the number of countries to display
     */
    public static void generateReport(Connection con, String region, int n)
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
                    WHERE country.Region = ?
                    ORDER BY country.Population DESC
                    LIMIT ?;
                    """);
            pstmt.setString(1, region);
            pstmt.setInt(2, n);

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

            System.out.println("\n--- Top " + n + " Countries in Region: " + region + " ---");
            System.out.println(table.render());
        }
        catch (Exception e)
        {
            System.out.println("Error generating TopNCountriesRegion report: " + e.getMessage());
        }
    }
}
