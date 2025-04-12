package com.pharmacy.pos.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pharmacy.pos.model.SalesOrder;
import com.pharmacy.pos.repository.SalesOrderRepository;
import com.pharmacy.pos.service.SalesOrderService;

@Service
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    
    @Autowired
    public SalesOrderServiceImpl(SalesOrderRepository salesOrderRepository) {
        this.salesOrderRepository = salesOrderRepository;
    }
    
    @Override
    public List<SalesOrder> findAllSalesOrders() {
        return salesOrderRepository.findAll();
    }
    
    @Override
    public Optional<SalesOrder> findSalesOrderById(Long id) {
        return salesOrderRepository.findById(id);
    }
    
    @Override
    public List<SalesOrder> findSalesOrdersByInvoice(String invoice) {
        return salesOrderRepository.findByInvoice(invoice);
    }
    
    @Override
    public List<SalesOrder> findSalesOrdersByProductCode(String productCode) {
        return salesOrderRepository.findByProductCode(productCode);
    }
    
    @Override
    public List<SalesOrder> findSalesOrdersByDate(String date) {
        return salesOrderRepository.findByDate(date);
    }
    
    @Override
    public List<SalesOrder> findSalesOrdersByProduct(String product) {
        return salesOrderRepository.findByProduct(product);
    }
    
    @Override
    public List<SalesOrder> findSalesOrdersByGenericName(String genericName) {
        return salesOrderRepository.findByGenericName(genericName);
    }
    
    @Override
    public SalesOrder saveSalesOrder(SalesOrder salesOrder) {
        return salesOrderRepository.save(salesOrder);
    }
    
    @Override
    public void deleteSalesOrder(Long id) {
        salesOrderRepository.deleteById(id);
    }
    
    @Override
    public String calculateTotalSalesAmountForDate(String date) {
        return salesOrderRepository.findTotalSalesByDate(date);
    }
    
    @Override
    public String calculateTotalProfitForDate(String date) {
        return salesOrderRepository.findTotalProfitByDate(date);
    }
    
    @Override
    public String findTotalSalesByDate(String date) {
        return salesOrderRepository.findTotalSalesByDate(date);
    }
    
    @Override
    public String findTotalProfitByDate(String date) {
        return salesOrderRepository.findTotalProfitByDate(date);
    }
}