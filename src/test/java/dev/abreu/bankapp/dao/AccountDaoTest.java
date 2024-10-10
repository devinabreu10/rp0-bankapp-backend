package dev.abreu.bankapp.dao;

import dev.abreu.bankapp.dao.impl.AccountDaoImpl;
import dev.abreu.bankapp.model.Account;
import dev.abreu.bankapp.utils.ConnectionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountDaoTest {

	@Mock
	private ConnectionUtil connectionUtilMock;

	@Mock
	private Connection connectionMock;

	@Mock
	private PreparedStatement preparedStatementMock;

	@Mock
	private ResultSet resultSetMock;

	@InjectMocks
	private AccountDaoImpl accountDao;

	@BeforeEach
	public void setup() {
		when(connectionUtilMock.getConnection()).thenReturn(connectionMock);
	}

	@Test
	void testFindAccountByAcctNo() throws SQLException {
		Long acctNo = 12345L;
		Account expectedAccount = new Account(12345L, "Checking", 1000.00, 1L);

		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
		when(resultSetMock.next()).thenReturn(true);
		when(resultSetMock.getLong("account_number")).thenReturn(12345L);
		when(resultSetMock.getString("account_type")).thenReturn("Checking");
		when(resultSetMock.getDouble("account_balance")).thenReturn(1000.00);
		when(resultSetMock.getLong("customer_id")).thenReturn(1L);
		
		Optional<Account> result = accountDao.findAccountByAcctNo(acctNo);
		
		assertTrue(result.isPresent());
		assertEquals(expectedAccount.getAccountNumber(), result.get().getAccountNumber());
	}

	@Test
	void testFindAccountByAcctNoEmptyOptional() throws SQLException {
		Long acctNo = 12345L;
		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
		when(resultSetMock.next()).thenReturn(false);
		Optional<Account> result = accountDao.findAccountByAcctNo(acctNo);
		assertEquals(Optional.empty(), result);
	}

	@Test
	void testFindAccountByAcctNoSQLException() throws SQLException {
		Long acctNo = 12345L;
		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeQuery()).thenThrow(SQLException.class);
		Optional<Account> result = accountDao.findAccountByAcctNo(acctNo);
		assertNotNull(result);
	}
	
	@Test
	void testFindAllAccountsByUsername() throws SQLException {
		String username = "testUser";
		List<Account> expectedAccountsList = new ArrayList<>();
		Account expectedAccount = new Account(12345L, "Checking", 1000.00, 1L);
		expectedAccountsList.add(expectedAccount);
		
		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
		when(resultSetMock.next()).thenReturn(true, false); // false needed to break out of loop
		
		when(resultSetMock.getLong("acc_no")).thenReturn(12345L);
		when(resultSetMock.getString("acc_typ")).thenReturn("Checking");
		when(resultSetMock.getDouble("acc_bal")).thenReturn(1000.00);
		when(resultSetMock.getLong("cust_id")).thenReturn(1L);
		
		List<Account> result = accountDao.findAllAccountsByUsername(username);
		
		assertNotNull(result);
		assertEquals(expectedAccountsList.size(), result.size());
	}
	
	@Test
	void testFindAllAccountsByUsernameSQLException() throws SQLException {
		String username = "testUser";
		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeQuery()).thenThrow(SQLException.class);
		List<Account> result = accountDao.findAllAccountsByUsername(username);
		assertNotNull(result);
	}
	
	@Test
	void testSaveAccount() throws SQLException {
		Account newAccount = new Account(12345L, "Checking", 1000.00, 1L);
		
	    when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
	    when(preparedStatementMock.executeUpdate()).thenReturn(1);
	    
	    Account result = accountDao.saveAccount(newAccount);
	    
	    assertEquals(newAccount, result);
	}
	
	@Test
	void testSaveAccountSQLException() throws SQLException {
		Account newAccount = new Account(12345L, "Checking", 1000.00, 1L);
		
	    when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
	    when(preparedStatementMock.executeUpdate()).thenThrow(SQLException.class);
	    Account result = accountDao.saveAccount(newAccount);
	    assertNotNull(result);
	}
	
	@Test
	void testUpdateAccount() throws SQLException {
		Account updatedAccount = new Account(12345L, "Checking", 1000.00, 1L);
		
	    when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
	    when(preparedStatementMock.executeUpdate()).thenReturn(1);
	    
	    Account result = accountDao.updateAccount(updatedAccount);
	    
	    assertEquals(updatedAccount, result);
	}
	
	@Test
	void testUpdateAccountSQLException() throws SQLException {
		Account updatedAccount = new Account(12345L, "Checking", 1000.00, 1L);
		
	    when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
	    when(preparedStatementMock.executeUpdate()).thenThrow(SQLException.class);
	    
	    Account result = accountDao.updateAccount(updatedAccount);
	    
	    assertNotNull(result);
	}
	
	@Test
	void testDeleteAccountByAcctNo() throws SQLException {
        long acctNo = 12345L;
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);
        boolean success = accountDao.deleteAccountByAcctNo(acctNo);
        assertTrue(success);
        verify(connectionMock, Mockito.times(1)).setAutoCommit(false);
        verify(preparedStatementMock, Mockito.times(1)).setLong(1, acctNo);
        verify(connectionMock, Mockito.times(1)).commit();
        verify(connectionMock, Mockito.times(1)).close();
        verify(preparedStatementMock, Mockito.times(1)).close();
	}
	
	@Test
	void testDeleteAccountByAcctNoMultipleRowsDeleted() throws SQLException {
        Long acctNo = 12345L;
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(2);
        boolean success = accountDao.deleteAccountByAcctNo(acctNo);
        assertFalse(success);
        verify(connectionMock, Mockito.times(1)).rollback();
        verify(connectionMock, Mockito.times(1)).close();
        verify(preparedStatementMock, Mockito.times(1)).close();
	}
	
	@Test
	void testDeleteAccountByAcctNoSQLException() throws SQLException {
        Long acctNo = 12345L;
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenThrow(SQLException.class);
        boolean success = accountDao.deleteAccountByAcctNo(acctNo);
        assertFalse(success);
	}
	
	
}
