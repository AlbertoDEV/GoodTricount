package com.goodtricount.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Test class for the database connection and operations.
 */
public class DatabaseTest {
    
    public static void main(String[] args) {
        System.out.println("Starting database test...");
        
        // Get the DatabaseManager instance
        DatabaseManager dbManager = DatabaseManager.getInstance();
        
        // Test the database connection
        System.out.println("Testing database connection...");
        boolean connectionSuccessful = dbManager.testConnection();
        System.out.println("Connection successful: " + connectionSuccessful);
        
        if (!connectionSuccessful) {
            System.out.println("Database connection failed. Exiting test.");
            return;
        }
        
        // Initialize the database
        System.out.println("Initializing database...");
        boolean initSuccessful = dbManager.initializeDatabase();
        System.out.println("Database initialization successful: " + initSuccessful);
        
        if (!initSuccessful) {
            System.out.println("Database initialization failed. Exiting test.");
            return;
        }
        
        // Test user operations
        System.out.println("\nTesting user operations...");
        UserDAO userDAO = dbManager.getUserDAO();
        
        // Create a test user
        UserDTO testUser = new UserDTO("testuser", "password123", "test@example.com", "Test User");
        boolean userInserted = userDAO.insertUser(testUser);
        System.out.println("User inserted: " + userInserted);
        
        // Get the user
        UserDTO retrievedUser = userDAO.getUserByUsername("testuser");
        System.out.println("User retrieved: " + (retrievedUser != null));
        if (retrievedUser != null) {
            System.out.println("User details: " + retrievedUser);
        }
        
        // Test group operations
        System.out.println("\nTesting group operations...");
        GroupDAO groupDAO = dbManager.getGroupDAO();
        
        // Create a test group
        GroupDTO testGroup = new GroupDTO("g1", "Test Group");
        testGroup.addParticipant("testuser");
        testGroup.addAdmin("testuser");
        
        boolean groupInserted = groupDAO.insertGroup(testGroup);
        System.out.println("Group inserted: " + groupInserted);
        
        // Get the group
        GroupDTO retrievedGroup = groupDAO.getGroupById("g1");
        System.out.println("Group retrieved: " + (retrievedGroup != null));
        if (retrievedGroup != null) {
            System.out.println("Group details: " + retrievedGroup);
        }
        
        // Test expense operations
        System.out.println("\nTesting expense operations...");
        ExpenseDAO expenseDAO = dbManager.getExpenseDAO();
        
        // Create a test expense
        ExpenseDTO testExpense = new ExpenseDTO("testuser", new BigDecimal("100.00"), "Test Expense");
        int expenseId = expenseDAO.insertExpense("g1", testExpense);
        System.out.println("Expense inserted with ID: " + expenseId);
        
        // Get the expense
        ExpenseDTO retrievedExpense = expenseDAO.getExpenseById(expenseId);
        System.out.println("Expense retrieved: " + (retrievedExpense != null));
        if (retrievedExpense != null) {
            System.out.println("Expense details: " + retrievedExpense);
        }
        
        // Test payment operations
        System.out.println("\nTesting payment operations...");
        PaymentDAO paymentDAO = dbManager.getPaymentDAO();
        
        // Create a test payment
        PaymentDTO testPayment = new PaymentDTO(
            "testuser",
            "testuser",
            new BigDecimal("50.00"),
            "pending",
            LocalDateTime.now()
        );
        
        int paymentId = paymentDAO.insertPayment("g1", testPayment);
        System.out.println("Payment inserted with ID: " + paymentId);
        
        // Get the payment
        PaymentDTO retrievedPayment = paymentDAO.getPaymentById(paymentId);
        System.out.println("Payment retrieved: " + (retrievedPayment != null));
        if (retrievedPayment != null) {
            System.out.println("Payment details: " + retrievedPayment);
        }
        
        // Clean up (optional)
        System.out.println("\nCleaning up...");
        
        // Delete the payment
        boolean paymentDeleted = paymentDAO.deletePayment(paymentId);
        System.out.println("Payment deleted: " + paymentDeleted);
        
        // Delete the expense
        boolean expenseDeleted = expenseDAO.deleteExpense(expenseId);
        System.out.println("Expense deleted: " + expenseDeleted);
        
        // Delete the group
        boolean groupDeleted = groupDAO.deleteGroup("g1");
        System.out.println("Group deleted: " + groupDeleted);
        
        // Delete the user
        boolean userDeleted = userDAO.deleteUser("testuser");
        System.out.println("User deleted: " + userDeleted);
        
        // Close the database connection
        System.out.println("\nClosing database connection...");
        dbManager.closeConnection();
        
        System.out.println("Database test completed.");
    }
}