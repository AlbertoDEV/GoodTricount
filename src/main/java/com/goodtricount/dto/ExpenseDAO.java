package com.goodtricount.dto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Expense entities.
 * Handles database operations for expenses.
 */
public class ExpenseDAO {
    
    /**
     * Insert a new expense into the database.
     * 
     * @param groupId the ID of the group the expense belongs to
     * @param expense the expense to insert
     * @return the ID of the inserted expense, or -1 if the insertion failed
     */
    public int insertExpense(String groupId, ExpenseDTO expense) {
        String sql = "INSERT INTO Expenses (group_id, payer, amount, description) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, groupId);
            pstmt.setString(2, expense.getPayer());
            pstmt.setBigDecimal(3, expense.getAmount());
            pstmt.setString(4, expense.getDescription());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error inserting expense: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Get an expense by ID.
     * 
     * @param expenseId the ID of the expense to get
     * @return the expense, or null if not found
     */
    public ExpenseDTO getExpenseById(int expenseId) {
        String sql = "SELECT * FROM Expenses WHERE expense_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, expenseId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ExpenseDTO expense = new ExpenseDTO();
                    expense.setPayer(rs.getString("payer"));
                    expense.setAmount(rs.getBigDecimal("amount"));
                    expense.setDescription(rs.getString("description"));
                    return expense;
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting expense: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all expenses for a group.
     * 
     * @param groupId the ID of the group
     * @return a list of expenses for the group
     */
    public List<ExpenseDTO> getExpensesForGroup(String groupId) {
        List<ExpenseDTO> expenses = new ArrayList<>();
        String sql = "SELECT * FROM Expenses WHERE group_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, groupId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ExpenseDTO expense = new ExpenseDTO();
                    expense.setPayer(rs.getString("payer"));
                    expense.setAmount(rs.getBigDecimal("amount"));
                    expense.setDescription(rs.getString("description"));
                    expenses.add(expense);
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting expenses for group: " + e.getMessage());
            e.printStackTrace();
        }
        
        return expenses;
    }
    
    /**
     * Update an expense in the database.
     * 
     * @param expenseId the ID of the expense to update
     * @param expense the updated expense
     * @return true if the expense was updated successfully, false otherwise
     */
    public boolean updateExpense(int expenseId, ExpenseDTO expense) {
        String sql = "UPDATE Expenses SET payer = ?, amount = ?, description = ? WHERE expense_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, expense.getPayer());
            pstmt.setBigDecimal(2, expense.getAmount());
            pstmt.setString(3, expense.getDescription());
            pstmt.setInt(4, expenseId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error updating expense: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete an expense from the database.
     * 
     * @param expenseId the ID of the expense to delete
     * @return true if the expense was deleted successfully, false otherwise
     */
    public boolean deleteExpense(int expenseId) {
        String sql = "DELETE FROM Expenses WHERE expense_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, expenseId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error deleting expense: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get the total amount of expenses for a group.
     * 
     * @param groupId the ID of the group
     * @return the total amount of expenses
     */
    public BigDecimal getTotalExpensesForGroup(String groupId) {
        String sql = "SELECT SUM(amount) FROM Expenses WHERE group_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, groupId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal(1);
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting total expenses: " + e.getMessage());
            e.printStackTrace();
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Get the total amount of expenses paid by a user in a group.
     * 
     * @param groupId the ID of the group
     * @param username the username of the user
     * @return the total amount of expenses paid by the user
     */
    public BigDecimal getTotalExpensesPaidByUser(String groupId, String username) {
        String sql = "SELECT SUM(amount) FROM Expenses WHERE group_id = ? AND payer = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, groupId);
            pstmt.setString(2, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal result = rs.getBigDecimal(1);
                    return result != null ? result : BigDecimal.ZERO;
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting total expenses paid by user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return BigDecimal.ZERO;
    }
}