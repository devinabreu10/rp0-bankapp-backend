package dev.abreu.bankapp.service.impl;

import static dev.abreu.bankapp.utils.BankappConstants.ACCOUNT_DEPOSIT;
import static dev.abreu.bankapp.utils.BankappConstants.ACCOUNT_TRANSFER;
import static dev.abreu.bankapp.utils.BankappConstants.ACCOUNT_WITHDRAW;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import dev.abreu.bankapp.dao.AccountDao;
import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.dao.TransactionDao;
import dev.abreu.bankapp.exception.InsufficientFundsException;
import dev.abreu.bankapp.exception.ResourceNotFoundException;
import dev.abreu.bankapp.model.Account;
import dev.abreu.bankapp.model.Transaction;
import dev.abreu.bankapp.service.AccountService;
import dev.abreu.bankapp.utils.ResourceType;

@Service
public class AccountServiceImpl implements AccountService {
	
	private static final Logger log = LogManager.getLogger(AccountServiceImpl.class);
	
	private final AccountDao accountDao;
	private final CustomerDao customerDao;
	private final TransactionDao transactionDao;
	
	public AccountServiceImpl(AccountDao accountDao, CustomerDao customerDao, TransactionDao transactionDao) {
		this.accountDao = accountDao;
		this.customerDao = customerDao;
		this.transactionDao = transactionDao;
	}

	@Override
	public Account getAccountByAcctNo(Long acctNo) {
		log.info("Fetching Account with AcctNo: {}", acctNo);
		return accountDao.findAccountByAcctNo(acctNo)
				.orElseThrow(() -> new ResourceNotFoundException(ResourceType.ACCOUNT, acctNo));
	}

	@Override
	public List<Account> getAllAccountsByUsername(String username) {
		log.info("Fetching all accounts associated with username: {}", username);

		return Optional.of(username)
				.filter(customerDao::existsByUsername)
				.map(accountDao::findAllAccountsByUsername)
				.orElseThrow(() -> new ResourceNotFoundException(ResourceType.ACCOUNT, username));
		
//		if(customerDao.existsByUsername(username)) {
//			return accountDao.findAllAccountsByUsername(username);
//		} else {
//			throw new ResourceNotFoundException(ResourceType.ACCOUNT, username);
//		}
	}

	@Override
	public Account saveAccount(Account account) {
		log.info("Saving new account...");
		return accountDao.saveAccount(account);
	}

	@Override
	public Account updateAccount(Account account) {
		log.info("Updating account details...");
		return accountDao.updateAccount(account);
	}

	@Override
	public boolean deleteAccountByAcctNo(Long acctNo) {
		log.info("Deleting account with account number: {}", acctNo);

		return accountDao.findAccountByAcctNo(acctNo)
				.map(a -> accountDao.deleteAccountByAcctNo(acctNo))
				.orElse(false);

//		boolean success = false;
//
//		if(!accountDao.findAccountByAcctNo(acctNo).equals(Optional.empty())) {
//			success = accountDao.deleteAccountByAcctNo(acctNo);
//		}
//
//		return success;
	}
	
	@Override
	public void transferFundsBetweenAccounts(Long sourceAcctNo, Long targetAcctNo, double amount) throws InsufficientFundsException {
		Optional<Account> source = accountDao.findAccountByAcctNo(sourceAcctNo);
		Optional<Account> target = accountDao.findAccountByAcctNo(targetAcctNo);
		String notes = "Funds were transferred from account number " + sourceAcctNo + " to account number "
						+ targetAcctNo + " to the amount of $"+ amount;
		
		if(Double.compare(source.orElseThrow().getAccountBalance(), amount) < 0) {
			throw new InsufficientFundsException(
					"Account transfer could not be completed due to insufficient funds");
		}
		
		// consider making the Account update and Transaction save in a single Transaction
		// This ensures atomicity and consistency in case of any failures.
		// This would involve an Account_Transaction junction table in the database.
		
		source.orElseThrow().decrementBalance(amount);
		target.orElseThrow().incrementBalance(amount);
		accountDao.updateAccount(source.orElseThrow());
		accountDao.updateAccount(target.orElseThrow());
		
		transactionDao.saveTransaction(new Transaction(ACCOUNT_TRANSFER, amount, 
				notes, source.orElseThrow().getAccountNumber()));
		
		log.info("Transfer successfully completed!");
	}
	
	@Override
	public void depositFundsIntoAccount(Long acctNo, double amount) {
		Optional<Account> account = accountDao.findAccountByAcctNo(acctNo);
		String notes = "$" + amount +" deposited into account with account number " + acctNo;
		
		account.orElseThrow().incrementBalance(amount);
		accountDao.updateAccount(account.orElseThrow());
		
		transactionDao.saveTransaction(new Transaction(ACCOUNT_DEPOSIT, amount, 
				notes, account.orElseThrow().getAccountNumber()));
		
		log.info("Successfully deposited ${} into account with acctNo {}", amount, acctNo);
	}
	
	@Override
	public void withdrawFundsFromAccount(Long acctNo, double amount) throws InsufficientFundsException {
		Optional<Account> account = accountDao.findAccountByAcctNo(acctNo);
		String notes = "$" + amount +" withdrawn from account with account number " + acctNo;
		
		if(Double.compare(account.orElseThrow().getAccountBalance(), amount) < 0) {
			throw new InsufficientFundsException(
					"Account withdrawal could not be completed due to insufficient funds");
		}
		
		account.orElseThrow().decrementBalance(amount);
		accountDao.updateAccount(account.orElseThrow());
		
		transactionDao.saveTransaction(new Transaction(ACCOUNT_WITHDRAW, amount, 
				notes, account.orElseThrow().getAccountNumber()));
		
		log.info("Successfully withdrew ${} from account with acctNo {}", amount, acctNo);
	}

}
