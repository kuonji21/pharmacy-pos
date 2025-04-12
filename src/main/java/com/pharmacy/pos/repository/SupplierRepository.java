package com.pharmacy.pos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.pos.model.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    
    List<Supplier> findBySupplierNameContainingIgnoreCase(String supplierName);
    
    List<Supplier> findByContactPersonContainingIgnoreCase(String contactPerson);
    
    List<Supplier> findBySupplierContactContaining(String supplierContact);
    
    List<Supplier> findBySupplierContactContainingIgnoreCase(String supplierContact);
    
    List<Supplier> findBySupplierAddressContainingIgnoreCase(String supplierAddress);
}