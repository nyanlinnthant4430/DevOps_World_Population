package com.napier.sem;

import com.napier.sem.country_report.DatabaseConnection;

import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        Connection con = db.connect();

        if (con != null) {
            Menu.showMenu(con);
            db.disconnect();
        } else {
            System.out.println("Failed to connect to database.");
        }
    }
}
