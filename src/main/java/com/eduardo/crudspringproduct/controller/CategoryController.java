package com.eduardo.crudspringproduct.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.eduardo.crudspringproduct.dto.CategoryRequestDto;
import com.eduardo.crudspringproduct.dto.ProductResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eduardo.crudspringproduct.model.Category;
import com.eduardo.crudspringproduct.repository.CategoryRepository;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> list() {
        return categoryRepository.findAll();
    }

    @PostMapping
    ResponseEntity<String> create(@RequestBody CategoryRequestDto categoryRequestDto) {
        Optional<Category> productCategory = categoryRepository.findById(categoryRequestDto.getName());

        if(productCategory.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A category with that name already exists!");
        }

        Category category = new Category();
        category.setName(categoryRequestDto.getName());

        categoryRepository.save(category);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Category created!");
    }
}
