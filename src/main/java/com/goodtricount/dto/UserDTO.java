package com.goodtricount.dto;

import java.io.Serializable;

/**
 * Data Transfer Object for User information.
 */
public class UserDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;
    private String email;
    private String name;
    
    /**
     * Default constructor
     */
    public UserDTO() {
    }
    
    /**
     * Constructor with all fields
     * 
     * @param username the username
     * @param password the password
     * @param email the email
     * @param name the name
     */
    public UserDTO(String username, String password, String email, String name) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
    }
    
    // Getters and Setters
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}