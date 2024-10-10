package dev.abreu.bankapp.dao.impl;

import dev.abreu.bankapp.dao.TransactionDao;
import dev.abreu.bankapp.model.Transaction;
import dev.abreu.bankapp.utils.ConnectionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dev.abreu.bankapp.utils.BankappConstants.SQL_EXCEPTION_CAUGHT;
import static dev.abreu.bankapp.utils.BankappQueryConstants.*;

@Repository
public class TransactionDaoImpl implements TransactionDao {
	
	private static final Logger log = LogManager.getLogger(TransactionDaoImpl.class);

	private final ConnectionUtil connUtil;

	public TransactionDaoImpl(ConnectionUtil connUtil) {
		this.connUtil = connUtil;
	}

	@Override
	public Optional<Transaction> findTransactionById(Long txnId) {
		Transaction transaction = new Transaction();
		
		try(Connection conn = connUtil.getConnection(); 
				PreparedStatement prepStmt = conn.prepareStatement(SELECT_TRANSACTIONS_BY_ID_QUERY)) {
			
			prepStmt.setLong(1, txnId);
			
			ResultSet rs = prepStmt.executeQuery();
			
			if(rs.next()) {
				transaction.setTransactionId(rs.getLong("transaction_id"));
				transaction.setTransactionType(rs.getString("transaction_type"));
				transaction.setTransactionAmount(rs.getDouble("transaction_amount"));
				transaction.setTransactionNotes(rs.getString("transaction_notes"));
				transaction.setAccountNumber(rs.getLong("account_number"));
				Timestamp ts = rs.getTimestamp("transaction_date");
				LocalDateTime localDtTime = ts.toLocalDateTime();
				transaction.setTransactionDate(localDtTime);
			} else {
				return Optional.empty();
			}
			
		} catch(SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT, e.getMessage());
		}
		
		return Optional.of(transaction);
	}

	@Override
	public List<Transaction> findAllTransactionsByAcctNo(Long acctNo) {
		List<Transaction> transactionsList = new ArrayList<>();
		Transaction transaction;
		
		try(Connection conn = connUtil.getConnection(); 
				PreparedStatement prepStmt = conn.prepareStatement(SELECT_ALL_TRANSACTIONS_BY_ACCTNO_QUERY)) {
			
			prepStmt.setLong(1, acctNo);
			
			ResultSet rs = prepStmt.executeQuery();
			
			while (rs.next()) {
				transaction = new Transaction();
				transaction.setTransactionId(rs.getLong("transaction_id"));
				transaction.setTransactionType(rs.getString("transaction_type"));
				transaction.setTransactionAmount(rs.getDouble("transaction_amount"));
				transaction.setTransactionNotes(rs.getString("transaction_notes"));
				transaction.setAccountNumber(rs.getLong("account_number"));
				Timestamp ts = rs.getTimestamp("transaction_date");
				LocalDateTime localDtTime = ts.toLocalDateTime();
				transaction.setTransactionDate(localDtTime);
				
				transactionsList.add(transaction);
			}
			
		} catch(SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT, e.getMessage());		
		}
		
		return transactionsList;
	}

	@Override
	public Transaction saveTransaction(Transaction txn) {
		log.info("Entering saveTransaction method...");
		
		try(Connection conn = connUtil.getConnection(); 
				PreparedStatement stmt = conn.prepareStatement(CREATE_NEW_TRANSACTION_QUERY)) {
			
			stmt.setString(1, txn.getTransactionType());
			stmt.setDouble(2, txn.getTransactionAmount());
			stmt.setString(3, txn.getTransactionNotes());
			stmt.setTimestamp(4, Timestamp.valueOf(txn.getTransactionDate()));
			stmt.setLong(5, txn.getAccountNumber());
			
			log.info("Create Transaction Query String: {}", CREATE_NEW_TRANSACTION_QUERY);
			int rowsAffected = stmt.executeUpdate();
			log.info("{} Row(s) Affected", rowsAffected);
			
		} catch (SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT, e.getMessage());
		}
		
		return txn;
	}

	@Override
	public Transaction updateTransaction(Transaction txn) {
		log.info("Entering updateTransaction method...");
		
		try(Connection conn = connUtil.getConnection(); 
				PreparedStatement stmt = conn.prepareStatement(UPDATE_TRANSACTION_QUERY)) {
			
			stmt.setString(1, txn.getTransactionType());
			stmt.setDouble(2, txn.getTransactionAmount());
			stmt.setString(3, txn.getTransactionNotes());
			stmt.setLong(4, txn.getTransactionId());
			
			log.info("Update Transaction Query String: {}", UPDATE_TRANSACTION_QUERY);
			int updateStatus = stmt.executeUpdate();
			log.info("{} Row(s) Updated", updateStatus);
			
		} catch (SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT, e.getMessage());
		}
		
		return txn;
	}

	@Override
	public boolean deleteTransactionById(Long txnId) {
		log.info("Entering deleteTransactionById method...");
		
		boolean success = false;
		
		try(Connection conn = connUtil.getConnection(); 
				PreparedStatement stmt = conn.prepareStatement(DELETE_TRANSACTION_BY_ID_QUERY)) {
			conn.setAutoCommit(false);
			
			stmt.setLong(1, txnId);

			log.info("Delete Transaction Query String: {}", DELETE_TRANSACTION_BY_ID_QUERY);
			int deleteStatus = stmt.executeUpdate();
			
			if(deleteStatus <= 1) {
				conn.commit();
				log.info("{} Row(s) Deleted", deleteStatus);
				success = true;
			} else {
				conn.rollback();
				log.info("There was an issue with deleteTransactionById, rolling back changes...");
			}
			
		} catch (SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT, e.getMessage());
		}
		
		return success;
	}

}
