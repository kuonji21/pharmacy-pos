package com.pharmacy.pos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.pos.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    List<Customer> findByCustomerNameContainingIgnoreCase(String customerName);
    
    Optional<Customer> findByMembershipNumber(String membershipNumber);
    
    List<Customer> findByContactContaining(String contact);
    
    List<Customer> findByContactContainingIgnoreCase(String contact);
    
    List<Customer> findByAddressContainingIgnoreCase(String address);
    
    List<Customer> findByProductNameContainingIgnoreCase(String productName);
    
    List<Customer> findByExpectedDateBefore(String date);
}