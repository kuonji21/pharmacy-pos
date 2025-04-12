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
@Table(name = "supliers") // Using the original table name with typo from database
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "suplier_id")
    private Long id;

    @Column(name = "suplier_name", nullable = false)
    private String supplierName;

    @Column(name = "suplier_address", nullable = false)
    private String supplierAddress;

    @Column(name = "suplier_contact", nullable = false)
    private String supplierContact;

    @Column(name = "contact_person", nullable = false)
    private String contactPerson;

    @Column(nullable = false)
    private String note;
}