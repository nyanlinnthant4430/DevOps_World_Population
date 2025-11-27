package com.napier.devops.feature_policymaker;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generates a report showing population by region.
 */
public class ReportPopulationByRegion {

    private static final Logger LOGGER =
            Logger.getLogger(ReportPopulationByRegion.class.getName());

    static class RegionPopulation {
        private final String name;
        private final long totalPopulation;
        private final long cityPopulation;
        private final long nonCityPopulation;

        public RegionPopulation(String name, long totalPopulation,
                                long cityPopulation, long nonCityPopulation) {
            this.name = name;
            this.totalPopulation = totalPopulation;
            this.cityPopulation = cityPopulation;
            this.nonCityPopulation = nonCityPopulation;
        }

        public String getName() { return name; }
        public long getTotalPopulation() { return totalPopulation; }
        public long getCityPopulation() { return cityPopulation; }
        public long getNonCityPopulation() { return nonCityPopulation; }
    }

    public static void generateReport(Connection con) {
        try {
            Statement stmt = con.createStatement();

            ResultSet rset = stmt.executeQuery(
                    "SELECT t.Region AS name, t.total_population, " +
                            "IFNULL(cities.city_population, 0) AS city_population, " +
                            "(t.total_population - IFNULL(cities.city_population, 0)) AS non_city_population " +
                            "FROM (SELECT Region, SUM(Population) AS total_population FROM country GROUP BY Region) t " +
                            "LEFT JOIN (SELECT country.Region, SUM(city.Population) AS city_population " +
                            "FROM city JOIN country ON city.CountryCode = country.Code GROUP BY country.Region) cities " +
                            "ON t.Region = cities.Region ORDER BY t.total_population DESC;"
            );

            LinkedList<RegionPopulation> rows = new LinkedList<>();

            while (rset.next()) {
                rows.add(new RegionPopulation(
                        rset.getString("name"),
                        rset.getLong("total_population"),
                        rset.getLong("city_population"),
                        rset.getLong("non_city_population")
                ));
            }

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("Region", "Total Population", "In Cities (%, count)", "Not in Cities (%, count)");
            table.addRule();

            for (RegionPopulation r : rows) {
                long total = r.getTotalPopulation();
                long city = r.getCityPopulation();
                long nonCity = r.getNonCityPopulation();

                double pctCity = total == 0 ? 0 : (city * 100.0 / total);
                double pctNonCity = total == 0 ? 0 : (nonCity * 100.0 / total);

                table.addRow(
                        r.getName(),
                        total,
                        String.format("%.2f%% (%d)", pctCity, city),
                        String.format("%.2f%% (%d)", pctNonCity, nonCity)
                );
                table.addRule();
            }

            table.setTextAlignment(TextAlignment.CENTER);

            String header = "\n--- Population by Region (Cities vs Non-Cities) ---";
            LOGGER.info(header);

            String output = table.render();
            LOGGER.info(output);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error generating Population by Region report", e);
        }
    }
}
