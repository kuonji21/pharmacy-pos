package com.pharmacy.pos.service;

import java.util.List;
import java.util.Optional;

import com.pharmacy.pos.model.SalesOrder;

public interface SalesOrderService {
    
    List<SalesOrder> findAllSalesOrders();
    
    Optional<SalesOrder> findSalesOrderById(Long id);
    
    List<SalesOrder> findSalesOrdersByInvoice(String invoice);
    
    List<SalesOrder> findSalesOrdersByProductCode(String productCode);
    
    List<SalesOrder> findSalesOrdersByDate(String date);
    
    List<SalesOrder> findSalesOrdersByProduct(String product);
    
    List<SalesOrder> findSalesOrdersByGenericName(String genericName);
    
    SalesOrder saveSalesOrder(SalesOrder salesOrder);
    
    void deleteSalesOrder(Long id);
    
    String calculateTotalSalesAmountForDate(String date);
    
    String calculateTotalProfitForDate(String date);
    
    String findTotalSalesByDate(String date);
    
    String findTotalProfitByDate(String date);
}