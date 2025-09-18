package com.goodtricount.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Example class demonstrating the use of the DTO classes.
 */
public class DTOExample {
    
    public static void main(String[] args) {
        // Create users
        UserDTO user1 = new UserDTO("pepe", "123", "pepe@mail.com", "Pepe");
        UserDTO user2 = new UserDTO("juan", "123", "juan@mail.com", "Juan");
        UserDTO user3 = new UserDTO("ana", "123", "ana@mail.com", "Ana");
        
        System.out.println("Created users:");
        System.out.println(user1);
        System.out.println(user2);
        System.out.println(user3);
        
        // Create a group
        GroupDTO group = new GroupDTO("g1", "Viaje a Roma");
        
        // Add participants
        group.addParticipant(user1.getUsername());
        group.addParticipant(user2.getUsername());
        group.addParticipant(user3.getUsername());
        
        // Add admin
        group.addAdmin(user1.getUsername());
        
        System.out.println("\nCreated group with participants and admin:");
        System.out.println(group);
        
        // Add expenses
        ExpenseDTO expense1 = new ExpenseDTO(user1.getUsername(), new BigDecimal("150"), "Hotel");
        ExpenseDTO expense2 = new ExpenseDTO(user2.getUsername(), new BigDecimal("50"), "Cena");
        
        group.addExpense(expense1);
        group.addExpense(expense2);
        
        System.out.println("\nGroup after adding expenses:");
        System.out.println(group);
        
        // Add a payment
        PaymentDTO payment = new PaymentDTO(
            user2.getUsername(),
            user1.getUsername(),
            new BigDecimal("50"),
            "pending",
            LocalDateTime.now()
        );
        
        group.addPayment(payment);
        
        System.out.println("\nGroup after adding a payment:");
        System.out.println(group);
        
        // Confirm the payment
        payment.setStatus("confirmed");
        payment.setConfirmedTimestamp(LocalDateTime.now());
        
        System.out.println("\nGroup after confirming the payment:");
        System.out.println(group);
        
        // Create a group with all fields at once
        GroupDTO anotherGroup = new GroupDTO(
            "g2",
            "Grupo de Ejemplo",
            Arrays.asList("Prueba 1", "Prueba 2", "Prueba 3"),
            Arrays.asList("Prueba 1"),
            Arrays.asList(
                new ExpenseDTO("Prueba 1", new BigDecimal("100"), "Pintura"),
                new ExpenseDTO("Prueba 2", new BigDecimal("50"), "Cinta")
            ),
            Arrays.asList()
        );
        
        System.out.println("\nAnother group created with all fields at once:");
        System.out.println(anotherGroup);
    }
}