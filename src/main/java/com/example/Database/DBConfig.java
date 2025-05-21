package com.example.Database;

/**
 * Configuration class for database connection settings.
 * This class centralizes database connection parameters to make them easier to change.
 */
public class DBConfig {
    // Database driver class name
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Database connection URL
    public static final String DB_URL = "jdbc:mysql://localhost:3306/car_store_management";
    
    // Database username
    public static final String DB_USER = "root";
    
    // Database password
    public static final String DB_PASSWORD = "Makwan2004111"; // Set your password here if needed
    
    // Connection pool settings
    public static final int MAX_POOL_SIZE = 10;
    public static final int MIN_POOL_SIZE = 2;
    public static final int MAX_IDLE_TIME = 300;
    
    // Query timeout settings (in seconds)
    public static final int QUERY_TIMEOUT = 30;
}