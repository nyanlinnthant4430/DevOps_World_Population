package com.napier.devops;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import java.sql.*;
import java.util.LinkedList;

/**
 * Generates and displays population reports grouped by continent.
 */
public class ReportContinent {

    public void generateReport(Connection con) {
        try {
            Statement stmt = con.createStatement();

            // FIX: Fully qualify Population columns with table names (country.Population, city.Population)
            ResultSet rs = stmt.executeQuery("""
                SELECT country.Continent AS Name,
                       SUM(country.Population) AS TotalPopulation,
                       SUM(CASE WHEN city.ID IS NOT NULL THEN city.Population ELSE 0 END) AS CityPopulation,
                       SUM(country.Population) - SUM(CASE WHEN city.ID IS NOT NULL THEN city.Population ELSE 0 END) AS NonCityPopulation
                FROM country
                LEFT JOIN city ON country.Code = city.CountryCode
                GROUP BY country.Continent
                ORDER BY TotalPopulation DESC;
            """);

            LinkedList<PopulationRecord> records = new LinkedList<>();
            while (rs.next()) {
                records.add(new PopulationRecord(
                        rs.getString("Name"),
                        rs.getLong("TotalPopulation"),
                        rs.getLong("CityPopulation"),
                        rs.getLong("NonCityPopulation")
                ));
            }

            // Display table
            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("Continent", "Total Population", "City Population", "Non-City Population");
            table.addRule();

            for (PopulationRecord r : records) {
                table.addRow(r.getName(), r.getTotalPopulation(), r.getCityPopulation(), r.getNonCityPopulation());
                table.addRule();
            }

            table.setTextAlignment(TextAlignment.CENTER);
            System.out.println("\n🌎 Population by Continent");
            System.out.println(table.render());
        } catch (SQLException e) {
            System.out.println("Error generating continent report: " + e.getMessage());
        }
    }
}
