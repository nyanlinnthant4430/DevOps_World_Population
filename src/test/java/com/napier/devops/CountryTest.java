package com.napier.devops;

import com.napier.devops.country_report.Country;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Country model class.
 */
public class CountryTest {

    @Test
    void testDefaultConstructorAndSetters() {
        Country c = new Country();

        c.setCode("MMR");
        c.setName("Myanmar");
        c.setContinent("Asia");
        c.setRegion("Southeast Asia");
        c.setPopulation(54000000);
        c.setCapital("Naypyidaw");

        assertEquals("MMR", c.getCode());
        assertEquals("Myanmar", c.getName());
        assertEquals("Asia", c.getContinent());
        assertEquals("Southeast Asia", c.getRegion());
        assertEquals(54000000, c.getPopulation());
        assertEquals("Naypyidaw", c.getCapital());
    }

    @Test
    void testOldConstructor() {
        Country c = new Country("Japan", "Asia", "Eastern Asia", 126000000);

        assertNull(c.getCode());        // not set in old constructor
        assertEquals("Japan", c.getName());
        assertEquals("Asia", c.getContinent());
        assertEquals("Eastern Asia", c.getRegion());
        assertEquals(126000000, c.getPopulation());
        assertNull(c.getCapital());     // not set in old constructor
    }

    @Test
    void testFullConstructor() {
        Country c = new Country(
                "USA",
                "United States",
                "North America",
                "Northern America",
                331000000,
                "Washington D.C."
        );

        assertEquals("USA", c.getCode());
        assertEquals("United States", c.getName());
        assertEquals("North America", c.getContinent());
        assertEquals("Northern America", c.getRegion());
        assertEquals(331000000, c.getPopulation());
        assertEquals("Washington D.C.", c.getCapital());
    }

    @Test
    void testSettersOverrideValues() {
        Country c = new Country();

        c.setCode("GBR");
        c.setName("United Kingdom");
        c.setContinent("Europe");
        c.setRegion("British Isles");
        c.setPopulation(67000000);
        c.setCapital("London");

        // override
        c.setCode("ENG");
        c.setName("England");
        c.setContinent("Europe");
        c.setRegion("England Region");
        c.setPopulation(56000000);
        c.setCapital("London");

        assertEquals("ENG", c.getCode());
        assertEquals("England", c.getName());
        assertEquals("Europe", c.getContinent());
        assertEquals("England Region", c.getRegion());
        assertEquals(56000000, c.getPopulation());
        assertEquals("London", c.getCapital());
    }

    @Test
    void testNullSafety() {
        Country c = new Country();

        c.setCode(null);
        c.setName(null);
        c.setContinent(null);
        c.setRegion(null);
        c.setCapital(null);

        assertNull(c.getCode());
        assertNull(c.getName());
        assertNull(c.getContinent());
        assertNull(c.getRegion());
        assertNull(c.getCapital());
    }
}
