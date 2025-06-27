package dev.abreu.bankapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccountTxnRequest(
        @JsonProperty("sourceAccountNumber")
        Long accountNumber,
        double amount,
        String notes) {
}
