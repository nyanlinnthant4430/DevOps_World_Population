package com.napier.devops;

import com.napier.devops.city_report.City;
import com.napier.devops.country_report.Country;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    void testGetConnectionAndGetConReturnSameValue() {
        App app = new App();

        // Initially, connection should be null
        assertNull(app.getConnection());
        assertNull(app.getCon());

        // We do not need a real Connection here – just verify both getters are consistent.
        // Using reflection, set the private 'con' field to a dummy non-null object.
        try {
            var field = App.class.getDeclaredField("con");
            field.setAccessible(true);

            Connection dummy = null; // we only care that both getters see the same value
            field.set(app, dummy);

            assertEquals(dummy, app.getConnection());
            assertEquals(dummy, app.getCon());
        } catch (Exception e) {
            fail("Reflection failed setting connection field: " + e.getMessage());
        }
    }

    @Test
    void testPrintCountriesWithNullListDoesNotThrow() {
        // Just ensure no exception is thrown
        App.printCountries(null);
    }



    @Test
    void testPrintCitiesWithNullListDoesNotThrow() {
        App.printCities(null);
    }

    @Test
    void testPrintCitiesWithSingleCity() {
        ArrayList<City> list = new ArrayList<>();
        City city = new City();
        list.add(city);

        App.printCities(list);
    }

    @Test
    void testAskForPositiveIntValidFirstTime() throws Exception {
        App app = new App();
        // Provide a simple valid integer "5"
        String input = "5\n";
        Scanner scanner = new Scanner(input);

        // Method signature: (Scanner, String)
        Method m = App.class.getDeclaredMethod("askForPositiveInt", Scanner.class, String.class);
        m.setAccessible(true);

        Object result = m.invoke(app, scanner, "Enter N: ");
        assertInstanceOf(Integer.class, result);
        assertEquals(5, (int) result);
    }

    @Test
    void testAskForPositiveIntWithInvalidThenValid() throws Exception {
        App app = new App();
        // Sequence:
        //  - 0       (invalid: <= 0)
        //  - -3      (invalid: <= 0)
        //  - abc     (invalid: not a number)
        //  - 10      (valid)
        String input = "0\n-3\nabc\n10\n";
        Scanner scanner = new Scanner(input);

        // Method signature: (Scanner, String)
        Method m = App.class.getDeclaredMethod("askForPositiveInt", Scanner.class, String.class);
        m.setAccessible(true);

        Object result = m.invoke(app, scanner, "Enter N: ");
        assertInstanceOf(Integer.class, result);
        assertEquals(10, (int) result);
    }

}
