package dev.abreu.bankapp.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import dev.abreu.bankapp.dao.AccountDao;
import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.exception.InsufficientFundsException;
import dev.abreu.bankapp.exception.ResourceNotFoundException;
import dev.abreu.bankapp.model.Account;
import dev.abreu.bankapp.service.AccountService;
import dev.abreu.bankapp.utils.ResourceType;

@Service
public class AccountServiceImpl implements AccountService {
	
	private static final Logger log = LogManager.getLogger(AccountServiceImpl.class);
	
	private AccountDao accountDao;
	private CustomerDao customerDao;
	//private TransactionDao transactionDao;
	
	public AccountServiceImpl(AccountDao accountDao, CustomerDao customerDao) {
		this.accountDao = accountDao;
		this.customerDao = customerDao;
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
		
		if(customerDao.existsByUsername(username)) {
			return accountDao.findAllAccountsByUsername(username);
		} else {
			throw new ResourceNotFoundException(ResourceType.ACCOUNT, username);
		}
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
		boolean success = false;
		
		if(accountDao.findAccountByAcctNo(acctNo).orElseThrow().getAccountNumber() != 0) {
			success = accountDao.deleteAccountByAcctNo(acctNo);
		}

		return success;
	}
	
	public void transferFundsBetweenAccounts(Long sourceAccNo, Long targetAccNo, double amount) throws InsufficientFundsException {
		Optional<Account> source = accountDao.findAccountByAcctNo(sourceAccNo);
		Optional<Account> target = accountDao.findAccountByAcctNo(targetAccNo);
		
		if(Double.compare(source.orElseThrow().getAccountBalance(), amount) < 0) {
			throw new InsufficientFundsException();
		}
		
		source.orElseThrow().decrementBalance(amount);
		target.orElseThrow().incrementBalance(amount);
		//transactionDao.saveTransaction();
		
		log.info("Transfer successfully completed!");
	}

}
