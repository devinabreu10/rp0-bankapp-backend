package dev.abreu.bankapp.dto;

/**
 * A data transfer object (DTO) for a login request.
 * Records are immutable and provide a concise syntax for declaring classes 
 * that consist only of declared final fields and a few simple methods.
 * 
 * @author Devin Abreu
 * 
 */
public record LoginRequest(
		String username,
		String password) {}
