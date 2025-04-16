package dev.abreu.bankapp.dao;

import java.util.List;
import java.util.Optional;

import dev.abreu.bankapp.entity.Transaction;

/**
 * The TransactionDao interface provides methods for interacting with the
 * Transaction model in the database. It is responsible for finding, saving,
 * updating, and deleting Transaction objects.
 * 
 * @author Devin Abreu
 *
 */
public interface TransactionDao {

	/**
	 * Finds a Transaction by transaction ID.
	 *
	 * @param txnId the ID of the Transaction to search for
	 * @return an Optional containing the Transaction if found, or an empty Optional if not found
	 */
	Optional<Transaction> findTransactionById(Long txnId);

	/**
	 * Finds all Transactions associated with a given account number.
	 *
	 * @param acctNo the account number to search for
	 * @return a List of Transaction objects associated with the account number
	 */
	List<Transaction> findAllTransactionsByAcctNo(Long acctNo);

	/**
	 * Saves a new Transaction to the database.
	 *
	 * @param txn the Transaction object to save
	 * @return the saved Transaction object
	 */
	Transaction saveTransaction(Transaction txn);

	/**
	 * Updates an existing Transaction in the database.
	 *
	 * @param txn the Transaction object to update
	 * @return the updated Transaction object
	 */
	Transaction updateTransaction(Transaction txn);

	/**
	 * Deletes a Transaction by transaction ID.
	 *
	 * @param txnId the ID of the Transaction to delete
	 * @return true if the Transaction was deleted, false otherwise
	 */
	boolean deleteTransactionById(Long txnId);
}
