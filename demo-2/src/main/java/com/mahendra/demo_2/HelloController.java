package com.mahendra.demo_2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class HelloController {
        
    private Author author;

    @Autowired 
    public void setAuthor(Author author) {
        this.author = author;
    }


    @GetMapping( value= "/hello", produces = "text/html")
    public String sayHello() {
        return "<h2>Hello, World!</h2><p>Author: " + author.name() + "</p>";
    }
}
