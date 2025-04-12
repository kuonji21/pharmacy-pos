package com.pharmacy.pos.controller;

import com.pharmacy.pos.model.Collection;
import com.pharmacy.pos.model.Customer;
import com.pharmacy.pos.service.CollectionService;
import com.pharmacy.pos.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Controller for handling collections-related requests
 */
@Controller
@RequestMapping("/collections")
public class CollectionController {

    private final CollectionService collectionService;
    private final CustomerService customerService;

    @Autowired
    public CollectionController(CollectionService collectionService, CustomerService customerService) {
        this.collectionService = collectionService;
        this.customerService = customerService;
    }

    /**
     * Display the collections list page
     * @param model Spring Model object for passing attributes to the view
     * @return The collections view template name
     */
    @GetMapping
    public String listCollections(Model model) {
        List<Collection> collections = collectionService.findAllCollections();
        
        model.addAttribute("collections", collections);
        model.addAttribute("todaysCollections", collectionService.findCollectionsByDate(
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        model.addAttribute("totalAmount", calculateTotalCollectionAmount(collections));
        model.addAttribute("outstandingCollections", 
                collectionService.findCollectionsWithOutstandingBalance(0));
        
        return "collections/list";
    }

    /**
     * Display the add collection form
     * @param model Spring Model object for passing attributes to the view
     * @return The add collection form view template name
     */
    @GetMapping("/add")
    public String showAddCollectionForm(Model model) {
        model.addAttribute("collection", new Collection());
        model.addAttribute("customers", customerService.findAllCustomers());
        
        return "collections/form";
    }

    /**
     * Process the add collection form submission
     * @param collection The collection record to add
     * @param redirectAttributes For flash messages
     * @return Redirect to collections list
     */
    @PostMapping("/add")
    public String addCollection(@ModelAttribute Collection collection, 
                               RedirectAttributes redirectAttributes) {
        // Set current date if not provided
        if (collection.getDate() == null || collection.getDate().isEmpty()) {
            collection.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        
        collectionService.saveCollection(collection);
        redirectAttributes.addFlashAttribute("success", "Collection record added successfully");
        
        return "redirect:/collections";
    }

    /**
     * Display the collection details page
     * @param id The ID of the collection to view
     * @param model Spring Model object for passing attributes to the view
     * @return The collection details view template name
     */
    @GetMapping("/details/{id}")
    public String viewCollectionDetails(@PathVariable Long id, Model model) {
        Optional<Collection> collectionOpt = collectionService.findCollectionById(id);
        
        if (collectionOpt.isPresent()) {
            Collection collection = collectionOpt.get();
            model.addAttribute("collection", collection);
            
            // Get customer details if available
            List<Customer> customers = customerService.findCustomersByName(collection.getName());
            if (!customers.isEmpty()) {
                model.addAttribute("customer", customers.get(0));
            }
            
            return "collections/details";
        } else {
            return "redirect:/collections";
        }
    }

    /**
     * Delete a collection record
     * @param id The ID of the collection to delete
     * @param redirectAttributes For flash messages
     * @return Redirect to collections list
     */
    @GetMapping("/delete/{id}")
    public String deleteCollection(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        collectionService.deleteCollection(id);
        redirectAttributes.addFlashAttribute("success", "Collection deleted successfully");
        
        return "redirect:/collections";
    }

    /**
     * Display collections report page
     * @param model Spring Model object for passing attributes to the view
     * @return The collections report view template name
     */
    @GetMapping("/report")
    public String showCollectionReport(Model model) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        String todayStr = today.format(formatter);
        String thisWeekStart = today.minusDays(today.getDayOfWeek().getValue() - 1).format(formatter);
        String thisMonthStart = today.withDayOfMonth(1).format(formatter);
        
        List<Collection> todayCollections = collectionService.findCollectionsByDate(todayStr);
        List<Collection> weekCollections = collectionService.findCollectionsByDateRange(thisWeekStart, todayStr);
        List<Collection> monthCollections = collectionService.findCollectionsByDateRange(thisMonthStart, todayStr);
        
        model.addAttribute("todayCollections", todayCollections);
        model.addAttribute("weekCollections", weekCollections);
        model.addAttribute("monthCollections", monthCollections);
        
        model.addAttribute("todayTotal", collectionService.calculateTotalCollectionForDate(todayStr));
        model.addAttribute("todayCount", todayCollections.size());
        model.addAttribute("weekTotal", calculateTotalCollectionAmount(weekCollections));
        model.addAttribute("monthTotal", calculateTotalCollectionAmount(monthCollections));
        
        model.addAttribute("outstandingCollections", 
                collectionService.findCollectionsWithOutstandingBalance(0));
        
        return "collections/report";
    }

    /**
     * Search for collections by date, customer name, or invoice
     * @param searchType The type of search (date, name, invoice)
     * @param searchTerm The search term
     * @param model Spring Model object for passing attributes to the view
     * @return The collections list view with filtered results
     */
    @GetMapping("/search")
    public String searchCollections(@RequestParam("searchType") String searchType,
                                   @RequestParam("searchTerm") String searchTerm,
                                   Model model) {
        List<Collection> collections = null;
        
        switch (searchType) {
            case "date":
                collections = collectionService.findCollectionsByDate(searchTerm);
                break;
            case "name":
                collections = collectionService.findCollectionsByName(searchTerm);
                break;
            case "invoice":
                collections = collectionService.findCollectionsByInvoice(searchTerm);
                break;
            default:
                collections = collectionService.findAllCollections();
                break;
        }
        
        model.addAttribute("collections", collections);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("totalAmount", calculateTotalCollectionAmount(collections));
        
        return "collections/list";
    }
    
    /**
     * Calculate the total amount for a list of collections
     * @param collections The list of collection records
     * @return The total amount
     */
    private String calculateTotalCollectionAmount(List<Collection> collections) {
        double total = collections.stream()
                .mapToDouble(collection -> {
                    try {
                        return Double.parseDouble(collection.getAmount());
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                })
                .sum();
        
        return String.valueOf(total);
    }
}