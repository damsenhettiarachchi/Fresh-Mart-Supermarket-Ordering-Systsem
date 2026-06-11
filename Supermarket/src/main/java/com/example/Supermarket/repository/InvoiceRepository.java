package com.example.Supermarket.repository;


import com.example.Supermarket.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Invoice Entity.
 * Spring Data JPA handles the implementation automatically.
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    // Native query (works with most SQL DBs). Returns rows of [year, month, total]
    @Query(value = "SELECT YEAR(i.date) AS yr, MONTH(i.date) AS m, COALESCE(SUM(i.amount),0) AS total " +
            "FROM invoice i " +
            "WHERE i.date >= :fromDate " +
            "GROUP BY YEAR(i.date), MONTH(i.date) " +
            "ORDER BY YEAR(i.date), MONTH(i.date)", nativeQuery = true)
    List<Object[]> findMonthlyRevenueSince(@Param("fromDate") LocalDate fromDate);

    // Query for daily revenue. Returns rows of [date, total]
    @Query(value = "SELECT i.date, COALESCE(SUM(i.amount),0) AS total " +
            "FROM invoice i " +
            "WHERE i.date >= :fromDate " +
            "GROUP BY i.date " +
            "ORDER BY i.date", nativeQuery = true)
    List<Object[]> findDailyRevenueSince(@Param("fromDate") LocalDate fromDate);
}