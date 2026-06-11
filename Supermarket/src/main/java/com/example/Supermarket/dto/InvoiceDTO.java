package com.example.Supermarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for Invoice information.
 * This object is used to transfer data between the service and the web layer (Thymeleaf).
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InvoiceDTO {
    private Long id;
    private Long orderId;
    private String customerName;
    private BigDecimal amount;
    private String status; // e.g., "Paid", "Pending", "Cancelled"


}