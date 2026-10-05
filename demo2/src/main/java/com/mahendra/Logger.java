package com.mahendra;
import org.springframework.stereotype.Component;


public class Logger{
    public static void log(String message){
        System.out.println(System.currentTimeMillis()+": "+ message);
    }

    public Logger(){
        System.out.println("Instance of Logger created");
    }
}