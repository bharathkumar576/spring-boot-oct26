package com.mahendra.bootdemo1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class BootDemo1Application implements CommandLineRunner {

	@Autowired	
	private Greeting greeting;

	public static void main(String[] args) {
		SpringApplication.run(BootDemo1Application.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		// Start using Injected Beans .....
		greeting.sayHello("World");
	}

}
