package com.example.Supermarket.service;

import com.example.Supermarket.dto.DailyRevenueDTO;
import com.example.Supermarket.dto.MonthlyRevenueDTO;
import com.example.Supermarket.repository.InvoiceRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class RevenueService {


    private final InvoiceRepository invoiceRepository;


    public RevenueService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }


    /**
     * Returns a list of MonthlyRevenueDTO for the last `months` months (including current month),
     * in chronological order (oldest -> newest).
     */
    public List<MonthlyRevenueDTO> getLastNMonthsRevenue(int months) {
        LocalDate now = LocalDate.now();
        YearMonth startYm = YearMonth.from(now).minusMonths(months - 1);
        LocalDate fromDate = startYm.atDay(1);


        List<Object[]> rows = invoiceRepository.findMonthlyRevenueSince(fromDate);


// Map year-month -> total
        Map<YearMonth, Double> totals = new HashMap<>();
        for (Object[] r : rows) {
// Expecting [year (BigInt/Integer), month (Integer), total (BigDecimal/Double)]
            int year = ((Number) r[0]).intValue();
            int month = ((Number) r[1]).intValue();
            double total = ((Number) r[2]).doubleValue();
            totals.put(YearMonth.of(year, month), total);
        }


        List<MonthlyRevenueDTO> result = new ArrayList<>();
        YearMonth iter = startYm;
        for (int i = 0; i < months; i++) {
            double total = totals.getOrDefault(iter, 0.0);
            result.add(new MonthlyRevenueDTO(iter.getYear(), iter.getMonthValue(), total));
            iter = iter.plusMonths(1);
        }


        return result;
    }

    /**
     * Returns a list of DailyRevenueDTO for the last `days` days (including today),
     * in chronological order (oldest -> newest).
     */
    public List<DailyRevenueDTO> getLastNDaysRevenue(int days) {
        LocalDate now = LocalDate.now();
        LocalDate fromDate = now.minusDays(days - 1);

        List<Object[]> rows = invoiceRepository.findDailyRevenueSince(fromDate);

        // Map date -> total
        Map<LocalDate, Double> totals = new HashMap<>();
        for (Object[] r : rows) {
            // Expecting [date (java.sql.Date or LocalDate), total (BigDecimal/Double)]
            LocalDate date;
            if (r[0] instanceof java.sql.Date) {
                date = ((java.sql.Date) r[0]).toLocalDate();
            } else {
                date = (LocalDate) r[0];
            }
            double total = ((Number) r[1]).doubleValue();
            totals.put(date, total);
        }

        List<DailyRevenueDTO> result = new ArrayList<>();
        LocalDate iter = fromDate;
        for (int i = 0; i < days; i++) {
            double total = totals.getOrDefault(iter, 0.0);
            result.add(new DailyRevenueDTO(iter, total));
            iter = iter.plusDays(1);
        }

        return result;
    }
}
