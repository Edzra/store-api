package com.eduardo.crudspringproduct.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.eduardo.crudspringproduct.model.Category;
import com.eduardo.crudspringproduct.model.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductRequestDto {
    public ProductRequestDto(String name, String category, String description, LocalDate purchaseDate, BigDecimal price) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.purchaseDate = purchaseDate;
        this.price = price;
    }

    private String name;

    private String category;

    private String description;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate purchaseDate;

    private BigDecimal price;

    public static Product convert(ProductRequestDto productDto, Category category) {
        Product product = new Product();

        product.setName(productDto.getName());
        product.setCategory(category);
        product.setDescription(productDto.getDescription());
        product.setPurchaseDate(productDto.getPurchaseDate());
        product.setPrice(productDto.getPrice());

        return product;
    }
}
