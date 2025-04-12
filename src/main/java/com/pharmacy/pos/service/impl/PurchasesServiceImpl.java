package com.pharmacy.pos.service.impl;

import com.pharmacy.pos.model.Purchases;
import com.pharmacy.pos.repository.PurchasesRepository;
import com.pharmacy.pos.service.PurchasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of purchases service
 */
@Service
public class PurchasesServiceImpl implements PurchasesService {

    private final PurchasesRepository purchasesRepository;
    
    @Autowired
    public PurchasesServiceImpl(PurchasesRepository purchasesRepository) {
        this.purchasesRepository = purchasesRepository;
    }
    
    @Override
    public List<Purchases> findAllPurchases() {
        return purchasesRepository.findAll();
    }
    
    @Override
    public Optional<Purchases> findPurchaseById(Long id) {
        return purchasesRepository.findById(id);
    }
    
    @Override
    public Optional<Purchases> findPurchaseByInvoiceNumber(String invoiceNumber) {
        return purchasesRepository.findByInvoiceNumber(invoiceNumber);
    }
    
    @Override
    public List<Purchases> findPurchasesByDate(String date) {
        return purchasesRepository.findByDate(date);
    }
    
    @Override
    public List<Purchases> findPurchasesBySupplier(String supplier) {
        return purchasesRepository.findBySupplier(supplier);
    }
    
    @Override
    public List<Purchases> findPurchasesByDateRange(String startDate, String endDate) {
        return purchasesRepository.findByDateBetween(startDate, endDate);
    }
    
    @Override
    public List<Purchases> findPurchasesByRemarksContaining(String remarks) {
        return purchasesRepository.findByRemarksContaining(remarks);
    }
    
    @Override
    public String calculateTotalPurchasesForDate(String date) {
        return purchasesRepository.findTotalPurchasesByDate(date);
    }
    
    @Override
    public Purchases savePurchase(Purchases purchase) {
        return purchasesRepository.save(purchase);
    }
    
    @Override
    public void deletePurchase(Long id) {
        purchasesRepository.deleteById(id);
    }
}