package dev.abreu.bankapp.service;

import dev.abreu.bankapp.entity.Transfer;

/**
 * The TransferService interface provides methods for managing Transfer entities.
 * It defines the business logic operations for transfer-related functionality.
 * 
 * @author Devin Abreu
 */
public interface TransferService {
	
	/**
	 * Retrieves a Transfer from database based on transfer id
	 * 
	 * @param transferId the ID of the transfer to retrieve
	 * @return Transfer entity
	 * @throws dev.abreu.bankapp.exception.ResourceNotFoundException if transfer not found
	 */
	Transfer getTransferById(Long transferId);
	
}