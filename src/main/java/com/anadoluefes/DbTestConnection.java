package com.anadoluefes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbTestConnection {
    private final String url = "jdbc:postgresql://localhost:5432/storetest";
    private final String user = "postgres";
    private final String password = "root1234";

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

}
