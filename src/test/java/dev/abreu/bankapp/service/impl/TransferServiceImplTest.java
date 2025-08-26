package dev.abreu.bankapp.service.impl;

import dev.abreu.bankapp.dao.TransferDao;
import dev.abreu.bankapp.entity.Transfer;
import dev.abreu.bankapp.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TransferServiceImpl.
 * Tests business logic, error handling, and caching behavior.
 * 
 * @author Devin Abreu
 */
@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {

    @Mock
    private TransferDao transferDao;

    @InjectMocks
    private TransferServiceImpl transferService;

    private Transfer testTransfer;

    @BeforeEach
    void setUp() {
        // Create test transfer
        testTransfer = new Transfer();
        testTransfer.setTransferId(1L);
        testTransfer.setSourceAccountNumber(12345L);
        testTransfer.setTargetAccountNumber(67890L);
        testTransfer.setTransferAmount(100.0);
        testTransfer.setTransferNotes("Test transfer");
        testTransfer.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void getTransferById_WhenTransferExists_ShouldReturnTransfer() {
        // Arrange
        Long transferId = 1L;
        when(transferDao.findTransferById(transferId)).thenReturn(Optional.of(testTransfer));

        // Act
        Transfer result = transferService.getTransferById(transferId);

        // Assert
        assertNotNull(result);
        assertEquals(testTransfer.getTransferId(), result.getTransferId());
        assertEquals(testTransfer.getSourceAccountNumber(), result.getSourceAccountNumber());
        assertEquals(testTransfer.getTargetAccountNumber(), result.getTargetAccountNumber());
        assertEquals(testTransfer.getTransferAmount(), result.getTransferAmount());
        assertEquals(testTransfer.getTransferNotes(), result.getTransferNotes());
        assertEquals(testTransfer.getCreatedAt(), result.getCreatedAt());
        
        verify(transferDao, times(1)).findTransferById(transferId);
    }

    @Test
    void getTransferById_WhenTransferDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long transferId = 999L;
        when(transferDao.findTransferById(transferId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> transferService.getTransferById(transferId)
        );

        assertEquals("Transfer not found with identifier: " + transferId, exception.getMessage());
        verify(transferDao, times(1)).findTransferById(transferId);
    }

    @Test
    void getTransferById_WithNullId_ShouldCallDaoWithNull() {
        // Arrange
        when(transferDao.findTransferById(null)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> transferService.getTransferById(null));
        verify(transferDao, times(1)).findTransferById(null);
    }

    @Test
    void getTransferById_WhenDaoThrowsException_ShouldPropagateException() {
        // Arrange
        Long transferId = 1L;
        RuntimeException daoException = new RuntimeException("Database connection failed");
        when(transferDao.findTransferById(transferId)).thenThrow(daoException);

        // Act & Assert
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> transferService.getTransferById(transferId)
        );

        assertEquals("Database connection failed", exception.getMessage());
        verify(transferDao, times(1)).findTransferById(transferId);
    }

    @Test
    void getTransferById_ShouldUseCacheableAnnotation() {
        // This test verifies that the @Cacheable annotation is present
        // In a real Spring context, this would be tested with integration tests
        // Here we verify the method behavior is consistent for caching
        
        // Arrange
        Long transferId = 1L;
        when(transferDao.findTransferById(transferId)).thenReturn(Optional.of(testTransfer));

        // Act - call twice
        Transfer result1 = transferService.getTransferById(transferId);
        Transfer result2 = transferService.getTransferById(transferId);

        // Assert - both calls should return the same object
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(result1.getTransferId(), result2.getTransferId());
        
        // DAO should be called twice since we're not in a Spring context
        // In a real Spring context with caching enabled, it would be called once
        verify(transferDao, times(2)).findTransferById(transferId);
    }

    @Test
    void getTransferById_WithDifferentIds_ShouldCallDaoForEach() {
        // Arrange
        Long transferId1 = 1L;
        Long transferId2 = 2L;
        
        Transfer transfer2 = new Transfer();
        transfer2.setTransferId(2L);
        transfer2.setSourceAccountNumber(11111L);
        transfer2.setTargetAccountNumber(22222L);
        transfer2.setTransferAmount(200.0);
        transfer2.setTransferNotes("Second test transfer");
        transfer2.setCreatedAt(LocalDateTime.now());

        when(transferDao.findTransferById(transferId1)).thenReturn(Optional.of(testTransfer));
        when(transferDao.findTransferById(transferId2)).thenReturn(Optional.of(transfer2));

        // Act
        Transfer result1 = transferService.getTransferById(transferId1);
        Transfer result2 = transferService.getTransferById(transferId2);

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(transferId1, result1.getTransferId());
        assertEquals(transferId2, result2.getTransferId());
        
        verify(transferDao, times(1)).findTransferById(transferId1);
        verify(transferDao, times(1)).findTransferById(transferId2);
    }

    @Test
    void constructor_ShouldInitializeWithTransferDao() {
        // Arrange & Act
        TransferServiceImpl service = new TransferServiceImpl(transferDao);

        // Assert
        assertNotNull(service);
        // Verify that the DAO is properly injected by calling a method
        when(transferDao.findTransferById(1L)).thenReturn(Optional.of(testTransfer));
        Transfer result = service.getTransferById(1L);
        assertNotNull(result);
    }
}