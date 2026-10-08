package com.example.jdbc_demo;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class JdbcDemoApplication {

	// @Bean 
	// public JdbcTemplate template(DataSource ds){
	// 	return new JdbcTemplate(ds);
	// }

	public static void main(String[] args) {
		SpringApplication.run(JdbcDemoApplication.class, args);
	}

}
