package com.eduardo.crudspringproduct.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.eduardo.crudspringproduct.model.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductResponseDto {

    public ProductResponseDto(Integer id, String name, String category, String description, LocalDate purchaseDate,
            BigDecimal price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.purchaseDate = purchaseDate;
        this.price = price;
    }

    private String name;

    @JsonProperty("_id")
    private Integer id;

    private String category;

    private String description;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate purchaseDate;

    private BigDecimal price;

    public static ProductResponseDto convert(Product product) {
        return new ProductResponseDto(product.getId(), product.getName(), product.getCategory().getName(), product.getDescription(),
                product.getPurchaseDate(), product.getPrice());
    }
}
