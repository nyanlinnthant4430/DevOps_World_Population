//package com.napier.devops;
//
//import com.napier.devops.city_report.City;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class CityTest
//{
//    @Test
//    void testCityConstructorAndGetters()
//    {
//        City city = new City(
//                "Yangon",
//                "Myanmar",
//                "Asia",
//                "Southeast Asia",
//                7360703
//        );
//
//        assertEquals("Yangon", city.getName());
//        assertEquals("Myanmar", city.getCountry());
//        assertEquals("Asia", city.getContinent());
//        assertEquals("Southeast Asia", city.getRegion());
//        assertEquals(7360703, city.getPopulation());
//    }
//
//    @Test
//    void testDefaultConstructorAndSetters() {
//        City city = new City();
//
//        city.setName("Yangon");
//        city.setCountry("Myanmar");
//        city.setContinent("Asia");
//        city.setRegion("South-East Asia");
//        city.setPopulation(5000000);
//
//        assertEquals("Yangon", city.getName());
//        assertEquals("Myanmar", city.getCountry());
//        assertEquals("Asia", city.getContinent());
//        assertEquals("South-East Asia", city.getRegion());
//        assertEquals(5000000, city.getPopulation());
//    }
//
//    @Test
//    void testFullConstructorAndGetters() {
//        City city = new City("Tokyo", "Japan", "Asia", "Kanto", 13960000);
//
//        assertEquals("Tokyo", city.getName());
//        assertEquals("Japan", city.getCountry());
//        assertEquals("Asia", city.getContinent());
//        assertEquals("Kanto", city.getRegion());
//        assertEquals(13960000, city.getPopulation());
//    }
//
//    @Test
//    void testToString() {
//        City city = new City("Paris", "France", "Europe", "Île-de-France", 2148000);
//
//        String result = city.toString();
//
//        assertTrue(result.contains("Paris"));
//        assertTrue(result.contains("France"));
//        assertTrue(result.contains("Europe"));
//        assertTrue(result.contains("Île-de-France"));
//        assertTrue(result.contains("2,148,000"));  // uses %,d formatting
//    }
//
//}
