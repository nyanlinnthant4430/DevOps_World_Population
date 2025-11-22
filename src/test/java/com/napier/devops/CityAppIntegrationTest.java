package com.napier.devops;

import com.napier.devops.city_report.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class CityAppIntegrationTest {

    static App app;

    @BeforeAll
    static void init() {
        app = new App();
        app.connect("localhost:33060", 0);
    }

    @AfterAll
    static void cleanup() {
        app.disconnect();
    }

    @Test
    void testConnectionNotNull() {
        assertNotNull(app.getConnection(), "Connection should not be null after connect()");
    }

    @Test
    void testCityTableHasData() throws Exception {
        Connection con = app.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rset = stmt.executeQuery("SELECT COUNT(*) FROM city");

        assertTrue(rset.next());
        assertTrue(rset.getInt(1) > 0, "City table must contain rows");

        rset.close();
        stmt.close();
    }

    private void assertReportRuns(Runnable report) {
        try {
            report.run();
        } catch (Exception e) {
            fail("Report should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    void testReportAllCitiesByPopulation() {
        assertReportRuns(() ->
                ReportAllCitiesByPopulation.generateReport(app.getConnection()));
    }

    @Test
    void testReportCitiesByContinent() {
        assertReportRuns(() ->
                ReportCitiesByContinent.generateReport(app.getConnection(), "Asia"));
    }

    @Test
    void testReportCitiesByRegion() {
        assertReportRuns(() ->
                ReportCitiesByRegion.generateReport(app.getConnection(), "Southeast Asia"));
    }

    @Test
    void testReportCitiesByCountry() {
        assertReportRuns(() ->
                ReportCitiesByCountry.generateReport(app.getConnection(), "Myanmar"));
    }

    @Test
    void testReportCitiesByDistrict() {
        assertReportRuns(() ->
                ReportCitiesByDistrict.generateReport(app.getConnection(), "Yangon"));
    }

    @Test
    void testTopNCitiesWorld() {
        assertReportRuns(() ->
                ReportTopNCitiesWorld.generateReport(app.getConnection(), 5));
    }

    @Test
    void testTopNCitiesContinent() {
        assertReportRuns(() ->
                ReportTopNCitiesContinent.generateReport(app.getConnection(), "Asia", 5));
    }

    @Test
    void testTopNCitiesRegion() {
        assertReportRuns(() ->
                ReportTopNCitiesRegion.generateReport(app.getConnection(), "Southeast Asia", 5));
    }

    @Test
    void testTopNCitiesCountry() {
        assertReportRuns(() ->
                ReportTopNCitiesCountry.generateReport(app.getConnection(), "Myanmar", 5));
    }

    @Test
    void testTopNCitiesDistrict() {
        assertReportRuns(() ->
                ReportTopNCitiesDistrict.generateReport(app.getConnection(), "Yangon", 5));
    }
}
