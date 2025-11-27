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
 * Generates a report showing population by country.
 */
public class ReportPopulationByCountry {

    private static final Logger LOGGER =
            Logger.getLogger(ReportPopulationByCountry.class.getName());

    static class CountryPopulation {
        private final String name;
        private final long totalPopulation;
        private final long cityPopulation;
        private final long nonCityPopulation;

        public CountryPopulation(String name,
                                 long totalPopulation,
                                 long cityPopulation,
                                 long nonCityPopulation) {
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
                    "SELECT c.Name AS name, c.Population AS total_population, " +
                            "IFNULL(city_pop.city_population, 0) AS city_population, " +
                            "(c.Population - IFNULL(city_pop.city_population, 0)) AS non_city_population " +
                            "FROM country c " +
                            "LEFT JOIN (SELECT CountryCode, SUM(Population) AS city_population FROM city GROUP BY CountryCode) city_pop " +
                            "ON c.Code = city_pop.CountryCode " +
                            "ORDER BY c.Population DESC;"
            );

            LinkedList<CountryPopulation> rows = new LinkedList<>();

            while (rset.next()) {
                rows.add(new CountryPopulation(
                        rset.getString("name"),
                        rset.getLong("total_population"),
                        rset.getLong("city_population"),
                        rset.getLong("non_city_population")
                ));
            }

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("Country", "Total Population", "In Cities (%, count)", "Not in Cities (%, count)");
            table.addRule();

            for (CountryPopulation c : rows) {
                long total = c.getTotalPopulation();
                long city = c.getCityPopulation();
                long nonCity = c.getNonCityPopulation();

                double pctCity = total == 0 ? 0 : (city * 100.0 / total);
                double pctNonCity = total == 0 ? 0 : (nonCity * 100.0 / total);

                table.addRow(
                        c.getName(),
                        total,
                        String.format("%.2f%% (%d)", pctCity, city),
                        String.format("%.2f%% (%d)", pctNonCity, nonCity)
                );
                table.addRule();
            }

            table.setTextAlignment(TextAlignment.CENTER);

            String header = "\n--- Population by Country (Cities vs Non-Cities) ---";
            LOGGER.info(header);

            String output = table.render();
            LOGGER.info(output);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error generating Population by Country report", e);
        }
    }
}
