package dev.abreu.bankapp.util;

/**
 * Constants that will be used throughout the project for clarity
 * 
 * @author Devin Abreu
 *
 */
public class BankappConstants {
	
	private BankappConstants() {}
	
	public static final String CHECKING_ACCOUNT = "Checking";
	public static final String SAVINGS_ACCOUNT = "Savings";
	public static final String ACCOUNT_DEPOSIT = "Account Deposit";
	public static final String ACCOUNT_WITHDRAW = "Account Withdraw";
	public static final String ACCOUNT_TRANSFER = "Account Transfer";
	
	public static final String JWT_RP0_BANKAPP_ISSUER = "rp0-bankapp";
	
	//Caught exception statements
	public static final String SQL_EXCEPTION_CAUGHT= "SQLException Caught: {}";

}
