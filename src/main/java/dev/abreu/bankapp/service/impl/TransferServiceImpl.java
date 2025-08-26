package dev.abreu.bankapp.service.impl;

import dev.abreu.bankapp.dao.TransferDao;
import dev.abreu.bankapp.entity.Transfer;
import dev.abreu.bankapp.exception.ResourceNotFoundException;
import dev.abreu.bankapp.service.TransferService;
import dev.abreu.bankapp.util.ResourceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Implementation of TransferService that provides business logic for Transfer operations.
 * Includes caching support and proper error handling.
 * 
 * @author Devin Abreu
 */
@Service
public class TransferServiceImpl implements TransferService {
	
	private static final Logger log = LogManager.getLogger(TransferServiceImpl.class);
	
	private final TransferDao transferDao;

	/**
	 * Constructor for TransferServiceImpl.
	 * 
	 * @param transferDao the DAO for transfer data access
	 */
	public TransferServiceImpl(TransferDao transferDao) {
		this.transferDao = transferDao;
	}

	@Override
	@Cacheable(value = "transfer", key = "#transferId")
	public Transfer getTransferById(Long transferId) {
		log.info("Fetching Transfer with id: {}", transferId);
		return transferDao.findTransferById(transferId)
				.orElseThrow(() -> new ResourceNotFoundException(ResourceType.TRANSFER, transferId));
	}
	
}