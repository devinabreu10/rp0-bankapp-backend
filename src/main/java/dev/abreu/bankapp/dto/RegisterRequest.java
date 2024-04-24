package dev.abreu.bankapp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

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
		@NotEmpty(message = "Username cannot be null nor empty")
		String username,
		@Size(min = 8, message = "Password must be at least 8 characters long")
		@NotEmpty(message = "Password cannot be null nor empty")
		String password) {}
