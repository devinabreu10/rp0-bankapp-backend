package dev.abreu.bankapp.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Represents a Transaction entity.
 * 
 * @author Devin Abreu
 */
@Table(name = "transactions")
public class Transaction {

	private @Id Long transactionId;
	private String transactionType;
	private double transactionAmount;
	private String transactionNotes;
	private LocalDateTime createdAt;
	private Long accountNumber;

	public Transaction() {
		
	}

	public Transaction(String type, double amount, String notes, Long accountNumber) {
		this.transactionType = type;
		this.transactionAmount = amount;
		this.transactionNotes = notes;
		this.createdAt = LocalDateTime.now();
		this.accountNumber = accountNumber;
	}
	
	public Transaction(Long id, String type, double amount, String notes, Long accountNumber) {
		this.transactionId = id;
		this.transactionType = type;
		this.transactionAmount = amount;
		this.transactionNotes = notes;
		this.createdAt = LocalDateTime.now();
		this.accountNumber = accountNumber;
	}

	public Long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Long accountId) {
		this.accountNumber = accountId;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getTransactionNotes() {
		return transactionNotes;
	}

	public void setTransactionNotes(String transactionNotes) {
		this.transactionNotes = transactionNotes;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", transactionType=" + transactionType
				+ ", transactionAmount=" + transactionAmount + ", transactionNotes=" + transactionNotes
				+ ", createdAt=" + createdAt + "]";
	}

}
