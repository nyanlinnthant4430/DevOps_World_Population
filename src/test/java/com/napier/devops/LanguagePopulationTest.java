package com.napier.devops;

import com.napier.devops.basicpopulation.LanguagePopulation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LanguagePopulationTest {

    @Test
    public void testNoArgConstructorAndSettersAndGetters() {
        LanguagePopulation lp = new LanguagePopulation();

        lp.setLanguage("English");
        lp.setSpeakers(500000000L);
        lp.setPercentageOfWorld(10.0);

        assertEquals("English", lp.getLanguage());
        assertEquals(500000000L, lp.getSpeakers());
        assertEquals(10.0, lp.getPercentageOfWorld());
    }

    @Test
    public void testAllArgsConstructor() {
        LanguagePopulation lp = new LanguagePopulation(
                "Chinese",
                900000000L,
                18.0
        );

        assertEquals("Chinese", lp.getLanguage());
        assertEquals(900000000L, lp.getSpeakers());
        assertEquals(18.0, lp.getPercentageOfWorld());
    }
}
