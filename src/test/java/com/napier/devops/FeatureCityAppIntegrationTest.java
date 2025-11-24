//package com.napier.devops;
//
//import com.napier.devops.FeatureCity_report.*;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.Statement;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class FeatureCityAppIntegrationTest {
//
//    static App app;
//
//    @BeforeAll
//    static void init() {
//        app = new App();
//        app.connect("localhost:33060", 0);
//    }
//
//    @AfterAll
//    static void cleanup() {
//        app.disconnect();
//    }
//
//    @Test
//    void testConnectionNotNull() {
//        assertNotNull(app.getConnection(), "Connection should not be null after connect()");
//    }
//
//    @Test
//    void testCityTableHasData() throws Exception {
//        Connection con = app.getConnection();
//        Statement stmt = con.createStatement();
//        ResultSet rset = stmt.executeQuery("SELECT COUNT(*) FROM city");
//
//        assertTrue(rset.next());
//        assertTrue(rset.getInt(1) > 0, "City table must contain rows");
//
//        rset.close();
//        stmt.close();
//    }
//
//    private void assertReportRuns(Runnable report) {
//        try {
//            report.run();
//        } catch (Exception e) {
//            fail("Report should not throw an exception: " + e.getMessage());
//        }
//    }
//
//    @Test
//    void testReportAllCitiesByPopulation() {
//        assertReportRuns(() ->
//                FeatureReportAllCitiesByPopulation.generateReport(app.getConnection()));
//    }
//
//    @Test
//    void testReportCitiesByContinent() {
//            assertReportRuns(() ->
//                FeatureReportCitiesByContinent.generateReport(app.getConnection(), "Asia"));
//    }
//
//    @Test
//    void testReportCitiesByRegion() {
//        assertReportRuns(() ->
//                FeatureReportCitiesByRegion.generateReport(app.getConnection(), "Southeast Asia"));
//    }
//
//    @Test
//    void testReportCitiesByCountry() {
//        assertReportRuns(() ->
//                FeatureReportCitiesByCountry.generateReport(app.getConnection(), "Myanmar"));
//    }
//
//    @Test
//    void testReportCitiesByDistrict() {
//        assertReportRuns(() ->
//                FeatureReportCitiesByDistrict.generateReport(app.getConnection(), "Yangon"));
//    }
//
//    @Test
//    void testTopNCitiesWorld() {
//        assertReportRuns(() ->
//                FeatureReportTopNCitiesWorld.generateReport(app.getConnection(), 5));
//    }
//
//    @Test
//    void testTopNCitiesContinent() {
//        assertReportRuns(() ->
//                FeatureReportTopNCitiesContinent.generateReport(app.getConnection(), "Asia", 5));
//    }
//
//    @Test
//    void testTopNCitiesRegion() {
//        assertReportRuns(() ->
//                FeatureReportTopNCitiesRegion.generateReport(app.getConnection(), "Southeast Asia", 5));
//    }
//
//    @Test
//    void testTopNCitiesCountry() {
//        assertReportRuns(() ->
//                FeatureReportTopNCitiesCountry.generateReport(app.getConnection(), "Myanmar", 5));
//    }
//
//    @Test
//    void testTopNCitiesDistrict() {
//        assertReportRuns(() ->
//                FeatureReportTopNCitiesDistrict.generateReport(app.getConnection(), "Yangon", 5));
//    }
//
//
//}
