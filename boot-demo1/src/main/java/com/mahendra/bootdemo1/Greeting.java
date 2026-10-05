package com.mahendra.bootdemo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Greeting {

    private final Logger logger;

    @Autowired
    public Greeting(Logger logger) {
        this.logger = logger;
    }

    public void sayHello(String name) {
        logger.log("Hello " + name);
    }
}
