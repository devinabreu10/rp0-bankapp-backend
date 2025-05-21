package dev.abreu.bankapp.dto;

/**
 * Transaction DTO (Data Transfer Object) to prepare Transaction object to be sent in HTTP
 * request and received in a response
 *
 * @author Devin Abreu
 */
public record TransactionDTO(
        Long transactionId,
        String transactionType,
        double transactionAmount,
        String transactionNotes,
        Long accountNumber) {
}
