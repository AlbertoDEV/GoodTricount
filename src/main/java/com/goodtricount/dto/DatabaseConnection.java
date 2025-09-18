package com.goodtricount.dto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection manager for GoodTricount application.
 * Handles connections to the PostgreSQL database.
 */
public class DatabaseConnection {
    
    // The database connection URL in the required format
    private static final String DATABASE_URL = "${{ Postgres.DATABASE_URL }}";
    
    // The actual database connection URL
    private static final String ACTUAL_URL = "jdbc:postgresql://postgres:GchuyqDtKfFufefxFnYGEGmIEbSJPryY@maglev.proxy.rlwy.net:39661/railway";
    
    private static Connection connection;
    
    /**
     * Get a connection to the database.
     * If a connection already exists, returns the existing connection.
     * Otherwise, creates a new connection.
     * 
     * @return the database connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load the PostgreSQL JDBC driver
                Class.forName("org.postgresql.Driver");
                
                // Create a connection to the database
                // Note: We're using ACTUAL_URL here, but in a real environment,
                // DATABASE_URL would be replaced with the actual URL by the deployment system
                connection = DriverManager.getConnection(ACTUAL_URL);
                
                System.out.println("Connected to the PostgreSQL database!");
            } catch (ClassNotFoundException e) {
                System.out.println("PostgreSQL JDBC driver not found!");
                e.printStackTrace();
                throw new SQLException("PostgreSQL JDBC driver not found!", e);
            }
        }
        
        return connection;
    }
    
    /**
     * Close the database connection.
     * 
     * @throws SQLException if a database access error occurs
     */
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Database connection closed.");
        }
    }
}