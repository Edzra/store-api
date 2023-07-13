package com.eduardo.crudspringproduct;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.eduardo.crudspringproduct.model.Category;
import com.eduardo.crudspringproduct.model.Product;
import com.eduardo.crudspringproduct.repository.CategoryRepository;
import com.eduardo.crudspringproduct.repository.ProductRepository;

@SpringBootApplication
public class CrudSpringProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudSpringProductApplication.class, args);
	}
}
