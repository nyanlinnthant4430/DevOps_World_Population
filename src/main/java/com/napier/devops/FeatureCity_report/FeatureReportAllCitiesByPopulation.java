package com.napier.devops.FeatureCity_report;

import de.vandermeer.asciitable.AsciiTable;

import java.sql.*;
import java.util.LinkedList;

public class FeatureReportAllCitiesByPopulation {

    public static void generateReport(Connection con) {
        try {
            PreparedStatement pstmt = con.prepareStatement("""
                    SELECT
                        city.Name AS Name,
                        country.Name AS Country,
                        city.District AS District,
                        city.Population AS Population
                    FROM city
                    JOIN country ON city.CountryCode = country.Code
                    ORDER BY city.Population DESC;
            """);

            ResultSet rset = pstmt.executeQuery();

            LinkedList<FeatureCity> cities = new LinkedList<>();
            while (rset.next()) {
                FeatureCity c = new FeatureCity();
                c.name = rset.getString("Name");
                c.country = rset.getString("Country");
                c.district = rset.getString("District");
                c.population = rset.getInt("Population");
                cities.add(c);
            }

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("Name", "Country", "District", "Population");
            table.addRule();

            for (FeatureCity c : cities) {
                table.addRow(c.name, c.country, c.district, String.format("%,d", c.population));
                table.addRule();
            }

            System.out.println("All Cities in the World by Population:");
            System.out.println(table.render());
        } catch (Exception e) {
            System.out.println("Error generating city report: " + e.getMessage());
        }
    }
}
