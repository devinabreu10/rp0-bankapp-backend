package dev.abreu.bankapp.exception;

import java.io.Serial;

public class InsufficientFundsException extends Exception {

	/**
	 * throws exception if source account does not have enough funds to 
	 * complete transfer with target account
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1577624760966623176L;

	public InsufficientFundsException(String message) {
		super(message);
	}
}
