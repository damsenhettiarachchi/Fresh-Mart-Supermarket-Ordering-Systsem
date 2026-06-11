package com.example.Supermarket.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DailyRevenueDTO {
    private LocalDate date;
    private double total;

    public DailyRevenueDTO(LocalDate date, double total) {
        this.date = date;
        this.total = total;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getTotal() {
        return total;
    }

    // Helper method for formatted date label
    public String getDateLabel() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");
        return date.format(formatter);
    }
}
