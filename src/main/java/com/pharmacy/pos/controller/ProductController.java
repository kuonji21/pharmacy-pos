package com.pharmacy.pos.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pharmacy.pos.model.Product;
import com.pharmacy.pos.service.ProductService;
import com.pharmacy.pos.service.SupplierService;

/**
 * Controller for handling product-related requests
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final SupplierService supplierService;

    @Autowired
    public ProductController(ProductService productService, SupplierService supplierService) {
        this.productService = productService;
        this.supplierService = supplierService;
    }

    /**
     * Display the product list page
     * @param model Spring Model object for passing attributes to the view
     * @return The products view template name
     */
    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.findAllProducts();
        BigDecimal totalInventoryValue = productService.calculateTotalInventoryValue();
        List<Product> lowStockProducts = productService.findLowStockProducts();
        
        model.addAttribute("products", products);
        model.addAttribute("totalInventoryValue", totalInventoryValue);
        model.addAttribute("lowStockProducts", lowStockProducts);
        
        return "products/list";
    }

    /**
     * Display the add product form
     * @param model Spring Model object for passing attributes to the view
     * @return The add product form view template name
     */
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("suppliers", supplierService.findAllSuppliers());
        
        return "products/form";
    }

    /**
     * Process the add product form submission
     * @param product The product to add
     * @param redirectAttributes For flash messages
     * @return Redirect to products list
     */
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        LocalDate today = LocalDate.now();
        String formattedToday = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        // Set current date as dateArrival for new products
        if (product.getDateArrival() == null) {
            product.setDateArrival(formattedToday);
        }
        
        // Set default expiry date (1 year from now) if not provided
        if (product.getExpiryDate() == null) {
            LocalDate oneYearFromNow = today.plusYears(1);
            product.setExpiryDate(oneYearFromNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        
        // Set default values for other required fields that aren't in the form
        if (product.getOnhandQuantity() == null) {
            product.setOnhandQuantity(product.getQty()); // Initial onhand = current qty
        }
        
        if (product.getOriginalPrice() == null) {
            product.setOriginalPrice(product.getPrice()); // Original price = current price
        }
        
        if (product.getQuantitySold() == null) {
            product.setQuantitySold(0); // No items sold yet
        }
        
        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("success", "Product added successfully");
        
        return "redirect:/products";
    }

    /**
     * Display the edit product form
     * @param id The ID of the product to edit
     * @param model Spring Model object for passing attributes to the view
     * @return The edit product form view template name
     */
    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Optional<Product> productOpt = productService.findProductById(id);
        
        if (productOpt.isPresent()) {
            model.addAttribute("product", productOpt.get());
            model.addAttribute("suppliers", supplierService.findAllSuppliers());
            return "products/form";
        } else {
            return "redirect:/products";
        }
    }

    /**
     * Process the edit product form submission
     * @param id The ID of the product to edit
     * @param product The updated product
     * @param redirectAttributes For flash messages
     * @return Redirect to products list
     */
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product, 
                               RedirectAttributes redirectAttributes) {
        // Get the existing product to preserve necessary fields
        Optional<Product> existingProductOpt = productService.findProductById(id);
        
        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            
            // Preserve dateArrival if it's null in the updated product
            if (product.getDateArrival() == null) {
                product.setDateArrival(existingProduct.getDateArrival());
            }
            
            // Preserve other required fields if they're null
            if (product.getExpiryDate() == null) {
                product.setExpiryDate(existingProduct.getExpiryDate());
            }
            
            if (product.getOnhandQuantity() == null) {
                product.setOnhandQuantity(existingProduct.getOnhandQuantity());
            }
            
            if (product.getOriginalPrice() == null) {
                product.setOriginalPrice(existingProduct.getOriginalPrice());
            }
            
            if (product.getQuantitySold() == null) {
                product.setQuantitySold(existingProduct.getQuantitySold());
            }
        } else {
            // If somehow the product doesn't exist, set defaults
            LocalDate today = LocalDate.now();
            String formattedToday = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            
            if (product.getDateArrival() == null) {
                product.setDateArrival(formattedToday);
            }
            
            if (product.getQuantitySold() == null) {
                product.setQuantitySold(0);
            }
        }
        
        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("success", "Product updated successfully");
        
        return "redirect:/products";
    }

    /**
     * Delete a product
     * @param id The ID of the product to delete
     * @param redirectAttributes For flash messages
     * @return Redirect to products list
     */
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("success", "Product deleted successfully");
        
        return "redirect:/products";
    }

    /**
     * Display the low stock products page
     * @param model Spring Model object for passing attributes to the view
     * @return The low stock products view template name
     */
    @GetMapping("/low-stock")
    public String listLowStockProducts(Model model) {
        List<Product> lowStockProducts = productService.findLowStockProducts();
        model.addAttribute("products", lowStockProducts);
        
        return "products/low-stock";
    }

    /**
     * Display the product inventory statistics page
     * @param model Spring Model object for passing attributes to the view
     * @return The product inventory statistics view template name
     */
    @GetMapping("/stats")
    public String showProductStats(Model model) {
        List<Product> topSellingProducts = productService.findTopSellingProducts();
        BigDecimal totalInventoryValue = productService.calculateTotalInventoryValue();
        
        model.addAttribute("topSellingProducts", topSellingProducts);
        model.addAttribute("totalInventoryValue", totalInventoryValue);
        
        return "products/stats";
    }

    /**
     * Display the product expiry tracking page
     * @param model Spring Model object for passing attributes to the view
     * @return The product expiry tracking view template name
     */
    @GetMapping("/expiry")
    public String listExpiringProducts(Model model) {
        // Get products expiring in the next 30 days
        LocalDate thirtyDaysFromNow = LocalDate.now().plusDays(30);
        String formattedDate = thirtyDaysFromNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        List<Product> expiringProducts = productService.findProductsExpiringBefore(formattedDate);
        model.addAttribute("products", expiringProducts);
        
        return "products/expiry";
    }
}