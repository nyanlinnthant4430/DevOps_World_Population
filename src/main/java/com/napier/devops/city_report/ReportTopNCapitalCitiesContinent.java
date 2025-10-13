package com.napier.devops.city_report;

import java.sql.*;
import java.util.LinkedList;
import de.vandermeer.asciitable.AsciiTable;

public class ReportTopNCapitalCitiesContinent {

    public static void generateReport(Connection con, String continent, int n) {
        try {
            Statement stmt = con.createStatement();
            String query = "SELECT city.Name AS City, country.Name AS Country, country.Region, city.Population " +
                    "FROM city INNER JOIN country ON city.ID = country.Capital " +
                    "WHERE country.Continent = '" + continent + "' " +
                    "ORDER BY city.Population DESC LIMIT " + n;

            ResultSet rset = stmt.executeQuery(query);
            LinkedList<City> cities = new LinkedList<>();

            while (rset.next()) {
                cities.add(new City(
                        rset.getString("City"),
                        rset.getString("Country"),
                        continent,
                        rset.getString("Region"),
                        rset.getInt("Population")
                ));
            }

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("City", "Country", "Region", "Population");
            table.addRule();

            for (City c : cities) {
                table.addRow(c.getName(), c.getCountry(), c.getRegion(), String.format("%,d", c.getPopulation()));
                table.addRule();
            }

            System.out.println("Top " + n + " Capital Cities in Continent: " + continent);
            System.out.println(table.render());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
