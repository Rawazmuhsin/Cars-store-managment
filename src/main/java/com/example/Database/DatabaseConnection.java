package com.example.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DatabaseConnection class handles connections to the MySQL database for the Car Store Management system.
 * It provides methods for connecting to the database, executing queries, and managing resources.
 */
public class DatabaseConnection {
    // Database connection parameters - defined directly for now
    private static final String DB_URL = "jdbc:mysql://localhost:3306/car_store_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Makwan2004111";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());
    
    private Connection connection;
    private static DatabaseConnection instance;
    
    /**
     * Private constructor to prevent direct instantiation.
     * Use getInstance() method to get the singleton instance.
     */
    private DatabaseConnection() {
        try {
            // Load MySQL JDBC driver
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found", e);
        }
    }
    
    /**
     * Get the singleton instance of DatabaseConnection.
     * @return DatabaseConnection instance
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Connect to the database.
     * @return true if connection successful, false otherwise
     */
    public boolean connect() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                LOGGER.info("Database connection established");
            }
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection failed: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Get the current connection object. Connects if not already connected.
     * @return Connection object
     * @throws SQLException if connection cannot be established
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
        return connection;
    }
    
    /**
     * Close the database connection.
     */
    public void disconnect() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    LOGGER.info("Database connection closed");
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing database connection: " + e.getMessage(), e);
            }
        }
    }
    
    /**
     * Execute a SELECT query and return the ResultSet.
     * @param query SQL query string
     * @return ResultSet containing query results
     * @throws SQLException if query execution fails
     */
    public ResultSet executeQuery(String query) throws SQLException {
        Statement statement = getConnection().createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        return statement.executeQuery(query);
    }
    
    /**
     * Execute a SELECT query with prepared statement to prevent SQL injection.
     * @param query SQL query with placeholders
     * @param params Parameters to replace placeholders in the query
     * @return ResultSet containing query results
     * @throws SQLException if query execution fails
     */
    public ResultSet executeQuery(String query, Object... params) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement(
                query,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
        
        return statement.executeQuery();
    }
    
    /**
     * Execute an INSERT, UPDATE, or DELETE query.
     * @param query SQL query string
     * @return number of rows affected
     * @throws SQLException if query execution fails
     */
    public int executeUpdate(String query) throws SQLException {
        try (Statement statement = getConnection().createStatement()) {
            return statement.executeUpdate(query);
        }
    }
    
    /**
     * Execute an INSERT, UPDATE, or DELETE query with prepared statement.
     * @param query SQL query with placeholders
     * @param params Parameters to replace placeholders in the query
     * @return number of rows affected
     * @throws SQLException if query execution fails
     */
    public int executeUpdate(String query, Object... params) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            
            return statement.executeUpdate();
        }
    }
    
    /**
     * Execute an INSERT query and return the generated key.
     * @param query SQL query with placeholders
     * @param params Parameters to replace placeholders in the query
     * @return Generated key (usually the ID of the inserted row)
     * @throws SQLException if query execution fails
     */
    public int executeInsert(String query, Object... params) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            
            statement.executeUpdate();
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    return -1; // No ID was generated
                }
            }
        }
    }
    
    /**
     * Close a ResultSet and its associated Statement.
     * @param resultSet ResultSet to close
     */
    public void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                Statement statement = resultSet.getStatement();
                resultSet.close();
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing ResultSet: " + e.getMessage(), e);
            }
        }
    }
    
    /**
     * Begin a transaction by turning off auto-commit.
     * @throws SQLException if setting auto-commit fails
     */
    public void beginTransaction() throws SQLException {
        getConnection().setAutoCommit(false);
    }
    
    /**
     * Commit a transaction.
     * @throws SQLException if commit fails
     */
    public void commitTransaction() throws SQLException {
        getConnection().commit();
        getConnection().setAutoCommit(true);
    }
    
    /**
     * Rollback a transaction.
     * @throws SQLException if rollback fails
     */
    public void rollbackTransaction() throws SQLException {
        getConnection().rollback();
        getConnection().setAutoCommit(true);
    }
    
    /**
     * Check if a table exists in the database.
     * @param tableName Name of the table to check
     * @return true if table exists, false otherwise
     */
    public boolean tableExists(String tableName) {
        try (ResultSet resultSet = getConnection().getMetaData().getTables(
                null, null, tableName, new String[] {"TABLE"})) {
            return resultSet.next();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error checking if table exists: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Create a test connection to verify database connectivity.
     * @return true if test successful, false otherwise
     */
    public static boolean testConnection() {
        DatabaseConnection db = getInstance();
        try {
            db.connect();
            LOGGER.info("Database connection test successful");
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Database connection test failed: " + e.getMessage(), e);
            return false;
        } finally {
            db.disconnect();
        }
    }
    
    /**
     * Execute a custom SQL query with result handling in a single transaction.
     * This method ensures resources are properly closed using try-with-resources.
     * 
     * @param <T> The type of result to return
     * @param query SQL query to execute
     * @param handler ResultSetHandler implementation to process the result
     * @return Result processed by the handler
     * @throws SQLException if a database access error occurs
     */
    public <T> T executeQuery(String query, ResultSetHandler<T> handler) throws SQLException {
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return handler.handle(rs);
        }
    }
    
    /**
     * Functional interface for handling ResultSet processing
     */
    @FunctionalInterface
    public interface ResultSetHandler<T> {
        T handle(ResultSet rs) throws SQLException;
    }
}