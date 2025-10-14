package com.napier.devops.city_report;

import de.vandermeer.asciitable.AsciiTable;

import java.sql.*;
import java.util.LinkedList;

public class ReportTopNCitiesContinent {
    public static void generateReport(Connection con, String continent, int n) {
        try {
            PreparedStatement pstmt = con.prepareStatement(
                    "SELECT city.ID, city.Name, country.Name AS Country, city.District, city.Population " +
                            "FROM city JOIN country ON city.CountryCode = country.Code " +
                            "WHERE country.Continent = ? " +
                            "ORDER BY city.Population DESC LIMIT ?;"
            );
            pstmt.setString(1, continent);
            pstmt.setInt(2, n);
            ResultSet rset = pstmt.executeQuery();

            LinkedList<City> cities = new LinkedList<>();
            while (rset.next()) {
                City c = new City();
                c.id = rset.getInt("ID");
                c.name = rset.getString("Name");
                c.country = rset.getString("Country");
                c.district = rset.getString("District");
                c.population = rset.getInt("Population");
                cities.add(c);
            }

            printCities(cities);
        } catch (Exception e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }

    private static void printCities(LinkedList<City> cities) {
        AsciiTable table = new AsciiTable();
        table.addRule();
        table.addRow("ID", "City", "Country", "District", "Population");
        table.addRule();

        for (City c : cities) {
            table.addRow(c.id, c.name, c.country, c.district, c.population);
            table.addRule();
        }

        System.out.println(table.render());
    }
}
