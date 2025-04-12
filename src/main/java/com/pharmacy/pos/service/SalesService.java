package com.pharmacy.pos.service;

import java.util.List;
import java.util.Optional;

import com.pharmacy.pos.model.Sales;

public interface SalesService {
    
    List<Sales> findAllSales();
    
    Optional<Sales> findSalesById(Long id);
    
    Optional<Sales> findSalesByInvoiceNumber(String invoiceNumber);
    
    List<Sales> findSalesByDate(String date);
    
    List<Sales> findSalesByCashier(String cashier);
    
    List<Sales> findSalesByDateRange(String startDate, String endDate);
    
    List<Sales> findSalesByCustomerName(String name);
    
    Sales saveSales(Sales sales);
    
    void deleteSales(Long id);
    
    String calculateTotalSalesForDate(String date);
    
    String calculateTotalProfitForDate(String date);
    
    String calculateTotalSales();
    
    String calculateTotalProfit();
}