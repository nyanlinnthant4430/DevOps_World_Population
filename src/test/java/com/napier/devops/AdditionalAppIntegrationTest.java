package com.napier.devops;

import com.napier.devops.feature_basicpopulation.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class AdditionalAppIntegrationTest {

    static App app;

    @BeforeAll
    static void init() {
        app = new App();
        // Localhost test DB
        app.connect("localhost:33060", 0);
    }

    @AfterAll
    static void cleanup() {
        app.disconnect();
    }

    @Test
    void testConnectionNotNull() {
        assertNotNull(app.getConnection(), "Database connection should not be null.");
    }

    @Test
    void testWorldTableHasData() throws Exception {
        Connection con = app.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rset = stmt.executeQuery("SELECT COUNT(*) FROM country");

        assertTrue(rset.next());
        assertTrue(rset.getInt(1) > 0, "Country table must contain rows.");

        rset.close();
        stmt.close();
    }

    // Helper
    void assertReportRuns(Runnable report) {
        assertDoesNotThrow(report::run, "Report should run without throwing exceptions.");
    }

    @Test
    void testReportWorldPopulation() {
        assertReportRuns(() -> ReportWorldPopulation.generateReport(app.getConnection()));
    }

    @Test
    void testPopulationByContinent() {
        assertReportRuns(() -> ReportPopulationOfContinent.generateReport(app.getConnection(), "Asia"));
    }

    @Test
    void testPopulationByRegion() {
        assertReportRuns(() -> ReportPopulationOfRegion.generateReport(app.getConnection(), "Southeast Asia"));
    }

    @Test
    void testPopulationByCountry() {
        assertReportRuns(() -> ReportPopulationOfCountry.generateReport(app.getConnection(), "Myanmar"));
    }

    @Test
    void testPopulationByDistrict() {
        assertReportRuns(() -> ReportPopulationOfDistrict.generateReport(app.getConnection(), "Yangon"));
    }

    @Test
    void testPopulationByCity() {
        assertReportRuns(() -> ReportPopulationOfCity.generateReport(app.getConnection(), "Rangoon (Yangon)"));
    }

    @Test
    void testPopulationByLanguage() {
        assertReportRuns(() -> ReportLanguagePopulation.generateReport(app.getConnection()));
    }
}
