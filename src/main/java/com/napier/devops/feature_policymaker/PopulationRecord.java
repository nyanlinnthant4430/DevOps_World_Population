package com.napier.devops.feature_policymaker;

/**
 * Represents a population record for a geographical entity
 * (continent, region, country, etc.).
 */
public class PopulationRecord {

    private final String name;
    private final long totalPopulation;
    private final long cityPopulation;
    private final long nonCityPopulation;

    public PopulationRecord(String name,
                            long totalPopulation,
                            long cityPopulation,
                            long nonCityPopulation) {
        this.name = name;
        this.totalPopulation = totalPopulation;
        this.cityPopulation = cityPopulation;
        this.nonCityPopulation = nonCityPopulation;
    }

    // Accessors used in the new report classes
    public String name() {
        return name;
    }

    public long totalPopulation() {
        return totalPopulation;
    }

    public long cityPopulation() {
        return cityPopulation;
    }

    public long nonCityPopulation() {
        return nonCityPopulation;
    }

    // Traditional getters for backwards compatibility
    public String getName() {
        return name;
    }

    public long getTotalPopulation() {
        return totalPopulation;
    }

    public long getCityPopulation() {
        return cityPopulation;
    }

    public long getNonCityPopulation() {
        return nonCityPopulation;
    }
}
