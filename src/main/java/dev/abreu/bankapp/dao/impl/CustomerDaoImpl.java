package dev.abreu.bankapp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.model.Customer;
import dev.abreu.bankapp.utils.BankappQueryConstants;
import dev.abreu.bankapp.utils.ConnectionUtil;

public class CustomerDaoImpl implements CustomerDao {
	
	private static final Logger log = LogManager.getLogger(CustomerDaoImpl.class);
	
	private ConnectionUtil connUtil = ConnectionUtil.getConnectionUtil();

	@Override
	public Customer findByUsername(String username) {
		Customer customer = new Customer();
		
		try(Connection conn = connUtil.getConnection(); 
				PreparedStatement stmt = conn.prepareStatement(BankappQueryConstants.SELECT_CUSTOMERS_QUERY);) {
			
			stmt.setString(1, username);
			
			ResultSet resultSet = stmt.executeQuery();
			
			if(resultSet.next()) {
				customer.setId(resultSet.getLong("customer_id"));
				customer.setFirstName(resultSet.getString("first_name"));
				customer.setLastName(resultSet.getString("last_name"));
				customer.setUsername(resultSet.getString("username"));
				customer.setPassword(resultSet.getString("passwrd"));
				customer.setAddress(resultSet.getString("address"));
			}
			
		} catch (SQLException e) {
			log.error("SQLException Thrown: {}", e.getMessage());
		}
		
		return customer;
	}

	@Override
	public void saveCustomer(Customer customer) {
		
		log.info("Entering saveCustomer class...");
		
		try(Connection conn = connUtil.getConnection(); 
				PreparedStatement stmt = conn.prepareStatement(BankappQueryConstants.CREATE_CUSTOMER_QUERY);) {
			
			stmt.setString(1, customer.getFirstName());
			stmt.setString(2, customer.getLastName());
			stmt.setString(3, customer.getAddress());
			stmt.setString(4, customer.getUsername());
			stmt.setString(5, customer.getPassword());
			
			log.info("Create Customer Query String: {}", BankappQueryConstants.CREATE_CUSTOMER_QUERY);
			int rowsAffected = stmt.executeUpdate();
			log.info("{} Row(s) Affected", rowsAffected);
			
		} catch (SQLException e) {
			log.error("SQLException Caught: {}", e.getMessage());
		}
	}

	@Override
	public void updateCustomer(Customer customer) {
		
		log.info("Entering updateCustomer class...");
		
		try(Connection conn = connUtil.getConnection(); 
				PreparedStatement stmt = conn.prepareStatement(BankappQueryConstants.UPDATE_CUSTOMER_QUERY);) {
			
			stmt.setLong(6, customer.getId());
			stmt.setString(1, customer.getFirstName());
			stmt.setString(2, customer.getLastName());
			stmt.setString(3, customer.getAddress());
			stmt.setString(4, customer.getUsername());
			stmt.setString(5, customer.getPassword());
			
			log.info("Update Customer Query String: {}", BankappQueryConstants.UPDATE_CUSTOMER_QUERY);
			int updateStatus = stmt.executeUpdate();
			log.info("{} Row(s) Updated", updateStatus);
			
		} catch (SQLException e) {
			log.error("SQLException Caught: {}", e.getMessage());
		}

	}

	@Override
	public void deleteCustomer(Long customerId) {
		
		log.info("Entering deleteCustomer class...");
		
		try(Connection conn = connUtil.getConnection(); 
				PreparedStatement stmt = conn.prepareStatement(BankappQueryConstants.DELETE_CUSTOMER_QUERY);) {
			conn.setAutoCommit(false);
			
			stmt.setLong(1, customerId);

			log.info("Delete Customer Query String: {}", BankappQueryConstants.DELETE_CUSTOMER_QUERY);
			int deleteStatus = stmt.executeUpdate();
			if(deleteStatus<=1)
				conn.commit();
			else
				conn.rollback();
			log.info("{} Row(s) Deleted", deleteStatus);
			
		} catch (SQLException e) {
			log.error("SQLException Caught: {}", e.getMessage());
		}
	}

}
