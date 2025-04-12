package com.pharmacy.pos.service;

import com.pharmacy.pos.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    
    List<Product> findAllProducts();
    
    Optional<Product> findProductById(Long id);
    
    Optional<Product> findProductByCode(String productCode);
    
    List<Product> findProductsByName(String productName);
    
    List<Product> findProductsByGenericName(String genericName);
    
    List<Product> findProductsBySupplier(String supplier);
    
    List<Product> findLowStockProducts();
    
    List<Product> findTopSellingProducts();
    
    List<Product> findProductsExpiringBefore(String date);
    
    Product saveProduct(Product product);
    
    void deleteProduct(Long id);
    
    BigDecimal calculateTotalInventoryValue();
    
    void updateStock(String productCode, Integer quantity);
    
    void recordSale(String productCode, Integer quantity);
}