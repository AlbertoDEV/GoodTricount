package com.goodtricount.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Data Transfer Object for Expense information.
 */
public class ExpenseDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String payer;
    private BigDecimal amount;
    private String description;
    
    /**
     * Default constructor
     */
    public ExpenseDTO() {
    }
    
    /**
     * Constructor with all fields
     * 
     * @param payer the username of the person who paid
     * @param amount the amount paid
     * @param description the description of the expense
     */
    public ExpenseDTO(String payer, BigDecimal amount, String description) {
        this.payer = payer;
        this.amount = amount;
        this.description = description;
    }
    
    // Getters and Setters
    
    public String getPayer() {
        return payer;
    }
    
    public void setPayer(String payer) {
        this.payer = payer;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return "ExpenseDTO{" +
                "payer='" + payer + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }
}