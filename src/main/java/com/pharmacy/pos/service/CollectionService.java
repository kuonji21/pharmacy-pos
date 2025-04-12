package com.pharmacy.pos.service;

import com.pharmacy.pos.model.Collection;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing payment collections
 */
public interface CollectionService {
    
    /**
     * Find all collections
     */
    List<Collection> findAllCollections();
    
    /**
     * Find collection by ID
     */
    Optional<Collection> findCollectionById(Long id);
    
    /**
     * Find collections by invoice number
     */
    List<Collection> findCollectionsByInvoice(String invoice);
    
    /**
     * Find collections by date
     */
    List<Collection> findCollectionsByDate(String date);
    
    /**
     * Find collections by customer name
     */
    List<Collection> findCollectionsByName(String name);
    
    /**
     * Find collections by date range
     */
    List<Collection> findCollectionsByDateRange(String startDate, String endDate);
    
    /**
     * Find collections with balance greater than specified value
     */
    List<Collection> findCollectionsWithOutstandingBalance(Integer balance);
    
    /**
     * Calculate total collection amount for a specific date
     */
    String calculateTotalCollectionForDate(String date);
    
    /**
     * Save a collection record
     */
    Collection saveCollection(Collection collection);
    
    /**
     * Delete a collection record
     */
    void deleteCollection(Long id);
}