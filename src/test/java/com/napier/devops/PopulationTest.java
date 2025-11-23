package com.napier.devops;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PopulationTest {

    @Test
    void testPopulationRecordConstructorAndGetters() {
        PopulationRecord record = new PopulationRecord(
                "Asia",
                4500000000L,
                1600000000L,
                2900000000L
        );

        assertEquals("Asia", record.getName());
        assertEquals(4500000000L, record.getTotalPopulation());
        assertEquals(1600000000L, record.getCityPopulation());
        assertEquals(2900000000L, record.getNonCityPopulation());
    }
}
