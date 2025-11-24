package com.napier.devops;

import com.napier.devops.country_report.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
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

    /** Run code with fake console input (for methods using new Scanner(System.in)) */
    private void withInput(String input, Runnable action) {
        InputStream originalIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            action.run();
        } finally {
            System.setIn(originalIn);
        }
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



    @Test
    void testConnectDefault() {
        App localApp = new App();
        assertDoesNotThrow(() -> app.connect());
        localApp.disconnect();
    }




    @Test
    void testDisconnect() {
        App localApp = new App();
        localApp.connect("localhost:33060", 0);
        assertDoesNotThrow(localApp::disconnect);
    }

    @Test
    void testRunCapitalCityReports() {
        // Continent + Region
        String input = "Asia\nSouth-Eastern Asia\n";

        withInput(input, () ->
                assertDoesNotThrow(() -> app.runCapitalCityReports())
        );
    }

    @Test
    void testRunCountryReports() {
        // Continent + Region
        String input = "Asia\nSouth-Eastern Asia\n";

        withInput(input, () ->
                assertDoesNotThrow(() -> app.runCountryReports())
        );
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

    @Test
    void testRunBasicPopulationReports() {
    String input = String.join("\n",
            "Asia",
            "South-Eastern Asia",
            "Myanmar",
            "Yangon",
            "Yangon"
    ) + "\n";

    withInput(input, () ->
            assertDoesNotThrow(() -> app.runBasicPopulationReports())
    );
}


    @Test
    void testMenuOnlyNavigation() {
        // Only open menu then exit immediately
        String fakeInput = "0\n";   // exit the menu

        System.setIn(new ByteArrayInputStream(fakeInput.getBytes()));

        App app = new App();
        app.connect("localhost:33060", 0);

        assertDoesNotThrow(() -> {
            Method runMenu = App.class.getDeclaredMethod("runMenu");
            runMenu.setAccessible(true);
            runMenu.invoke(app);
        });

        app.disconnect();
    }

    @Test
    void testMainMethod() {
        // Fake menu input: choose option "0" to exit immediately
        String fakeInput = "0\n";

        InputStream originalIn = System.in;

        try {
            System.setIn(new ByteArrayInputStream(fakeInput.getBytes(StandardCharsets.UTF_8)));

            // Run main with args (to cover the args.length >= 2 path)
            String[] args = { "localhost:33060", "0" };

            assertDoesNotThrow(() -> App.main(args));

        } finally {
            System.setIn(originalIn);  // restore input so other tests aren't affected
        }
    }

    @Test
    void testRunPolicymakerReports() {
        assertDoesNotThrow(() -> app.runPolicymakerReports());
    }

    @Test
    void testRunCityReports() {
        // Fake inputs: Continent + Region
        String fakeInput = "Asia\nSouth-Eastern Asia\n";

        InputStream originalIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream(fakeInput.getBytes(StandardCharsets.UTF_8)));

            assertDoesNotThrow(() -> app.runCityReports());

        } finally {
            System.setIn(originalIn);   // restore original System.in
        }
    }

}
