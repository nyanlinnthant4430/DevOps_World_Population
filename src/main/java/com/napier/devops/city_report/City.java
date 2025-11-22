package com.napier.devops.city_report;

/**
 * Represents a city, including its name, country, continent, region, and population.
 */
public class City {
    private String name;
    private String country;
    private String continent;
    private String region;
    private int population;

    // Constructors
    public City() {}

    public City(String name, String country, String continent, String region, int population) {
        this.name = name;
        this.country = country;
        this.continent = continent;
        this.region = region;
        this.population = population;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getContinent() { return continent; }
    public void setContinent(String continent) { this.continent = continent; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public int getPopulation() { return population; }
    public void setPopulation(int population) { this.population = population; }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s - %,d", name, country, continent, region, population);
    }
}
