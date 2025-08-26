package dev.abreu.bankapp.service.impl;

import dev.abreu.bankapp.dto.UnifiedTransactionDetailDTO;
import dev.abreu.bankapp.dto.mapper.DtoMapper;
import dev.abreu.bankapp.entity.Transaction;
import dev.abreu.bankapp.entity.Transfer;
import dev.abreu.bankapp.exception.ResourceNotFoundException;
import dev.abreu.bankapp.service.TransactionService;
import dev.abreu.bankapp.service.TransferService;
import dev.abreu.bankapp.service.UnifiedTransactionDetailService;
import dev.abreu.bankapp.util.ResourceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Implementation of UnifiedTransactionDetailService that orchestrates
 * the retrieval of transaction or transfer details based on ID type detection.
 * 
 * @author Devin Abreu
 */
@Service
public class UnifiedTransactionDetailServiceImpl implements UnifiedTransactionDetailService {
    
    private static final Logger log = LogManager.getLogger(UnifiedTransactionDetailServiceImpl.class);
    
    private final TransactionService transactionService;
    private final TransferService transferService;
    private final DtoMapper dtoMapper;
    
    /**
     * Constructor for UnifiedTransactionDetailServiceImpl.
     * 
     * @param transactionService service for transaction operations
     * @param transferService service for transfer operations
     * @param dtoMapper mapper for converting entities to DTOs
     */
    public UnifiedTransactionDetailServiceImpl(
            TransactionService transactionService,
            TransferService transferService,
            DtoMapper dtoMapper) {
        this.transactionService = transactionService;
        this.transferService = transferService;
        this.dtoMapper = dtoMapper;
    }
    
    @Override
    @Cacheable(value = "unified-transaction-detail", key = "#id + '_' + #idType")
    public UnifiedTransactionDetailDTO getUnifiedTransactionDetailById(Long id, String idType) {
        log.info("Retrieving unified transaction detail for ID: {} with type: {}", id, idType);
        
        if (id == null) {
            log.warn("Null ID provided to getUnifiedTransactionDetailById");
            throw new ResourceNotFoundException(ResourceType.TRANSACTION, null);
        }
        
        if (idType == null || idType.trim().isEmpty()) {
            log.warn("Null or empty type provided to getUnifiedTransactionDetailById");
            throw new IllegalArgumentException("Type parameter cannot be null or empty");
        }

        return switch (idType.toUpperCase()) {
            case "DEPOSIT", "WITHDRAW" -> handleTransactionDetail(id);
            case "TRANSFER" -> handleTransferDetail(id);
            default -> {
                log.warn("Invalid type '{}' provided for ID: {}", idType, id);
                throw new IllegalArgumentException("Invalid type: " + idType + ". Must be 'DEPOSIT', 'WITHDRAW' or 'TRANSFER'");
            }
        };
    }
    
    /**
     * Handles retrieval and transformation of transaction details.
     * 
     * @param id the transaction ID
     * @return UnifiedTransactionDetailDTO for the transaction
     */
    private UnifiedTransactionDetailDTO handleTransactionDetail(Long id) {
        log.debug("Retrieving transaction details for ID: {}", id);
        Transaction transaction = transactionService.getTransactionById(id);
        UnifiedTransactionDetailDTO dto = dtoMapper.toUnifiedTransactionDetailDto(transaction);
        log.debug("Successfully transformed transaction to unified DTO for ID: {}", id);
        return dto;
    }
    
    /**
     * Handles retrieval and transformation of transfer details.
     * 
     * @param id the transfer ID
     * @return UnifiedTransactionDetailDTO for the transfer
     */
    private UnifiedTransactionDetailDTO handleTransferDetail(Long id) {
        log.debug("Retrieving transfer details for ID: {}", id);
        Transfer transfer = transferService.getTransferById(id);
        UnifiedTransactionDetailDTO dto = dtoMapper.toUnifiedTransactionDetailDto(transfer);
        log.debug("Successfully transformed transfer to unified DTO for ID: {}", id);
        return dto;
    }
}