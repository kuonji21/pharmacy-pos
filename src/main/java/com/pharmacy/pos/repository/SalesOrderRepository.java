package com.pharmacy.pos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pharmacy.pos.model.SalesOrder;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
    
    List<SalesOrder> findByInvoice(String invoice);
    
    List<SalesOrder> findByProductCode(String productCode);
    
    List<SalesOrder> findByDate(String date);
    
    List<SalesOrder> findByProduct(String product);
    
    List<SalesOrder> findByGenericName(String genericName);
    
    @Query("SELECT SUM(o.amount) FROM SalesOrder o WHERE o.date = ?1")
    String findTotalSalesByDate(String date);
    
    @Query("SELECT SUM(o.profit) FROM SalesOrder o WHERE o.date = ?1")
    String findTotalProfitByDate(String date);
    
    @Query("SELECT SUM(o.amount) FROM SalesOrder o")
    String findTotalSales();
    
    @Query("SELECT SUM(o.profit) FROM SalesOrder o")
    String findTotalProfit();
}