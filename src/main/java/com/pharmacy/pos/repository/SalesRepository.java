package com.pharmacy.pos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.pos.model.Sales;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
    
    Optional<Sales> findByInvoiceNumber(String invoiceNumber);
    
    List<Sales> findByDate(String date);
    
    List<Sales> findByCashier(String cashier);
    
    List<Sales> findByType(String type);
    
    List<Sales> findByName(String customerName);
    
    List<Sales> findByNameContainingIgnoreCase(String customerName);
    
    List<Sales> findByDueDateBefore(String date);
    
    List<Sales> findByDateBetween(String startDate, String endDate);
}