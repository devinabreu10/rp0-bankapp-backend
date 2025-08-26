package dev.abreu.bankapp.service.impl;

import dev.abreu.bankapp.dto.UnifiedTransactionDetailDTO;
import dev.abreu.bankapp.dto.mapper.DtoMapper;
import dev.abreu.bankapp.entity.Transaction;
import dev.abreu.bankapp.entity.Transfer;
import dev.abreu.bankapp.exception.ResourceNotFoundException;
import dev.abreu.bankapp.service.TransactionService;
import dev.abreu.bankapp.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UnifiedTransactionDetailServiceImpl
 * 
 * @author Devin Abreu
 */
@ExtendWith(MockitoExtension.class)
class UnifiedTransactionDetailServiceImplTest {
    
    @Mock
    private TransactionService transactionService;
    
    @Mock
    private TransferService transferService;
    
    @Mock
    private DtoMapper dtoMapper;
    
    @InjectMocks
    private UnifiedTransactionDetailServiceImpl unifiedTransactionDetailService;
    
    private Transaction mockTransaction;
    private Transfer mockTransfer;
    private UnifiedTransactionDetailDTO mockTransactionDto;
    private UnifiedTransactionDetailDTO mockTransferDto;
    
    @BeforeEach
    void setUp() {
        // Setup mock transaction
        mockTransaction = new Transaction();
        mockTransaction.setTransactionId(1L);
        mockTransaction.setTransactionType("Deposit");
        mockTransaction.setTransactionAmount(100.0);
        mockTransaction.setTransactionNotes("Test deposit");
        mockTransaction.setAccountNumber(12345L);
        mockTransaction.setCreatedAt(LocalDateTime.now());
        
        // Setup mock transfer
        mockTransfer = new Transfer();
        mockTransfer.setTransferId(2L);
        mockTransfer.setSourceAccountNumber(12345L);
        mockTransfer.setTargetAccountNumber(67890L);
        mockTransfer.setTransferAmount(50.0);
        mockTransfer.setTransferNotes("Test transfer");
        mockTransfer.setCreatedAt(LocalDateTime.now());
        
        // Setup mock DTOs
        mockTransactionDto = new UnifiedTransactionDetailDTO(
                1L, "Deposit", 100.0, "Test deposit", 
                LocalDateTime.now(), 12345L, "TRANSACTION", Map.of()
        );
        
        mockTransferDto = new UnifiedTransactionDetailDTO(
                2L, "Account Transfer", 50.0, "Test transfer",
                LocalDateTime.now(), 12345L, "TRANSFER", 
                Map.of("targetAccountNumber", 67890L)
        );
    }
    
    @Test
    void testGetUnifiedTransactionDetailById_WithTransactionType_ShouldReturnTransactionDetails() {
        // Arrange
        Long transactionId = 1L;
        String type = "DEPOSIT";
        when(transactionService.getTransactionById(transactionId)).thenReturn(mockTransaction);
        when(dtoMapper.toUnifiedTransactionDetailDto(mockTransaction)).thenReturn(mockTransactionDto);
        
        // Act
        UnifiedTransactionDetailDTO result = unifiedTransactionDetailService.getUnifiedTransactionDetailById(transactionId, type);
        
        // Assert
        assertNotNull(result);
        assertEquals(mockTransactionDto, result);
        assertEquals(1L, result.id());
        assertEquals("Deposit", result.type());
        assertEquals(100.0, result.amount());
        
        verify(transactionService).getTransactionById(transactionId);
        verify(dtoMapper).toUnifiedTransactionDetailDto(mockTransaction);
        verifyNoInteractions(transferService);
    }
    
    @Test
    void testGetUnifiedTransactionDetailById_WithTransferType_ShouldReturnTransferDetails() {
        // Arrange
        Long transferId = 2L;
        String type = "TRANSFER";
        when(transferService.getTransferById(transferId)).thenReturn(mockTransfer);
        when(dtoMapper.toUnifiedTransactionDetailDto(mockTransfer)).thenReturn(mockTransferDto);
        
        // Act
        UnifiedTransactionDetailDTO result = unifiedTransactionDetailService.getUnifiedTransactionDetailById(transferId, type);
        
        // Assert
        assertNotNull(result);
        assertEquals(mockTransferDto, result);
        assertEquals("TRANSFER", result.itemType());
        assertEquals(2L, result.id());
        assertEquals("Account Transfer", result.type());
        assertEquals(50.0, result.amount());
        assertTrue(result.additionalDetails().containsKey("targetAccountNumber"));
        
        verify(transferService).getTransferById(transferId);
        verify(dtoMapper).toUnifiedTransactionDetailDto(mockTransfer);
        verifyNoInteractions(transactionService);
    }
    
    @Test
    void testGetUnifiedTransactionDetailById_WithInvalidType_ShouldThrowIllegalArgumentException() {
        // Arrange
        Long id = 1L;
        String invalidType = "INVALID";
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> unifiedTransactionDetailService.getUnifiedTransactionDetailById(id, invalidType)
        );
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Invalid type"));
        verifyNoInteractions(transactionService);
        verifyNoInteractions(transferService);
        verifyNoInteractions(dtoMapper);
    }
    
    @Test
    void testGetUnifiedTransactionDetailById_WithNullId_ShouldThrowResourceNotFoundException() {
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> unifiedTransactionDetailService.getUnifiedTransactionDetailById(null, "TRANSACTION")
        );
        
        assertNotNull(exception);
        verifyNoInteractions(transactionService);
        verifyNoInteractions(transferService);
        verifyNoInteractions(dtoMapper);
    }
    
    @Test
    void testGetUnifiedTransactionDetailById_WithNullType_ShouldThrowIllegalArgumentException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> unifiedTransactionDetailService.getUnifiedTransactionDetailById(1L, null)
        );
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Type parameter cannot be null"));
        verifyNoInteractions(transactionService);
        verifyNoInteractions(transferService);
        verifyNoInteractions(dtoMapper);
    }
    
    @Test
    void testGetUnifiedTransactionDetailById_WithEmptyType_ShouldThrowIllegalArgumentException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> unifiedTransactionDetailService.getUnifiedTransactionDetailById(1L, "")
        );
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Type parameter cannot be null"));
        verifyNoInteractions(transactionService);
        verifyNoInteractions(transferService);
        verifyNoInteractions(dtoMapper);
    }
    
    @Test
    void testGetUnifiedTransactionDetailById_WhenTransactionServiceThrowsException_ShouldPropagateException() {
        // Arrange
        String type = "TRANSACTION";
        
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> unifiedTransactionDetailService.getUnifiedTransactionDetailById(null, type)
        );
        
        assertNotNull(exception);
        verifyNoInteractions(transferService);
        verifyNoInteractions(dtoMapper);
    }
    
    @Test
    void testGetUnifiedTransactionDetailById_WhenTransferServiceThrowsException_ShouldPropagateException() {
        // Arrange
        Long transferId = 2L;
        String type = "TRANSFER";
        when(transferService.getTransferById(transferId))
                .thenThrow(new ResourceNotFoundException(dev.abreu.bankapp.util.ResourceType.TRANSFER, transferId));
        
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> unifiedTransactionDetailService.getUnifiedTransactionDetailById(transferId, type)
        );
        
        assertNotNull(exception);
        verify(transferService).getTransferById(transferId);
        verifyNoInteractions(transactionService);
        verifyNoInteractions(dtoMapper);
    }
    
    @Test
    void testGetUnifiedTransactionDetailById_WithLowercaseType_ShouldWork() {
        // Arrange
        Long transactionId = 1L;
        String type = "DEPOSIT"; // lowercase
        when(transactionService.getTransactionById(transactionId)).thenReturn(mockTransaction);
        when(dtoMapper.toUnifiedTransactionDetailDto(mockTransaction)).thenReturn(mockTransactionDto);
        
        // Act
        UnifiedTransactionDetailDTO result = unifiedTransactionDetailService.getUnifiedTransactionDetailById(transactionId, type);
        
        // Assert
        assertNotNull(result);
        assertEquals(mockTransactionDto, result);
        verify(transactionService).getTransactionById(transactionId);
        verify(dtoMapper).toUnifiedTransactionDetailDto(mockTransaction);
        verifyNoInteractions(transferService);
    }
    
    @Test
    void testGetUnifiedTransactionDetailById_WithMixedCaseType_ShouldWork() {
        // Arrange
        Long transferId = 2L;
        String type = "Transfer"; // mixed case
        when(transferService.getTransferById(transferId)).thenReturn(mockTransfer);
        when(dtoMapper.toUnifiedTransactionDetailDto(mockTransfer)).thenReturn(mockTransferDto);
        
        // Act
        UnifiedTransactionDetailDTO result = unifiedTransactionDetailService.getUnifiedTransactionDetailById(transferId, type);
        
        // Assert
        assertNotNull(result);
        assertEquals(mockTransferDto, result);
        verify(transferService).getTransferById(transferId);
        verify(dtoMapper).toUnifiedTransactionDetailDto(mockTransfer);
        verifyNoInteractions(transactionService);
    }
}