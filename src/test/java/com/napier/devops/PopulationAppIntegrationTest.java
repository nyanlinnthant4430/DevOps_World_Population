package com.napier.devops;

import com.napier.devops.feature_policymaker.ReportContinent;
import com.napier.devops.feature_policymaker.ReportCountry;
import com.napier.devops.feature_policymaker.ReportRegion;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the population App.
 * - Connects to the real MySQL database (localhost:33080 / world)
 * - Verifies connection and that core reports run without throwing.
 */
public class PopulationAppIntegrationTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
        // Uses App.connect() → jdbc:mysql://localhost:33080/world
        app.connect();
    }

    @AfterAll
    static void teardown()
    {
        app.disconnect();
    }

    @Test
    void testConnectionIsNotNull()
    {
        Connection con = app.getConnection();
        assertNotNull(con, "Database connection should not be null after connect()");
    }

    @Test
    void testCountryTableHasData() throws Exception
    {
        Connection con = app.getConnection();
        assertNotNull(con, "Connection should not be null");

        Statement stmt = con.createStatement();
        ResultSet rset = stmt.executeQuery("SELECT COUNT(*) FROM country");

        assertTrue(rset.next(), "ResultSet should have at least one row");
        int count = rset.getInt(1);
        assertTrue(count > 0, "country table should contain at least one row");

        rset.close();
        stmt.close();
    }

    // Helper to assert that a report runs without throwing an exception
    private void assertReportRuns(Runnable report)
    {
        try
        {
            report.run();
        }
        catch (Exception e)
        {
            fail("Report should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    void testReportContinentRuns()
    {
        assertReportRuns(() ->
                new ReportContinent().generateReport(app.getConnection()));
    }

    @Test
    void testReportRegionRuns()
    {
        assertReportRuns(() ->
                new ReportRegion().generateReport(app.getConnection()));
    }

    @Test
    void testReportCountryRuns()
    {
        assertReportRuns(() ->
                new ReportCountry().generateReport(app.getConnection()));
    }
}
