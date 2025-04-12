package com.pharmacy.pos.service;

import com.pharmacy.pos.model.PurchasesItem;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing purchase items
 */
public interface PurchasesItemService {
    
    /**
     * Find all purchase items
     */
    List<PurchasesItem> findAllPurchasesItems();
    
    /**
     * Find purchase item by ID
     */
    Optional<PurchasesItem> findPurchasesItemById(Long id);
    
    /**
     * Find purchase items by invoice number
     */
    List<PurchasesItem> findPurchasesItemsByInvoice(String invoice);
    
    /**
     * Find purchase items by name
     */
    List<PurchasesItem> findPurchasesItemsByName(String name);
    
    /**
     * Find purchase items with quantity greater than specified value
     */
    List<PurchasesItem> findPurchasesItemsByQtyGreaterThan(Integer quantity);
    
    /**
     * Calculate total quantity for an invoice
     */
    Integer calculateTotalQuantityForInvoice(String invoice);
    
    /**
     * Calculate total cost for an invoice
     */
    String calculateTotalCostForInvoice(String invoice);
    
    /**
     * Save a purchase item
     */
    PurchasesItem savePurchasesItem(PurchasesItem purchasesItem);
    
    /**
     * Delete a purchase item
     */
    void deletePurchasesItem(Long id);
}