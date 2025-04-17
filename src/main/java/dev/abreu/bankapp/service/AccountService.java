package dev.abreu.bankapp.service;

import java.util.List;

import dev.abreu.bankapp.exception.InsufficientFundsException;
import dev.abreu.bankapp.exception.ResourceNotFoundException;
import dev.abreu.bankapp.entity.Account;

/**
 * Provides methods for managing accounts in the banking system.
 */
public interface AccountService {
	
	/**
	 * Retrieves an Account from database based on account number
	 * 
	 * @param acctNo the account number to search for
	 * @return the account associated with the provided account number
	 * @throws ResourceNotFoundException if Account with acctNo is not found
	 */
	Account getAccountByAcctNo(Long acctNo);
	
	/**
	 * Retrieves a list of all accounts tied to a Customer 
	 * based on their username
	 * 
	 * @param username the username of the customer to retrieve accounts for
	 * @return a list of accounts associated with the customer
	 */
	List<Account> getAllAccountsByUsername(String username);
	
	/**
	 * Saves a new Account in the database
	 * 
	 * @param account the account details to create a new account with
	 * @return the newly created account
	 */
	Account saveAccount(Account account);
	
	/**
	 * Updates Account details
	 * 
	 * @param account the updated account details
	 * @return the updated account
	 */
	Account updateAccount(Account account);
	
	/**
	 * Removes an Account from database based on account number
	 * 
	 * @param acctNo the account number to delete
	 * @return true if the account was successfully deleted, false otherwise
	 */
	boolean deleteAccountByAcctNo(Long acctNo);

	/**
	 * Transfers funds from one account to another.
	 *
	 * @param sourceAcctNo the account number to transfer funds from
	 * @param targetAcctNo the account number to transfer funds to
	 * @param amount the amount to transfer
	 * @throws InsufficientFundsException if the source account does not have sufficient funds for transfer
	 */
	void transferFundsBetweenAccounts(Long sourceAcctNo, Long targetAcctNo, double amount) throws InsufficientFundsException;

	/**
	 * Deposits funds into an account.
	 *
	 * @param acctNo the account number to deposit funds into
	 * @param amount the amount to deposit
	 */
	void depositFundsIntoAccount(Long acctNo, double amount);

	/**
	 * Withdraws funds from an account.
	 *
	 * @param acctNo the account number to withdraw funds from
	 * @param amount the amount to withdraw
	 * @throws InsufficientFundsException if the account does not have sufficient funds for the withdrawal
	 */
	void withdrawFundsFromAccount(Long acctNo, double amount) throws InsufficientFundsException;

}
