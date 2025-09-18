package com.goodtricount.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Payment information.
 */
public class PaymentDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String payer;
    private String receiver;
    private BigDecimal amount;
    private String status; // "pending" or "confirmed"
    private LocalDateTime timestamp;
    private LocalDateTime confirmedTimestamp;
    
    /**
     * Default constructor
     */
    public PaymentDTO() {
    }
    
    /**
     * Constructor with essential fields
     * 
     * @param payer the username of the person who pays
     * @param receiver the username of the person who receives
     * @param amount the amount paid
     * @param status the status of the payment
     * @param timestamp the timestamp when the payment was created
     */
    public PaymentDTO(String payer, String receiver, BigDecimal amount, String status, LocalDateTime timestamp) {
        this.payer = payer;
        this.receiver = receiver;
        this.amount = amount;
        this.status = status;
        this.timestamp = timestamp;
    }
    
    /**
     * Constructor with all fields
     * 
     * @param payer the username of the person who pays
     * @param receiver the username of the person who receives
     * @param amount the amount paid
     * @param status the status of the payment
     * @param timestamp the timestamp when the payment was created
     * @param confirmedTimestamp the timestamp when the payment was confirmed
     */
    public PaymentDTO(String payer, String receiver, BigDecimal amount, String status, 
                     LocalDateTime timestamp, LocalDateTime confirmedTimestamp) {
        this.payer = payer;
        this.receiver = receiver;
        this.amount = amount;
        this.status = status;
        this.timestamp = timestamp;
        this.confirmedTimestamp = confirmedTimestamp;
    }
    
    // Getters and Setters
    
    public String getPayer() {
        return payer;
    }
    
    public void setPayer(String payer) {
        this.payer = payer;
    }
    
    public String getReceiver() {
        return receiver;
    }
    
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public LocalDateTime getConfirmedTimestamp() {
        return confirmedTimestamp;
    }
    
    public void setConfirmedTimestamp(LocalDateTime confirmedTimestamp) {
        this.confirmedTimestamp = confirmedTimestamp;
    }
    
    @Override
    public String toString() {
        return "PaymentDTO{" +
                "payer='" + payer + '\'' +
                ", receiver='" + receiver + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                ", confirmedTimestamp=" + confirmedTimestamp +
                '}';
    }
}