package com.example.Supermarket.service;


import com.example.Supermarket.dto.InvoiceDTO;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for handling Invoice business logic and data aggregation.
 */
@Service
public class InvoiceService {

    // Mock data to simulate retrieval from InvoiceRepository
    private final List<InvoiceDTO> mockInvoices = Arrays.asList(
            new InvoiceDTO(1001L, 5432L, "Jane Doe", new BigDecimal("125.00"), "Paid"),
            new InvoiceDTO(1002L, 5433L, "John Smith", new BigDecimal("88.99"), "Pending"),
            new InvoiceDTO(1003L, 5434L, "Alice Brown", new BigDecimal("35.20"), "Cancelled"),
            new InvoiceDTO(1004L, 5435L, "Bob Johnson", new BigDecimal("450.00"), "Pending"),
            new InvoiceDTO(1005L, 5436L, "Chris Lee", new BigDecimal("1000.00"), "Paid"),
            new InvoiceDTO(1006L, 5437L, "David Kim", new BigDecimal("50.00"), "Pending")
    );

    // In a real app, you would autowire the repository:
    // @Autowired
    // private InvoiceRepository invoiceRepository;

    /**
     * Retrieves a list of recent invoices.
     * @return List of InvoiceDTOs.
     */
    public List<InvoiceDTO> getRecentInvoices() {
        // In a real app: return invoiceRepository.findAll().stream().map(this::convertToDTO).toList();
        return mockInvoices;
    }

    /**
     * Calculates key statistics for the dashboard cards.
     * @return A List<Object> containing {pendingAmount, paidToday, pendingCount, cancelledCount, paidCount}.
     */
    public List<Object> getInvoiceStats() {
        BigDecimal pendingAmount = mockInvoices.stream()
                .filter(i -> "Pending".equals(i.getStatus()))
                .map(InvoiceDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long pendingCount = mockInvoices.stream()
                .filter(i -> "Pending".equals(i.getStatus()))
                .count();

        long paidCount = mockInvoices.stream()
                .filter(i -> "Paid".equals(i.getStatus()))
                .count();

        long cancelledCount = mockInvoices.stream()
                .filter(i -> "Cancelled".equals(i.getStatus()))
                .count();

        // Mocked "Paid Today" amount for the card
        BigDecimal paidToday = new BigDecimal("1230.50");

        return Arrays.asList(pendingAmount, paidToday, pendingCount, cancelledCount, paidCount);
    }

    /**
     * Generates the monthly revenue data for the chart.
     * @return List of BigDecimal representing monthly revenue amounts.
     */
    public List<BigDecimal> getMonthlyRevenueData() {
        // Mock revenue data for 12 months (in thousands for variety)
        return Arrays.asList(
                new BigDecimal("12000"), new BigDecimal("19000"), new BigDecimal("30000"),
                new BigDecimal("50000"), new BigDecimal("25000"), new BigDecimal("35000"),
                new BigDecimal("42000"), new BigDecimal("48000"), new BigDecimal("55000"),
                new BigDecimal("30000"), new BigDecimal("32000"), new BigDecimal("65000")
        );
    }


}