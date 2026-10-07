package com.mahendra.demo4;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

public class ShoppingCart {
    private double totalAmount;
    private List<Product> items;

    public ShoppingCart() {
        this.totalAmount = 0.0;
        this.items = new ArrayList<>();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void addItem(Product product) {
        items.add(product);
        totalAmount += product.getPrice();
    }
}