package dev.abreu.bankapp.dao;

import java.util.List;
import java.util.Optional;

import dev.abreu.bankapp.entity.Account;

/**
 * The AccountDao interface provides methods for interacting with the Account
 * model in the database. It is responsible for finding, saving, updating, 
 * and deleting Account objects.
 * 
 * @author Devin Abreu
 *
 */
public interface AccountDao {

	/**
	 * Finds an Account by account number.
	 *
	 * @param acctNo the account number to search for
	 * @return an Optional containing the Account if found, or an empty Optional if not found
	 */
	Optional<Account> findAccountByAcctNo(Long acctNo);

	/**
	 * Finds all Accounts associated with a given username.
	 *
	 * @param username the username to search for
	 * @return a List of Account objects associated with the username
	 */
	List<Account> findAllAccountsByUsername(String username);

	/**
	 * Saves a new Account to the database.
	 *
	 * @param account the Account object to save
	 * @return the saved Account object
	 */
	Account saveAccount(Account account);

	/**
	 * Updates an existing Account in the database.
	 *
	 * @param account the Account object to update
	 * @return the updated Account object
	 */
	Account updateAccount(Account account);

	/**
	 * Deletes an Account from the database by account number.
	 *
	 * @param acctNo the account number of the Account to delete
	 * @return true if the Account was deleted, false otherwise
	 */
	boolean deleteAccountByAcctNo(Long acctNo);

	/**
	 * Soft deletes, by setting is_active to false, an Account from the database by account number.
	 *
	 * @param acctNo the account number of the Account to soft delete
	 * @return true if the Account was soft deleted, false otherwise
	 */
	boolean softDeleteAccountByAcctNo(Long acctNo);

	/**
	 * Calls the stored procedure to transfer funds between two accounts
	 * and records the transfer in the database.
	 *
	 * @param sourceAcctNo the account number of the source account
	 * @param targetAcctNo the account number of the target account
	 * @param amount the amount to transfer
	 * @param notes the notes associated with the transfer
	 */
	void transferFunds(Long sourceAcctNo, Long targetAcctNo, Double amount, String notes);
}
