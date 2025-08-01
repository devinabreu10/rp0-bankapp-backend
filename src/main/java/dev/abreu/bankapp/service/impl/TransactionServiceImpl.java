package dev.abreu.bankapp.service.impl;

import dev.abreu.bankapp.dao.AccountDao;
import dev.abreu.bankapp.dao.TransactionDao;
import dev.abreu.bankapp.entity.Transaction;
import dev.abreu.bankapp.exception.ResourceNotFoundException;
import dev.abreu.bankapp.service.TransactionService;
import dev.abreu.bankapp.util.ResourceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {
	
	private static final Logger log = LogManager.getLogger(TransactionServiceImpl.class);
	
	private final TransactionDao transactionDao;
	private final AccountDao accountDao;

	public TransactionServiceImpl(TransactionDao transactionDao, AccountDao accountDao) {
		this.transactionDao = transactionDao;
		this.accountDao = accountDao;
	}

	@Override
	@Cacheable(value = "transaction", key = "#txnId")
	public Transaction getTransactionById(Long txnId) {
		log.info("Fetching Transaction with id: {}", txnId);
		return transactionDao.findTransactionById(txnId)
				.orElseThrow(() -> new ResourceNotFoundException(ResourceType.TRANSACTION, txnId));
	}

	@Override
	public List<Transaction> getAllTransactionsByAcctNo(Long acctNo) {
		log.info("Fetching all transactions associated with account number: {}", acctNo);
		
		if(accountDao.findAccountByAcctNo(acctNo).isPresent()) {
			return transactionDao.findAllTransactionsByAcctNo(acctNo);
		} else {
			throw new ResourceNotFoundException(ResourceType.TRANSACTION, acctNo);
		}
	}

	@Override
	@CachePut(value = "transaction", key = "#txn.transactionId")
	public Transaction saveTransaction(Transaction txn) {
		log.info("Saving new transaction...");
		return transactionDao.saveTransaction(txn);
	}

	@Override
	@CachePut(value = "transaction", key = "#txn.transactionId")
	public Transaction updateTransactionDetails(Transaction txn) {
		log.info("Updating transaction details...");
		return transactionDao.updateTransaction(txn);
	}

	@Override
	@CacheEvict(value = "transaction", key = "#txnId")
	public boolean deleteTransactionById(Long txnId) {
		log.info("Deleting transaction with id: {}", txnId);
		boolean success = false;
		
		if(!transactionDao.findTransactionById(txnId).equals(Optional.empty())) {
			success = transactionDao.deleteTransactionById(txnId);
		}

		return success;
	}

	@Override
	public List<Transaction> getAllTransactionsAndTransfersByCustomerId(Long customerId) {
        log.info("Fetching all transactions and transfers for customer id: {}", customerId);
        return transactionDao.findAllTransactionsAndTransfersByCustomerId(customerId);
    }

}
