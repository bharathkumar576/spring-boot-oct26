package com.example.val_demo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class Product {

    @NotNull(message = "Product ID cannot be null") 
    private int productId;

    @NotEmpty(message = "Product name cannot be empty")
    private String name;

    private String description;

    @Positive(message = "Price must be positive") 
    private double price;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Product() {
    }
    public Product(int productId, String name, String description, double price) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
