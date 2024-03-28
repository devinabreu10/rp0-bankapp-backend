package dev.abreu.bankapp.exception;

public class TokenExpirationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3460541969272723273L;
	
	public TokenExpirationException() {
		super("Token has expired, please try logging in again...");
	}

}
