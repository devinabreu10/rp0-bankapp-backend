package dev.abreu.bankapp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.model.Customer;
import dev.abreu.bankapp.utils.ConnectionUtil;

public class CustomerDaoImpl implements CustomerDao {
	
	private static final Logger log = LogManager.getLogger(CustomerDaoImpl.class);
	
	private ConnectionUtil connUtil = ConnectionUtil.getConnectionUtil();

	@Override
	public Customer findByUsername(String username) {
		Customer customer = new Customer();
		
		try(Connection conn = connUtil.getConnection()) {
			
			String sql = "SELECT * FROM users WHERE username = ?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
			
			ResultSet resultSet = stmt.executeQuery();
			
			if(resultSet.next()) {
				customer.setId(resultSet.getLong("customer_id"));
				customer.setFirstName(resultSet.getString("first_name"));
				customer.setLastName(resultSet.getString("last_name"));
				customer.setUsername(resultSet.getString("username"));
				customer.setPassword(resultSet.getString("passwrd"));
			}
			conn.close();
			
		} catch (SQLException e) {
			log.error("SQLException Thrown: " + e.getMessage());
		}
		
		return customer;
	}

	@Override
	public Customer saveCustomer(Customer customer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer updateCustomer(Customer customer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer deleteCustomer(Customer customer) {
		// TODO Auto-generated method stub
		return null;
	}

}
