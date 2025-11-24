//package com.napier.devops;
//
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class AdditionalAppTest {
//
//    @Test
//    void testAppConstructor() {
//        App app = new App();
//        assertNotNull(app, "App instance should be created.");
//    }
//
//    @Test
//    void testGetConnectionBeforeConnect() {
//        App app = new App();
//        assertNull(app.getConnection(), "Connection should be null before connect().");
//    }
//
//    @Test
//    void testDisconnectWithoutConnect() {
//        App app = new App();
//        assertDoesNotThrow(app::disconnect, "disconnect() must not throw even if connection is null.");
//    }
//}
