package com.napier.devops;

import com.napier.devops.city_report.*;
import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        Connection con = db.connect();

        Menu menu = new Menu();
        menu.show(con);

        db.disconnect();
    }
}
