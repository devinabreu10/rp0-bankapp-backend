package dev.abreu.bankapp.dto;

/**
 * A data transfer object (DTO) for an account response.
 *
 * @author Devin Abreu
 */
public record AccountResponseDTO(
        Long accountNumber,
        String accountType,
        double accountBalance) {
}
