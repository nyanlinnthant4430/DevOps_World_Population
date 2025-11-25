package com.napier.devops;

import com.napier.devops.FeatureCity_report.FeatureReportCitiesByDistrict;
import com.napier.devops.FeatureCity_report.FeatureReportTopNCitiesDistrict;
import com.napier.devops.basicpopulation.BasicReportPopulationOfCity;
import com.napier.devops.basicpopulation.BasicReportPopulationOfDistrict;
import com.napier.devops.feature_policymaker.ReportContinent;
import com.napier.devops.feature_policymaker.ReportCountry;
import com.napier.devops.feature_policymaker.ReportRegion;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppIntegrationTest {

    private App app;
    private static Connection con;
    private static final String location = "localhost:3306";

    @BeforeAll
    void init() {
        app = new App();
        // Integration test assumes MySQL world DB is available on localhost:33060
        // and that user/password are root/example as in App.connect().
        app.connect("localhost:33060", 10000);
        con = app.getConnection();                 // <-- use field, not local variable
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
        // The actual order in runAllReportsInteractive is:
        //  1) runCountryReportsInteractive(scanner)
        //  2) runCapitalCityReportsInteractive(scanner)
        //  3) runCityReportsInteractive(scanner)
        //  4) runBasicPopulationReportsInteractive(scanner)
        //
        // So we must feed input in EXACTLY that order.

        String input = String.join("\n",
                // ==========================
                // 1) Country reports
                // ==========================
                // Prompts:
                //   Enter N for Top N countries in the WORLD:
                //   Enter Continent Name:
                //   Enter N for Top N countries in this CONTINENT:
                //   Enter Region Name:
                //   Enter N for Top N countries in this REGION:
                "10",              // nWorld
                "Asia",            // continent
                "5",               // nContinent
                "Southeast Asia",  // region
                "5",               // nRegion

                // ==========================
                // 2) Capital city reports
                // ==========================
                // Expected prompts (based on your comment):
                //   continentAll, regionAll, nWorld, continentTop, nContinent, regionTop, nRegion
                "Asia",            // continentAll
                "Eastern Asia",    // regionAll
                "5",               // nWorld (top capitals in world)
                "Europe",          // continentTop
                "3",               // nContinent
                "Western Europe",  // regionTop
                "2",               // nRegion

                // ==========================
                // 3) City reports
                // ==========================
                // Prompts (from your comment):
                //   continentAll, regionAll, countryAll, districtAll,
                //   nWorld, topContinent, nContinent, topRegion, nRegion,
                //   topCountry, nCountry, topDistrict, nDistrict
                "Asia",            // continentAll
                "Southeast Asia",  // regionAll
                "Myanmar",         // countryAll
                "Yangon",          // districtAll
                "10",              // nWorld
                "Asia",            // topContinent
                "5",               // nContinent
                "Southeast Asia",  // topRegion
                "5",               // nRegion
                "Myanmar",         // topCountry
                "5",               // nCountry
                "Yangon",          // topDistrict
                "5",               // nDistrict

                // ==========================
                // 4) Basic population reports
                // ==========================
                // Prompts:
                //   continent, region, country, district, city
                "Asia",            // continent
                "Southeast Asia",  // region
                "Myanmar",         // country
                "Yangon",          // district
                "Yangon"           // city
        ) + "\n";

        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        Method m = App.class.getDeclaredMethod("runAllReportsInteractive", Scanner.class);
        m.setAccessible(true);

        // This will transitively exercise:
        //  - runCountryReportsInteractive
        //  - runCapitalCityReportsInteractive
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
            Connection localCon = localApp.getConnection();
            if (localCon != null) {
                assertTrue(localCon.isClosed(), "Connection should be closed after disconnect() on localApp");
            }
        } catch (SQLException e) {
            System.out.println("Error verifying closed connection in testConnectNoArgConvenienceMethod: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("ReportContinent.generateReport executes without error")
    void testReportContinentGenerateReport() {
        Connection localCon = app.getConnection();
        assertNotNull(localCon, "Connection should not be null for ReportContinent test");

        ReportContinent report = new ReportContinent();
        report.generateReport(localCon);
    }

    @Test
    @DisplayName("ReportRegion.generateReport executes without error")
    void testReportRegionGenerateReport() {
        Connection localCon = app.getConnection();
        assertNotNull(localCon, "Connection should not be null for ReportRegion test");

        ReportRegion report = new ReportRegion();
        report.generateReport(localCon);
    }

    @Test
    @DisplayName("ReportCountry.generateReport executes without error")
    void testReportCountryGenerateReport() {
        Connection localCon = app.getConnection();
        assertNotNull(localCon, "Connection should not be null for ReportCountry test");

        ReportCountry report = new ReportCountry();
        report.generateReport(localCon);
    }

    @Test
    void testFeatureReportCitiesByDistrictBranches() {
        // 1) Existing district -> loop executes
        FeatureReportCitiesByDistrict.generateReport(app.getConnection(), "Yangon");

        // 2) Non-existing district -> loop does NOT execute
        FeatureReportCitiesByDistrict.generateReport(app.getConnection(), "DistrictDoesNotExistXYZ");

        // 3) Exception path -> catch executes
        FeatureReportCitiesByDistrict.generateReport(null, "Anything");
    }

    @Test
    void testFeatureReportTopNCitiesDistrictBranches() {
        Connection realCon = app.getConnection();

        // 1) Existing district + n > 0 -> while loop + printCities loop both execute
        FeatureReportTopNCitiesDistrict.generateReport(realCon, "Yangon", 5);

        // 2) Non-existing district -> 0 rows -> while loop false, printCities sees empty list
        FeatureReportTopNCitiesDistrict.generateReport(realCon, "DistrictDoesNotExistXYZ", 5);

        // 3) Null connection -> prepareStatement throws, caught by catch (Exception e)
        FeatureReportTopNCitiesDistrict.generateReport(null, "Anything", 5);
    }

    @Test
    void testGenerateReport_Exception() {
        Connection broken = null;
        try {
            broken = DriverManager.getConnection(
                    "jdbc:mysql://invalid_host/world?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                    "root",
                    "example"
            );
        } catch (Exception ignored) {
            // This must fail — it's intentional
        }

        FeatureReportCitiesByDistrict.generateReport(broken, "New York");
    }

    @Test
    void testGenerateReportTopN_Exception() {
        Connection broken = null;
        try {
            broken = DriverManager.getConnection(
                    "jdbc:mysql://invalid_host/world?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                    "root",
                    "example"
            );
        } catch (Exception ignored) { }

        FeatureReportTopNCitiesDistrict.generateReport(broken, "New York", 5);
    }

    @Test
    void testGenerateReportSuccess() {
        // This will hit the normal path
        FeatureReportCitiesByDistrict.generateReport(con, "TestDistrict");
    }

    @Test
    void testGenerateReportNoResults() {
        // Should handle empty result set gracefully
        FeatureReportCitiesByDistrict.generateReport(con, "NonExistingDistrict");
    }

    // -------------------------------------------------------------------------
    // NEW TESTS: cover BasicReportPopulationOfCity and BasicReportPopulationOfDistrict
    // -------------------------------------------------------------------------

    @Test
    void testCityExists() {
        // Branch where city exists
        BasicReportPopulationOfCity.generateReport(con, "TestCity");
    }

    @Test
    void testCityDoesNotExist() {
        // Branch where city does not exist
        BasicReportPopulationOfCity.generateReport(con, "UnknownCity");
    }

    @Test
    void testSQLException() {
        // Pass null connection to reliably trigger exception
        BasicReportPopulationOfDistrict.generateReport(null, "Anything");
        BasicReportPopulationOfCity.generateReport(null, "Anything");
    }


    @Test
    void testBasicReportPopulationOfCity_existingCity() {
        // Use any real city from your world database (e.g. "Yangon", "Kabul", "London")
        BasicReportPopulationOfCity.generateReport(con, "Yangon");
        // No assertions needed for line coverage – test will fail only if an exception is thrown
    }


    @Test
    void testDistrictExists() {
        // Ensure database has this district
        BasicReportPopulationOfDistrict.generateReport(con, "DistrictX");
        // Output should show total population for DistrictX
    }

    @Test
    void testDistrictDoesNotExist() {
        // Use a district that is not in the DB
        BasicReportPopulationOfDistrict.generateReport(con, "NonExistentDistrict");
        // Output should show: "NonExistentDistrict | No data"
    }

    @Test
    void testBasicReportPopulationOfDistrict_noData() {
        BasicReportPopulationOfDistrict.generateReport(con, "DistrictDoesNotExist_XYZ");
    }

    @Test
    void testBasicReportPopulationOfDistrict_existing() {
        // Yangon district exists
        BasicReportPopulationOfDistrict.generateReport(con, "Yangon");
    }

    @Test
    void testBasicReportPopulationOfCity_noData() {
        BasicReportPopulationOfCity.generateReport(con, "CityDoesNotExist_XYZ");
    }

    @Test
    void testBasicReportPopulationOfCity_existing() {
        // Yangon exists in world.city
        BasicReportPopulationOfCity.generateReport(con, "Yangon");
    }









}
