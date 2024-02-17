package dev.abreu.bankapp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.abreu.bankapp.model.Account;
import dev.abreu.bankapp.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

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

}
