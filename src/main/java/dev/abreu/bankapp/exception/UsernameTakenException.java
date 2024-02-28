package dev.abreu.bankapp.exception;

public class UsernameTakenException extends Exception {

	/**
	 * throws exception if customer tries to register with a username that already exists
	 * 
	 */
	private static final long serialVersionUID = 4515974340454198388L;
	
	public UsernameTakenException() {
		super("Username is already taken. Please choose a different username.");
	}

	public UsernameTakenException(String message) {
		super(message);
	}
	
	

}
