package dev.abreu.bankapp.model.dto;

import dev.abreu.bankapp.model.Customer;

/**
 * Customer DTO (Data Transfer Object) to prepare Customer object to be sent in HTTP
 * request and received in a response
 * 
 * @author Devin Abreu
 */
public class CustomerDTO {
	private Long id;
	private String firstName;
	private String lastName;
	private String username;
	private String address;
	
	public CustomerDTO() {}
	
	public CustomerDTO(Customer customer) {
		this.id = customer.getId();
		this.firstName = customer.getFirstName();
		this.lastName = customer.getLastName();
		this.username = customer.getUsername();
		this.address = customer.getAddress();
	}
	
	public Customer toEntity() {
		Customer customer = new Customer();
		customer.setId(this.id);
		customer.setFirstName(this.firstName);
		customer.setLastName(this.lastName);
		customer.setUsername(this.username);
		customer.setAddress(this.address);
		return customer;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
