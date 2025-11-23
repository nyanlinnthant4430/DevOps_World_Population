package com.napier.devops;

import com.napier.devops.country_report.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
        app.connect("localhost:33060", 0);
    }

    @AfterAll
    static void teardown()
    {
        app.disconnect();
    }

    // ---- DATABASE TESTS ----

    @Test
    void testConnectionIsNotNull()
    {
        assertNotNull(app.getConnection(), "Connection should not be null");
    }

    @Test
    void testCountryTableHasData() throws SQLException
    {
        Connection con = app.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rset = stmt.executeQuery("SELECT COUNT(*) FROM country");

        assertTrue(rset.next());
        int count = rset.getInt(1);
        assertTrue(count > 0, "country table must contain rows");

        rset.close();
        stmt.close();
    }

    // ---- REPORT TEST HELPERS ----

    private void assertReportRuns(Runnable report)
    {
        try {
            report.run();
        } catch (Exception e) {
            fail("Report should not throw an exception: " + e.getMessage());
        }
    }

    // ---- REPORT INTEGRATION TESTS ----

    @Test
    void testReportAllCountriesByPopulation()
    {
        assertReportRuns(() ->
                ReportAllCountriesByPopulation.generateReport(app.getConnection()));
    }

    @Test
    void testReportCountriesByContinent()
    {
        assertReportRuns(() ->
                ReportCountriesByContinent.generateReport(app.getConnection(), "Asia"));
    }

    @Test
    void testReportCountriesByRegion()
    {
        assertReportRuns(() ->
                ReportCountriesByRegion.generateReport(app.getConnection(), "Southeast Asia"));
    }

    @Test
    void testReportTopNCountriesWorld()
    {
        assertReportRuns(() ->
                ReportTopNCountriesWorld.generateReport(app.getConnection(), 5));
    }

    @Test
    void testReportTopNCountriesContinent()
    {
        assertReportRuns(() ->
                ReportTopNCountriesContinent.generateReport(app.getConnection(), "Asia", 5));
    }

    @Test
    void testReportTopNCountriesRegion()
    {
        assertReportRuns(() ->
                ReportTopNCountriesRegion.generateReport(app.getConnection(), "Southeast Asia", 5));
    }
}
