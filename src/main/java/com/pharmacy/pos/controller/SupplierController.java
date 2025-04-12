package com.pharmacy.pos.controller;

import com.pharmacy.pos.model.Supplier;
import com.pharmacy.pos.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * Controller for handling supplier-related requests
 */
@Controller
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    /**
     * Display the supplier list page
     * @param model Spring Model object for passing attributes to the view
     * @return The suppliers view template name
     */
    @GetMapping
    public String listSuppliers(Model model) {
        List<Supplier> suppliers = supplierService.findAllSuppliers();
        long supplierCount = supplierService.countSuppliers();
        
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("supplierCount", supplierCount);
        
        return "suppliers/list";
    }

    /**
     * Display the add supplier form
     * @param model Spring Model object for passing attributes to the view
     * @return The add supplier form view template name
     */
    @GetMapping("/add")
    public String showAddSupplierForm(Model model) {
        model.addAttribute("supplier", new Supplier());
        return "suppliers/form";
    }

    /**
     * Process the add supplier form submission
     * @param supplier The supplier to add
     * @param redirectAttributes For flash messages
     * @return Redirect to suppliers list
     */
    @PostMapping("/add")
    public String addSupplier(@ModelAttribute Supplier supplier, RedirectAttributes redirectAttributes) {
        supplierService.saveSupplier(supplier);
        redirectAttributes.addFlashAttribute("success", "Supplier added successfully");
        
        return "redirect:/suppliers";
    }

    /**
     * Display the edit supplier form
     * @param id The ID of the supplier to edit
     * @param model Spring Model object for passing attributes to the view
     * @return The edit supplier form view template name
     */
    @GetMapping("/edit/{id}")
    public String showEditSupplierForm(@PathVariable Long id, Model model) {
        Optional<Supplier> supplierOpt = supplierService.findSupplierById(id);
        
        if (supplierOpt.isPresent()) {
            model.addAttribute("supplier", supplierOpt.get());
            return "suppliers/form";
        } else {
            return "redirect:/suppliers";
        }
    }

    /**
     * Process the edit supplier form submission
     * @param id The ID of the supplier to edit
     * @param supplier The updated supplier
     * @param redirectAttributes For flash messages
     * @return Redirect to suppliers list
     */
    @PostMapping("/edit/{id}")
    public String updateSupplier(@PathVariable Long id, @ModelAttribute Supplier supplier, 
                                RedirectAttributes redirectAttributes) {
        supplierService.saveSupplier(supplier);
        redirectAttributes.addFlashAttribute("success", "Supplier updated successfully");
        
        return "redirect:/suppliers";
    }

    /**
     * Delete a supplier
     * @param id The ID of the supplier to delete
     * @param redirectAttributes For flash messages
     * @return Redirect to suppliers list
     */
    @GetMapping("/delete/{id}")
    public String deleteSupplier(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        supplierService.deleteSupplier(id);
        redirectAttributes.addFlashAttribute("success", "Supplier deleted successfully");
        
        return "redirect:/suppliers";
    }

    /**
     * Display suppliers filtered by search criteria
     * @param searchType The field to search (name, contact, address)
     * @param searchTerm The term to search for
     * @param model Spring Model object for passing attributes to the view
     * @return The suppliers view template name with filtered results
     */
    @GetMapping("/search")
    public String searchSuppliers(@RequestParam("searchType") String searchType, 
                                 @RequestParam("searchTerm") String searchTerm,
                                 Model model) {
        List<Supplier> suppliers;
        
        switch (searchType) {
            case "name":
                suppliers = supplierService.findSuppliersByName(searchTerm);
                break;
            case "contact":
                suppliers = supplierService.findSuppliersByContact(searchTerm);
                break;
            case "address":
                suppliers = supplierService.findSuppliersByAddress(searchTerm);
                break;
            case "contactPerson":
                suppliers = supplierService.findSuppliersByContactPerson(searchTerm);
                break;
            default:
                suppliers = supplierService.findAllSuppliers();
                break;
        }
        
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("supplierCount", suppliers.size());
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchTerm", searchTerm);
        
        return "suppliers/list";
    }
}