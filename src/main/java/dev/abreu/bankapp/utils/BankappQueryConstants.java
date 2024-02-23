package dev.abreu.bankapp.utils;

/**
 * Constants that will be used for SQL queries
 * 
 * @author Devin Abreu
 *
 */
public class BankappQueryConstants {
	
	private BankappQueryConstants() {}
	
	public static final String SELECT_CUSTOMERS_BY_USERNAME_QUERY = "SELECT customer_id,first_name,last_name,address,username,passwrd FROM customers WHERE username=?";
	
	public static final String SELECT_CUSTOMERS_BY_ID_QUERY = "SELECT customer_id,first_name,last_name,address,username,passwrd FROM customers WHERE customer_id=?";
	
	public static final String SELECT_ALL_CUSTOMERS_QUERY = "SELECT * FROM customers";
	
	public static final String CREATE_CUSTOMER_QUERY = "INSERT into customers (customer_id,first_name,last_name,address,username,passwrd) VALUES (default,?,?,?,?,?)";

	public static final String UPDATE_CUSTOMER_QUERY = "UPDATE customers SET first_name=?,last_name=?,address=?,username=?,passwrd=? WHERE customer_id=?";

	public static final String DELETE_CUSTOMER_BY_USERNAME_QUERY = "DELETE from customers WHERE username=?";
	
	public static final String DELETE_CUSTOMER_BY_ID_QUERY = "DELETE from customers WHERE customer_id=?";
	
	public static final String QUERY = "";
	
	public static final String SELECT_ACCOUNTS_BY_ACCTNO_QUERY = "SELECT * FROM accounts WHERE account_number=?";
	
	public static final String SELECT_ALL_ACCOUNTS_BY_USERNAME_QUERY = "SELECT b.username,a.customer_id as cust_id,a.account_number as acc_no,a.account_type as acc_typ,a.account_balance as acc_bal "
						+ "FROM accounts a JOIN customers b ON a.customer_id = b.customer_id WHERE b.username=?";

	public static final String CREATE_NEW_ACCOUNT_QUERY = "INSERT into accounts (account_number,account_type,account_balance,customer_id) VALUES (default,?,?,?)";

	public static final String UPDATE_ACCOUNT_QUERY = "UPDATE accounts SET account_type=?,account_balance=? WHERE account_number=?";

	public static final String DELETE_ACCOUNT_BY_ACCTNO_QUERY = "DELETE from accounts WHERE account_number=?";
}
