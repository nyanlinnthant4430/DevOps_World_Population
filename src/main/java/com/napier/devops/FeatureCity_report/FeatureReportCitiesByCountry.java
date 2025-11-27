package com.napier.devops.FeatureCity_report;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.sql.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FeatureReportCitiesByCountry {

    private static final Logger LOGGER =
            Logger.getLogger(FeatureReportCitiesByCountry.class.getName());

    public static void generateReport(Connection con, String country) {
        try {
            PreparedStatement pstmt = con.prepareStatement("""
                SELECT city.ID, city.Name, country.Name AS Country,
                       city.District, city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE country.Name = ?
                ORDER BY city.Population DESC;
            """);

            pstmt.setString(1, country);
            ResultSet rset = pstmt.executeQuery();

            LinkedList<FeatureCity> cities = new LinkedList<>();

            while (rset.next()) {
                FeatureCity c = new FeatureCity();
                c.id = rset.getInt("ID");
                c.name = rset.getString("Name");
                c.country = rset.getString("Country");
                c.district = rset.getString("District");
                c.population = rset.getInt("Population");
                cities.add(c);
            }

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("ID", "Name", "Country", "District", "Population");
            table.addRule();

            for (FeatureCity c : cities) {
                table.addRow(
                        c.id,
                        c.name,
                        c.country,
                        c.district,
                        String.format("%,d", c.population)
                );
                table.addRule();
            }

            table.setTextAlignment(TextAlignment.CENTER);

            String header = String.format(
                    "\n--- Cities in Country: %s (by Population) ---",
                    country);
            LOGGER.info(header);

            String output = table.render();
            LOGGER.info(output);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error generating cities by country report", e);
        }
    }
}
