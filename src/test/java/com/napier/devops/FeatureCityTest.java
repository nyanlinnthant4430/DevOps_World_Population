package com.napier.devops;
import com.napier.devops.FeatureCity_report.FeatureCity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the FeatureCity model.
 */
public class FeatureCityTest {

    @Test
    void testNoArgConstructorAndSetters() {
        FeatureCity city = new FeatureCity();

        city.setId(1);
        city.setName("Bangkok");
        city.setCountry("Thailand");
        city.setContinent("Asia");
        city.setRegion("South-Eastern Asia");
        city.setDistrict("Bangkok Metropolis");
        city.setPopulation(8000000);

        assertEquals(1, city.getId());
        assertEquals("Bangkok", city.getName());
        assertEquals("Thailand", city.getCountry());
        assertEquals("Asia", city.getContinent());
        assertEquals("South-Eastern Asia", city.getRegion());
        assertEquals("Bangkok Metropolis", city.getDistrict());
        assertEquals(8000000, city.getPopulation());
    }

    @Test
    void testFullConstructor() {
        FeatureCity city = new FeatureCity(
                10,
                "New York",
                "United States",
                "North America",
                "Northern America",
                "New York",
                8400000
        );

        assertEquals(10, city.getId());
        assertEquals("New York", city.getName());
        assertEquals("United States", city.getCountry());
        assertEquals("North America", city.getContinent());
        assertEquals("Northern America", city.getRegion());
        assertEquals("New York", city.getDistrict());
        assertEquals(8400000, city.getPopulation());
    }

    @Test
    void testMutableFieldsCanBeUpdated() {
        FeatureCity city = new FeatureCity(
                5,
                "Old Name",
                "Old Country",
                "Old Continent",
                "Old Region",
                "Old District",
                1000
        );

        city.setId(6);
        city.setName("New Name");
        city.setCountry("New Country");
        city.setContinent("New Continent");
        city.setRegion("New Region");
        city.setDistrict("New District");
        city.setPopulation(2000);

        assertEquals(6, city.getId());
        assertEquals("New Name", city.getName());
        assertEquals("New Country", city.getCountry());
        assertEquals("New Continent", city.getContinent());
        assertEquals("New Region", city.getRegion());
        assertEquals("New District", city.getDistrict());
        assertEquals(2000, city.getPopulation());
    }
}
