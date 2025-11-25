package com.napier.devops;
import com.napier.devops.capital_city_report.City;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CityTest {

    @Test
    void testConstructorWithoutDistrict() {
        City c = new City("Yangon", "Myanmar", "Asia", "Southeast Asia", 5000000);
        assertEquals("Yangon", c.getName());
        assertEquals("Myanmar", c.getCountry());
        assertEquals("Asia",   c.getContinent());
        assertEquals("Southeast Asia", c.getRegion());
        assertEquals(5000000, c.getPopulation());
    }

    @Test
    void testConstructorWithDistrict() {
        City c = new City("Yangon", "Myanmar", "Asia", "Southeast Asia", "Yangon", 5000000);
        assertEquals("Yangon", c.getDistrict());
    }

    @Test
    void testSettersAndGetters() {
        City c = new City();

        c.setName("Mandalay");
        c.setCountry("Myanmar");
        c.setContinent("Asia");
        c.setRegion("Central");
        c.setDistrict("Chan Aye Thar Zan");
        c.setPopulation(1200000);

        assertEquals("Mandalay", c.getName());
        assertEquals("Myanmar", c.getCountry());
        assertEquals("Asia", c.getContinent());
        assertEquals("Central", c.getRegion());
        assertEquals("Chan Aye Thar Zan", c.getDistrict());
        assertEquals(1200000, c.getPopulation());
    }

    @Test
    void testToString() {
        City c = new City("Naypyidaw", "Myanmar", "Asia", "Middle Burma", "Dekkhina", 250000);
        String s = c.toString();
        assertTrue(s.contains("Naypyidaw"));
        assertTrue(s.contains("Myanmar"));
    }
}
