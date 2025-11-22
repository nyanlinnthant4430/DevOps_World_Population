package com.napier.devops.feature_policymaker;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

/**
 * Generates a report showing, for each country:
 *  - Name of the country
 *  - Total population
 *  - Population living in cities (with %)
 *  - Population not living in cities (with %)
 */
public class ReportPopulationByCountry {

    /**
     * Simple DTO to hold country population data.
     */
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

        public String getName() {
            return name;
        }

        public long getTotalPopulation() {
            return totalPopulation;
        }

        public long getCityPopulation() {
            return cityPopulation;
        }

        public long getNonCityPopulation() {
            return nonCityPopulation;
        }
    }

    /**
     * Generates and displays a report for each country.
     *
     * @param con the database connection
     */
    public static void generateReport(Connection con) {
        try {
            Statement stmt = con.createStatement();

            ResultSet rset = stmt.executeQuery(
                    "SELECT " +
                            "c.Name AS name, " +
                            "c.Population AS total_population, " +
                            "IFNULL(city_pop.city_population, 0) AS city_population, " +
                            "(c.Population - IFNULL(city_pop.city_population, 0)) AS non_city_population " +
                            "FROM country c " +
                            "LEFT JOIN ( " +
                            "SELECT CountryCode, SUM(Population) AS city_population " +
                            "FROM city " +
                            "GROUP BY CountryCode " +
                            ") city_pop " +
                            "ON c.Code = city_pop.CountryCode " +
                            "ORDER BY c.Population DESC;"
            );

            LinkedList<CountryPopulation> rows = new LinkedList<>();

            while (rset.next()) {
                String name = rset.getString("name");
                long total = rset.getLong("total_population");
                long city = rset.getLong("city_population");
                long nonCity = rset.getLong("non_city_population");

                rows.add(new CountryPopulation(name, total, city, nonCity));
            }

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("Country",
                    "Total Population",
                    "In Cities (%, count)",
                    "Not in Cities (%, count)");
            table.addRule();

            for (CountryPopulation c : rows) {
                long total = c.getTotalPopulation();
                long city = c.getCityPopulation();
                long nonCity = c.getNonCityPopulation();

                double cityPct = (total == 0) ? 0.0 : (city * 100.0 / total);
                double nonCityPct = (total == 0) ? 0.0 : (nonCity * 100.0 / total);

                table.addRow(
                        c.getName(),
                        total,
                        String.format("%.2f%% (%d)", cityPct, city),
                        String.format("%.2f%% (%d)", nonCityPct, nonCity)
                );
                table.addRule();
            }

            table.setTextAlignment(TextAlignment.CENTER);

            System.out.println("\n--- Population by Country (Cities vs Non-Cities) ---");
            System.out.println(table.render());

        } catch (Exception e) {
            System.out.println("Error generating Population by Country report: " + e.getMessage());
        }
    }
}

