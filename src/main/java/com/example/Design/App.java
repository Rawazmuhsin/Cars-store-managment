package com.example.Design;

import java.sql.Connection;
import java.sql.SQLException;

import com.example.Database.DatabaseConnection;

/**
 * Simple main class to test database connection.
 */
public class App {
    
    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        
        // Get database connection instance
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        
        try {
            // Test connection
            boolean connected = dbConnection.connect();
            
            if (connected) {
                System.out.println("✅ Database connection successful!");
                
                // Get and print connection info
                Connection conn = dbConnection.getConnection();
                System.out.println("Connected to database: " + conn.getCatalog());
                System.out.println("Database product: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("Database version: " + conn.getMetaData().getDatabaseProductVersion());
                
                // Test if we can query the database
                try {
                    boolean carsTableExists = dbConnection.tableExists("cars");
                    System.out.println("Cars table exists: " + carsTableExists);
                } catch (Exception e) {
                    System.out.println("Error checking tables: " + e.getMessage());
                }
                
                System.out.println("All tests completed!");
            } else {
                System.out.println("❌ Failed to connect to database");
            }
        } catch (SQLException e) {
            System.out.println("❌ Database connection error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close connection
            dbConnection.disconnect();
            System.out.println("Database connection closed");
        }
    }
}