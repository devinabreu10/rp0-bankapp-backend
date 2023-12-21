package dev.abreu.bankapp.utils;

/**
 * Constants that will be used for SQL queries
 * 
 * @author Devin Abreu
 *
 */
public class BankappQueryConstants {
	
	private BankappQueryConstants() {}
	
	public static final String SELECT_CUSTOMERS_QUERY = "SELECT customer_id,first_name,last_name,address,username,passwrd FROM customers WHERE username=?";
	
	public static final String CREATE_CUSTOMER_QUERY = "INSERT into customers (customer_id,first_name,last_name,address,username,passwrd) VALUES (default,?,?,?,?,?)";

	public static final String UPDATE_CUSTOMER_QUERY = "UPDATE customers SET first_name=?,last_name=?,address=?,username=?,passwrd=? WHERE customer_id=?";

	public static final String DELETE_CUSTOMER_QUERY = "DELETE from customers WHERE customer_id=?";

}
