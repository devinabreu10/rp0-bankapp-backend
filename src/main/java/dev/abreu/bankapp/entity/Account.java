package dev.abreu.bankapp.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Represents an Account entity.
 * 
 * @author Devin Abreu
 */
@Table(name = "accounts")
public class Account {

	private @Id Long accountNumber;
	private String nickname;
	private String accountType;
	private double accountBalance;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Long customerId;

	public Account() {
		
	}
	
	public Account(String accountType, double initialDeposit, Long customerId) {
		this.accountType = accountType;
		this.accountBalance = initialDeposit;
		this.customerId = customerId;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}
	
	public Account(Long accountNumber, String accountType, double initialDeposit, Long customerId) {
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.accountBalance = initialDeposit;
		this.customerId = customerId;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
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
		return "Account{" +
				"accountNumber=" + accountNumber +
				", nickname='" + nickname + '\'' +
				", accountType='" + accountType + '\'' +
				", accountBalance=" + accountBalance +
				", createdAt=" + createdAt +
				", updatedAt=" + updatedAt +
				", customerId=" + customerId +
				'}';
	}
}
