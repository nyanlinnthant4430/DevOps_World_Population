package com.napier.devops;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PopulationAppTest {

    @Test
    void testAppConstructor() {
        App app = new App();
        assertNotNull(app, "App instance should be created.");
    }

    @Test
    void testDisconnectWithoutConnect() {
        App app = new App();
        assertDoesNotThrow(app::disconnect, "Disconnect should not throw when con is null");
    }


    @Test
    void testGetConnectionBeforeConnect() {
        App app = new App();
        assertNull(app.getConnection(), "Connection should be null before connect().");
    }
}