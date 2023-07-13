package com.eduardo.crudspringproduct.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.eduardo.crudspringproduct.model.Category;
import com.eduardo.crudspringproduct.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<List<Product>> findAllProductsByCategory(@Param("category") Category category);
}
