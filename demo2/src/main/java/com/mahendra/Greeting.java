package com.mahendra;

public class Greeting{

    private SPLogger log;
    
    public Greeting(SPLogger log){
        this.log = log ;
    }

    public void sayHello(String name){
        log.log("Hello, " + name + "!");
    }
}