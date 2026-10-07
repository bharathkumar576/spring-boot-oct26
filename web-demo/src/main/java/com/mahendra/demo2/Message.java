package com.mahendra.demo2;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "message")
public class Message implements java.io.Serializable{

	private String text;
	
	public Message() {
	
	}

	public Message(String text) {
		super();
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
