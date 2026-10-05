package com.mahendra.bootdemo1;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    public void pay() {
        System.out.println("Payment processed successfully.");
    }
}