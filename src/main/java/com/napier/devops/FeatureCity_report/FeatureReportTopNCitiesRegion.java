package com.napier.devops.FeatureCity_report;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.sql.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FeatureReportTopNCitiesRegion {

    private static final Logger LOGGER =
            Logger.getLogger(FeatureReportTopNCitiesRegion.class.getName());

    public static void generateReport(Connection con, String region, int n) {
        try {
            PreparedStatement pstmt = con.prepareStatement("""
                    SELECT
                        city.Name AS Name,
                        country.Name AS Country,
                        city.District AS District,
                        city.Population AS Population
                    FROM city
                    JOIN country ON city.CountryCode = country.Code
                    WHERE country.Region = ?
                    ORDER BY city.Population DESC
                    LIMIT ?;
                    """);
            pstmt.setString(1, region);
            pstmt.setInt(2, n);
            ResultSet rset = pstmt.executeQuery();

            LinkedList<FeatureCity> cities = new LinkedList<>();
            while (rset.next()) {
                FeatureCity c = new FeatureCity();
                c.name = rset.getString("Name");
                c.country = rset.getString("Country");
                c.district = rset.getString("District");
                c.population = rset.getInt("Population");
                cities.add(c);
            }

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("Name", "Country", "District", "Population");
            table.addRule();

            for (FeatureCity c : cities) {
                table.addRow(
                        c.name,
                        c.country,
                        c.district,
                        String.format("%,d", c.population)
                );
                table.addRule();
            }

            table.setTextAlignment(TextAlignment.CENTER);

            String header = String.format(
                    "\n--- Top %d Cities in Region: %s ---",
                    n, region
            );
            LOGGER.info(header);

            String output = table.render();
            LOGGER.info(output);
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error generating Top N cities in region report", e);
        }
    }
}
