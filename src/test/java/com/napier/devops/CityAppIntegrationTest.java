//package com.napier.devops;
//
//import com.napier.devops.city_report.*;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Scanner;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class CityAppIntegrationTest
//{
//    static App app;
//
//    @BeforeAll
//    static void init()
//    {
//        app = new App();
//        // Same as your country tests: local Docker MySQL on 33060
//        app.connect("localhost:33060", 0);
//    }
//
//    @AfterAll
//    static void teardown()
//    {
//        app.disconnect();
//    }
//
//    // ---- DATABASE TESTS ----
//
//    @Test
//    void testConnectionIsNotNull()
//    {
//        assertNotNull(app.getConnection(), "Connection should not be null");
//    }
//
//    @Test
//    void testCityTableHasData() throws SQLException
//    {
//        Connection con = app.getConnection();
//        Statement stmt = con.createStatement();
//        ResultSet rset = stmt.executeQuery("SELECT COUNT(*) FROM city");
//
//        assertTrue(rset.next());
//        int count = rset.getInt(1);
//        assertTrue(count > 0, "city table must contain rows");
//
//        rset.close();
//        stmt.close();
//    }
//
//
//
//
//
//
//
//
//    // ---- REPORT TEST HELPERS ----
//
//    private void assertReportRuns(Runnable report)
//    {
//        try
//        {
//            report.run();
//        }
//        catch (Exception e)
//        {
//            fail("Report should not throw an exception: " + e.getMessage());
//        }
//    }
//
//    // ---- CAPITAL CITY REPORT INTEGRATION TESTS ----
//
//    @Test
//    void testReportAllCapitalCitiesByPopulation()
//    {
//        assertReportRuns(() ->
//                ReportAllCapitalCitiesByPopulation.generateReport(app.getConnection()));
//    }
//
//    @Test
//    void testReportCapitalCitiesByContinent()
//    {
//        assertReportRuns(() ->
//                ReportCapitalCitiesByContinent.generateReport(app.getConnection(), "Asia"));
//    }
//
//    @Test
//    void testReportCapitalCitiesByRegion()
//    {
//        assertReportRuns(() ->
//                ReportCapitalCitiesByRegion.generateReport(app.getConnection(), "Southeast Asia"));
//    }
//
//    @Test
//    void testReportTopNCapitalCitiesWorld()
//    {
//        assertReportRuns(() ->
//                ReportTopNCapitalCitiesWorld.generateReport(app.getConnection(), 5));
//    }
//
//    @Test
//    void testReportTopNCapitalCitiesContinent()
//    {
//        assertReportRuns(() ->
//                ReportTopNCapitalCitiesContinent.generateReport(app.getConnection(), "Asia", 5));
//    }
//
//    @Test
//    void testReportTopNCapitalCitiesRegion()
//    {
//        assertReportRuns(() ->
//                ReportTopNCapitalCitiesRegion.generateReport(app.getConnection(), "Southeast Asia", 5));
//    }
//}
//
