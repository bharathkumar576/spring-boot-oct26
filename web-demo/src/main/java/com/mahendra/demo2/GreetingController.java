package com.mahendra.demo2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greet")
public class GreetingController {

	/*
	 * @GetMapping(produces="application/json") public String sayHelloJSON() {
	 * return "{'msg','Hello World' }"; }
	 * 
	 * @GetMapping(produces="application/xml") public String sayHelloXML() { return
	 * "<msg>Hello World</msg>"; }
	 */
	
	@GetMapping(produces= { "application/json", "application/xml" })
	public Message sayHello() {
		return new Message("Hello World");
	}
	
	@GetMapping(produces="text/plain")
	public String sayHelloOther() {
		return "Hello World";
	}
	
	
	
	
	
}
