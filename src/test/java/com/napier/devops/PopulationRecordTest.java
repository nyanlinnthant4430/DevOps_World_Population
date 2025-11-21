package com.napier.devops;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PopulationRecord data model.
 */
public class PopulationRecordTest
{
    @Test
    void testConstructorAndGetters()
    {
        PopulationRecord record = new PopulationRecord(
                "Asia",
                4500000000L,
                1800000000L,
                2700000000L
        );

        assertEquals("Asia", record.getName(), "Name should match constructor value");
        assertEquals(4500000000L, record.getTotalPopulation(), "Total population should match constructor value");
        assertEquals(1800000000L, record.getCityPopulation(), "City population should match constructor value");
        assertEquals(2700000000L, record.getNonCityPopulation(), "Non-city population should match constructor value");
    }

    @Test
    void testZeroValues()
    {
        PopulationRecord record = new PopulationRecord(
                "EmptyLand",
                0L,
                0L,
                0L
        );

        assertEquals("EmptyLand", record.getName());
        assertEquals(0L, record.getTotalPopulation());
        assertEquals(0L, record.getCityPopulation());
        assertEquals(0L, record.getNonCityPopulation());
    }

    @Test
    void testLargeValues()
    {
        PopulationRecord record = new PopulationRecord(
                "BigPlace",
                Long.MAX_VALUE,
                Long.MAX_VALUE - 1000,
                1000
        );

        assertEquals("BigPlace", record.getName());
        assertEquals(Long.MAX_VALUE, record.getTotalPopulation());
        assertEquals(Long.MAX_VALUE - 1000, record.getCityPopulation());
        assertEquals(1000, record.getNonCityPopulation());
    }
}
