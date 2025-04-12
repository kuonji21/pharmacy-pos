package com.pharmacy.pos.service;

import com.pharmacy.pos.model.Purchases;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing purchase transactions
 */
public interface PurchasesService {
    
    /**
     * Find all purchases
     */
    List<Purchases> findAllPurchases();
    
    /**
     * Find purchase by ID
     */
    Optional<Purchases> findPurchaseById(Long id);
    
    /**
     * Find purchase by invoice number
     */
    Optional<Purchases> findPurchaseByInvoiceNumber(String invoiceNumber);
    
    /**
     * Find purchases by date
     */
    List<Purchases> findPurchasesByDate(String date);
    
    /**
     * Find purchases by supplier
     */
    List<Purchases> findPurchasesBySupplier(String supplier);
    
    /**
     * Find purchases by date range
     */
    List<Purchases> findPurchasesByDateRange(String startDate, String endDate);
    
    /**
     * Find purchases by remarks containing
     */
    List<Purchases> findPurchasesByRemarksContaining(String remarks);
    
    /**
     * Calculate total purchases for a specific date
     */
    String calculateTotalPurchasesForDate(String date);
    
    /**
     * Save a purchase
     */
    Purchases savePurchase(Purchases purchase);
    
    /**
     * Delete a purchase
     */
    void deletePurchase(Long id);
}