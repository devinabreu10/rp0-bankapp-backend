package dev.abreu.bankapp.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

public class Credit {
	
	private @Id Long creditId;
	private double creditBalance;
	private double minimumDue;
	private int creditLimit;
	private LocalDateTime expiryDate;
	private LocalDateTime dueDate;
	AggregateReference<Customer, Long> customerId;
	
	public Credit() {
		
	}
	
	public Credit(double creditBalance, int creditLimit, AggregateReference<Customer, Long> customerId) {
		this.creditBalance = creditBalance;
		this.minimumDue = creditBalance*0.05;
		this.creditLimit = creditLimit;
		this.expiryDate = LocalDateTime.now().plusYears(3);
		this.dueDate = LocalDateTime.now().plusMonths(1);
		this.customerId = customerId;
	}

	public Long getCreditId() {
		return creditId;
	}

	public void setCreditId(Long creditId) {
		this.creditId = creditId;
	}

	public double getCreditBalance() {
		return creditBalance;
	}

	public void setCreditBalance(double creditBalance) {
		this.creditBalance = creditBalance;
	}

	public double getMinimumDue() {
		return minimumDue;
	}

	public void setMinimumDue(double minimumDue) {
		this.minimumDue = minimumDue;
	}

	public int getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(int creditLimit) {
		this.creditLimit = creditLimit;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}

	public LocalDateTime getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDateTime dueDate) {
		this.dueDate = dueDate;
	}

	@Override
	public String toString() {
		return "Credit [creditId=" + creditId + ", creditBalance=" + creditBalance + ", minimumDue=" + minimumDue
				+ ", creditLimit=" + creditLimit + ", expiryDate=" + expiryDate + ", dueDate=" + dueDate + "]";
	}

}
