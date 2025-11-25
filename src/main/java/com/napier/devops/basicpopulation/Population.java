package com.napier.devops.basicpopulation;

/**
 * Represents a population record for:
 *  - the world
 *  - a continent
 *  - a region
 *  - a country
 *  - a district
 *  - a city

 * It stores:
 *  - the name (e.g. "World", "Asia", "Western Europe", "Myanmar", "Yangon", "Hlaing")
 *  - the total population
 *  - the population living in cities
 *  - the population not living in cities
 *  - the percentage of people living in cities
 *  - the percentage of people not living in cities
 */
public class Population {

    /**
     * Name of the geographical unit:
     *  - "World"
     *  - continent name
     *  - region name
     *  - country name
     *  - district name
     *  - city name
     */
    private String name;

    /** Total population of this world/continent/region/country/district/city. */
    private long totalPopulation;

    /** Number of people living in cities within this unit. */
    private long cityPopulation;

    /** Number of people NOT living in cities within this unit. */
    private long nonCityPopulation;

    /** Percentage of the total population living in cities. */
    private double cityPercentage;

    /** Percentage of the total population not living in cities. */
    private double nonCityPercentage;

    // ===== Constructors =====

    public Population() {
    }

    public Population(String name,
                      long totalPopulation,
                      long cityPopulation,
                      long nonCityPopulation,
                      double cityPercentage,
                      double nonCityPercentage) {
        this.name = name;
        this.totalPopulation = totalPopulation;
        this.cityPopulation = cityPopulation;
        this.nonCityPopulation = nonCityPopulation;
        this.cityPercentage = cityPercentage;
        this.nonCityPercentage = nonCityPercentage;
    }

    // ===== Getters and Setters =====

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalPopulation() {
        return totalPopulation;
    }

    public void setTotalPopulation(long totalPopulation) {
        this.totalPopulation = totalPopulation;
    }

    public long getCityPopulation() {
        return cityPopulation;
    }

    public void setCityPopulation(long cityPopulation) {
        this.cityPopulation = cityPopulation;
    }

    public long getNonCityPopulation() {
        return nonCityPopulation;
    }

    public void setNonCityPopulation(long nonCityPopulation) {
        this.nonCityPopulation = nonCityPopulation;
    }

    public double getCityPercentage() {
        return cityPercentage;
    }

    public void setCityPercentage(double cityPercentage) {
        this.cityPercentage = cityPercentage;
    }

    public double getNonCityPercentage() {
        return nonCityPercentage;
    }

    public void setNonCityPercentage(double nonCityPercentage) {
        this.nonCityPercentage = nonCityPercentage;
    }
}
