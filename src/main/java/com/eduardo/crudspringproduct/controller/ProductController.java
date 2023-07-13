package com.eduardo.crudspringproduct.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eduardo.crudspringproduct.dto.ProductRequestDto;
import com.eduardo.crudspringproduct.dto.ProductResponseDto;
import com.eduardo.crudspringproduct.model.Category;
import com.eduardo.crudspringproduct.model.Product;
import com.eduardo.crudspringproduct.repository.CategoryRepository;
import com.eduardo.crudspringproduct.repository.ProductRepository;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> list(
            @RequestParam(required = false, name = "category") String category) {
        if (category == null) {
            return ResponseEntity.ok(productRepository.findAll().stream().map(ProductResponseDto::convert).collect(Collectors.toList()));
        }

        final Category refCategory = categoryRepository.getReferenceById(category);

        Optional<List<Product>> productsInThatCategory = productRepository.findAllProductsByCategory(refCategory);

        return productsInThatCategory
                .map(productList -> ResponseEntity.ok(productList.stream().map(ProductResponseDto::convert).collect(Collectors.toList())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    ResponseEntity<ProductResponseDto> findById(@PathVariable Integer id) {
        return productRepository.findById(id)
                .map(productRecord -> ResponseEntity.ok(ProductResponseDto.convert(productRecord)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<Object> create(@RequestBody ProductRequestDto product) {
        // Verifies if the category is registered.
        Optional<Category> productCategory = categoryRepository.findById(product.getCategory());

        if (!productCategory.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category does not exists!!");
        }

        Product newProductRecord = ProductRequestDto.convert(product, productCategory.get());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProductResponseDto.convert(productRepository.save(newProductRecord)));
    }

    @PutMapping("/{id}")
    ResponseEntity<ProductResponseDto> update(@PathVariable Integer id, @RequestBody ProductRequestDto product) {
        return productRepository.findById(id).map(productRecord -> {
            final Category productCategory = categoryRepository.getReferenceById(product.getCategory());

            productRecord.setCategory(productCategory);
            productRecord.setDescription(product.getDescription());
            productRecord.setPrice(product.getPrice());
            productRecord.setPurchaseDate(product.getPurchaseDate());

            Product updated = productRepository.save(productRecord);

            return ResponseEntity.ok(ProductResponseDto.convert(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Integer id) {
        return productRepository.findById(id).map(
                productRecord -> {
                    productRepository.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
