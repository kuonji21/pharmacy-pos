package com.pharmacy.pos.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pharmacy.pos.model.Product;
import com.pharmacy.pos.model.Sales;
import com.pharmacy.pos.model.SalesOrder;
import com.pharmacy.pos.service.CustomerService;
import com.pharmacy.pos.service.ProductService;
import com.pharmacy.pos.service.SalesOrderService;
import com.pharmacy.pos.service.SalesService;

/**
 * Controller for handling sales-related requests
 */
@Controller
@RequestMapping("/sales")
public class SalesController {

    private final SalesService salesService;
    private final SalesOrderService salesOrderService;
    private final ProductService productService;
    private final CustomerService customerService;

    @Autowired
    public SalesController(SalesService salesService, SalesOrderService salesOrderService,
                          ProductService productService, CustomerService customerService) {
        this.salesService = salesService;
        this.salesOrderService = salesOrderService;
        this.productService = productService;
        this.customerService = customerService;
    }

    /**
     * Display the sales list page
     * @param model Spring Model object for passing attributes to the view
     * @return The sales view template name
     */
    @GetMapping
    public String listSales(Model model) {
        List<Sales> sales = salesService.findAllSales();
        
        model.addAttribute("sales", sales);
        model.addAttribute("todaysSales", salesService.findSalesByDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        model.addAttribute("totalSales", salesService.calculateTotalSales());
        model.addAttribute("totalProfit", salesService.calculateTotalProfit());
        
        return "sales/list";
    }

    /**
     * Display the create new sale page
     * @param model Spring Model object for passing attributes to the view
     * @return The new sale form view template name
     */
    @GetMapping("/new")
    public String showNewSaleForm(@RequestParam(required = false) String invoiceNumber, Model model) {
        Sales sales = new Sales();
        
        // If invoice number was passed from dashboard, use it
        if (invoiceNumber != null && !invoiceNumber.isEmpty()) {
            sales.setInvoiceNumber(invoiceNumber);
        }
        
        model.addAttribute("products", productService.findAllProducts());
        model.addAttribute("customers", customerService.findAllCustomers());
        model.addAttribute("sales", sales);
        model.addAttribute("salesOrder", new SalesOrder());
        
        return "sales/new";
    }

    /**
     * Process the new sale form submission
     * @param sales The sales transaction to create
     * @param redirectAttributes For flash messages
     * @return Redirect to sales list
     */
    @PostMapping("/new")
    public String createSale(@ModelAttribute Sales sales, 
                            @RequestParam("productIds") List<Long> productIds,
                            @RequestParam("quantities") List<Integer> quantities,
                            RedirectAttributes redirectAttributes) {
        
        // Create sales record
        sales.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Sales savedSales = salesService.saveSales(sales);
        
        // Create sales order records for each product
        for (int i = 0; i < productIds.size(); i++) {
            if (quantities.get(i) > 0) {
                Long productId = productIds.get(i);
                Integer quantity = quantities.get(i);
                
                Optional<Product> productOpt = productService.findProductById(productId);
                if (productOpt.isPresent()) {
                    Product product = productOpt.get();
                    
                    // Create sales order record
                    SalesOrder salesOrder = new SalesOrder();
                    salesOrder.setInvoice(savedSales.getInvoiceNumber());
                    salesOrder.setProduct(product.getProductName());
                    salesOrder.setQty(quantity.toString());
                    salesOrder.setAmount(calculateAmount(product.getPrice(), quantity));
                    salesOrder.setProfit(calculateProfit(product.getPrice(), product.getCost(), quantity));
                    salesOrder.setProductCode(product.getProductCode());
                    salesOrder.setGenericName(product.getGenericName());
                    salesOrder.setName(savedSales.getName());
                    salesOrder.setPrice(product.getPrice());
                    salesOrder.setDiscount("0");
                    salesOrder.setDate(savedSales.getDate());
                    
                    salesOrderService.saveSalesOrder(salesOrder);
                    
                    // Update product inventory
                    productService.recordSale(product.getProductCode(), quantity);
                }
            }
        }
        
        redirectAttributes.addFlashAttribute("success", "Sale created successfully");
        return "redirect:/sales";
    }

    /**
     * Display the sale details page
     * @param invoiceNumber The invoice number of the sale to view
     * @param model Spring Model object for passing attributes to the view
     * @return The sale details view template name
     */
    @GetMapping("/details/{invoiceNumber}")
    public String viewSaleDetails(@PathVariable String invoiceNumber, Model model) {
        Optional<Sales> salesOpt = salesService.findSalesByInvoiceNumber(invoiceNumber);
        
        if (salesOpt.isPresent()) {
            Sales sales = salesOpt.get();
            List<SalesOrder> salesOrders = salesOrderService.findSalesOrdersByInvoice(invoiceNumber);
            
            model.addAttribute("sales", sales);
            model.addAttribute("salesOrders", salesOrders);
            model.addAttribute("totalItems", salesOrders.size());
            model.addAttribute("totalAmount", calculateTotalAmount(salesOrders));
            model.addAttribute("totalProfit", calculateTotalProfit(salesOrders));
            
            return "sales/details";
        } else {
            return "redirect:/sales";
        }
    }

    /**
     * Delete a sale
     * @param id The ID of the sale to delete
     * @param redirectAttributes For flash messages
     * @return Redirect to sales list
     */
    @GetMapping("/delete/{id}")
    public String deleteSale(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Sales> salesOpt = salesService.findSalesById(id);
        
        if (salesOpt.isPresent()) {
            Sales sales = salesOpt.get();
            String invoiceNumber = sales.getInvoiceNumber();
            
            // Delete all associated sales orders
            List<SalesOrder> salesOrders = salesOrderService.findSalesOrdersByInvoice(invoiceNumber);
            for (SalesOrder salesOrder : salesOrders) {
                salesOrderService.deleteSalesOrder(salesOrder.getId());
            }
            
            // Delete the sales record
            salesService.deleteSales(id);
            redirectAttributes.addFlashAttribute("success", "Sale deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Sale not found");
        }
        
        return "redirect:/sales";
    }

    /**
     * Display sales report page
     * @param model Spring Model object for passing attributes to the view
     * @return The sales report view template name
     */
    @GetMapping("/report")
    public String showSalesReport(Model model) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        String todayStr = today.format(formatter);
        String thisWeekStart = today.minusDays(today.getDayOfWeek().getValue() - 1).format(formatter);
        String thisMonthStart = today.withDayOfMonth(1).format(formatter);
        
        model.addAttribute("todaySales", salesService.findSalesByDate(todayStr));
        model.addAttribute("weekSales", salesService.findSalesByDateRange(thisWeekStart, todayStr));
        model.addAttribute("monthSales", salesService.findSalesByDateRange(thisMonthStart, todayStr));
        
        model.addAttribute("todayTotal", salesOrderService.findTotalSalesByDate(todayStr));
        model.addAttribute("todayProfit", salesOrderService.findTotalProfitByDate(todayStr));
        
        model.addAttribute("topSellingProducts", productService.findTopSellingProducts());
        model.addAttribute("lowStockProducts", productService.findLowStockProducts());
        
        return "sales/report";
    }

    /**
     * Search for sales by date range, cashier, or invoice number
     * @param searchType The type of search (date, cashier, invoice)
     * @param searchTerm The search term
     * @param model Spring Model object for passing attributes to the view
     * @return The sales list view with filtered results
     */
    @GetMapping("/search")
    public String searchSales(@RequestParam("searchType") String searchType,
                             @RequestParam("searchTerm") String searchTerm,
                             Model model) {
        List<Sales> sales = null;
        
        switch (searchType) {
            case "date":
                sales = salesService.findSalesByDate(searchTerm);
                break;
            case "cashier":
                sales = salesService.findSalesByCashier(searchTerm);
                break;
            case "invoice":
                Optional<Sales> saleOpt = salesService.findSalesByInvoiceNumber(searchTerm);
                sales = saleOpt.map(List::of).orElse(List.of());
                break;
            case "customer":
                sales = salesService.findSalesByCustomerName(searchTerm);
                break;
            default:
                sales = salesService.findAllSales();
                break;
        }
        
        model.addAttribute("sales", sales);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchTerm", searchTerm);
        
        return "sales/list";
    }
    
    /**
     * Calculate the total amount for a product in a sale
     * @param price The product price
     * @param quantity The quantity sold
     * @return The total amount as a String
     */
    private String calculateAmount(String price, Integer quantity) {
        try {
            double priceValue = Double.parseDouble(price);
            return String.valueOf(priceValue * quantity);
        } catch (NumberFormatException e) {
            return "0";
        }
    }
    
    /**
     * Calculate the profit for a product in a sale
     * @param price The selling price
     * @param cost The product cost
     * @param quantity The quantity sold
     * @return The profit as a String
     */
    private String calculateProfit(String price, String cost, Integer quantity) {
        try {
            double priceValue = Double.parseDouble(price);
            double costValue = Double.parseDouble(cost);
            return String.valueOf((priceValue - costValue) * quantity);
        } catch (NumberFormatException e) {
            return "0";
        }
    }
    
    /**
     * Calculate the total amount for all products in a sale
     * @param salesOrders The list of sales order items
     * @return The total amount
     */
    private double calculateTotalAmount(List<SalesOrder> salesOrders) {
        return salesOrders.stream()
                .mapToDouble(order -> {
                    try {
                        return Double.parseDouble(order.getAmount());
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                })
                .sum();
    }
    
    /**
     * Calculate the total profit for all products in a sale
     * @param salesOrders The list of sales order items
     * @return The total profit
     */
    private double calculateTotalProfit(List<SalesOrder> salesOrders) {
        return salesOrders.stream()
                .mapToDouble(order -> {
                    try {
                        return Double.parseDouble(order.getProfit());
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                })
                .sum();
    }
}