package dev.abreu.bankapp.dto;

/**
 * A data transfer object (DTO) for a customer's response.
 * Records are immutable and provide a concise syntax for declaring classes 
 * that consist only of declared final fields and a few simple methods.
 * 
 * @author Devin Abreu
 * 
 */
public record CustomerResponseDTO(
        String firstName,
        String lastName,
        String username,
        String address,
        String token) {}
