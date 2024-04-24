package dev.abreu.bankapp.dto;

/**
 * Customer DTO (Data Transfer Object) to prepare Customer object to be sent in HTTP
 * request and received in a response
 * 
 * @author Devin Abreu
 */
public record CustomerDTO(
        Long id,
        String firstName,
        String lastName,
        String username,
        String address) {}
