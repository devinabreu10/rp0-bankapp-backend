package dev.abreu.bankapp.dto;

/**
 * A data transfer object (DTO) for a registration request.
 * Records are immutable and provide a concise syntax for declaring classes 
 * that consist only of declared final fields and a few simple methods.
 * 
 * @author Devin Abreu
 * 
 */
public record RegisterRequest(
		String firstName,
		String lastName,
		String address,
		String username,
		String password) {}
