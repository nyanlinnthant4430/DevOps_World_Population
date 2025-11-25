package com.napier.devops;

import com.napier.devops.basicpopulation.Population;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PopulationTest {

    @Test
    public void testNoArgConstructorAndSettersAndGetters() {
        Population p = new Population();

        p.setName("Asia");
        p.setTotalPopulation(1000000000L);
        p.setCityPopulation(600000000L);
        p.setNonCityPopulation(400000000L);
        p.setCityPercentage(60.0);
        p.setNonCityPercentage(40.0);

        assertEquals("Asia", p.getName());
        assertEquals(1000000000L, p.getTotalPopulation());
        assertEquals(600000000L, p.getCityPopulation());
        assertEquals(400000000L, p.getNonCityPopulation());
        assertEquals(60.0, p.getCityPercentage());
        assertEquals(40.0, p.getNonCityPercentage());
    }

    @Test
    public void testAllArgsConstructor() {
        Population p = new Population(
                "Europe",
                750000000L,
                500000000L,
                250000000L,
                66.67,
                33.33
        );

        assertEquals("Europe", p.getName());
        assertEquals(750000000L, p.getTotalPopulation());
        assertEquals(500000000L, p.getCityPopulation());
        assertEquals(250000000L, p.getNonCityPopulation());
        assertEquals(66.67, p.getCityPercentage());
        assertEquals(33.33, p.getNonCityPercentage());
    }
}
