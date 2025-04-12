package com.pharmacy.pos.repository;

import com.pharmacy.pos.model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    
    List<Collection> findByInvoice(String invoice);
    
    List<Collection> findByDate(String date);
    
    List<Collection> findByName(String name);
    
    List<Collection> findByAmount(String amount);
    
    List<Collection> findByDateBetween(String startDate, String endDate);
    
    @Query("SELECT SUM(c.amount) FROM Collection c WHERE c.date = ?1")
    String findTotalCollectionByDate(String date);
    
    List<Collection> findByBalanceGreaterThan(Integer balance);
}