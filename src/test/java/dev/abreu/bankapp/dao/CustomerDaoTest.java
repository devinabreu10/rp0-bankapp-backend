package dev.abreu.bankapp.dao;

import dev.abreu.bankapp.dao.impl.CustomerDaoImpl;
import dev.abreu.bankapp.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerDaoTest {

	@Mock
	private DataSource dataSourceMock;

	@Mock
	private Connection connectionMock;
	
	@Mock
	private Statement statementMock;

	@Mock
	private PreparedStatement preparedStatementMock;

	@Mock
	private ResultSet resultSetMock;

	@InjectMocks
	private CustomerDaoImpl customerDao;

	@BeforeEach
	void setup() throws SQLException {
		when(dataSourceMock.getConnection()).thenReturn(connectionMock);
	}

	@Test
	void testFindByUsername() throws SQLException {
		String username = "testUser";
		Customer expectedCustomer = new Customer("John", "Doe", "123 Street", "testUser", "password");
		expectedCustomer.setId(1L);

		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
		when(resultSetMock.next()).thenReturn(true);
		when(resultSetMock.getLong("customer_id")).thenReturn(1L);
		when(resultSetMock.getString("first_name")).thenReturn("John");
		when(resultSetMock.getString("last_name")).thenReturn("Doe");
		when(resultSetMock.getString("username")).thenReturn("testUser");
		when(resultSetMock.getString("passwrd")).thenReturn("password");
		when(resultSetMock.getString("address")).thenReturn("123 Street");

		Optional<Customer> result = customerDao.findByUsername(username);

		assertTrue(result.isPresent());
		assertEquals(expectedCustomer.getUsername(), result.get().getUsername());
	}

	@Test
	void testFindByUsernameEmptyOptional() throws SQLException {
		String username = "testUser";
		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
		when(resultSetMock.next()).thenReturn(false);
		Optional<Customer> result = customerDao.findByUsername(username);
		assertEquals(Optional.empty(), result);
	}

	@Test
	void testFindByUsernameSQLException() throws SQLException {
		String username = "testUser";
		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeQuery()).thenThrow(SQLException.class);
		Optional<Customer> result = customerDao.findByUsername(username);
		assertNotNull(result);
	}

	@Test
	void testFindById() throws SQLException {
		Long customerId = 1L;
		Customer expectedCustomer = new Customer("John", "Doe", "123 Street", "testUser", "password");
		expectedCustomer.setId(1L);

		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
		when(resultSetMock.next()).thenReturn(true);
		when(resultSetMock.getLong("customer_id")).thenReturn(1L);
		when(resultSetMock.getString("first_name")).thenReturn("John");
		when(resultSetMock.getString("last_name")).thenReturn("Doe");
		when(resultSetMock.getString("username")).thenReturn("testUser");
		when(resultSetMock.getString("passwrd")).thenReturn("password");
		when(resultSetMock.getString("address")).thenReturn("123 Street");

		Optional<Customer> result = customerDao.findById(customerId);

		assertTrue(result.isPresent());
		assertEquals(expectedCustomer.getUsername(), result.get().getUsername());
	}

	@Test
	void testFindByIdEmptyOptional() throws SQLException {
		Long customerId = 1L;
		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
		when(resultSetMock.next()).thenReturn(false);
		Optional<Customer> result = customerDao.findById(customerId);
		assertEquals(Optional.empty(), result);
	}

	@Test
	void testFindByIdSQLException() throws SQLException {
		Long customerId = 1L;
		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeQuery()).thenThrow(SQLException.class);
		Optional<Customer> result = customerDao.findById(customerId);
		assertNotNull(result);
	}

	@Test
	void testFindAllCustomers() throws SQLException {
		List<Customer> expectedCustomers = new ArrayList<>();
		Customer expectedCustomer1 = new Customer("John", "Doe", "testUser1", "password1", "123 Street");
		expectedCustomer1.setId(1L);
		Customer expectedCustomer2 = new Customer("Jane", "Smith", "testUser2", "password2", "456 Street");
		expectedCustomer2.setId(2L);
		expectedCustomers.add(expectedCustomer1);
		expectedCustomers.add(expectedCustomer2);

		when(connectionMock.createStatement()).thenReturn(statementMock);
		when(statementMock.executeQuery(anyString())).thenReturn(resultSetMock);
		when(resultSetMock.next()).thenReturn(true, true, false);

		when(resultSetMock.getLong("customer_id")).thenReturn(1L, 2L);
		when(resultSetMock.getString("first_name")).thenReturn("John", "Jane");
		when(resultSetMock.getString("last_name")).thenReturn("Doe", "Smith");
		when(resultSetMock.getString("username")).thenReturn("testUser1", "testUser2");
		when(resultSetMock.getString("passwrd")).thenReturn("password1", "password2");
		when(resultSetMock.getString("address")).thenReturn("123 Street", "456 Street");

		List<Customer> result = customerDao.findAllCustomers();

		assertNotNull(result);
		assertEquals(expectedCustomers.size(), result.size());
	}

	@Test
	void testFindByAllSQLException() throws SQLException {
		when(connectionMock.createStatement()).thenReturn(statementMock);
		when(statementMock.executeQuery(anyString())).thenThrow(SQLException.class);
		List<Customer> result = customerDao.findAllCustomers();
		assertNotNull(result);
	}

	@Test
	void testExistsByUsernameWhenUsernameExists() throws SQLException {
		String existingUsername = "testUser1";

		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
		when(resultSetMock.isBeforeFirst()).thenReturn(true);

		assertTrue(customerDao.existsByUsername(existingUsername));
	}

	@Test
	void testExistsByUsernameSQLException() throws SQLException {
		String existingUsername = "testUser1";
		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeQuery()).thenThrow(SQLException.class);
		assertFalse(customerDao.existsByUsername(existingUsername));
	}

	@Test
	void testExistsByUsernameWhenUsernameDoesNotExist() throws SQLException {
		String nonExistingUsername = "nonExistentUser";

		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
		when(resultSetMock.isBeforeFirst()).thenReturn(false);

		assertFalse(customerDao.existsByUsername(nonExistingUsername));
	}

	@Test
	void testSaveCustomer() throws SQLException {
		Customer newCustomer = new Customer("Alice", "Johnson", "789 Street", "alice123", "password123");
		newCustomer.setId(1L);

		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeUpdate()).thenReturn(1);

		Customer result = customerDao.saveCustomer(newCustomer);

		assertEquals(newCustomer, result);
	}

	@Test
	void testSaveCustomerSQLException() throws SQLException {
		Customer newCustomer = new Customer("Alice", "Johnson", "789 Street", "alice123", "password123");
		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeUpdate()).thenThrow(SQLException.class);
		Customer result = customerDao.saveCustomer(newCustomer);
		assertNotNull(result);
	}

	@Test
	void testUpdateCustomer() throws SQLException {
		Customer updatedCustomer = new Customer("Alice", "Johnson", "789 Street Updated", "alice123", null);
		updatedCustomer.setId(1L);

		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeUpdate()).thenReturn(1);

		Customer result = customerDao.updateCustomer(updatedCustomer);

		assertEquals(updatedCustomer, result);
	}

	@Test
	void testUpdateCustomerSQLException() throws SQLException {
		Customer updatedCustomer = new Customer("Alice", "Johnson", "789 Street Updated", "alice123", null);
		updatedCustomer.setId(1L);
		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeUpdate()).thenThrow(SQLException.class);
		Customer result = customerDao.updateCustomer(updatedCustomer);
		assertNotNull(result);
	}

	@Test
	void testDeleteCustomerById() throws SQLException {
		long customerId = 1L;
		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeUpdate()).thenReturn(1);
		boolean success = customerDao.deleteCustomerById(customerId);
		assertTrue(success);
		verify(connectionMock, Mockito.times(1)).setAutoCommit(false);
		verify(preparedStatementMock, Mockito.times(1)).setLong(1, customerId);
		verify(connectionMock, Mockito.times(1)).commit();
		verify(connectionMock, Mockito.times(1)).close();
		verify(preparedStatementMock, Mockito.times(1)).close();
	}

	@Test
	void testDeleteCustomerByIdMultipleRowsDeleted() throws SQLException {
		Long customerId = 1L;
		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeUpdate()).thenReturn(2);
		boolean success = customerDao.deleteCustomerById(customerId);
		assertFalse(success);
		verify(connectionMock, Mockito.times(1)).rollback();
		verify(connectionMock, Mockito.times(1)).close();
		verify(preparedStatementMock, Mockito.times(1)).close();
	}

	@Test
	void testDeleteCustomerByIdSQLException() throws SQLException {
		Long customerId = 1L;
		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeUpdate()).thenThrow(SQLException.class);
		boolean success = customerDao.deleteCustomerById(customerId);
		assertFalse(success);
	}

	@Test
	void testDeleteCustomerByUsername() throws SQLException {
		String username = "testUser1";
		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeUpdate()).thenReturn(1);
		boolean success = customerDao.deleteCustomerByUsername(username);
		assertTrue(success);
		verify(connectionMock, Mockito.times(1)).setAutoCommit(false);
		verify(preparedStatementMock, Mockito.times(1)).setString(1, username);
		verify(connectionMock, Mockito.times(1)).commit();
		verify(connectionMock, Mockito.times(1)).close();
		verify(preparedStatementMock, Mockito.times(1)).close();
	}

	@Test
	void testDeleteCustomerByUsernameMultipleRowsDeleted() throws SQLException {
		String username = "testUser1";
		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeUpdate()).thenReturn(2);
		boolean success = customerDao.deleteCustomerByUsername(username);
		assertFalse(success);
		verify(connectionMock, Mockito.times(1)).rollback();
		verify(connectionMock, Mockito.times(1)).close();
		verify(preparedStatementMock, Mockito.times(1)).close();
	}

	@Test
	void testDeleteCustomerByUsernameSQLException() throws SQLException {
		String username = "testUser1";
		when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
		when(preparedStatementMock.executeUpdate()).thenThrow(SQLException.class);
		boolean success = customerDao.deleteCustomerByUsername(username);
		assertFalse(success);
	}
}