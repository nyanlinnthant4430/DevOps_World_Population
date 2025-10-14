package com.napier.devops.city_report;

import java.sql.*;
import java.util.LinkedList;
import de.vandermeer.asciitable.AsciiTable;

public class ReportAllCitiesByPopulation {
    public static void generateReport(Connection con) {
        try {
            Statement stmt = con.createStatement();
            ResultSet rset = stmt.executeQuery(
                    "SELECT city.ID, city.Name, country.Name AS Country, city.District, city.Population " +
                            "FROM city JOIN country ON city.CountryCode = country.Code " +
                            "ORDER BY city.Population DESC;"
            );

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
