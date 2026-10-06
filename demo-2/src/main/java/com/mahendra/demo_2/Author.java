package com.mahendra.demo_2;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "author")
public record Author(String name, String email) {
    
}
