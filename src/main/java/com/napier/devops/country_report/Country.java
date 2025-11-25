package com.napier.devops.country_report;

/**
 * Represents a country record used in country reports.
 * Required fields:
 *  - Code
 *  - Name
 *  - Continent
 *  - Region
 *  - Population
 *  - Capital
 */
public class Country
{
    // Country code (e.g., USA, GBR)
    private String code;

    // Name of the country
    private String name;

    // Continent where the country is located
    private String continent;

    // Specific region within the continent
    private String region;

    // Total population of the country
    private int population;

    // Name of the capital city
    private String capital;

    /**
     * Default constructor.
     */
    public Country() { }

    /**
     * Backwards-compatible constructor (old version without code/capital).
     * Only use this if some old code still calls it.
     */
    public Country(String name, String continent, String region, int population)
    {
        this.name = name;
        this.continent = continent;
        this.region = region;
        this.population = population;
    }

    /**
     * Full constructor using the new report schema.
     *
     * @param code       the country code (e.g. "USA")
     * @param name       the name of the country
     * @param continent  the continent of the country
     * @param region     the region of the country
     * @param population the total population
     * @param capital    the name of the capital city
     */
    public Country(String code,
                   String name,
                   String continent,
                   String region,
                   int population,
                   String capital)
    {
        this.code = code;
        this.name = name;
        this.continent = continent;
        this.region = region;
        this.population = population;
        this.capital = capital;
    }

    // ===== Getters =====

    public String getCode() { return code; }

    public String getName() { return name; }

    public String getContinent() { return continent; }

    public String getRegion() { return region; }

    public int getPopulation() { return population; }

    public String getCapital() { return capital; }

    // ===== Setters =====

    public void setCode(String code) { this.code = code; }

    public void setName(String name) { this.name = name; }

    public void setContinent(String continent) { this.continent = continent; }

    public void setRegion(String region) { this.region = region; }

    public void setPopulation(int population) { this.population = population; }

    public void setCapital(String capital) { this.capital = capital; }
}
