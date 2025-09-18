package com.goodtricount.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for Group information.
 */
public class GroupDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String name;
    private List<String> participants;
    private List<String> admins;
    private List<ExpenseDTO> expenses;
    private List<PaymentDTO> payments;
    
    /**
     * Default constructor
     */
    public GroupDTO() {
        this.participants = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.payments = new ArrayList<>();
    }
    
    /**
     * Constructor with essential fields
     * 
     * @param id the group ID
     * @param name the group name
     */
    public GroupDTO(String id, String name) {
        this();
        this.id = id;
        this.name = name;
    }
    
    /**
     * Constructor with all fields
     * 
     * @param id the group ID
     * @param name the group name
     * @param participants the list of participant usernames
     * @param admins the list of admin usernames
     * @param expenses the list of expenses
     * @param payments the list of payments
     */
    public GroupDTO(String id, String name, List<String> participants, List<String> admins, 
                   List<ExpenseDTO> expenses, List<PaymentDTO> payments) {
        this.id = id;
        this.name = name;
        this.participants = participants != null ? participants : new ArrayList<>();
        this.admins = admins != null ? admins : new ArrayList<>();
        this.expenses = expenses != null ? expenses : new ArrayList<>();
        this.payments = payments != null ? payments : new ArrayList<>();
    }
    
    // Getters and Setters
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<String> getParticipants() {
        return participants;
    }
    
    public void setParticipants(List<String> participants) {
        this.participants = participants != null ? participants : new ArrayList<>();
    }
    
    public List<String> getAdmins() {
        return admins;
    }
    
    public void setAdmins(List<String> admins) {
        this.admins = admins != null ? admins : new ArrayList<>();
    }
    
    public List<ExpenseDTO> getExpenses() {
        return expenses;
    }
    
    public void setExpenses(List<ExpenseDTO> expenses) {
        this.expenses = expenses != null ? expenses : new ArrayList<>();
    }
    
    public List<PaymentDTO> getPayments() {
        return payments;
    }
    
    public void setPayments(List<PaymentDTO> payments) {
        this.payments = payments != null ? payments : new ArrayList<>();
    }
    
    /**
     * Add a participant to the group
     * 
     * @param username the username to add
     * @return true if the participant was added, false if already present
     */
    public boolean addParticipant(String username) {
        if (!participants.contains(username)) {
            return participants.add(username);
        }
        return false;
    }
    
    /**
     * Add an admin to the group
     * 
     * @param username the username to add as admin
     * @return true if the admin was added, false if already present
     */
    public boolean addAdmin(String username) {
        if (!admins.contains(username)) {
            // Ensure the user is also a participant
            if (!participants.contains(username)) {
                participants.add(username);
            }
            return admins.add(username);
        }
        return false;
    }
    
    /**
     * Add an expense to the group
     * 
     * @param expense the expense to add
     * @return true if the expense was added
     */
    public boolean addExpense(ExpenseDTO expense) {
        if (expense != null) {
            return expenses.add(expense);
        }
        return false;
    }
    
    /**
     * Add a payment to the group
     * 
     * @param payment the payment to add
     * @return true if the payment was added
     */
    public boolean addPayment(PaymentDTO payment) {
        if (payment != null) {
            return payments.add(payment);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "GroupDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", participants=" + participants +
                ", admins=" + admins +
                ", expenses=" + expenses +
                ", payments=" + payments +
                '}';
    }
}