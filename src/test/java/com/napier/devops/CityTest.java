package com.napier.devops;
import com.napier.devops.city_report.City;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the City model.
 */
public class CityTest {

    @Test
    void testNoArgConstructorAndSetters() {
        City city = new City();

        city.setName("Yangon");
        city.setCountry("Myanmar");
        city.setContinent("Asia");
        city.setRegion("South-Eastern Asia");
        city.setDistrict("Yangon District");
        city.setPopulation(5000000);

        assertEquals("Yangon", city.getName());
        assertEquals("Myanmar", city.getCountry());
        assertEquals("Myanmar", city.getCountryName(), "getCountryName() should alias getCountry()");
        assertEquals("Asia", city.getContinent());
        assertEquals("South-Eastern Asia", city.getRegion());
        assertEquals("Yangon District", city.getDistrict());
        assertEquals(5000000, city.getPopulation());
    }

    @Test
    void testConstructorWithoutDistrict() {
        City city = new City(
                "Tokyo",
                "Japan",
                "Asia",
                "Eastern Asia",
                13000000
        );

        assertEquals("Tokyo", city.getName());
        assertEquals("Japan", city.getCountry());
        assertEquals("Japan", city.getCountryName());
        assertEquals("Asia", city.getContinent());
        assertEquals("Eastern Asia", city.getRegion());
        assertNull(city.getDistrict(), "District should be null when using constructor without district");
        assertEquals(13000000, city.getPopulation());
    }

    @Test
    void testConstructorWithDistrict() {
        City city = new City(
                "London",
                "United Kingdom",
                "Europe",
                "British Isles",
                "Greater London",
                9000000
        );

        assertEquals("London", city.getName());
        assertEquals("United Kingdom", city.getCountry());
        assertEquals("United Kingdom", city.getCountryName());
        assertEquals("Europe", city.getContinent());
        assertEquals("British Isles", city.getRegion());
        assertEquals("Greater London", city.getDistrict());
        assertEquals(9000000, city.getPopulation());
    }

    @Test
    void testToStringFormat() {
        City city = new City(
                "Paris",
                "France",
                "Europe",
                "Western Europe",
                "Île-de-France",
                2148327
        );

        String text = city.toString();
        // Basic checks so that internal formatting is covered
        assertTrue(text.contains("Paris"), "toString() should contain city name");
        assertTrue(text.contains("France"), "toString() should contain country");
        assertTrue(text.contains("Europe"), "toString() should contain continent");
        assertTrue(text.contains("Western Europe"), "toString() should contain region");
        assertTrue(text.contains("Île-de-France"), "toString() should contain district");
        assertTrue(text.contains("2,148,327"), "toString() should contain formatted population");
    }
}
