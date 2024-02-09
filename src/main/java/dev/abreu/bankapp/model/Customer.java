package dev.abreu.bankapp.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "customers")
public class Customer {
	
	private @Column(value="customer_id") @Id Long id;
	private String firstName;
	private String lastName;
	private String address;
	private String username;
	private @Column(value="passwrd") String password;
	private List<Account> accounts;
	
	// instantiates an empty Customer
	public Customer() {
		this.id = 0L;
		this.firstName = "";
		this.lastName = "";
		this.address = "";
		this.username = "";
		this.password = "";
		this.accounts = new ArrayList<>();
	}
	
	public Customer(Long id, String firstName, String lastName, String address, String username, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.username = username;
		this.password = password;
	}
	
	public Customer(Long id, String firstName, String lastName, String address, String username, String password, List<Account> accounts) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.username = username;
		this.password = password;
		this.accounts = accounts; // initial account associated with Customer
	}
	
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
	
	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", address=" + address
				+ ", username=" + username + ", password=" + password + ", accounts=" + accounts + "]";
	}

}
