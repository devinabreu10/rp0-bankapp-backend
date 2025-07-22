package dev.abreu.bankapp.service;

import java.util.List;

import dev.abreu.bankapp.entity.Transaction;

public interface TransactionService {
	
	/**
	 * Retrieves a Transaction from database based on transaction id
	 * 
	 * @param txnId
	 * @return Transaction
	 */
	Transaction getTransactionById(Long txnId);
	
	/**
	 * Retrieves a list of all transactions tied to Account
	 * based on the account number
	 * 
	 * @param acctNo
	 * @return list of Transaction
	 */
	List<Transaction> getAllTransactionsByAcctNo(Long acctNo);
	
	/**
	 * Saves a new Transaction in the database
	 * 
	 * @param txn
	 * @return Transaction
	 */
	Transaction saveTransaction(Transaction txn);
	
	/**
	 * Updates Transaction details
	 * 
	 * @param txn
	 * @return Transaction
	 */
	Transaction updateTransactionDetails(Transaction txn);
	
	/**
	 * Removes a Transaction from database based on transaction id
	 * 
	 * @param txnId
	 * @return boolean
	 */
	boolean deleteTransactionById(Long txnId);

	/**
	 * Retrieves all transactions and transfers associated with a customer ID.
	 *
	 * @param customerId
	 * @return list of Transaction
	 */
	List<Transaction> getAllTransactionsAndTransfersByCustomerId(Long customerId);

}
