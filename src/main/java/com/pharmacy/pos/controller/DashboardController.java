package com.pharmacy.pos.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pharmacy.pos.model.Product;
import com.pharmacy.pos.model.Sales;
import com.pharmacy.pos.service.CustomerService;
import com.pharmacy.pos.service.ProductService;
import com.pharmacy.pos.service.SalesOrderService;
import com.pharmacy.pos.service.SalesService;

/**
 * Controller for handling dashboard-related requests
 */
@Controller
public class DashboardController {

    private final ProductService productService;
    private final CustomerService customerService;
    private final SalesService salesService;
    private final SalesOrderService salesOrderService;

    @Autowired
    public DashboardController(ProductService productService, CustomerService customerService,
                            SalesService salesService, SalesOrderService salesOrderService) {
        this.productService = productService;
        this.customerService = customerService;
        this.salesService = salesService;
        this.salesOrderService = salesOrderService;
    }

    /**
     * Display the dashboard page
     * @param model Spring Model object for passing attributes to the view
     * @return The dashboard view template name
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Add basic statistics to the model
        model.addAttribute("totalCustomers", customerService.countCustomers());
        model.addAttribute("lowStockCount", productService.findLowStockProducts().size());
        
        // Return the dashboard view
        return "dashboard";
    }
    
    /**
     * REST API endpoint for dashboard statistics
     * Used by the dashboard page for AJAX updates
     * @return JSON response with dashboard statistics
     */
    @GetMapping("/api/dashboard/stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Get today's date in the format used by the database (YYYY-MM-DD)
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        // Get today's sales and profit from the database
        BigDecimal todaySales = stringToBigDecimal(salesService.calculateTotalSalesForDate(today));
        BigDecimal todayProfit = stringToBigDecimal(salesService.calculateTotalProfitForDate(today));
        
        // Customer count
        long totalCustomers = customerService.countCustomers();
        
        // Low stock products - these use the fixed getTotalValue() method for accurate calculations
        List<Product> lowStockProducts = productService.findLowStockProducts();
        
        // Recent activities from sales data
        List<Map<String, String>> recentActivities = getRecentActivities();
        
        // Fill the stats map
        stats.put("todaySales", todaySales);
        stats.put("todayProfit", todayProfit);
        stats.put("totalCustomers", totalCustomers);
        stats.put("lowStockCount", lowStockProducts.size());
        stats.put("lowStockProducts", lowStockProducts);
        stats.put("recentActivities", recentActivities);
        
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Helper method to convert a string value to BigDecimal safely
     * Handles null values, empty strings and formatting issues
     * @param value The string value to convert
     * @return The converted BigDecimal or BigDecimal.ZERO if conversion fails
     */
    private BigDecimal stringToBigDecimal(String value) {
        if (value == null || value.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            // Log the error and return zero if conversion fails
            System.err.println("Failed to convert value to BigDecimal: " + value);
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * Helper method to get recent activities from the database
     * @return List of recent activity objects
     */
    private List<Map<String, String>> getRecentActivities() {
        List<Map<String, String>> activities = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // Get recent sales (last 5)
        List<Sales> allSales = salesService.findAllSales();
        List<Sales> recentSales = new ArrayList<>();
        
        // Take only the 5 most recent sales
        if (allSales != null && !allSales.isEmpty()) {
            // Sort by date (most recent first) - assuming newer sales have higher IDs
            allSales.sort((s1, s2) -> Long.compare(s2.getId(), s1.getId()));
            
            // Take up to 5 most recent sales
            for (int i = 0; i < Math.min(5, allSales.size()); i++) {
                recentSales.add(allSales.get(i));
            }
        }
        
        if (!recentSales.isEmpty()) {
            for (Sales sale : recentSales) {
                Map<String, String> activity = new HashMap<>();
                activity.put("description", "Sale completed - Invoice #" + sale.getInvoiceNumber() + 
                             " - Customer: " + sale.getName());
                
                // Format the time display based on the sale date
                try {
                    LocalDate saleDate = LocalDate.parse(sale.getDate(), dateFormatter);
                    long daysBetween = ChronoUnit.DAYS.between(saleDate, today);
                    
                    String timeAgo;
                    if (daysBetween == 0) {
                        timeAgo = "Today";
                    } else if (daysBetween == 1) {
                        timeAgo = "Yesterday";
                    } else {
                        timeAgo = daysBetween + " days ago";
                    }
                    
                    activity.put("time", timeAgo);
                } catch (Exception e) {
                    // Fallback if date parsing fails
                    activity.put("time", sale.getDate());
                }
                
                activities.add(activity);
            }
        }
        
        // Add low stock alert if applicable
        List<Product> lowStockProducts = productService.findLowStockProducts();
        if (lowStockProducts != null && !lowStockProducts.isEmpty()) {
            Map<String, String> activity = new HashMap<>();
            activity.put("description", "Low stock alert - " + lowStockProducts.size() + 
                         " products below threshold");
            activity.put("time", "Today");
            activities.add(activity);
        }
        
        return activities;
    }
}