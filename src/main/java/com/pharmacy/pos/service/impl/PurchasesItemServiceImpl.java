package com.pharmacy.pos.service.impl;

import com.pharmacy.pos.model.PurchasesItem;
import com.pharmacy.pos.repository.PurchasesItemRepository;
import com.pharmacy.pos.service.PurchasesItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of purchases item service
 */
@Service
public class PurchasesItemServiceImpl implements PurchasesItemService {

    private final PurchasesItemRepository purchasesItemRepository;
    
    @Autowired
    public PurchasesItemServiceImpl(PurchasesItemRepository purchasesItemRepository) {
        this.purchasesItemRepository = purchasesItemRepository;
    }
    
    @Override
    public List<PurchasesItem> findAllPurchasesItems() {
        return purchasesItemRepository.findAll();
    }
    
    @Override
    public Optional<PurchasesItem> findPurchasesItemById(Long id) {
        return purchasesItemRepository.findById(id);
    }
    
    @Override
    public List<PurchasesItem> findPurchasesItemsByInvoice(String invoice) {
        return purchasesItemRepository.findByInvoice(invoice);
    }
    
    @Override
    public List<PurchasesItem> findPurchasesItemsByName(String name) {
        return purchasesItemRepository.findByName(name);
    }
    
    @Override
    public List<PurchasesItem> findPurchasesItemsByQtyGreaterThan(Integer quantity) {
        return purchasesItemRepository.findByQtyGreaterThan(quantity);
    }
    
    @Override
    public Integer calculateTotalQuantityForInvoice(String invoice) {
        return purchasesItemRepository.findTotalQuantityByInvoice(invoice);
    }
    
    @Override
    public String calculateTotalCostForInvoice(String invoice) {
        return purchasesItemRepository.findTotalCostByInvoice(invoice);
    }
    
    @Override
    public PurchasesItem savePurchasesItem(PurchasesItem purchasesItem) {
        return purchasesItemRepository.save(purchasesItem);
    }
    
    @Override
    public void deletePurchasesItem(Long id) {
        purchasesItemRepository.deleteById(id);
    }
}