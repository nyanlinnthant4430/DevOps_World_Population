package com.napier.devops;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CityAppTest {

    @Test
    void testAppConstructor() {
        App app = new App();
        assertNotNull(app, "App should create successfully");
    }

    @Test
    void testDisconnectWithoutConnect() {
        App app = new App();
        assertDoesNotThrow(app::disconnect, "Disconnect should not throw when con is null");
    }

    @Test
    void testGetConnectionBeforeConnect() {
        App app = new App();
        assertNull(app.getConnection(), "Connection should be null before connect()");
    }
}
