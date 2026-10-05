package com.mahendra.bootdemo1;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final PaymentService paymentService; // Immutable dependency

    // Spring Boot automatically injects PaymentService here
    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void processOrder() {
        paymentService.pay();
    }
}

