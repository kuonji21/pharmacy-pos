package com.pharmacy.pos.controller;

import com.pharmacy.pos.model.Customer;
import com.pharmacy.pos.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * Controller for handling customer-related requests
 */
@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Display the customer list page
     * @param model Spring Model object for passing attributes to the view
     * @return The customers view template name
     */
    @GetMapping
    public String listCustomers(Model model) {
        List<Customer> customers = customerService.findAllCustomers();
        model.addAttribute("customers", customers);
        model.addAttribute("totalCustomers", customerService.countCustomers());
        return "customers/list";
    }

    /**
     * Display the add customer form
     * @param model Spring Model object for passing attributes to the view
     * @return The add customer form view template name
     */
    @GetMapping("/add")
    public String showAddCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customers/form";
    }

    /**
     * Process the add customer form submission
     * @param customer The customer to add
     * @param redirectAttributes For flash messages
     * @return Redirect to customers list
     */
    @PostMapping("/add")
    public String addCustomer(@ModelAttribute Customer customer, RedirectAttributes redirectAttributes) {
        customerService.saveCustomer(customer);
        redirectAttributes.addFlashAttribute("success", "Customer added successfully");
        return "redirect:/customers";
    }

    /**
     * Display the edit customer form
     * @param id The ID of the customer to edit
     * @param model Spring Model object for passing attributes to the view
     * @return The edit customer form view template name
     */
    @GetMapping("/edit/{id}")
    public String showEditCustomerForm(@PathVariable Long id, Model model) {
        Optional<Customer> customerOpt = customerService.findCustomerById(id);
        
        if (customerOpt.isPresent()) {
            model.addAttribute("customer", customerOpt.get());
            return "customers/form";
        } else {
            return "redirect:/customers";
        }
    }

    /**
     * Process the edit customer form submission
     * @param id The ID of the customer to edit
     * @param customer The updated customer
     * @param redirectAttributes For flash messages
     * @return Redirect to customers list
     */
    @PostMapping("/edit/{id}")
    public String updateCustomer(@PathVariable Long id, @ModelAttribute Customer customer, 
                                RedirectAttributes redirectAttributes) {
        customerService.saveCustomer(customer);
        redirectAttributes.addFlashAttribute("success", "Customer updated successfully");
        return "redirect:/customers";
    }

    /**
     * Delete a customer
     * @param id The ID of the customer to delete
     * @param redirectAttributes For flash messages
     * @return Redirect to customers list
     */
    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        customerService.deleteCustomer(id);
        redirectAttributes.addFlashAttribute("success", "Customer deleted successfully");
        return "redirect:/customers";
    }

    /**
     * Search for customers by name
     * @param keyword The search keyword
     * @param model Spring Model object for passing attributes to the view
     * @return The customers list view with filtered results
     */
    @GetMapping("/search")
    public String searchCustomers(@RequestParam String keyword, Model model) {
        List<Customer> customers = customerService.findCustomersByName(keyword);
        model.addAttribute("customers", customers);
        model.addAttribute("keyword", keyword);
        return "customers/list";
    }
}