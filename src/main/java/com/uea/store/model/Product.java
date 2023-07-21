package com.uea.store.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("_id")
    private Integer id;

    @Column(length = 50)
    private String name;

    @Column(length = 100)
    private String description;

    @Column
    private LocalDate purchaseDate;

    @Column
    private BigDecimal price;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category")
    private Category category;
}
