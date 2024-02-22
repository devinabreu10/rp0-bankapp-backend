package dev.abreu.bankapp.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import dev.abreu.bankapp.dao.AccountDao;
import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.exception.InsufficientFundsException;
import dev.abreu.bankapp.model.Account;
import dev.abreu.bankapp.service.AccountService;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Account> getAllAccountsByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account saveAccount(Account account) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account updateAccount(Account account) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteAccountByAcctNo(Long acctNo) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void transferFundsBetweenAccounts(Long sourceAccNo, Long targetAccNo, double amount) throws InsufficientFundsException {
		Account source = accountDao.findAccountByAcctNo(sourceAccNo);
		Account target = accountDao.findAccountByAcctNo(targetAccNo);
		
		if(Double.compare(source.getAccountBalance(), amount) < 0) {
			throw new InsufficientFundsException();
		}
		
		source.decrementBalance(amount);
		target.incrementBalance(amount);
		//transactionDao.saveTransaction();
	}

}
