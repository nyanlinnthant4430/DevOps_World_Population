package com.napier.devops.FeatureCity_report;

import de.vandermeer.asciitable.AsciiTable;

import java.sql.*;
import java.util.LinkedList;

public class FeatureReportTopNCitiesDistrict {

    public static void generateReport(Connection con, String district, int n) {
        try {
            PreparedStatement pstmt = con.prepareStatement("""
                    SELECT
                        city.Name AS Name,
                        country.Name AS Country,
                        city.District AS District,
                        city.Population AS Population
                    FROM city
                    JOIN country ON city.CountryCode = country.Code
                    WHERE city.District = ?
                    ORDER BY city.Population DESC
                    LIMIT ?;
            """);
            pstmt.setString(1, district);
            pstmt.setInt(2, n);

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

            System.out.println("Top " + n + " Cities in District: " + district);
            System.out.println(table.render());

        } catch (Exception e) {
            System.out.println("Error generating Top N district city report: " + e.getMessage());
        }
    }
}
