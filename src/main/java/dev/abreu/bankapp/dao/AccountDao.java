package dev.abreu.bankapp.dao;

import java.util.List;

import dev.abreu.bankapp.model.Account;

/**
 * Along with using Spring Data JDBC I will also be using the JDBC API
 * for extra practice with both methods
 * 
 * @author Devin Abreu
 *
 */
public interface AccountDao {
	
	Account findAccountByAcctNo(Long acctNo);
	
	List<Account> findAllAccountsByUsername(String username);
	
	Account saveAccount(Account account);
	
	Account updateAccount(Account account);
	
	boolean deleteAccountByAcctNo(Long acctNo);

}
