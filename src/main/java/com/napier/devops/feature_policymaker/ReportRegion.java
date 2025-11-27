package com.napier.devops.feature_policymaker;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.sql.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Report: Population by Region
 */
public class ReportRegion {

    private static final Logger LOGGER =
            Logger.getLogger(ReportRegion.class.getName());

    public void generateReport(Connection con) {
        try {
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("""
                SELECT country.Region AS Name,
                       SUM(country.Population) AS TotalPopulation,
                       SUM(CASE WHEN city.ID IS NOT NULL THEN city.Population ELSE 0 END) AS CityPopulation,
                       SUM(country.Population) - SUM(CASE WHEN city.ID IS NOT NULL THEN city.Population ELSE 0 END) AS NonCityPopulation
                FROM country
                LEFT JOIN city ON country.Code = city.CountryCode
                GROUP BY country.Region
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

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("Region", "Total Population", "City Population", "Non-City Population");
            table.addRule();

            for (PopulationRecord r : records) {
                table.addRow(
                        r.name(),
                        String.format("%,d", r.totalPopulation()),
                        String.format("%,d", r.cityPopulation()),
                        String.format("%,d", r.nonCityPopulation())
                );
                table.addRule();
            }

            table.setTextAlignment(TextAlignment.CENTER);

            String header = "\n--- Population by Region ---";
            LOGGER.info(header);

            String output = table.render();
            LOGGER.info(output);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error generating region population report", e);
        }
    }
}
