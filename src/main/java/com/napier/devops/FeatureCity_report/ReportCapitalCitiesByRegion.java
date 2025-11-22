package com.napier.devops.city_report;

import java.sql.*;
import java.util.LinkedList;
import de.vandermeer.asciitable.AsciiTable;

public class ReportCapitalCitiesByRegion {

    public static void generateReport(Connection con, String region) {
        try {
            Statement stmt = con.createStatement();
            String query = "SELECT city.Name AS City, country.Name AS Country, country.Continent, city.Population " +
                    "FROM city INNER JOIN country ON city.ID = country.Capital " +
                    "WHERE country.Region = '" + region + "' " +
                    "ORDER BY city.Population DESC";

            ResultSet rset = stmt.executeQuery(query);
            LinkedList<City> cities = new LinkedList<>();

            while (rset.next()) {
                cities.add(new City(
                        rset.getString("City"),
                        rset.getString("Country"),
                        rset.getString("Continent"),
                        region,
                        rset.getInt("Population")
                ));
            }

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("City", "Country", "Continent", "Population");
            table.addRule();

            for (City c : cities) {
                table.addRow(c.getName(), c.getCountry(), c.getContinent(), String.format("%,d", c.getPopulation()));
                table.addRule();
            }

            System.out.println("Capital Cities in Region: " + region);
            System.out.println(table.render());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

