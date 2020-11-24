package com.example.demo.exception;

public class IllegalLastNameException extends Exception {
	
	public IllegalLastNameException() {
		super("last name is invalid");
	}
}
