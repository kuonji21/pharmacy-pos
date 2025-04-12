package com.pharmacy.pos.service.impl;

import com.pharmacy.pos.model.Product;
import com.pharmacy.pos.repository.ProductRepository;
import com.pharmacy.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
    
    @Override
    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }
    
    @Override
    public Optional<Product> findProductByCode(String productCode) {
        return productRepository.findByProductCode(productCode);
    }
    
    @Override
    public List<Product> findProductsByName(String productName) {
        return productRepository.findByProductNameContainingIgnoreCase(productName);
    }
    
    @Override
    public List<Product> findProductsByGenericName(String genericName) {
        return productRepository.findByGenericNameContainingIgnoreCase(genericName);
    }
    
    @Override
    public List<Product> findProductsBySupplier(String supplier) {
        return productRepository.findBySupplierContainingIgnoreCase(supplier);
    }
    
    @Override
    public List<Product> findLowStockProducts() {
        return productRepository.findLowStockProducts();
    }
    
    @Override
    public List<Product> findTopSellingProducts() {
        return productRepository.findTopSellingProducts();
    }
    
    @Override
    public List<Product> findProductsExpiringBefore(String date) {
        return productRepository.findByExpiryDateBefore(date);
    }
    
    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    
    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    @Override
    public BigDecimal calculateTotalInventoryValue() {
        List<Product> allProducts = productRepository.findAll();
        return allProducts.stream()
                .map(Product::getTotalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    @Override
    @Transactional
    public void updateStock(String productCode, Integer quantity) {
        Optional<Product> productOpt = productRepository.findByProductCode(productCode);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setQty(product.getQty() + quantity);
            productRepository.save(product);
        }
    }
    
    @Override
    @Transactional
    public void recordSale(String productCode, Integer quantity) {
        Optional<Product> productOpt = productRepository.findByProductCode(productCode);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setQty(product.getQty() - quantity);
            product.setQuantitySold(product.getQuantitySold() + quantity);
            productRepository.save(product);
        }
    }
}