package dev.abreu.bankapp.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "transactions")
public class Transaction {

	private @Id Long transactionId;
	private String transactionType;
	private double transactionAmount;
	private String transactionNotes;
	private LocalDateTime transactionDate;
	private Long accountId;
//	AggregateReference<Account, Long> accountId;
//	AggregateReference<Loan, Long> loanId;
//	AggregateReference<Credit, Long> creditId;

	public Transaction() {
		
	}
	
	public Transaction(String type, double amount, String notes, Long accountId/*AggregateReference<Account, Long> accountId*/) {
		this.transactionType = type;
		this.transactionAmount = amount;
		this.transactionNotes = notes;
		this.transactionDate = LocalDateTime.now();
		this.accountId = accountId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
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

	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", transactionType=" + transactionType
				+ ", transactionAmount=" + transactionAmount + ", transactionNotes=" + transactionNotes
				+ ", transactionDate=" + transactionDate + "]";
	}

}
