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
@Table(name = "collection")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String invoice;

    @Column(nullable = false)
    private String amount;

    @Column(nullable = false)
    private String remarks;

    @Column(nullable = false)
    private Integer balance;
}