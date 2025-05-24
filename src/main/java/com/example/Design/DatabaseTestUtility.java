package com.example.Design;

import com.example.Database.DatabaseConnection;
import javax.swing.JOptionPane;
import java.sql.Connection;

/**
 * Utility class for testing database connections
 */
public class DatabaseTestUtility {
    
    /**
     * Test database connection and show status to user
     */
    public static void testAndShowDatabaseStatus() {
        try {
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            
            // Test connection
            boolean connected = dbConnection.connect();
            
            if (connected) {
                // Try to get connection and test with a simple query
                Connection conn = dbConnection.getConnection();
                if (conn != null && !conn.isClosed()) {
                    JOptionPane.showMessageDialog(null,
                        "✅ Database Connection Successful!\n\n" +
                        "Status: Connected\n" +
                        "Database: car_store_management\n" +
                        "Server: localhost:3306\n" +
                        "Connection is active and ready.",
                        "Database Test - SUCCESS",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    throw new Exception("Connection is null or closed");
                }
            } else {
                throw new Exception("Failed to establish connection");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "❌ Database Connection Failed!\n\n" +
                "Error: " + e.getMessage() + "\n\n" +
                "Please check:\n" +
                "• MySQL server is running\n" +
                "• Database 'car_store_management' exists\n" +
                "• Username/password are correct\n" +
                "• Connection URL is correct",
                "Database Test - FAILED",
                JOptionPane.ERROR_MESSAGE);
            
            System.err.println("Database connection test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test database connection without showing dialog (returns boolean)
     */
    public static boolean testConnection() {
        try {
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            boolean connected = dbConnection.connect();
            
            if (connected) {
                Connection conn = dbConnection.getConnection();
                return conn != null && !conn.isClosed();
            }
            return false;
            
        } catch (Exception e) {
            System.err.println("Silent database test failed: " + e.getMessage());
            return false;
        }
    }
}