package com.napier.devops.basicpopulation;

/**
 * Represents the population of people who speak a specific language.

 * Required languages for the report:
 *   - Chinese
 *   - English
 *   - Hindi
 *   - Spanish
 *   - Arabic

 * Each object stores:
 *   - the name of the language
 *   - number of speakers
 *   - percentage of the world population
 */
public class LanguagePopulation {

    /** Language name (e.g. "Chinese", "English"). */
    private String language;

    /** Number of speakers worldwide. */
    private long speakers;

    /** Percentage of world population who speak this language. */
    private double percentageOfWorld;

    // ===== Constructors =====

    public LanguagePopulation() {
    }

    public LanguagePopulation(String language, long speakers, double percentageOfWorld) {
        this.language = language;
        this.speakers = speakers;
        this.percentageOfWorld = percentageOfWorld;
    }

    // ===== Getters and Setters =====

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public long getSpeakers() {
        return speakers;
    }

    public void setSpeakers(long speakers) {
        this.speakers = speakers;
    }

    public double getPercentageOfWorld() {
        return percentageOfWorld;
    }

    public void setPercentageOfWorld(double percentageOfWorld) {
        this.percentageOfWorld = percentageOfWorld;
    }
}
