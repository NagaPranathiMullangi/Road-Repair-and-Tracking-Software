package com.rrts.utils;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDBConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                System.out.println("✅ Database Connected Successfully!");
                conn.close(); // Close connection
            } else {
                System.out.println("❌ Connection Failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

