package com.napier.devops;

import com.napier.devops.city_report.City;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CityTest
{
    @Test
    void testCityConstructorAndGetters()
    {
        City city = new City(
                "Yangon",
                "Myanmar",
                "Asia",
                "Southeast Asia",
                7360703
        );

        assertEquals("Yangon", city.getName());
        assertEquals("Myanmar", city.getCountry());
        assertEquals("Asia", city.getContinent());
        assertEquals("Southeast Asia", city.getRegion());
        assertEquals(7360703, city.getPopulation());
    }
}
