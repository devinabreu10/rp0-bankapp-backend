package dev.abreu.bankapp.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for UnifiedTransactionDetailDTO record.
 *
 * @author Devin Abreu
 */
class UnifiedTransactionDetailDTOTest {

    @Test
    void testUnifiedTransactionDetailDTOCreation() {
        LocalDateTime createdAt = LocalDateTime.parse("2022-01-01T10:30:00");
        Map<String, Object> additionalDetails = new HashMap<>();
        additionalDetails.put("targetAccountNumber", 456L);

        UnifiedTransactionDetailDTO dto = new UnifiedTransactionDetailDTO(
                1L,
                "deposit",
                100.00,
                "test transaction",
                createdAt,
                123L,
                "TRANSACTION",
                additionalDetails
        );

        assertEquals(1L, dto.id());
        assertEquals("deposit", dto.type());
        assertEquals(100.00, dto.amount());
        assertEquals("test transaction", dto.notes());
        assertEquals(createdAt, dto.createdAt());
        assertEquals(123L, dto.accountNumber());
        assertEquals("TRANSACTION", dto.itemType());
        assertEquals(additionalDetails, dto.additionalDetails());
        assertEquals(456L, dto.additionalDetails().get("targetAccountNumber"));
    }

    @Test
    void testUnifiedTransactionDetailDTOEquality() {
        LocalDateTime createdAt = LocalDateTime.parse("2022-01-01T10:30:00");
        Map<String, Object> additionalDetails = new HashMap<>();
        additionalDetails.put("targetAccountNumber", 456L);

        UnifiedTransactionDetailDTO dto1 = new UnifiedTransactionDetailDTO(
                1L, "deposit", 100.00, "test transaction", createdAt, 123L, "TRANSACTION", additionalDetails
        );

        UnifiedTransactionDetailDTO dto2 = new UnifiedTransactionDetailDTO(
                1L, "deposit", 100.00, "test transaction", createdAt, 123L, "TRANSACTION", additionalDetails
        );

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testUnifiedTransactionDetailDTOWithEmptyAdditionalDetails() {
        LocalDateTime createdAt = LocalDateTime.parse("2022-01-01T10:30:00");
        Map<String, Object> emptyDetails = new HashMap<>();

        UnifiedTransactionDetailDTO dto = new UnifiedTransactionDetailDTO(
                2L,
                "withdrawal",
                50.00,
                "ATM withdrawal",
                createdAt,
                789L,
                "TRANSACTION",
                emptyDetails
        );

        assertEquals(2L, dto.id());
        assertEquals("withdrawal", dto.type());
        assertEquals(50.00, dto.amount());
        assertEquals("ATM withdrawal", dto.notes());
        assertEquals(createdAt, dto.createdAt());
        assertEquals(789L, dto.accountNumber());
        assertEquals("TRANSACTION", dto.itemType());
        assertTrue(dto.additionalDetails().isEmpty());
    }
}