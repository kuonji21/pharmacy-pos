package com.pharmacy.pos.controller;

import com.pharmacy.pos.model.Purchases;
import com.pharmacy.pos.model.PurchasesItem;
import com.pharmacy.pos.model.Supplier;
import com.pharmacy.pos.service.ProductService;
import com.pharmacy.pos.service.PurchasesItemService;
import com.pharmacy.pos.service.PurchasesService;
import com.pharmacy.pos.service.SupplierService;
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
 * Controller for handling purchases-related requests
 */
@Controller
@RequestMapping("/purchases")
public class PurchasesController {

    private final PurchasesService purchasesService;
    private final PurchasesItemService purchasesItemService;
    private final ProductService productService;
    private final SupplierService supplierService;

    @Autowired
    public PurchasesController(PurchasesService purchasesService, PurchasesItemService purchasesItemService,
                              ProductService productService, SupplierService supplierService) {
        this.purchasesService = purchasesService;
        this.purchasesItemService = purchasesItemService;
        this.productService = productService;
        this.supplierService = supplierService;
    }

    /**
     * Display the purchases list page
     * @param model Spring Model object for passing attributes to the view
     * @return The purchases view template name
     */
    @GetMapping
    public String listPurchases(Model model) {
        List<Purchases> purchases = purchasesService.findAllPurchases();
        
        model.addAttribute("purchases", purchases);
        model.addAttribute("todaysPurchases", purchasesService.findPurchasesByDate(
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        
        return "purchases/list";
    }

    /**
     * Display the create new purchase page
     * @param model Spring Model object for passing attributes to the view
     * @return The new purchase form view template name
     */
    @GetMapping("/new")
    public String showNewPurchaseForm(Model model) {
        model.addAttribute("products", productService.findAllProducts());
        model.addAttribute("suppliers", supplierService.findAllSuppliers());
        model.addAttribute("purchase", new Purchases());
        model.addAttribute("purchaseItem", new PurchasesItem());
        
        return "purchases/form";
    }

    /**
     * Process the new purchase form submission
     * @param purchase The purchase transaction to create
     * @param redirectAttributes For flash messages
     * @return Redirect to purchases list
     */
    @PostMapping("/new")
    public String createPurchase(@ModelAttribute Purchases purchase, 
                                @RequestParam("productNames") List<String> productNames,
                                @RequestParam("quantities") List<Integer> quantities,
                                @RequestParam("costs") List<String> costs,
                                RedirectAttributes redirectAttributes) {
        
        // Create purchase record
        purchase.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Purchases savedPurchase = purchasesService.savePurchase(purchase);
        
        // Create purchase item records for each product
        for (int i = 0; i < productNames.size(); i++) {
            if (quantities.get(i) > 0) {
                String productName = productNames.get(i);
                Integer quantity = quantities.get(i);
                String cost = costs.get(i);
                
                // Create purchase item record
                PurchasesItem purchaseItem = new PurchasesItem();
                purchaseItem.setName(productName);
                purchaseItem.setQty(quantity);
                purchaseItem.setCost(cost);
                purchaseItem.setInvoice(savedPurchase.getInvoiceNumber());
                
                purchasesItemService.savePurchasesItem(purchaseItem);
                
                // Update product inventory
                productService.updateStock(productName, quantity);
            }
        }
        
        redirectAttributes.addFlashAttribute("success", "Purchase created successfully");
        return "redirect:/purchases";
    }

    /**
     * Display the purchase details page
     * @param invoiceNumber The invoice number of the purchase to view
     * @param model Spring Model object for passing attributes to the view
     * @return The purchase details view template name
     */
    @GetMapping("/details/{invoiceNumber}")
    public String viewPurchaseDetails(@PathVariable String invoiceNumber, Model model) {
        Optional<Purchases> purchaseOpt = purchasesService.findPurchaseByInvoiceNumber(invoiceNumber);
        
        if (purchaseOpt.isPresent()) {
            Purchases purchase = purchaseOpt.get();
            List<PurchasesItem> purchaseItems = purchasesItemService.findPurchasesItemsByInvoice(invoiceNumber);
            
            model.addAttribute("purchase", purchase);
            model.addAttribute("purchaseItems", purchaseItems);
            model.addAttribute("totalItems", purchaseItems.size());
            model.addAttribute("totalQuantity", purchasesItemService.calculateTotalQuantityForInvoice(invoiceNumber));
            model.addAttribute("totalCost", purchasesItemService.calculateTotalCostForInvoice(invoiceNumber));
            
            // Get supplier details
            Optional<Supplier> supplierOpt = supplierService.findSuppliersByName(purchase.getSupplier()).stream().findFirst();
            supplierOpt.ifPresent(supplier -> model.addAttribute("supplier", supplier));
            
            return "purchases/details";
        } else {
            return "redirect:/purchases";
        }
    }

    /**
     * Delete a purchase
     * @param id The ID of the purchase to delete
     * @param redirectAttributes For flash messages
     * @return Redirect to purchases list
     */
    @GetMapping("/delete/{id}")
    public String deletePurchase(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Purchases> purchaseOpt = purchasesService.findPurchaseById(id);
        
        if (purchaseOpt.isPresent()) {
            Purchases purchase = purchaseOpt.get();
            String invoiceNumber = purchase.getInvoiceNumber();
            
            // Delete all associated purchase items
            List<PurchasesItem> purchaseItems = purchasesItemService.findPurchasesItemsByInvoice(invoiceNumber);
            for (PurchasesItem purchaseItem : purchaseItems) {
                purchasesItemService.deletePurchasesItem(purchaseItem.getId());
            }
            
            // Delete the purchase record
            purchasesService.deletePurchase(id);
            redirectAttributes.addFlashAttribute("success", "Purchase deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Purchase not found");
        }
        
        return "redirect:/purchases";
    }

    /**
     * Display purchase report page
     * @param model Spring Model object for passing attributes to the view
     * @return The purchase report view template name
     */
    @GetMapping("/report")
    public String showPurchaseReport(Model model) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        String todayStr = today.format(formatter);
        String thisWeekStart = today.minusDays(today.getDayOfWeek().getValue() - 1).format(formatter);
        String thisMonthStart = today.withDayOfMonth(1).format(formatter);
        
        model.addAttribute("todayPurchases", purchasesService.findPurchasesByDate(todayStr));
        model.addAttribute("weekPurchases", purchasesService.findPurchasesByDateRange(thisWeekStart, todayStr));
        model.addAttribute("monthPurchases", purchasesService.findPurchasesByDateRange(thisMonthStart, todayStr));
        
        model.addAttribute("todayTotal", purchasesService.calculateTotalPurchasesForDate(todayStr));
        model.addAttribute("suppliers", supplierService.findAllSuppliers());
        
        return "purchases/report";
    }

    /**
     * Search for purchases by date range, supplier, or invoice number
     * @param searchType The type of search (date, supplier, invoice)
     * @param searchTerm The search term
     * @param model Spring Model object for passing attributes to the view
     * @return The purchases list view with filtered results
     */
    @GetMapping("/search")
    public String searchPurchases(@RequestParam("searchType") String searchType,
                                 @RequestParam("searchTerm") String searchTerm,
                                 Model model) {
        List<Purchases> purchases = null;
        
        switch (searchType) {
            case "date":
                purchases = purchasesService.findPurchasesByDate(searchTerm);
                break;
            case "supplier":
                purchases = purchasesService.findPurchasesBySupplier(searchTerm);
                break;
            case "invoice":
                Optional<Purchases> purchaseOpt = purchasesService.findPurchaseByInvoiceNumber(searchTerm);
                purchases = purchaseOpt.map(List::of).orElse(List.of());
                break;
            case "remarks":
                purchases = purchasesService.findPurchasesByRemarksContaining(searchTerm);
                break;
            default:
                purchases = purchasesService.findAllPurchases();
                break;
        }
        
        model.addAttribute("purchases", purchases);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchTerm", searchTerm);
        
        return "purchases/list";
    }
}