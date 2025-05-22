package dev.abreu.bankapp.service.impl;

import dev.abreu.bankapp.dao.AccountDao;
import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.dao.TransactionDao;
import dev.abreu.bankapp.entity.Account;
import dev.abreu.bankapp.entity.Transaction;
import dev.abreu.bankapp.exception.InsufficientFundsException;
import dev.abreu.bankapp.exception.ResourceNotFoundException;
import dev.abreu.bankapp.service.AccountService;
import dev.abreu.bankapp.util.ResourceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static dev.abreu.bankapp.util.BankappConstants.*;

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
		log.info("Fetching all accounts associated with customer");

		return Optional.of(username)
				.filter(customerDao::existsByUsername)
				.map(accountDao::findAllAccountsByUsername)
				.orElseThrow(() -> new ResourceNotFoundException(ResourceType.ACCOUNT, username));
	}

	@Override
	public Account saveAccount(Account account) {
		log.info("Saving new account...");
		return accountDao.saveAccount(account);
	}

	@Override
	public Account updateAccount(Account account) {
		log.info("Updating account details...");
		account.setUpdatedAt(LocalDateTime.now());
		return accountDao.updateAccount(account);
	}

	@Override
	public boolean deleteAccountByAcctNo(Long acctNo) {
		log.info("Deleting account with account number: {}", acctNo);

		return accountDao.findAccountByAcctNo(acctNo)
				.map(a -> accountDao.deleteAccountByAcctNo(acctNo))
				.orElse(false);
	}

	@Override
	public void transferFundsBetweenAccounts(Long sourceAcctNo, Long targetAcctNo, double amount, String notes) throws InsufficientFundsException {
		Optional<Account> source = accountDao.findAccountByAcctNo(sourceAcctNo);

		if (sourceAcctNo.equals(targetAcctNo)) {
			throw new IllegalArgumentException("Source and target accounts cannot be the same. Please try again.");
		}

		if(Double.compare(source.orElseThrow().getAccountBalance(), amount) < 0) {
			throw new InsufficientFundsException(
					"Account transfer could not be completed due to insufficient funds");
		}

		accountDao.transferFunds(sourceAcctNo, targetAcctNo, amount, notes);
		
		log.info("Transfer successfully completed!");
	}

	@Transactional
	@Override
	public void depositFundsIntoAccount(Long acctNo, double amount, String notes) {
		Optional<Account> account = accountDao.findAccountByAcctNo(acctNo);
		
		account.orElseThrow().incrementBalance(amount);
		account.orElseThrow().setUpdatedAt(LocalDateTime.now());
		accountDao.updateAccount(account.orElseThrow());
		
		transactionDao.saveTransaction(new Transaction(ACCOUNT_DEPOSIT, amount, 
				notes, account.orElseThrow().getAccountNumber()));
		
		log.info("Successfully deposited ${} into account with acctNo {}", amount, acctNo);
	}

	@Transactional
	@Override
	public void withdrawFundsFromAccount(Long acctNo, double amount, String notes) throws InsufficientFundsException {
		Optional<Account> account = accountDao.findAccountByAcctNo(acctNo);
		
		if(Double.compare(account.orElseThrow().getAccountBalance(), amount) < 0) {
			throw new InsufficientFundsException(
					"Account withdrawal could not be completed due to insufficient funds");
		}
		
		account.orElseThrow().decrementBalance(amount);
		account.orElseThrow().setUpdatedAt(LocalDateTime.now());
		accountDao.updateAccount(account.orElseThrow());
		
		transactionDao.saveTransaction(new Transaction(ACCOUNT_WITHDRAW, amount, 
				notes, account.orElseThrow().getAccountNumber()));
		
		log.info("Successfully withdrew ${} from account with acctNo {}", amount, acctNo);
	}

}
