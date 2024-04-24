package dev.abreu.bankapp.dao.impl;

import static dev.abreu.bankapp.utils.BankappConstants.SQL_EXCEPTION_CAUGHT;
import static dev.abreu.bankapp.utils.BankappQueryConstants.CREATE_NEW_ACCOUNT_QUERY;
import static dev.abreu.bankapp.utils.BankappQueryConstants.DELETE_ACCOUNT_BY_ACCTNO_QUERY;
import static dev.abreu.bankapp.utils.BankappQueryConstants.SELECT_ACCOUNTS_BY_ACCTNO_QUERY;
import static dev.abreu.bankapp.utils.BankappQueryConstants.SELECT_ALL_ACCOUNTS_BY_USERNAME_QUERY;
import static dev.abreu.bankapp.utils.BankappQueryConstants.UPDATE_ACCOUNT_QUERY;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	public Optional<Account> findAccountByAcctNo(Long acctNo) {
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
			} else {
				return Optional.empty();
			}
			
		} catch(SQLException e) {
			log.error("SQLException caught: {}", e.getMessage());
		}
		
		return Optional.of(account);
	}

	@Override
	public List<Account> findAllAccountsByUsername(String username) {
		List<Account> accountsList = new ArrayList<>();
		Account account;
		
		try(Connection conn = connUtil.getConnection(); 
				PreparedStatement prepStmt = conn.prepareStatement(SELECT_ALL_ACCOUNTS_BY_USERNAME_QUERY)) {
			
			prepStmt.setString(1, username);
			
			ResultSet rs = prepStmt.executeQuery();
			
			while (rs.next()) {
				account = new Account();
				account.setAccountNumber(rs.getLong("acc_no"));
				account.setAccountType(rs.getString("acc_typ"));
				account.setAccountBalance(rs.getDouble("acc_bal"));
				account.setCustomerId(rs.getLong("cust_id"));
				
				accountsList.add(account);
			}
			
		} catch(SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT, e.getMessage());		
		}
		
		return accountsList;
	}

	@Override
	public Account saveAccount(Account account) {
		
		log.info("Entering saveAccount method...");
		
		try(Connection conn = connUtil.getConnection(); 
				PreparedStatement stmt = conn.prepareStatement(CREATE_NEW_ACCOUNT_QUERY);) {
			
			stmt.setString(1, account.getAccountType());
			stmt.setDouble(2, account.getAccountBalance());
			stmt.setLong(3, account.getCustomerId());
			
			log.info("Create Account Query String: {}", CREATE_NEW_ACCOUNT_QUERY);
			int rowsAffected = stmt.executeUpdate();
			log.info("{} Row(s) Affected", rowsAffected);
			
		} catch (SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT, e.getMessage());
		}
		
		return account;
	}

	@Override
	public Account updateAccount(Account account) {
		
		log.info("Entering updateAccount method...");
		
		try(Connection conn = connUtil.getConnection(); 
				PreparedStatement stmt = conn.prepareStatement(UPDATE_ACCOUNT_QUERY);) {
			
			stmt.setString(1, account.getAccountType());
			stmt.setDouble(2, account.getAccountBalance());
			stmt.setLong(3, account.getAccountNumber());
			
			log.info("Update Account Query String: {}", UPDATE_ACCOUNT_QUERY);
			int updateStatus = stmt.executeUpdate();
			log.info("{} Row(s) Updated", updateStatus);
			
		} catch (SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT, e.getMessage());
		}
		
		return account;
	}

	@Override
	public boolean deleteAccountByAcctNo(Long acctNo) {
		log.info("Entering deleteAccountByAcctNo method...");
		
		boolean success = false;
		
		try(Connection conn = connUtil.getConnection(); 
				PreparedStatement stmt = conn.prepareStatement(DELETE_ACCOUNT_BY_ACCTNO_QUERY);) {
			conn.setAutoCommit(false);
			
			stmt.setLong(1, acctNo);

			log.info("Delete Account Query String: {}", DELETE_ACCOUNT_BY_ACCTNO_QUERY);
			int deleteStatus = stmt.executeUpdate();
			
			if(deleteStatus <= 1) {
				conn.commit();
				log.info("{} Row(s) Deleted", deleteStatus);
				success = true;
			} else {
				conn.rollback();
				log.info("There was an issue with deleteAccountByAcctNo, rolling back changes...");
			}
			
		} catch (SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT, e.getMessage());
		}
		
		return success;
	}

}
