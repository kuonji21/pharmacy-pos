package com.pharmacy.pos.repository;

import com.pharmacy.pos.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Optional<Product> findByProductCode(String productCode);
    
    List<Product> findByProductNameContainingIgnoreCase(String productName);
    
    List<Product> findByGenericNameContainingIgnoreCase(String genericName);
    
    List<Product> findBySupplierContainingIgnoreCase(String supplier);
    
    @Query("SELECT p FROM Product p WHERE p.qty < 10")
    List<Product> findLowStockProducts();
    
    @Query("SELECT p FROM Product p ORDER BY p.quantitySold DESC")
    List<Product> findTopSellingProducts();
    
    List<Product> findByExpiryDateBefore(String date);
}