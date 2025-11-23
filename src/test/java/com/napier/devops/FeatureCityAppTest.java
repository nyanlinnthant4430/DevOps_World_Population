package com.napier.devops;

import com.napier.devops.FeatureCity_report.FeatureCity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FeatureCityAppTest {

    @Test
    void testAppConstructor() {
        App app = new App();
        assertNotNull(app, "App should create successfully");
    }

    @Test
    void testDisconnectWithoutConnect() {
        App app = new App();
        assertDoesNotThrow(app::disconnect, "Disconnect should not throw when con is null");
    }

    @Test
    void testGetConnectionBeforeConnect() {
        App app = new App();
        assertNull(app.getConnection(), "Connection should be null before connect()");
    }

    @Test
    void testDefaultConstructorAndSettersAndGetters() {
        FeatureCity c = new FeatureCity();

        c.setId(1);
        c.setName("Tokyo");
        c.setCountry("Japan");
        c.setContinent("Asia");
        c.setRegion("Kanto");
        c.setDistrict("Tokyo-to");
        c.setPopulation(14000000);

        assertEquals(1, c.getId());
        assertEquals("Tokyo", c.getName());
        assertEquals("Japan", c.getCountry());
        assertEquals("Asia", c.getContinent());
        assertEquals("Kanto", c.getRegion());
        assertEquals("Tokyo-to", c.getDistrict());
        assertEquals(14000000, c.getPopulation());
    }

    @Test
    void testFullConstructor() {
        FeatureCity c = new FeatureCity(
                2,
                "New York",
                "USA",
                "North America",
                "New York State",
                "New York",
                8500000
        );

        assertEquals(2, c.getId());
        assertEquals("New York", c.getName());
        assertEquals("USA", c.getCountry());
        assertEquals("North America", c.getContinent());
        assertEquals("New York State", c.getRegion());
        assertEquals("New York", c.getDistrict());
        assertEquals(8500000, c.getPopulation());
    }
}
