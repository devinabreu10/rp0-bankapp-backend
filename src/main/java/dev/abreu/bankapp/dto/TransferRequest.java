package dev.abreu.bankapp.dto;

/**
 * A data transfer object (DTO) to prepare Transfer object to be sent in HTTP request.
 * Records are immutable and provide a concise syntax for declaring classes
 * that consist only of declared final fields and a few simple methods.
 *
 * @author Devin Abreu
 */
public record TransferRequest(
        Long sourceAccountNumber,
        Long targetAccountNumber,
        double amount) {
}
