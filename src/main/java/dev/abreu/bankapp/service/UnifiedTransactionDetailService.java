package dev.abreu.bankapp.service;

import dev.abreu.bankapp.dto.UnifiedTransactionDetailDTO;

/**
 * Service for retrieving unified transaction details that can handle both
 * transactions and transfers through a single interface.
 * 
 * @author Devin Abreu
 */
public interface UnifiedTransactionDetailService {
    
    /**
     * Retrieves unified transaction details for the given ID.
     * The ID can correspond to either a transaction or transfer.
     * 
     * @param id the ID to retrieve details for
     * @param idType the type of the ID
     * @return UnifiedTransactionDetailDTO containing the details
     * @throws dev.abreu.bankapp.exception.ResourceNotFoundException if no transaction or transfer found with the given ID
     */
    UnifiedTransactionDetailDTO getUnifiedTransactionDetailById(Long id, String idType);
}