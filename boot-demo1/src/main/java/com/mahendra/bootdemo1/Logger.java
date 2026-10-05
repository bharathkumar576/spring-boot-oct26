package com.mahendra.bootdemo1;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Logger {
    public void log(String message) {
        System.out.println( LocalDate.now().toString() +" "+ message);
    }
}
