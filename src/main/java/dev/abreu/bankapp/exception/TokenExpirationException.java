package dev.abreu.bankapp.exception;

import java.io.Serial;

public class TokenExpirationException extends Exception {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = -3460541969272723273L;
	
	public TokenExpirationException() {
		super("Token has expired, please try logging in again...");
	}

}
