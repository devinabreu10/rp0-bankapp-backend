package dev.abreu.bankapp.util;

/**
 * Constants that will be used for SQL queries
 * 
 * @author Devin Abreu
 *
 */
public class BankappQueryConstants {

	private static final String SCHEMA_NM = "\"rp0-bankapp\".";
	private static final String CUSTOMERS_TABLE = SCHEMA_NM + "customers";
	private static final String ACCOUNTS_TABLE = SCHEMA_NM + "accounts";
	private static final String TRANSACTIONS_TABLE = SCHEMA_NM + "transactions";
	
	private BankappQueryConstants() {}
	
	public static final String SELECT_CUSTOMERS_BY_USERNAME_QUERY = "SELECT customer_id,first_name,last_name,address,username,passwrd FROM " + CUSTOMERS_TABLE + " WHERE username=?";
	
	public static final String SELECT_CUSTOMERS_BY_ID_QUERY = "SELECT customer_id,first_name,last_name,address,username,passwrd FROM " + CUSTOMERS_TABLE + " WHERE customer_id=?";
	
	public static final String SELECT_ALL_CUSTOMERS_QUERY = "SELECT * FROM " + CUSTOMERS_TABLE;
	
	public static final String CREATE_CUSTOMER_QUERY = "INSERT into " + CUSTOMERS_TABLE + " (customer_id,first_name,last_name,address,username,passwrd) VALUES (default,?,?,?,?,?)";

	public static final String UPDATE_CUSTOMER_QUERY = "UPDATE " + CUSTOMERS_TABLE + " SET first_name=?,last_name=?,address=?,username=? WHERE customer_id=?";

	public static final String DELETE_CUSTOMER_BY_USERNAME_QUERY = "DELETE from " + CUSTOMERS_TABLE + " WHERE username=?";
	
	public static final String DELETE_CUSTOMER_BY_ID_QUERY = "DELETE from " + CUSTOMERS_TABLE + " WHERE customer_id=?";
	
	public static final String SELECT_ACCOUNTS_BY_ACCTNO_QUERY = "SELECT * FROM " + ACCOUNTS_TABLE + " WHERE account_number=?";
	
	public static final String SELECT_ALL_ACCOUNTS_BY_USERNAME_QUERY = "SELECT b.username,a.customer_id as cust_id,a.account_number as acc_no,a.account_type as acc_typ,a.account_balance as acc_bal "
						+ "FROM " + ACCOUNTS_TABLE + " a JOIN " + CUSTOMERS_TABLE + " b ON a.customer_id = b.customer_id WHERE b.username=?";

	public static final String CREATE_NEW_ACCOUNT_QUERY = "INSERT into " + ACCOUNTS_TABLE + " (account_number,account_type,account_balance,customer_id) VALUES (default,?,?,?)";

	public static final String UPDATE_ACCOUNT_QUERY = "UPDATE " + ACCOUNTS_TABLE + " SET account_type=?,account_balance=? WHERE account_number=?";

	public static final String DELETE_ACCOUNT_BY_ACCTNO_QUERY = "DELETE from " + ACCOUNTS_TABLE + " WHERE account_number=?";
	
	public static final String SELECT_TRANSACTIONS_BY_ID_QUERY = "SELECT * FROM " + TRANSACTIONS_TABLE + " WHERE transaction_id=?";

	public static final String SELECT_ALL_TRANSACTIONS_BY_ACCTNO_QUERY = "SELECT * FROM " + TRANSACTIONS_TABLE + " WHERE account_number=?";

	public static final String CREATE_NEW_TRANSACTION_QUERY = "INSERT into " + TRANSACTIONS_TABLE + " (transaction_id,transaction_type,transaction_amount,transaction_notes,transaction_date,account_number) VALUES (default,?,?,?,?,?)";

	public static final String UPDATE_TRANSACTION_QUERY = "UPDATE " + TRANSACTIONS_TABLE + " SET transaction_type=?,transaction_amount=?,transaction_notes=? WHERE transaction_id=?";

	public static final String DELETE_TRANSACTION_BY_ID_QUERY = "DELETE from " + TRANSACTIONS_TABLE + " WHERE transaction_id=?";
}
