package com.napier.devops;

import com.napier.devops.city_report.City;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class CityAppTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
    }

    @Test
    void printCitiesTestNull()
    {
        App.printCities(null);
    }

    @Test
    void printCitiesTestEmpty()
    {
        ArrayList<City> cities = new ArrayList<>();
        App.printCities(cities);
    }

    @Test
    void printCitiesTestContainsNull()
    {
        ArrayList<City> cities = new ArrayList<>();
        cities.add(null);
        App.printCities(cities);
    }

    @Test
    void printCities()
    {
        ArrayList<City> cities = new ArrayList<>();

        // Sample city (match your City constructor: name, country, continent, region, population)
        City c = new City(
                "Yangon",
                "Myanmar",
                "Asia",
                "Southeast Asia",
                7360703
        );

        cities.add(c);

        // Call the method (we're just checking it doesn't crash / NPE)
        App.printCities(cities);
    }
}
