package dev.abreu.bankapp.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Represents an Account entity.
 * 
 * @author Devin Abreu
 */
@Table(name = "accounts")
public class Account {

	private @Id Long accountNumber;
	private String accountType;
	private double accountBalance;
	private Long customerId;
	
	public Account() {
		
	}
	
	public Account(String accountType, double initialDeposit, Long customerId) {
		this.accountType = accountType;
		this.accountBalance = initialDeposit;
		this.customerId = customerId;
	}
	
	public Account(Long accountNumber, String accountType, double initialDeposit, Long customerId) {
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.accountBalance = initialDeposit;
		this.customerId = customerId;
	}
	
	
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}
	
	public void incrementBalance(double amount) {
		this.accountBalance += amount;
	}
	
	public void decrementBalance(double amount) {
		this.accountBalance -= amount;
	}

	@Override
	public String toString() {
		return "Account [accountNumber=" + accountNumber + ", accountType=" + accountType + ", accountBalance="
				+ accountBalance + ", customerId=" + customerId + "]";
	}

}
