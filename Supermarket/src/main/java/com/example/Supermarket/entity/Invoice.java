package com.example.Supermarket.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;


import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * Entity representing the Invoice table in the database.
 * (Simplified for demonstration, using standard JPA annotations).
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Customer name: must be a non-empty String
    @NotBlank(message = "Customer name is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Customer name can only contain letters and spaces")
    private String customerName;

    // Order ID: must contain at least one letter (not purely numeric)
    @NotBlank(message = "Order ID is required")
    @Pattern(regexp = "^(?!\\d+$)[A-Za-z0-9 ]+$", message = "The Order ID is invalid. It must contain at least one letter.")
    private String orderId;


    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;


    private LocalDate date;


    @Pattern(regexp = "Pending|Paid|Cancelled",
            message = "Status must be Pending, Paid, or Cancelled")
    private String status;
}

// getters and setters...

    // Default constructor for JPA
