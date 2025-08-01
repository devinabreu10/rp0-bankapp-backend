package dev.abreu.bankapp.dao.impl;

import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.entity.Customer;
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
public class CustomerDaoImpl implements CustomerDao {
	
	private static final Logger log = LogManager.getLogger(CustomerDaoImpl.class);

	private static final String CUSTOMER_ID = "customer_id";
	private static final String FIRST_NAME = "first_name";
	private static final String LAST_NAME = "last_name";
	private static final String USERNAME = "username";
	private static final String PASSWRD = "passwrd";
	private static final String ADDRESS = "address";

	private final DataSource dataSource;

    public CustomerDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
	public Optional<Customer> findByUsername(String username) {
		Customer customer = new Customer();
		
		try(Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SELECT_CUSTOMERS_BY_USERNAME_QUERY)) {
			
			stmt.setString(1, username);

			ResultSet resultSet = stmt.executeQuery();

			if(resultSet.next()) {
				setCustomerFromResultSet(customer, resultSet);
			} else {
				return Optional.empty();
			}

		} catch (SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT + "findByUsername: {}", e.getMessage());
		}
		
		return Optional.of(customer);
	}

	@Override
	public Optional<Customer> findById(Long customerId) {
		Customer customer = new Customer();
		
		try(Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SELECT_CUSTOMERS_BY_ID_QUERY)) {
			
			stmt.setLong(1, customerId);

			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				setCustomerFromResultSet(customer, rs);
			} else {
				return Optional.empty();
			}

		} catch (SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT + "findById: {}", e.getMessage());
		}
		
		return Optional.of(customer);
	}
	
	@Override
	public List<Customer> findAllCustomers() {
		List<Customer> customerList = new ArrayList<>();
		
		try(Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {

			ResultSet resultSet = stmt.executeQuery(SELECT_ALL_CUSTOMERS_QUERY);
			
			while(resultSet.next()) {
				Customer customer = new Customer();
				setCustomerFromResultSet(customer, resultSet);
				customerList.add(customer);
			}
			
		} catch (SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT + "findAllCustomers: {}", e.getMessage());
		}
		
		return customerList;
	}
	
	@Override
	public boolean existsByUsername(String username) {
		boolean usernameExists = false;
		
		try(Connection conn = dataSource.getConnection();
				PreparedStatement prepStmt = conn.prepareStatement(SELECT_CUSTOMERS_BY_USERNAME_QUERY)) {
			
			prepStmt.setString(1, username);
			
			ResultSet resultSet = prepStmt.executeQuery();
			
			if(resultSet.isBeforeFirst()) {
				usernameExists = true;
			}
			
		} catch (SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT + "existsByUsername: {}", e.getMessage());
		}

		return usernameExists;
	}

	@Override
	public Customer saveCustomer(Customer customer) {
		
		log.info("Entering saveCustomer method...");
		
		try(Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(CREATE_CUSTOMER_QUERY)) {
			
			stmt.setString(1, customer.getFirstName());
			stmt.setString(2, customer.getLastName());
			stmt.setString(3, customer.getAddress());
			stmt.setString(4, customer.getUsername());
			stmt.setString(5, customer.getPassword());
			
			log.info("Create Customer Query String: {}", CREATE_CUSTOMER_QUERY);
			int rowsAffected = stmt.executeUpdate();
			log.info("{} Row(s) Affected", rowsAffected);
			
		} catch (SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT + "saveCustomer: {}", e.getMessage());
		}
		
		return customer;
	}

	@Override
	public Customer updateCustomer(Customer customer) {
		
		log.info("Entering updateCustomer method...");
		
		try(Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(UPDATE_CUSTOMER_QUERY)) {
			
			stmt.setString(1, customer.getFirstName());
			stmt.setString(2, customer.getLastName());
			stmt.setString(3, customer.getAddress());
			stmt.setString(4, customer.getUsername());
			stmt.setLong(5, customer.getId());
			
			log.info("Update Customer Query String: {}", UPDATE_CUSTOMER_QUERY);
			int updateStatus = stmt.executeUpdate();
			log.info("{} Row(s) Updated", updateStatus);
			
		} catch (SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT + "updateCustomer: {}", e.getMessage());
		}

		return customer;
	}
	
	@Override
	public boolean deleteCustomerByUsername(String username) {
		log.info("Entering deleteCustomerByUsername method...");
		
		boolean success = false;
		
		try(Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(DELETE_CUSTOMER_BY_USERNAME_QUERY)) {
			conn.setAutoCommit(false);
			
			stmt.setString(1, username);

			log.info("Delete Customer Query String: {}", DELETE_CUSTOMER_BY_USERNAME_QUERY);
			int deleteStatus = stmt.executeUpdate();
			
			if(deleteStatus <= 1) {
				conn.commit();
				log.info("{} Row(s) Deleted", deleteStatus);
				success = true;
			} else {
				conn.rollback();
				log.info("There was an issue with deleteCustomer, rolling back changes...");
			}
			
		} catch (SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT + "deleteCustomerByUsername: {}", e.getMessage());
		}
		
		return success;
	}

	@Override
	public boolean deleteCustomerById(Long customerId) {
		log.info("Entering deleteCustomer method...");
		
		boolean success = false;
		
		try(Connection conn = dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(DELETE_CUSTOMER_BY_ID_QUERY)) {
			conn.setAutoCommit(false);
			
			stmt.setLong(1, customerId);

			log.info("Delete Customer By Id Query String: {}", DELETE_CUSTOMER_BY_ID_QUERY);
			int deleteStatus = stmt.executeUpdate();
			
			if(deleteStatus <= 1) {
				conn.commit();
				log.info("{} Row(s) Deleted...", deleteStatus);
				success = true;
			} else {
				conn.rollback();
				log.info("There was an issue with deleteCustomerById, rolling back changes...");
			}
			
		} catch (SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT + "deleteCustomerById: {}", e.getMessage());
		}
		
		return success;
	}

	private void setCustomerFromResultSet(Customer customer, ResultSet resultSet) throws SQLException {
		customer.setId(resultSet.getLong(CUSTOMER_ID));
		customer.setFirstName(resultSet.getString(FIRST_NAME));
		customer.setLastName(resultSet.getString(LAST_NAME));
		customer.setUsername(resultSet.getString(USERNAME));
		customer.setPassword(resultSet.getString(PASSWRD));
		customer.setAddress(resultSet.getString(ADDRESS));
	}

}
