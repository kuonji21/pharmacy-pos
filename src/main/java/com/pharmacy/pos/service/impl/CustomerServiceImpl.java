package com.pharmacy.pos.service.impl;

import com.pharmacy.pos.model.Customer;
import com.pharmacy.pos.repository.CustomerRepository;
import com.pharmacy.pos.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    
    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    @Override
    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }
    
    @Override
    public Optional<Customer> findCustomerById(Long id) {
        return customerRepository.findById(id);
    }
    
    @Override
    public List<Customer> findCustomersByName(String customerName) {
        return customerRepository.findByCustomerNameContainingIgnoreCase(customerName);
    }
    
    @Override
    public List<Customer> findCustomersByAddress(String address) {
        return customerRepository.findByAddressContainingIgnoreCase(address);
    }
    
    @Override
    public List<Customer> findCustomersByContact(String contact) {
        return customerRepository.findByContactContainingIgnoreCase(contact);
    }
    
    @Override
    public Optional<Customer> findCustomerByMembershipNumber(String membershipNumber) {
        return customerRepository.findByMembershipNumber(membershipNumber);
    }
    
    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    
    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
    
    @Override
    public long countCustomers() {
        return customerRepository.count();
    }
}