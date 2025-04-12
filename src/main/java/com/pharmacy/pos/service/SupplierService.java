package com.pharmacy.pos.service;

import com.pharmacy.pos.model.Supplier;

import java.util.List;
import java.util.Optional;

public interface SupplierService {
    
    List<Supplier> findAllSuppliers();
    
    Optional<Supplier> findSupplierById(Long id);
    
    List<Supplier> findSuppliersByName(String supplierName);
    
    List<Supplier> findSuppliersByContact(String supplierContact);
    
    List<Supplier> findSuppliersByContactPerson(String contactPerson);
    
    List<Supplier> findSuppliersByAddress(String supplierAddress);
    
    Supplier saveSupplier(Supplier supplier);
    
    void deleteSupplier(Long id);
    
    long countSuppliers();
}