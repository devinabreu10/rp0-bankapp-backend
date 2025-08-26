package dev.abreu.bankapp.dto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Unified Transaction Detail DTO (Data Transfer Object) that can represent both
 * transactions and transfers in a consistent format.
 *
 * @author Devin Abreu
 */
public record UnifiedTransactionDetailDTO(
        Long id,
        String type,
        double amount,
        String notes,
        LocalDateTime createdAt,
        Long accountNumber,
        String itemType,
        Map<String, Object> additionalDetails) {
}