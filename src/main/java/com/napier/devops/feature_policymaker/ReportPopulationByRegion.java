package com.napier.devops.feature_policymaker;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

/**
 * Generates a report showing, for each region:
 *  - Name of the region
 *  - Total population
 *  - Population living in cities (with %)
 *  - Population not living in cities (with %)
 */
public class ReportPopulationByRegion {

    /**
     * Simple DTO to hold region population data.
     */
    static class RegionPopulation {
        private final String name;
        private final long totalPopulation;
        private final long cityPopulation;
        private final long nonCityPopulation;

        public RegionPopulation(String name,
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
     * Generates and displays a report for each region.
     *
     * @param con the database connection
     */
    public static void generateReport(Connection con) {
        try {
            Statement stmt = con.createStatement();

            ResultSet rset = stmt.executeQuery(
                    "SELECT " +
                            "t.Region AS name, " +
                            "t.total_population, " +
                            "IFNULL(cities.city_population, 0) AS city_population, " +
                            "(t.total_population - IFNULL(cities.city_population, 0)) AS non_city_population " +
                            "FROM " +
                            "(SELECT Region, SUM(Population) AS total_population " +
                            "FROM country " +
                            "GROUP BY Region) t " +
                            "LEFT JOIN " +
                            "(SELECT country.Region, SUM(city.Population) AS city_population " +
                            "FROM city " +
                            "JOIN country ON city.CountryCode = country.Code " +
                            "GROUP BY country.Region) cities " +
                            "ON t.Region = cities.Region " +
                            "ORDER BY t.total_population DESC;"
            );

            LinkedList<RegionPopulation> rows = new LinkedList<>();

            while (rset.next()) {
                String name = rset.getString("name");
                long total = rset.getLong("total_population");
                long city = rset.getLong("city_population");
                long nonCity = rset.getLong("non_city_population");

                rows.add(new RegionPopulation(name, total, city, nonCity));
            }

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("Region",
                    "Total Population",
                    "In Cities (%, count)",
                    "Not in Cities (%, count)");
            table.addRule();

            for (RegionPopulation r : rows) {
                long total = r.getTotalPopulation();
                long city = r.getCityPopulation();
                long nonCity = r.getNonCityPopulation();

                double cityPct = (total == 0) ? 0.0 : (city * 100.0 / total);
                double nonCityPct = (total == 0) ? 0.0 : (nonCity * 100.0 / total);

                table.addRow(
                        r.getName(),
                        total,
                        String.format("%.2f%% (%d)", cityPct, city),
                        String.format("%.2f%% (%d)", nonCityPct, nonCity)
                );
                table.addRule();
            }

            table.setTextAlignment(TextAlignment.CENTER);

            System.out.println("\n--- Population by Region (Cities vs Non-Cities) ---");
            System.out.println(table.render());

        } catch (Exception e) {
            System.out.println("Error generating Population by Region report: " + e.getMessage());
        }
    }
}