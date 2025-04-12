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
@Table(name = "purchases_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchasesItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "qty", nullable = false)
    private Integer qty;

    @Column(name = "cost", nullable = false)
    private String cost;

    @Column(name = "invoice", nullable = false)
    private String invoice;
}