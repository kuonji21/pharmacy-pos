package com.pharmacy.pos.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pharmacy.pos.model.Sales;
import com.pharmacy.pos.repository.SalesOrderRepository;
import com.pharmacy.pos.repository.SalesRepository;
import com.pharmacy.pos.service.SalesService;

@Service
public class SalesServiceImpl implements SalesService {

    private final SalesRepository salesRepository;
    private final SalesOrderRepository salesOrderRepository;
    
    @Autowired
    public SalesServiceImpl(SalesRepository salesRepository, SalesOrderRepository salesOrderRepository) {
        this.salesRepository = salesRepository;
        this.salesOrderRepository = salesOrderRepository;
    }
    
    @Override
    public List<Sales> findAllSales() {
        return salesRepository.findAll();
    }
    
    @Override
    public Optional<Sales> findSalesById(Long id) {
        return salesRepository.findById(id);
    }
    
    @Override
    public Optional<Sales> findSalesByInvoiceNumber(String invoiceNumber) {
        return salesRepository.findByInvoiceNumber(invoiceNumber);
    }
    
    @Override
    public List<Sales> findSalesByDate(String date) {
        return salesRepository.findByDate(date);
    }
    
    @Override
    public List<Sales> findSalesByCashier(String cashier) {
        return salesRepository.findByCashier(cashier);
    }
    
    @Override
    public List<Sales> findSalesByDateRange(String startDate, String endDate) {
        return salesRepository.findByDateBetween(startDate, endDate);
    }
    
    @Override
    public List<Sales> findSalesByCustomerName(String name) {
        return salesRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Override
    public Sales saveSales(Sales sales) {
        return salesRepository.save(sales);
    }
    
    @Override
    public void deleteSales(Long id) {
        salesRepository.deleteById(id);
    }
    
    @Override
    public String calculateTotalSalesForDate(String date) {
        return salesOrderRepository.findTotalSalesByDate(date);
    }
    
    @Override
    public String calculateTotalProfitForDate(String date) {
        return salesOrderRepository.findTotalProfitByDate(date);
    }
    
    @Override
    public String calculateTotalSales() {
        return salesOrderRepository.findTotalSales();
    }
    
    @Override
    public String calculateTotalProfit() {
        return salesOrderRepository.findTotalProfit();
    }
}