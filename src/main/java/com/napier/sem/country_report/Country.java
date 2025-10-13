package com.napier.sem.country_report;
/**
 * This class stores country data retrieved from the database.
 */
public class Country {
    // Name of the country
    private String name;

    // Continent where the country is located
    private String continent;

    // Specific region within the continent
    private String region;

    // Total population of the country
    private int population;

    /**
     * Constructs a new Country object with the specified details.
     *
     * @param name the name of the country
     * @param continent the continent where the country is located
     * @param region the region within the continent
     * @param population the total population of the country
     */
    public Country(String name, String continent, String region, int population) {
        this.name = name;
        this.continent = continent;
        this.region = region;
        this.population = population;
    }

    public String getName() { return name; }
    public String getContinent() { return continent; }
    public String getRegion() { return region; }
    public int getPopulation() { return population; }
}