package com.napier.devops;

import com.napier.devops.FeatureCity_report.City;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CityModelTest {

    @Test
    void testCityConstructorAndGetters() {
        City c = new City(
                10010,
                "Yangon",
                "Myanmar",
                "Asia",
                "Southeast Asia",
                "Yangon",
                7360000
        );

        assertEquals(10010, c.getId());
        assertEquals("Yangon", c.getName());
        assertEquals("Myanmar", c.getCountry());
        assertEquals("Asia", c.getContinent());
        assertEquals("Southeast Asia", c.getRegion());
        assertEquals("Yangon", c.getDistrict());
        assertEquals(7360000, c.getPopulation());
    }
}
