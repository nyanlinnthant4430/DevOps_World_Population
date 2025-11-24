//package com.napier.devops.feature_policymaker;
//
//import org.junit.jupiter.api.Test;
//import java.sql.Connection;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class DatabaseConnectionTest {
//
//    @Test
//    void testConnectFailure() {
//        DatabaseConnection.disconnect();
//        assertThrows(RuntimeException.class, () -> {
//            DatabaseConnection.connect();
//        });
//    }
//
//    @Test
//    void testDisconnectDoesNotThrow() {
//        assertDoesNotThrow(DatabaseConnection::disconnect);
//    }
//}
