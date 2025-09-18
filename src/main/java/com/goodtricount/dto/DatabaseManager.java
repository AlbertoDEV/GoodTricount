package com.goodtricount.dto;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database manager for the GoodTricount application.
 * Provides a unified interface for all database operations.
 */
public class DatabaseManager {
    
    private static DatabaseManager instance;
    
    private UserDAO userDAO;
    private GroupDAO groupDAO;
    private ExpenseDAO expenseDAO;
    private PaymentDAO paymentDAO;
    
    /**
     * Private constructor to enforce singleton pattern.
     */
    private DatabaseManager() {
        userDAO = new UserDAO();
        groupDAO = new GroupDAO();
        expenseDAO = new ExpenseDAO();
        paymentDAO = new PaymentDAO();
    }
    
    /**
     * Get the singleton instance of the DatabaseManager.
     * 
     * @return the DatabaseManager instance
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    /**
     * Initialize the database by creating all necessary tables.
     * 
     * @return true if the database was initialized successfully, false otherwise
     */
    public boolean initializeDatabase() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create Users table
            stmt.execute("CREATE TABLE IF NOT EXISTS Users (" +
                         "username VARCHAR(50) PRIMARY KEY, " +
                         "password VARCHAR(255) NOT NULL, " +
                         "email VARCHAR(100) NOT NULL UNIQUE, " +
                         "name VARCHAR(100)" +
                         ")");
            
            // Create Groups table
            stmt.execute("CREATE TABLE IF NOT EXISTS Groups (" +
                         "id VARCHAR(50) PRIMARY KEY, " +
                         "name VARCHAR(100) NOT NULL" +
                         ")");
            
            // Create GroupParticipants table
            stmt.execute("CREATE TABLE IF NOT EXISTS GroupParticipants (" +
                         "group_id VARCHAR(50) NOT NULL, " +
                         "user_id VARCHAR(50) NOT NULL, " +
                         "PRIMARY KEY (group_id, user_id), " +
                         "FOREIGN KEY (group_id) REFERENCES Groups(id) ON DELETE CASCADE, " +
                         "FOREIGN KEY (user_id) REFERENCES Users(username) ON DELETE CASCADE" +
                         ")");
            
            // Create GroupAdmins table
            stmt.execute("CREATE TABLE IF NOT EXISTS GroupAdmins (" +
                         "group_id VARCHAR(50) NOT NULL, " +
                         "admin_id VARCHAR(50) NOT NULL, " +
                         "PRIMARY KEY (group_id, admin_id), " +
                         "FOREIGN KEY (group_id) REFERENCES Groups(id) ON DELETE CASCADE, " +
                         "FOREIGN KEY (admin_id) REFERENCES Users(username) ON DELETE CASCADE" +
                         ")");
            
            // Create Expenses table
            stmt.execute("CREATE TABLE IF NOT EXISTS Expenses (" +
                         "expense_id SERIAL PRIMARY KEY, " +
                         "group_id VARCHAR(50) NOT NULL, " +
                         "payer VARCHAR(50) NOT NULL, " +
                         "amount NUMERIC(10, 2) NOT NULL, " +
                         "description VARCHAR(255), " +
                         "FOREIGN KEY (group_id) REFERENCES Groups(id) ON DELETE CASCADE, " +
                         "FOREIGN KEY (payer) REFERENCES Users(username)" +
                         ")");
            
            // Create Payments table
            stmt.execute("CREATE TABLE IF NOT EXISTS Payments (" +
                         "payment_id SERIAL PRIMARY KEY, " +
                         "group_id VARCHAR(50) NOT NULL, " +
                         "payer VARCHAR(50) NOT NULL, " +
                         "receiver VARCHAR(50) NOT NULL, " +
                         "amount NUMERIC(10, 2) NOT NULL, " +
                         "status VARCHAR(10) NOT NULL CHECK (status IN ('pending', 'confirmed')), " +
                         "timestamp TIMESTAMP NOT NULL, " +
                         "confirmedTimestamp TIMESTAMP, " +
                         "FOREIGN KEY (group_id) REFERENCES Groups(id) ON DELETE CASCADE, " +
                         "FOREIGN KEY (payer) REFERENCES Users(username), " +
                         "FOREIGN KEY (receiver) REFERENCES Users(username)" +
                         ")");
            
            return true;
            
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get the UserDAO instance.
     * 
     * @return the UserDAO instance
     */
    public UserDAO getUserDAO() {
        return userDAO;
    }
    
    /**
     * Get the GroupDAO instance.
     * 
     * @return the GroupDAO instance
     */
    public GroupDAO getGroupDAO() {
        return groupDAO;
    }
    
    /**
     * Get the ExpenseDAO instance.
     * 
     * @return the ExpenseDAO instance
     */
    public ExpenseDAO getExpenseDAO() {
        return expenseDAO;
    }
    
    /**
     * Get the PaymentDAO instance.
     * 
     * @return the PaymentDAO instance
     */
    public PaymentDAO getPaymentDAO() {
        return paymentDAO;
    }
    
    /**
     * Close the database connection.
     */
    public void closeConnection() {
        try {
            DatabaseConnection.closeConnection();
        } catch (SQLException e) {
            System.out.println("Error closing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test the database connection.
     * 
     * @return true if the connection is successful, false otherwise
     */
    public boolean testConnection() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.out.println("Error testing database connection: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}