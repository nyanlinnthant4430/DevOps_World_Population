package com.napier.devops;

import com.napier.devops.feature_policymaker.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class PopulationAppIntegrationTest {

    static App app;

    @BeforeAll
    static void init() {
        app = new App();
        // Localhost MySQL for integration tests
        app.connect("localhost:33080", 0);
    }

    @AfterAll
    static void cleanup() {
        app.disconnect();
    }

    @Test
    void testConnectionNotNull() {
        assertNotNull(app.getConnection(), "Connection should not be null after connect().");
    }

    @Test
    void testCountryTableHasData() throws Exception {
        Connection con = app.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rset = stmt.executeQuery("SELECT COUNT(*) FROM country");

        assertTrue(rset.next());
        assertTrue(rset.getInt(1) > 0, "Country table must contain at least 1 row.");

        rset.close();
        stmt.close();
    }

    // Helper to check if a report runs successfully
    private void assertReportRuns(Runnable report) {
        try {
            report.run();
        } catch (Exception e) {
            fail("Report should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    void testPopulationByContinentReport() {
        assertReportRuns(() ->
                ReportPopulationByContinent.generateReport(app.getConnection()));
    }

    @Test
    void testPopulationByRegionReport() {
        assertReportRuns(() ->
                ReportPopulationByRegion.generateReport(app.getConnection()));
    }

    @Test
    void testPopulationByCountryReport() {
        assertReportRuns(() ->
                ReportPopulationByCountry.generateReport(app.getConnection()));
    }
}