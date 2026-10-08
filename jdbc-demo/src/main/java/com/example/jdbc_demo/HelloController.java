package com.example.jdbc_demo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @Autowired 
    private JdbcTemplate template;

    @GetMapping("/hello")
    public String sayHello() {
        String sql = "create table products(prodId int, name nvarchar(20));";

        template.execute(sql);

        return "Hello, World!";
    }
}
