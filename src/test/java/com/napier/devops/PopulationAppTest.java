package com.napier.devops;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Lightweight unit tests for the App class that
 * don’t require a real database.
 */
public class PopulationAppTest
{
    @Test
    void testAppConstructor()
    {
        App app = new App();
        assertNotNull(app, "App instance should be created");
    }

    @Test
    void testDisconnectOnFreshAppDoesNotThrow()
    {
        App app = new App();
        // con is null, should safely do nothing and not throw
        assertDoesNotThrow(app::disconnect, "disconnect() should not throw when connection is null");
    }
}
