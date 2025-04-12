package com.pharmacy.pos.repository;

import com.pharmacy.pos.model.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchasesRepository extends JpaRepository<Purchases, Long> {
    
    Optional<Purchases> findByInvoiceNumber(String invoiceNumber);
    
    List<Purchases> findByDate(String date);
    
    List<Purchases> findBySupplier(String supplier);
    
    List<Purchases> findByDateBetween(String startDate, String endDate);
    
    @Query("SELECT SUM(p.remarks) FROM Purchases p WHERE p.date = ?1")
    String findTotalPurchasesByDate(String date);
    
    List<Purchases> findByRemarksContaining(String remarks);
}