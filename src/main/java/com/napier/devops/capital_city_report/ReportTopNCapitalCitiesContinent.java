package com.napier.devops.capital_city_report;

import java.sql.*;
import java.util.LinkedList;
import de.vandermeer.asciitable.AsciiTable;

public class ReportTopNCapitalCitiesContinent {

    public static void generateReport(Connection con, String continent, int n) {
        try {
            String sql = """
                    SELECT
                        city.Name      AS Name,
                        country.Name   AS Country,
                        city.Population AS Population
                    FROM city
                    INNER JOIN country ON city.ID = country.Capital
                    WHERE country.Continent = ?
                    ORDER BY city.Population DESC
                    LIMIT ?;
                    """;

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, continent);
            stmt.setInt(2, n);
            ResultSet rset = stmt.executeQuery();

            LinkedList<City> cities = new LinkedList<>();
            while (rset.next()) {
                City c = new City();
                c.setName(rset.getString("Name"));
                c.setCountry(rset.getString("Country"));
                c.setPopulation(rset.getInt("Population"));
                cities.add(c);
            }

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("Name", "Country", "Population");
            table.addRule();

            for (City c : cities) {
                table.addRow(
                        c.getName(),
                        c.getCountry(),
                        String.format("%,d", c.getPopulation())
                );
                table.addRule();
            }

            System.out.println("Top " + n + " Capital Cities in Continent: " + continent);
            System.out.println(table.render());
        } catch (Exception e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }
}
