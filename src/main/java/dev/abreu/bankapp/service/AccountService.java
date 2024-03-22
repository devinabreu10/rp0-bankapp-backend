package dev.abreu.bankapp.service;

import java.util.List;

import dev.abreu.bankapp.exception.InsufficientFundsException;
import dev.abreu.bankapp.model.Account;

public interface AccountService {
	
	/**
	 * Retrieves an Account from database based on account number
	 * 
	 * @param acctNo
	 * @return Account
	 * @throws ResourceNotFoundException if Account with acctNo is not found
	 */
	Account getAccountByAcctNo(Long acctNo);
	
	/**
	 * Retrieves a list of all accounts tied to a Customer 
	 * based on their username
	 * 
	 * @param username
	 * @return
	 */
	List<Account> getAllAccountsByUsername(String username);
	
	/**
	 * Saves a new Account in the database
	 * 
	 * @param account
	 * @return Account
	 */
	Account saveAccount(Account account);
	
	/**
	 * Updates Account details
	 * 
	 * @param account
	 * @return Account
	 */
	Account updateAccount(Account account);
	
	/**
	 * Removes an Account from database based on account number
	 * 
	 * @param acctNo
	 * @return boolean
	 */
	boolean deleteAccountByAcctNo(Long acctNo);

	void transferFundsBetweenAccounts(Long sourceAcctNo, Long targetAcctNo, double amount)
			throws InsufficientFundsException;

	void depositFundsIntoAccount(Long acctNo, double amount);

	void withdrawFundsFromAccount(Long acctNo, double amount) throws InsufficientFundsException;

}
