package com.napier.devops.FeatureCity_report;

public class FeatureCity {
    public int id;
    public String name;
    public String country;
    public String continent;
    public String region;
    public String district;
    public int population;

    // No-argument constructor (required for report classes)
    public FeatureCity() {}

    // Full constructor (used in model tests)
    public FeatureCity(int id, String name, String country, String continent, String region, String district, int population) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.continent = continent;
        this.region = region;
        this.district = district;
        this.population = population;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getContinent() { return continent; }
    public void setContinent(String continent) { this.continent = continent; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public int getPopulation() { return population; }
    public void setPopulation(int population) { this.population = population; }
}
