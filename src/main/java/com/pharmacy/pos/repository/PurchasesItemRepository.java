package com.pharmacy.pos.repository;

import com.pharmacy.pos.model.PurchasesItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchasesItemRepository extends JpaRepository<PurchasesItem, Long> {
    
    List<PurchasesItem> findByInvoice(String invoice);
    
    List<PurchasesItem> findByName(String name);
    
    List<PurchasesItem> findByQtyGreaterThan(Integer quantity);
    
    @Query("SELECT SUM(p.qty) FROM PurchasesItem p WHERE p.invoice = ?1")
    Integer findTotalQuantityByInvoice(String invoice);
    
    @Query("SELECT SUM(p.cost) FROM PurchasesItem p WHERE p.invoice = ?1")
    String findTotalCostByInvoice(String invoice);
}