package com.napier.devops;

import com.napier.devops.feature_policymaker.ReportContinent;
import com.napier.devops.feature_policymaker.ReportCountry;
import com.napier.devops.feature_policymaker.ReportRegion;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppIntegrationTest {

    private App app;
    private static Connection con;

    @BeforeAll
    void init() {
        app = new App();
        // Integration test assumes MySQL world DB is available on localhost:33060
        // and that user/password are root/example as in App.connect().
        app.connect("localhost:33060", 10000);
        Connection con = app.getConnection();
        assertNotNull(con, "Connection should not be null after connect()");
        try {
            assertFalse(con.isClosed(), "Connection should be open");
        } catch (SQLException e) {
            fail("Error checking if connection is closed: " + e.getMessage());
        }
    }

    @AfterAll
    void tearDown() {
        app.disconnect();
        try {
            Connection con = app.getConnection();
            if (con != null) {
                assertTrue(con.isClosed(), "Connection should be closed after disconnect()");
            }
        } catch (SQLException e) {
            // If we cannot check, do not fail the whole suite – just log
            System.out.println("Error verifying closed connection in tearDown: " + e.getMessage());
        }
    }

    @Test
    void testRunAllReportsNonInteractiveViaReflection() throws Exception {
        Method m = App.class.getDeclaredMethod("runAllReportsNonInteractive");
        m.setAccessible(true);

        // This will call:
        //  - runCapitalCityReportsNonInteractive()
        //  - runCountryReportsNonInteractive()
        //  - runCityReportsNonInteractive()
        //  - runBasicPopulationReportsNonInteractive()
        //  - runPolicyMakerReportsNonInteractive()
        // and each of those will hit the real DB through static report classes.
        m.invoke(app);
    }

    @Test
    void testRunAllReportsInteractiveViaReflection() throws Exception {
        // Build a big input string for ALL interactive prompts in this order:
        //
        // 1) Capital city reports:
        //    continentAll, regionAll, nWorld, continentTop, nContinent, regionTop, nRegion
        // 2) Country reports:
        //    nWorld, continent, nContinent, region, nRegion
        // 3) City reports:
        //    continentAll, regionAll, countryAll, districtAll,
        //    nWorld, topContinent, nContinent, topRegion, nRegion,
        //    topCountry, nCountry, topDistrict, nDistrict
        // 4) Basic population:
        //    continent, region, country, district, city
        //
        String input = String.join("\n",
                // Capital city reports
                "Asia",             // continentAll
                "Eastern Asia",     // regionAll
                "5",                // nWorld
                "Europe",           // continentTop
                "3",                // nContinent
                "Western Europe",   // regionTop
                "2",                // nRegion

                // Country reports
                "10",               // nWorld
                "Asia",             // continent
                "5",                // nContinent
                "Southeast Asia",   // region
                "5",                // nRegion

                // City reports
                "Asia",             // continentAll
                "Southeast Asia",   // regionAll
                "Myanmar",          // countryAll
                "Yangon",           // districtAll
                "10",               // nWorld
                "Asia",             // topContinent
                "5",                // nContinent
                "Southeast Asia",   // topRegion
                "5",                // nRegion
                "Myanmar",          // topCountry
                "5",                // nCountry
                "Yangon",           // topDistrict
                "5",                // nDistrict

                // Basic population
                "Asia",             // continent
                "Southeast Asia",   // region
                "Myanmar",          // country
                "Yangon",           // district
                "Yangon"            // city
        ) + "\n";

        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        Method m = App.class.getDeclaredMethod("runAllReportsInteractive", Scanner.class);
        m.setAccessible(true);

        // This will transitively exercise:
        //  - runCapitalCityReportsInteractive
        //  - runCountryReportsInteractive
        //  - runCityReportsInteractive
        //  - runBasicPopulationReportsInteractive
        //  - runPolicyMakerReports
        m.invoke(app, scanner);
    }

    @Test
    void testMainNonInteractiveDockerLikeMode() {
        // Simulate Docker/CI mode:
        //  - args length >= 2
        //  - System.console() will be null in JUnit
        // This should go into:
        //  - connect("localhost:33060", 0)
        //  - runAllReportsNonInteractive()
        String[] args = {"localhost:33060", "0"};
        App.main(args);
    }

    @Test
    void testConnectNoArgConvenienceMethod() {
        App localApp = new App();

        // This uses the convenience method: localhost:33060, 0 delay
        localApp.connect();

        assertNotNull(localApp.getConnection(), "Connection should not be null after no-arg connect()");

        localApp.disconnect();
        try {
            Connection con = localApp.getConnection();
            if (con != null) {
                assertTrue(con.isClosed(), "Connection should be closed after disconnect() on localApp");
            }
        } catch (SQLException e) {
            System.out.println("Error verifying closed connection in testConnectNoArgConvenienceMethod: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("ReportContinent.generateReport executes without error")
    void testReportContinentGenerateReport() {
        // Use the connection established in @BeforeAll
        Connection con = app.getConnection();
        assertNotNull(con, "Connection should not be null for ReportContinent test");

        ReportContinent report = new ReportContinent();

        // If anything goes wrong (e.g. bad SQL), this test will fail with an exception.
        report.generateReport(con);
    }

    @Test
    @DisplayName("ReportRegion.generateReport executes without error")
    void testReportRegionGenerateReport() {
        Connection con = app.getConnection();
        assertNotNull(con, "Connection should not be null for ReportRegion test");

        ReportRegion report = new ReportRegion();
        report.generateReport(con);
    }

    @Test
    @DisplayName("ReportCountry.generateReport executes without error")
    void testReportCountryGenerateReport() {
        Connection con = app.getConnection();
        assertNotNull(con, "Connection should not be null for ReportCountry test");

        ReportCountry report = new ReportCountry();
        report.generateReport(con);
    }


    }
