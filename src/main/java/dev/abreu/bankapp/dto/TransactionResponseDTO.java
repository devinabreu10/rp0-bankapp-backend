package dev.abreu.bankapp.dto;

import java.time.LocalDateTime;

/**
 * A data transfer object (DTO) for a transaction response.
 *
 * @author Devin Abreu
 */
public record TransactionResponseDTO(
        Long transactionId,
        String transactionType,
        double transactionAmount,
        String transactionNotes,
        LocalDateTime createdAt,
        Long accountNumber) {
}
