package dev.abreu.bankapp.dao.impl;

import dev.abreu.bankapp.dao.TransactionDao;
import dev.abreu.bankapp.entity.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dev.abreu.bankapp.util.BankappConstants.SQL_EXCEPTION_CAUGHT;
import static dev.abreu.bankapp.util.BankappQueryConstants.*;

@Repository
public class TransactionDaoImpl implements TransactionDao {
	
	private static final Logger log = LogManager.getLogger(TransactionDaoImpl.class);

	private static final String TRANSACTION_ID = "transaction_id";
	private static final String TYPE = "transaction_type";
	private static final String AMOUNT = "transaction_amount";
	private static final String NOTES = "transaction_notes";
	private static final String ACCT_NO = "account_number";
	private static final String CREATED_AT = "created_at";

	private final DataSource dataSource;

	public TransactionDaoImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Optional<Transaction> findTransactionById(Long txnId) {
		Transaction transaction = new Transaction();
		
		try(Connection conn = dataSource.getConnection();
				PreparedStatement prepStmt = conn.prepareStatement(SELECT_TRANSACTIONS_BY_ID_QUERY)) {
			
			prepStmt.setLong(1, txnId);
			
			ResultSet resultSet = prepStmt.executeQuery();
			
			if(resultSet.next()) {
				setTransactionFromResultSet(transaction, resultSet);
			} else {
				return Optional.empty();
			}
			
		} catch(SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT + "findTransactionById: {}", e.getMessage());
		}
		
		return Optional.of(transaction);
	}

	@Override
	public List<Transaction> findAllTransactionsByAcctNo(Long acctNo) {
		List<Transaction> transactionsList = new ArrayList<>();
		Transaction transaction;
		
		try(Connection conn = dataSource.getConnection();
				PreparedStatement prepStmt = conn.prepareStatement(SELECT_ALL_TRANSACTIONS_BY_ACCTNO_QUERY)) {
			
			prepStmt.setLong(1, acctNo);
			
			ResultSet rs = prepStmt.executeQuery();
			
			while (rs.next()) {
				transaction = new Transaction();
				setTransactionFromResultSet(transaction, rs);
				transactionsList.add(transaction);
			}
			
		} catch(SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT + "findAllTransactionsByAcctNo: {}", e.getMessage());
		}
		
		return transactionsList;
	}

	@Override
	public Transaction saveTransaction(Transaction txn) {
		log.info("Entering saveTransaction method...");
		
		try(Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(CREATE_NEW_TRANSACTION_QUERY)) {
			
			stmt.setString(1, txn.getTransactionType());
			stmt.setDouble(2, txn.getTransactionAmount());
			stmt.setString(3, txn.getTransactionNotes());
			stmt.setTimestamp(4, Timestamp.valueOf(txn.getCreatedAt()));
			stmt.setLong(5, txn.getAccountNumber());
			
			log.info("Create Transaction Query String: {}", CREATE_NEW_TRANSACTION_QUERY);
			int rowsAffected = stmt.executeUpdate();
			log.info("{} Row(s) Affected", rowsAffected);
			
		} catch (SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT + "saveTransaction: {}", e.getMessage());
		}
		
		return txn;
	}

	@Override
	public Transaction updateTransaction(Transaction txn) {
		log.info("Entering updateTransaction method...");
		
		try(Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(UPDATE_TRANSACTION_QUERY)) {
			
			stmt.setString(1, txn.getTransactionType());
			stmt.setDouble(2, txn.getTransactionAmount());
			stmt.setString(3, txn.getTransactionNotes());
			stmt.setLong(4, txn.getTransactionId());
			
			log.info("Update Transaction Query String: {}", UPDATE_TRANSACTION_QUERY);
			int updateStatus = stmt.executeUpdate();
			log.info("{} Row(s) Updated", updateStatus);
			
		} catch (SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT + "updateTransaction: {}", e.getMessage());
		}
		
		return txn;
	}

	@Override
	public boolean deleteTransactionById(Long txnId) {
		log.info("Entering deleteTransactionById method...");
		
		boolean success = false;
		
		try(Connection conn = dataSource.getConnection();
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
			log.error(SQL_EXCEPTION_CAUGHT + "deleteTransactionById: {}", e.getMessage());
		}
		
		return success;
	}

	@Override
	public List<Transaction> findAllTransactionsAndTransfersByCustomerId(Long customerId) {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(SELECT_ALL_TRANSACTIONS_AND_TRANSFERS_BY_CUSTOMER_ID_QUERY)) {
            prepStmt.setLong(1, customerId);
            prepStmt.setLong(2, customerId);
            ResultSet resultSet = prepStmt.executeQuery();
            while (resultSet.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(resultSet.getLong("id"));
                transaction.setTransactionType(resultSet.getString("type"));
                transaction.setTransactionAmount(resultSet.getDouble("amount"));
                transaction.setTransactionNotes(resultSet.getString("notes"));
                transaction.setCreatedAt(resultSet.getTimestamp(CREATED_AT).toLocalDateTime());
                transaction.setAccountNumber(resultSet.getLong(ACCT_NO));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            log.error(SQL_EXCEPTION_CAUGHT, e);
        }
        return transactions;
    }

	private void setTransactionFromResultSet(Transaction transaction, ResultSet rs) throws SQLException {
		transaction.setTransactionId(rs.getLong(TRANSACTION_ID));
		transaction.setTransactionType(rs.getString(TYPE));
		transaction.setTransactionAmount(rs.getDouble(AMOUNT));
		transaction.setTransactionNotes(rs.getString(NOTES));
		transaction.setAccountNumber(rs.getLong(ACCT_NO));
		transaction.setCreatedAt(rs.getTimestamp(CREATED_AT).toLocalDateTime());
	}

}
