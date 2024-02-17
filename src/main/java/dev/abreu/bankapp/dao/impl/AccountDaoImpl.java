package dev.abreu.bankapp.dao.impl;

import static dev.abreu.bankapp.utils.BankappQueryConstants.SELECT_ACCOUNTS_BY_ACCTNO_QUERY;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import dev.abreu.bankapp.dao.AccountDao;
import dev.abreu.bankapp.model.Account;
import dev.abreu.bankapp.utils.ConnectionUtil;

@Repository
public class AccountDaoImpl implements AccountDao {
	
	private static final Logger log = LogManager.getLogger(AccountDaoImpl.class);
	
	private ConnectionUtil connUtil = ConnectionUtil.getConnectionUtil();

	@Override
	public Account findAccountByAcctNo(Long acctNo) {
		Account account = new Account();
		
		try(Connection conn = connUtil.getConnection(); 
				PreparedStatement prepStmt = conn.prepareStatement(SELECT_ACCOUNTS_BY_ACCTNO_QUERY)) {
			
			prepStmt.setLong(1, acctNo);
			
			ResultSet rs = prepStmt.executeQuery();
			
			if(rs.next()) {
				account.setAccountNumber(rs.getLong("account_number"));
				account.setAccountType(rs.getString("account_type"));
				account.setAccountBalance(rs.getDouble("account_balance"));
				account.setCustomerId(rs.getLong("customer_id"));
			}
			
		} catch(SQLException e) {
			log.error("SQLException caught: {}", e.getMessage());
		}
		
		return account;
	}

	@Override
	public List<Account> findAllAccountsByUsername(String username) {
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
