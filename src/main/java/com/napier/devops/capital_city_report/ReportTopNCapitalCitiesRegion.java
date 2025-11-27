package com.napier.devops.capital_city_report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

/**
 * Report 12:
 * The top N populated capital cities in a given region.
 * Columns:
 *  - Name
 *  - Country
 *  - Population
 */
public class ReportTopNCapitalCitiesRegion {

    private static final Logger LOGGER =
            Logger.getLogger(ReportTopNCapitalCitiesRegion.class.getName());

    public static void generateReport(Connection con, String region, int n) {
        try {
            String sql = """
                    SELECT
                        city.Name      AS Name,
                        country.Name   AS Country,
                        city.Population AS Population
                    FROM city
                    JOIN country ON city.ID = country.Capital
                    WHERE country.Region = ?
                    ORDER BY city.Population DESC
                    LIMIT ?;
                    """;

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, region);
            pstmt.setInt(2, n);
            ResultSet rset = pstmt.executeQuery();

            LinkedList<City> capitals = new LinkedList<>();

            while (rset.next()) {
                City c = new City();
                c.setName(rset.getString("Name"));
                c.setCountry(rset.getString("Country"));
                c.setPopulation(rset.getInt("Population"));
                capitals.add(c);
            }

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("Name", "Country", "Population");
            table.addRule();

            for (City c : capitals) {
                table.addRow(
                        c.getName(),
                        c.getCountryName(),
                        String.format("%,d", c.getPopulation())
                );
                table.addRule();
            }

            table.setTextAlignment(TextAlignment.CENTER);

            String header = String.format(
                    "\n--- Top %d Capital Cities in Region: %s ---",
                    n, region
            );
            LOGGER.info(header);

            String output = table.render();
            LOGGER.info(output);
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error generating TopNCapitalCitiesRegion report", e);
        }
    }
}
