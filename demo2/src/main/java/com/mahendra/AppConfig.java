package com.mahendra;

import java.beans.BeanProperty;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.mahendra")
public class AppConfig {

    @Bean
    public Logger logger() {
        return new Logger();
    }

    @Bean
    public SPLogger spLogger(Logger logger) {
        return new SPLogger(logger);
    }

    @Bean
    public Greeting greeting(SPLogger spLogger) {
        return new Greeting(spLogger);
    }

}