package com.eduardo.crudspringproduct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eduardo.crudspringproduct.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {}
