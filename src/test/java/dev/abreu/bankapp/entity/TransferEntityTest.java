package dev.abreu.bankapp.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransferEntityTest {

    @Test
    void testTransferEntityValidation() {
        // Test default constructor
        Transfer transfer = new Transfer();
        assertNull(transfer.getTransferId());
        assertNull(transfer.getSourceAccountNumber());
        assertNull(transfer.getTargetAccountNumber());
        assertEquals(0.0, transfer.getTransferAmount());
        assertNull(transfer.getTransferNotes());
        assertNull(transfer.getCreatedAt());

        // Test constructor with parameters
        Long sourceAccount = 12345L;
        Long targetAccount = 67890L;
        double amount = 100.00;
        String notes = "Test transfer";
        
        Transfer transferWithParams = new Transfer(sourceAccount, targetAccount, amount, notes);
        assertNull(transferWithParams.getTransferId());
        assertEquals(sourceAccount, transferWithParams.getSourceAccountNumber());
        assertEquals(targetAccount, transferWithParams.getTargetAccountNumber());
        assertEquals(amount, transferWithParams.getTransferAmount());
        assertEquals(notes, transferWithParams.getTransferNotes());
        assertNotNull(transferWithParams.getCreatedAt());

        // Test constructor with ID
        Long transferId = 1L;
        Transfer transferWithId = new Transfer(transferId, sourceAccount, targetAccount, amount, notes);
        assertEquals(transferId, transferWithId.getTransferId());
        assertEquals(sourceAccount, transferWithId.getSourceAccountNumber());
        assertEquals(targetAccount, transferWithId.getTargetAccountNumber());
        assertEquals(amount, transferWithId.getTransferAmount());
        assertEquals(notes, transferWithId.getTransferNotes());
        assertNotNull(transferWithId.getCreatedAt());
    }

    @Test
    void testTransferEntityFieldMapping() {
        Transfer transfer = new Transfer();
        Long transferId = 1L;
        Long sourceAccount = 12345L;
        Long targetAccount = 67890L;
        double amount = 250.75;
        String notes = "Monthly transfer";
        LocalDateTime createdAt = LocalDateTime.now();

        // Test setters
        transfer.setTransferId(transferId);
        transfer.setSourceAccountNumber(sourceAccount);
        transfer.setTargetAccountNumber(targetAccount);
        transfer.setTransferAmount(amount);
        transfer.setTransferNotes(notes);
        transfer.setCreatedAt(createdAt);

        // Test getters
        assertEquals(transferId, transfer.getTransferId());
        assertEquals(sourceAccount, transfer.getSourceAccountNumber());
        assertEquals(targetAccount, transfer.getTargetAccountNumber());
        assertEquals(amount, transfer.getTransferAmount());
        assertEquals(notes, transfer.getTransferNotes());
        assertEquals(createdAt, transfer.getCreatedAt());

        // Test toString method
        String expectedToString = "Transfer [transferId=" + transferId + ", sourceAccountNumber=" + sourceAccount
                + ", targetAccountNumber=" + targetAccount + ", transferAmount=" + amount
                + ", transferNotes=" + notes + ", createdAt=" + createdAt + "]";
        assertEquals(expectedToString, transfer.toString());
    }

}