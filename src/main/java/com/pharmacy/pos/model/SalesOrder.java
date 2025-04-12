package com.pharmacy.pos.model;

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
@Table(name = "sales_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @Column(name = "invoice", nullable = false)
    private String invoice;

    @Column(name = "product", nullable = false)
    private String product;

    @Column(nullable = false)
    private String qty;

    @Column(nullable = false)
    private String amount;

    @Column(nullable = false)
    private String profit;

    @Column(name = "product_code", nullable = false)
    private String productCode;

    @Column(name = "gen_name", nullable = false)
    private String genericName;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(nullable = false)
    private String price;

    @Column(nullable = false)
    private String discount;

    @Column(nullable = false)
    private String date;
}