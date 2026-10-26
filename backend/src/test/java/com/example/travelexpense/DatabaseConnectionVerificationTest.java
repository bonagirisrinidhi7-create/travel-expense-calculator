package com.example.travelexpense;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DatabaseConnectionVerificationTest {

    @Test
    @DisplayName("Verify MySQL connectivity and schema existence")
    void checkDatabaseConnection() {
        String host = System.getenv().getOrDefault("DB_HOST", "localhost");
        String port = System.getenv().getOrDefault("DB_PORT", "3306");
        String user = System.getenv().getOrDefault("DB_USERNAME", "root");
        String password = System.getenv().getOrDefault("DB_PASSWORD", "");

        System.out.println("=== DB Verification Details ===");
        System.out.println("Host: " + host);
        System.out.println("Port: " + port);
        System.out.println("User: " + user);
        System.out.println("Password set: " + (!password.isEmpty()));

        String serverUrl = "jdbc:mysql://" + host + ":" + port + "/?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        try (Connection conn = DriverManager.getConnection(serverUrl, user, password);
             Statement stmt = conn.createStatement()) {

            System.out.println("MySQL Server Authentication: SUCCESS");

            try (ResultSet rs = stmt.executeQuery("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = 'travel_expense_db'")) {
                if (rs.next()) {
                    System.out.println("Database 'travel_expense_db': EXISTS");

                    String dbUrl = "jdbc:mysql://" + host + ":" + port + "/travel_expense_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
                    try (Connection dbConn = DriverManager.getConnection(dbUrl, user, password);
                         Statement dbStmt = dbConn.createStatement();
                         ResultSet tablesRs = dbStmt.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'travel_expense_db' AND TABLE_NAME = 'trips'")) {
                        if (tablesRs.next()) {
                            System.out.println("Table 'trips': EXISTS");
                        } else {
                            System.out.println("Table 'trips': DOES NOT EXIST");
                        }
                    }
                } else {
                    System.out.println("Database 'travel_expense_db': DOES NOT EXIST");
                }
            }

        } catch (SQLException e) {
            System.out.println("MySQL Connection/Authentication FAILED: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("ErrorCode: " + e.getErrorCode());
        }
    }
}
