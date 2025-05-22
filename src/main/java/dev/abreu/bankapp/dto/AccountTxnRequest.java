package dev.abreu.bankapp.dto;

public record AccountTxnRequest(
        Long accountNumber,
        double amount,
        String notes) {
}
