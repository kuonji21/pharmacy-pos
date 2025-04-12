package com.pharmacy.pos.service.impl;

import com.pharmacy.pos.model.Supplier;
import com.pharmacy.pos.repository.SupplierRepository;
import com.pharmacy.pos.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    
    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }
    
    @Override
    public List<Supplier> findAllSuppliers() {
        return supplierRepository.findAll();
    }
    
    @Override
    public Optional<Supplier> findSupplierById(Long id) {
        return supplierRepository.findById(id);
    }
    
    @Override
    public List<Supplier> findSuppliersByName(String supplierName) {
        return supplierRepository.findBySupplierNameContainingIgnoreCase(supplierName);
    }
    
    @Override
    public List<Supplier> findSuppliersByContact(String supplierContact) {
        return supplierRepository.findBySupplierContactContainingIgnoreCase(supplierContact);
    }
    
    @Override
    public List<Supplier> findSuppliersByContactPerson(String contactPerson) {
        return supplierRepository.findByContactPersonContainingIgnoreCase(contactPerson);
    }
    
    @Override
    public List<Supplier> findSuppliersByAddress(String supplierAddress) {
        return supplierRepository.findBySupplierAddressContainingIgnoreCase(supplierAddress);
    }
    
    @Override
    public Supplier saveSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }
    
    @Override
    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }
    
    @Override
    public long countSuppliers() {
        return supplierRepository.count();
    }
}