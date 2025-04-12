package com.pharmacy.pos.service;

import com.pharmacy.pos.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    
    List<Customer> findAllCustomers();
    
    Optional<Customer> findCustomerById(Long id);
    
    List<Customer> findCustomersByName(String customerName);
    
    List<Customer> findCustomersByAddress(String address);
    
    List<Customer> findCustomersByContact(String contact);
    
    Optional<Customer> findCustomerByMembershipNumber(String membershipNumber);
    
    Customer saveCustomer(Customer customer);
    
    void deleteCustomer(Long id);
    
    long countCustomers();
}