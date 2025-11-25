package com.napier.devops.city_report;

/**
 * Represents a city, including its name, country, continent, region, district, and population.
 */
public class City {
    private String name;
    private String country;
    private String continent;
    private String region;
    private String district;
    private int population;

    // Constructors
    public City() {}

    // Existing constructor (no district) – kept for backwards compatibility
    public City(String name, String country, String continent, String region, int population) {
        this.name = name;
        this.country = country;
        this.continent = continent;
        this.region = region;
        this.population = population;
        this.district = null; // or "" if you prefer
    }

    // New constructor including district
    public City(String name, String country, String continent, String region, String district, int population) {
        this.name = name;
        this.country = country;
        this.continent = continent;
        this.region = region;
        this.district = district;
        this.population = population;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Alias for compatibility with code that expects getCountryName().
     */
    public String getCountryName() {
        return country;
    }

    public String getContinent() {
        return continent;
    }
    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }

    public int getPopulation() {
        return population;
    }
    public void setPopulation(int population) {
        this.population = population;
    }

    @Override
    public String toString() {
        return String.format(
                "%s, %s, %s, %s, %s - %,d",
                name,
                country,
                continent,
                region,
                district,
                population
        );
    }
}
