package com.napier.devops.city_report;

import java.sql.*;
import java.util.LinkedList;
import de.vandermeer.asciitable.AsciiTable;

public class ReportAllCapitalCitiesByPopulation {

    public static void generateReport(Connection con) {
        try {
            Statement stmt = con.createStatement();
            String query = "SELECT city.Name AS City, country.Name AS Country, country.Continent, " +
                    "country.Region, city.Population " +
                    "FROM city INNER JOIN country ON city.ID = country.Capital " +
                    "ORDER BY city.Population DESC";

            ResultSet rset = stmt.executeQuery(query);
            LinkedList<City> cities = new LinkedList<>();

            while (rset.next()) {
                City city = new City(
                        rset.getString("City"),
                        rset.getString("Country"),
                        rset.getString("Continent"),
                        rset.getString("Region"),
                        rset.getInt("Population")
                );
                cities.add(city);
            }

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("City", "Country", "Continent", "Region", "Population");
            table.addRule();

            for (City c : cities) {
                table.addRow(c.getName(), c.getCountry(), c.getContinent(), c.getRegion(), String.format("%,d", c.getPopulation()));
                table.addRule();
            }

            System.out.println("All Capital Cities by Population (World):");
            System.out.println(table.render());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
