package dev.abreu.bankapp.dao;

import java.util.List;
import java.util.Optional;

import dev.abreu.bankapp.model.Transaction;

/**
 * Along with using Spring Data JDBC I will also be using the JDBC API
 * for extra practice with both methods
 * 
 * @author Devin Abreu
 *
 */
public interface TransactionDao {
	
	Optional<Transaction> findTransactionById(Long txnId);
	
	List<Transaction> findAllTransactionsByAcctNo(Long acctNo);
	
	Transaction saveTransaction(Transaction txn);
	
	Transaction updateTransaction(Transaction txn);
	
	boolean deleteTransactionById(Long txnId);

}
