package com.goodtricount.dto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Payment entities.
 * Handles database operations for payments.
 */
public class PaymentDAO {
    
    /**
     * Insert a new payment into the database.
     * 
     * @param groupId the ID of the group the payment belongs to
     * @param payment the payment to insert
     * @return the ID of the inserted payment, or -1 if the insertion failed
     */
    public int insertPayment(String groupId, PaymentDTO payment) {
        String sql = "INSERT INTO Payments (group_id, payer, receiver, amount, status, timestamp, confirmedTimestamp) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, groupId);
            pstmt.setString(2, payment.getPayer());
            pstmt.setString(3, payment.getReceiver());
            pstmt.setBigDecimal(4, payment.getAmount());
            pstmt.setString(5, payment.getStatus());
            pstmt.setTimestamp(6, Timestamp.valueOf(payment.getTimestamp()));
            
            if (payment.getConfirmedTimestamp() != null) {
                pstmt.setTimestamp(7, Timestamp.valueOf(payment.getConfirmedTimestamp()));
            } else {
                pstmt.setNull(7, java.sql.Types.TIMESTAMP);
            }
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error inserting payment: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Get a payment by ID.
     * 
     * @param paymentId the ID of the payment to get
     * @return the payment, or null if not found
     */
    public PaymentDTO getPaymentById(int paymentId) {
        String sql = "SELECT * FROM Payments WHERE payment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, paymentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    PaymentDTO payment = new PaymentDTO();
                    payment.setPayer(rs.getString("payer"));
                    payment.setReceiver(rs.getString("receiver"));
                    payment.setAmount(rs.getBigDecimal("amount"));
                    payment.setStatus(rs.getString("status"));
                    payment.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                    
                    Timestamp confirmedTimestamp = rs.getTimestamp("confirmedTimestamp");
                    if (confirmedTimestamp != null) {
                        payment.setConfirmedTimestamp(confirmedTimestamp.toLocalDateTime());
                    }
                    
                    return payment;
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting payment: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all payments for a group.
     * 
     * @param groupId the ID of the group
     * @return a list of payments for the group
     */
    public List<PaymentDTO> getPaymentsForGroup(String groupId) {
        List<PaymentDTO> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payments WHERE group_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, groupId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PaymentDTO payment = new PaymentDTO();
                    payment.setPayer(rs.getString("payer"));
                    payment.setReceiver(rs.getString("receiver"));
                    payment.setAmount(rs.getBigDecimal("amount"));
                    payment.setStatus(rs.getString("status"));
                    payment.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                    
                    Timestamp confirmedTimestamp = rs.getTimestamp("confirmedTimestamp");
                    if (confirmedTimestamp != null) {
                        payment.setConfirmedTimestamp(confirmedTimestamp.toLocalDateTime());
                    }
                    
                    payments.add(payment);
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting payments for group: " + e.getMessage());
            e.printStackTrace();
        }
        
        return payments;
    }
    
    /**
     * Get payments made by a user in a group.
     * 
     * @param groupId the ID of the group
     * @param username the username of the user
     * @return a list of payments made by the user
     */
    public List<PaymentDTO> getPaymentsMadeByUser(String groupId, String username) {
        List<PaymentDTO> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payments WHERE group_id = ? AND payer = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, groupId);
            pstmt.setString(2, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PaymentDTO payment = new PaymentDTO();
                    payment.setPayer(rs.getString("payer"));
                    payment.setReceiver(rs.getString("receiver"));
                    payment.setAmount(rs.getBigDecimal("amount"));
                    payment.setStatus(rs.getString("status"));
                    payment.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                    
                    Timestamp confirmedTimestamp = rs.getTimestamp("confirmedTimestamp");
                    if (confirmedTimestamp != null) {
                        payment.setConfirmedTimestamp(confirmedTimestamp.toLocalDateTime());
                    }
                    
                    payments.add(payment);
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting payments made by user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return payments;
    }
    
    /**
     * Get payments received by a user in a group.
     * 
     * @param groupId the ID of the group
     * @param username the username of the user
     * @return a list of payments received by the user
     */
    public List<PaymentDTO> getPaymentsReceivedByUser(String groupId, String username) {
        List<PaymentDTO> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payments WHERE group_id = ? AND receiver = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, groupId);
            pstmt.setString(2, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PaymentDTO payment = new PaymentDTO();
                    payment.setPayer(rs.getString("payer"));
                    payment.setReceiver(rs.getString("receiver"));
                    payment.setAmount(rs.getBigDecimal("amount"));
                    payment.setStatus(rs.getString("status"));
                    payment.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                    
                    Timestamp confirmedTimestamp = rs.getTimestamp("confirmedTimestamp");
                    if (confirmedTimestamp != null) {
                        payment.setConfirmedTimestamp(confirmedTimestamp.toLocalDateTime());
                    }
                    
                    payments.add(payment);
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting payments received by user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return payments;
    }
    
    /**
     * Update a payment in the database.
     * 
     * @param paymentId the ID of the payment to update
     * @param payment the updated payment
     * @return true if the payment was updated successfully, false otherwise
     */
    public boolean updatePayment(int paymentId, PaymentDTO payment) {
        String sql = "UPDATE Payments SET payer = ?, receiver = ?, amount = ?, status = ?, timestamp = ?, confirmedTimestamp = ? " +
                     "WHERE payment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, payment.getPayer());
            pstmt.setString(2, payment.getReceiver());
            pstmt.setBigDecimal(3, payment.getAmount());
            pstmt.setString(4, payment.getStatus());
            pstmt.setTimestamp(5, Timestamp.valueOf(payment.getTimestamp()));
            
            if (payment.getConfirmedTimestamp() != null) {
                pstmt.setTimestamp(6, Timestamp.valueOf(payment.getConfirmedTimestamp()));
            } else {
                pstmt.setNull(6, java.sql.Types.TIMESTAMP);
            }
            
            pstmt.setInt(7, paymentId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error updating payment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Confirm a payment in the database.
     * 
     * @param paymentId the ID of the payment to confirm
     * @return true if the payment was confirmed successfully, false otherwise
     */
    public boolean confirmPayment(int paymentId) {
        String sql = "UPDATE Payments SET status = 'confirmed', confirmedTimestamp = ? WHERE payment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(2, paymentId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error confirming payment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete a payment from the database.
     * 
     * @param paymentId the ID of the payment to delete
     * @return true if the payment was deleted successfully, false otherwise
     */
    public boolean deletePayment(int paymentId) {
        String sql = "DELETE FROM Payments WHERE payment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, paymentId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error deleting payment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get the total amount of payments made by a user in a group.
     * 
     * @param groupId the ID of the group
     * @param username the username of the user
     * @return the total amount of payments made by the user
     */
    public BigDecimal getTotalPaymentsMadeByUser(String groupId, String username) {
        String sql = "SELECT SUM(amount) FROM Payments WHERE group_id = ? AND payer = ? AND status = 'confirmed'";
        
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
            System.out.println("Error getting total payments made by user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Get the total amount of payments received by a user in a group.
     * 
     * @param groupId the ID of the group
     * @param username the username of the user
     * @return the total amount of payments received by the user
     */
    public BigDecimal getTotalPaymentsReceivedByUser(String groupId, String username) {
        String sql = "SELECT SUM(amount) FROM Payments WHERE group_id = ? AND receiver = ? AND status = 'confirmed'";
        
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
            System.out.println("Error getting total payments received by user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return BigDecimal.ZERO;
    }
}