package com.napier.devops;

/**
 * Represents a population record for a geographical entity (continent, region, etc.).
 */
public class PopulationRecord {
    private final String name;
    private final long totalPopulation;
    private final long cityPopulation;
    private final long nonCityPopulation;

    public PopulationRecord(String name, long totalPopulation, long cityPopulation, long nonCityPopulation) {
        this.name = name;
        this.totalPopulation = totalPopulation;
        this.cityPopulation = cityPopulation;
        this.nonCityPopulation = nonCityPopulation;
    }

    public String getName() { return name; }
    public long getTotalPopulation() { return totalPopulation; }
    public long getCityPopulation() { return cityPopulation; }
    public long getNonCityPopulation() { return nonCityPopulation; }
}
