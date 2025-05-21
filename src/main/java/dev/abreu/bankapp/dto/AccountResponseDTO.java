package dev.abreu.bankapp.dto;

import java.time.LocalDateTime;

/**
 * A data transfer object (DTO) for an account response.
 *
 * @author Devin Abreu
 */
public record AccountResponseDTO(
        Long accountNumber,
        String nickname,
        String accountType,
        double accountBalance,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
