package com.uea.store.controller;

import com.uea.store.model.Category;
import com.uea.store.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    ResponseEntity<String> create(@RequestBody Category category) {
        Optional<Category> newCategory = categoryRepository.findById(category.getName());

        if(newCategory.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A category with that name already exists!");
        }

        Category _category = new Category();
        _category.setName(category.getName());

        categoryRepository.save(_category);

        return ResponseEntity.status(HttpStatus.CREATED).body("Category created!");
    }

}
