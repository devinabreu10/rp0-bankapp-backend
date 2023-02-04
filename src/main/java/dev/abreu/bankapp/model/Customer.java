package dev.abreu.bankapp.model;

import org.springframework.data.annotation.Id;

public class Customer {
	
	private @Id Long id;
	private String firstName;
	private String lastName;
	private String address;
	private String username;
	private String password;
	
	// still unsure on how these will be implemented
	//private Account account;
	//List<Account> accounts;
	
	public Customer() {
		
	}
	
	public Customer(Long id, String firstName, String lastName, String address, String username, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Used for a Customer who already has an account and wants to sign in
	 * 
	 * @param username
	 * @param password
	 */
	public Customer(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", address=" + address
				+ ", username=" + username + ", password=" + password + "]";
	}

}
