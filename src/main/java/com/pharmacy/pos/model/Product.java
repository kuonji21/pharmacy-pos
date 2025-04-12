package com.pharmacy.pos.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_code", nullable = false)
    private String productCode;

    @Column(name = "gen_name", nullable = false)
    private String genericName;

    @Column(name = "product_name", nullable = false)
    private String productName;
    
    @Column(name = "category")
    private String category;

    @Column(nullable = false)
    private String cost;

    @Column(name = "o_price", nullable = false)
    private String originalPrice;

    @Column(nullable = false)
    private String price;

    @Column(nullable = false)
    private String profit;

    @Column(nullable = false)
    private String supplier;

    @Column(name = "onhand_qty", nullable = false)
    private Integer onhandQuantity;

    @Column(nullable = false)
    private Integer qty;

    @Column(name = "qty_sold", nullable = false)
    private Integer quantitySold;

    @Column(name = "expiry_date", nullable = false)
    private String expiryDate;

    @Column(name = "date_arrival", nullable = false)
    private String dateArrival;
    
    @Column(name = "reorder_level")
    private Integer reorderLevel;
    
    @Column(name = "description", length = 1000)
    private String description;
    
    // Calculated field
    public BigDecimal getTotalValue() {
        try {
            BigDecimal priceValue = new BigDecimal(price);
            return priceValue.multiply(new BigDecimal(qty));
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
    
    // Low stock indicator
    public boolean isLowStock() {
        return qty < 10;
    }
    
    // Getter for template compatibility with qtySold field
    public Integer getQtySold() {
        return this.quantitySold;
    }
}