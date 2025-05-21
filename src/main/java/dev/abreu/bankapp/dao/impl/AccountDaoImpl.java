package dev.abreu.bankapp.dao.impl;

import dev.abreu.bankapp.dao.AccountDao;
import dev.abreu.bankapp.entity.Account;
import dev.abreu.bankapp.util.AccountNumberGenerator;
import dev.abreu.bankapp.util.ConnectionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dev.abreu.bankapp.util.BankappConstants.SQL_EXCEPTION_CAUGHT;
import static dev.abreu.bankapp.util.BankappQueryConstants.*;

@Repository
public class AccountDaoImpl implements AccountDao {

	private static final Logger log = LogManager.getLogger(AccountDaoImpl.class);

	private static final String ACCOUNT_NUMBER = "account_number";
	private static final String NICKNAME = "nickname";
	private static final String TYPE = "account_type";
	private static final String BALANCE = "account_balance";
	private static final String CREATED_AT = "created_at";
	private static final String UPDATED_AT = "updated_at";
	private static final String CUSTOMER_ID = "customer_id";

	private final ConnectionUtil connUtil;

	public AccountDaoImpl(ConnectionUtil connUtil) {
		this.connUtil = connUtil;
	}

	@Override
	public Optional<Account> findAccountByAcctNo(Long acctNo) {
		Account account = new Account();

		try(Connection conn = connUtil.getConnection(); 
				PreparedStatement prepStmt = conn.prepareStatement(SELECT_ACCOUNTS_BY_ACCTNO_QUERY)) {

			prepStmt.setLong(1, acctNo);

			ResultSet resultSet = prepStmt.executeQuery();

			if(resultSet.next()) {
				setAccountFromResultSet(account, resultSet);
			} else {
				return Optional.empty();
			}

		} catch(SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT + "findAccountByAcctNo: {}", e.getMessage());
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
				setAccountFromResultSet(account, rs);
				accountsList.add(account);
			}

		} catch(SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT + "findAllAccountsByUsername: {}", e.getMessage());
		}

		return accountsList;
	}

	@Override
	public Account saveAccount(Account account) {

		log.info("Entering saveAccount method...");

		// Generate a random 8-digit account number and ensure it's unique
		Long accountNumber;
		boolean isUnique;

		do {
			accountNumber = AccountNumberGenerator.generateAccountNumber();
			// Check if this account number already exists
			isUnique = findAccountByAcctNo(accountNumber).isEmpty();
		} while (!isUnique);

		account.setAccountNumber(accountNumber);
		log.info("Generated unique account number: {}", accountNumber);

		try(Connection conn = connUtil.getConnection(); 
				PreparedStatement stmt = conn.prepareStatement(CREATE_NEW_ACCOUNT_QUERY)) {

			stmt.setLong(1, account.getAccountNumber());
			stmt.setString(2, account.getNickname());
			stmt.setString(3, account.getAccountType());
			stmt.setDouble(4, account.getAccountBalance());
			stmt.setTimestamp(5, Timestamp.valueOf(account.getCreatedAt()));
			stmt.setTimestamp(6, Timestamp.valueOf(account.getUpdatedAt()));
			stmt.setLong(7, account.getCustomerId());

			log.info("Create Account Query String: {}", CREATE_NEW_ACCOUNT_QUERY);
			int rowsAffected = stmt.executeUpdate();
			log.info("{} Row(s) Affected", rowsAffected);

		} catch (SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT + "saveAccount: {}", e.getMessage());
		}

		return account;
	}

	@Override
	public Account updateAccount(Account account) {

		log.info("Entering updateAccount method...");

		try(Connection conn = connUtil.getConnection(); 
				PreparedStatement stmt = conn.prepareStatement(UPDATE_ACCOUNT_QUERY)) {

			stmt.setString(1, account.getAccountType());
			stmt.setString(2, account.getNickname());
			stmt.setDouble(3, account.getAccountBalance());
			stmt.setTimestamp(4, Timestamp.valueOf(account.getUpdatedAt()));
			stmt.setLong(5, account.getAccountNumber());

			log.info("Update Account Query String: {}", UPDATE_ACCOUNT_QUERY);
			int updateStatus = stmt.executeUpdate();
			log.info("{} Row(s) Updated", updateStatus);

		} catch (SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT + "updateAccount: {}", e.getMessage());
		}

		return account;
	}

	@Override
	public boolean deleteAccountByAcctNo(Long acctNo) {
		log.info("Entering deleteAccountByAcctNo method...");

		boolean success = false;

		try(Connection conn = connUtil.getConnection(); 
				PreparedStatement stmt = conn.prepareStatement(DELETE_ACCOUNT_BY_ACCTNO_QUERY)) {
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
			log.error(SQL_EXCEPTION_CAUGHT + "deleteAccountByAcctNo: {}", e.getMessage());
		}

		return success;
	}

	private void setAccountFromResultSet(Account account, ResultSet rs) throws SQLException {
		account.setAccountNumber(rs.getLong(ACCOUNT_NUMBER));
		account.setNickname(rs.getString(NICKNAME));
		account.setAccountType(rs.getString(TYPE));
		account.setAccountBalance(rs.getDouble(BALANCE));
		account.setCreatedAt(rs.getTimestamp(CREATED_AT).toLocalDateTime());
		account.setUpdatedAt(rs.getTimestamp(UPDATED_AT).toLocalDateTime());
		account.setCustomerId(rs.getLong(CUSTOMER_ID));
	}

}
