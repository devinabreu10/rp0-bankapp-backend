package dev.abreu.bankapp.dto;

import jakarta.validation.constraints.NotEmpty;

/**
 * A data transfer object (DTO) for a login request.
 * Records are immutable and provide a concise syntax for declaring classes 
 * that consist only of declared final fields and a few simple methods.
 * 
 * @author Devin Abreu
 * 
 */
public record LoginRequest(
		@NotEmpty(message = "Username cannot be null nor empty")
		String username,
		@NotEmpty(message = "Password cannot be null nor empty")
		String password) {}
