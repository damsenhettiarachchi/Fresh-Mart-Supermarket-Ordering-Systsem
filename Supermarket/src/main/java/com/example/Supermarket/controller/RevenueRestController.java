package com.example.Supermarket.controller;

import com.example.Supermarket.dto.DailyRevenueDTO;
import com.example.Supermarket.dto.MonthlyRevenueDTO;
import com.example.Supermarket.service.RevenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/revenue")
public class RevenueRestController {


    private final RevenueService revenueService;


    public RevenueRestController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }


    @GetMapping("/monthly")
    public ResponseEntity<List<MonthlyRevenueDTO>> monthlyRevenue(@RequestParam(defaultValue = "12") int months) {
        List<MonthlyRevenueDTO> list = revenueService.getLastNMonthsRevenue(months);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/daily")
    public ResponseEntity<List<DailyRevenueDTO>> dailyRevenue(@RequestParam(defaultValue = "7") int days) {
        List<DailyRevenueDTO> list = revenueService.getLastNDaysRevenue(days);
        return ResponseEntity.ok(list);
    }
}
