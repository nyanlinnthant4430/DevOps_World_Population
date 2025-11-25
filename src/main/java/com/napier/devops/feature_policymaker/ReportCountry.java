package com.napier.devops.feature_policymaker;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import java.sql.*;

/**
 * Generates and displays population reports grouped by country.
 */
public class ReportCountry {

    public void generateReport(Connection con) {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("""
                SELECT Name AS Country,
                       Continent,
                       Region,
                       Population
                FROM country
                ORDER BY Population DESC;
            """);

            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow("Country", "Continent", "Region", "Population");
            table.addRule();

            while (rs.next()) {
                table.addRow(
                        rs.getString("Country"),
                        rs.getString("Continent"),
                        rs.getString("Region"),
                        rs.getLong("Population")
                );
                table.addRule();
            }

            table.setTextAlignment(TextAlignment.CENTER);
            System.out.println("\n🏳️ Population by Country");
            System.out.println(table.render());
        } catch (SQLException e) {
            System.out.println("Error generating country report: " + e.getMessage());
        }
    }
}