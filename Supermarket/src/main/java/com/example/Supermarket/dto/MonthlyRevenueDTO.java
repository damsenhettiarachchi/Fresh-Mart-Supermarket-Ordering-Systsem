package com.example.Supermarket.dto;

import java.time.YearMonth;


public class MonthlyRevenueDTO {
    private int year;
    private int month; // 1-12
    private double total;


    public MonthlyRevenueDTO(int year, int month, double total) {
        this.year = year;
        this.month = month;
        this.total = total;
    }


    public int getYear() { return year; }
    public int getMonth() { return month; }
    public double getTotal() { return total; }


    // Helper label for client-side if needed
    public String getMonthLabel() {
        YearMonth ym = YearMonth.of(year, month);
        return ym.getMonth().toString() + " " + year;
    }
}
