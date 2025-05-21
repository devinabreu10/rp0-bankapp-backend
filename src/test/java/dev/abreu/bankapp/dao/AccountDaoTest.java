package dev.abreu.bankapp.dao;

import dev.abreu.bankapp.dao.impl.AccountDaoImpl;
import dev.abreu.bankapp.entity.Account;
import dev.abreu.bankapp.util.ConnectionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dev.abreu.bankapp.util.BankappConstants.CHECKING_ACCOUNT;
import static dev.abreu.bankapp.util.BankappConstants.SAVINGS_ACCOUNT;
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
	void setup() {
		when(connectionUtilMock.getConnection()).thenReturn(connectionMock);
	}

	@Test
	void testFindAccountByAcctNo() throws SQLException {
		Long acctNo = 12345L;
		Account expectedAccount = new Account(12345L, CHECKING_ACCOUNT, 1000.00, 1L);
		LocalDateTime testDateTime = LocalDateTime.of(2024, 4, 24, 15, 30);
		expectedAccount.setCreatedAt(testDateTime);

		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
		when(resultSetMock.next()).thenReturn(true);
		when(resultSetMock.getLong("account_number")).thenReturn(12345L);
		when(resultSetMock.getString("nickname")).thenReturn("my checking");
		when(resultSetMock.getString("account_type")).thenReturn(CHECKING_ACCOUNT);
		when(resultSetMock.getDouble("account_balance")).thenReturn(1000.00);
		when(resultSetMock.getTimestamp("created_at")).thenReturn(Timestamp.valueOf(testDateTime));
		when(resultSetMock.getTimestamp("updated_at")).thenReturn(Timestamp.valueOf(testDateTime));
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
		Account expectedAccount = new Account(12345L, SAVINGS_ACCOUNT, 1000.00, 1L);
		LocalDateTime testDateTime = LocalDateTime.of(2024, 4, 24, 15, 30);
		expectedAccount.setCreatedAt(testDateTime);
		expectedAccountsList.add(expectedAccount);

		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
		when(resultSetMock.next()).thenReturn(true, false); // false needed to break out of loop

		when(resultSetMock.getLong("account_number")).thenReturn(12345L);
		when(resultSetMock.getString("nickname")).thenReturn("my savings");
		when(resultSetMock.getString("account_type")).thenReturn(SAVINGS_ACCOUNT);
		when(resultSetMock.getDouble("account_balance")).thenReturn(1000.00);
		when(resultSetMock.getTimestamp("created_at")).thenReturn(Timestamp.valueOf(testDateTime));
		when(resultSetMock.getTimestamp("updated_at")).thenReturn(Timestamp.valueOf(testDateTime));
		when(resultSetMock.getLong("customer_id")).thenReturn(1L);

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
		// Create account without account number - it should be generated
		Account newAccount = new Account("Checking", 1000.00, 1L);

	    // First mock the findAccountByAcctNo call that checks for uniqueness
	    // We need to set up the mocks for two different PreparedStatement calls
	    PreparedStatement findStmt = Mockito.mock(PreparedStatement.class);
	    PreparedStatement saveStmt = Mockito.mock(PreparedStatement.class);
	    ResultSet findRs = Mockito.mock(ResultSet.class);

	    // First call is for findAccountByAcctNo
	    when(connectionMock.prepareStatement(Mockito.contains("SELECT * FROM"))).thenReturn(findStmt);
	    when(findStmt.executeQuery()).thenReturn(findRs);
	    when(findRs.next()).thenReturn(false); // No existing account with this number

	    // Second call is for saveAccount
	    when(connectionMock.prepareStatement(Mockito.contains("INSERT into"))).thenReturn(saveStmt);
	    when(saveStmt.executeUpdate()).thenReturn(1);

	    Account result = accountDao.saveAccount(newAccount);

	    // Verify account number was generated (8 digits)
	    assertNotNull(result.getAccountNumber());
	    assertTrue(result.getAccountNumber() >= 10000000L && result.getAccountNumber() <= 99999999L);

	    // Verify the account number was set on the original account object
	    assertEquals(newAccount.getAccountNumber(), result.getAccountNumber());

	    // Verify the prepared statement was called with the generated account number
	    verify(saveStmt).setLong(1, result.getAccountNumber());
	}

	@Test
	void testSaveAccountSQLException() throws SQLException {
		// Create account without account number - it should be generated
		Account newAccount = new Account("Checking", 1000.00, 1L);

	    // First mock the findAccountByAcctNo call that checks for uniqueness
	    // We need to set up the mocks for two different PreparedStatement calls
	    PreparedStatement findStmt = Mockito.mock(PreparedStatement.class);
	    PreparedStatement saveStmt = Mockito.mock(PreparedStatement.class);
	    ResultSet findRs = Mockito.mock(ResultSet.class);

	    // First call is for findAccountByAcctNo
	    when(connectionMock.prepareStatement(Mockito.contains("SELECT * FROM"))).thenReturn(findStmt);
	    when(findStmt.executeQuery()).thenReturn(findRs);
	    when(findRs.next()).thenReturn(false); // No existing account with this number

	    // Second call is for saveAccount - this one throws SQLException
	    when(connectionMock.prepareStatement(Mockito.contains("INSERT into"))).thenReturn(saveStmt);
	    when(saveStmt.executeUpdate()).thenThrow(SQLException.class);

	    Account result = accountDao.saveAccount(newAccount);

	    // Verify account number was generated (8 digits)
	    assertNotNull(result.getAccountNumber());
	    assertTrue(result.getAccountNumber() >= 10000000L && result.getAccountNumber() <= 99999999L);

	    // Even with SQL exception, the account number should be set
	    assertEquals(newAccount.getAccountNumber(), result.getAccountNumber());
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
