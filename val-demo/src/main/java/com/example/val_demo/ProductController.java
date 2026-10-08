package com.example.val_demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.Valid;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController 
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable("id") Integer id) {
        if(id > 1 && id <5){
            return new Product(id, "Product " + id, "Description " + id, id * 10.0);
        }
        throw new ProductNotFoundException("Product not found with id: " + id);
    }
    
    @GetMapping
    public List<Product> getAllProducts() {
        // This is just a placeholder. You would typically fetch this from a database.
        return Arrays.asList(
            new Product(1, "Product 1", "Description 1", 10.0),
            new Product(2, "Product 2", "Description 2", 20.0)
        );
    }

    @PostMapping
    public String create(@RequestBody @Valid Product product) {
        return "Product created: " + product.getName();
    }
    
}
