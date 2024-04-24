package dev.abreu.bankapp.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import dev.abreu.bankapp.dao.impl.TransactionDaoImpl;
import dev.abreu.bankapp.model.Transaction;
import dev.abreu.bankapp.utils.ConnectionUtil;

@ExtendWith(MockitoExtension.class)
class TransactionDaoTest {
	
	@Mock
	private ConnectionUtil connectionUtilMock;

	@Mock
	private Connection connectionMock;
	
	@Mock
	private Statement statementMock;

	@Mock
	private PreparedStatement preparedStatementMock;

	@Mock
	private ResultSet resultSetMock;

	private TransactionDaoImpl transactionDao;

	@BeforeEach
	public void setup() {
		when(connectionUtilMock.getConnection()).thenReturn(connectionMock);
		transactionDao = new TransactionDaoImpl();
		ReflectionTestUtils.setField(transactionDao, "connUtil", connectionUtilMock);
	}

    @Test
    void testFindTransactionById() throws SQLException {
        Long transactionId = 1L;
        Transaction expectedTransaction = new Transaction("Account Deposit", 100.00, "notes", 12345L);
        LocalDateTime testDateTime = LocalDateTime.of(2024, 4, 24, 15, 30);
        expectedTransaction.setTransactionId(transactionId);
        expectedTransaction.setTransactionDate(testDateTime);

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getLong("transaction_id")).thenReturn(1L);
		when(resultSetMock.getString("transaction_type")).thenReturn("Account Deposit");
		when(resultSetMock.getDouble("transaction_amount")).thenReturn(100.00);
		when(resultSetMock.getString("transaction_notes")).thenReturn("notes");
		when(resultSetMock.getLong("account_number")).thenReturn(12345L);
		when(resultSetMock.getTimestamp("transaction_date")).thenReturn(Timestamp.valueOf(testDateTime));

        Optional<Transaction> result = transactionDao.findTransactionById(transactionId);

        assertTrue(result.isPresent());
        assertEquals(expectedTransaction.getTransactionId(), result.get().getTransactionId());
    }
  
    @Test
    void testFindTransactionByIdEmptyOptional() throws SQLException {
        Long transactionId = 1L;

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(false);

        Optional<Transaction> result = transactionDao.findTransactionById(transactionId);

        assertEquals(Optional.empty(), result);
    }

    @Test
    void testFindTransactionByIdSQLException() throws SQLException {
        Long transactionId = 1L;

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenThrow(SQLException.class);

        Optional<Transaction> result = transactionDao.findTransactionById(transactionId);

        assertNotNull(result);
    }
    
    @Test
    void testFindAllTransactionsByAcctNo() throws SQLException {
    	Long transactionId = 1L;
		List<Transaction> expectedTransactionsList = new ArrayList<>();
		Transaction expectedTransaction = new Transaction("Account Deposit", 100.00, "notes", 12345L);
		LocalDateTime testDateTime = LocalDateTime.of(2024, 4, 24, 15, 30);
        expectedTransaction.setTransactionId(transactionId);
        expectedTransaction.setTransactionDate(testDateTime);
		expectedTransactionsList.add(expectedTransaction);
		
		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
		when(resultSetMock.next()).thenReturn(true, false); // false needed to break out of loop
		
        when(resultSetMock.getLong("transaction_id")).thenReturn(1L);
		when(resultSetMock.getString("transaction_type")).thenReturn("Account Deposit");
		when(resultSetMock.getDouble("transaction_amount")).thenReturn(100.00);
		when(resultSetMock.getString("transaction_notes")).thenReturn("notes");
		when(resultSetMock.getLong("account_number")).thenReturn(12345L);
		when(resultSetMock.getTimestamp("transaction_date")).thenReturn(Timestamp.valueOf(testDateTime));
		
		List<Transaction> result = transactionDao.findAllTransactionsByAcctNo(transactionId);
		
		assertNotNull(result);
		assertEquals(expectedTransactionsList.size(), result.size());
    }
    
    @Test
    void testFindAllTransactionsByAcctNoSQLException() throws SQLException {
    	Long transactionId = 1L;
		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeQuery()).thenThrow(SQLException.class);
		List<Transaction> result = transactionDao.findAllTransactionsByAcctNo(transactionId);;
		assertNotNull(result);
    }
    
    @Test
    void testSaveTransaction() throws SQLException {
		Transaction newTransaction = new Transaction("Account Deposit", 100.00, "notes", 12345L);
		newTransaction.setTransactionId(1L);

		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeUpdate()).thenReturn(1);

		Transaction result = transactionDao.saveTransaction(newTransaction);

		assertEquals(newTransaction, result);
    }
    
    @Test
    void testSaveTransactionSQLException() throws SQLException {
    	Transaction newTransaction = new Transaction("Account Deposit", 100.00, "notes", 12345L);
		newTransaction.setTransactionId(1L);
		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeUpdate()).thenThrow(SQLException.class);
		Transaction result = transactionDao.saveTransaction(newTransaction);
		assertNotNull(result);
    }
    
    @Test
    void testUpdateTransaction() throws SQLException {
		Transaction updatedTransaction = new Transaction("Account Deposit", 100.00, "notes", 12345L);
		updatedTransaction.setTransactionId(1L);

		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeUpdate()).thenReturn(1);

		Transaction result = transactionDao.updateTransaction(updatedTransaction);

		assertEquals(updatedTransaction, result);
    }
    
    @Test
    void testUpdateTransactionSQLException() throws SQLException {
		Transaction updatedTransaction = new Transaction("Account Deposit", 100.00, "notes", 12345L);
		updatedTransaction.setTransactionId(1L);
		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeUpdate()).thenThrow(SQLException.class);
		Transaction result = transactionDao.updateTransaction(updatedTransaction);
		assertNotNull(result);
    }
	
	@Test
	void testDeleteTransactionById() throws SQLException {
		Long txnId = 1L;
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);
        boolean success = transactionDao.deleteTransactionById(txnId);
        assertTrue(success);
        verify(connectionMock, Mockito.times(1)).setAutoCommit(false);
        verify(preparedStatementMock, Mockito.times(1)).setLong(1, txnId);
        verify(connectionMock, Mockito.times(1)).commit();
        verify(connectionMock, Mockito.times(1)).close();
        verify(preparedStatementMock, Mockito.times(1)).close();
	}

    @Test
    void testDeleteCustomerByIdMultipleRowsDeleted() throws SQLException {
        Long txnId = 1L;
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(2);
        boolean success = transactionDao.deleteTransactionById(txnId);
        assertFalse(success);
        verify(connectionMock, Mockito.times(1)).rollback();
        verify(connectionMock, Mockito.times(1)).close();
        verify(preparedStatementMock, Mockito.times(1)).close();
    }

    @Test
    void testDeleteCustomerByIdSQLException() throws SQLException {
        Long txnId = 1L;
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenThrow(SQLException.class);
        boolean success = transactionDao.deleteTransactionById(txnId);
        assertFalse(success);
    }

}
