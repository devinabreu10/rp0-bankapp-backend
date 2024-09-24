package dev.abreu.bankapp.dto;

/**
 * Account DTO (Data Transfer Object) to prepare Account object to be sent in HTTP
 * request and received in a response
 *
 * @author Devin Abreu
 */
public record AccountDTO(
        Long accountNumber,
        String accountType,
        double accountBalance,
        Long customerId) {
}
