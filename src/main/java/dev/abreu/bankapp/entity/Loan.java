package dev.abreu.bankapp.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

public class Loan {
	
	private @Id Long loanId;
	private double loanAmount;
	private int loanLength;
	private double interest;
	private double minimumDue;
	private LocalDateTime dueDate; // how do I establish date in Java class?
	AggregateReference<Customer, Long> customerId;
	
	public Loan(){
		
	}
	
	public Loan(double loanAmount, int loanLength, double interest, AggregateReference<Customer, Long> customerId) { 
		
		this.loanAmount = loanAmount;
		this.loanLength = loanLength;
		this.interest = interest;
		this.minimumDue = (loanAmount + (loanAmount*interest))/loanLength; // minimum due every month
		this.dueDate = LocalDateTime.now().plusMonths(loanLength); //when the loan is due
		this.customerId = customerId;
	}

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}

	public int getLoanLength() {
		return loanLength;
	}

	public void setLoanLength(int loanLength) {
		this.loanLength = loanLength;
	}

	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public double getMinimumDue() {
		return minimumDue;
	}

	public void setMinimumDue(double minimumDue) {
		this.minimumDue = minimumDue;
	}

	public LocalDateTime getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDateTime dueDate) {
		this.dueDate = dueDate;
	}

	@Override
	public String toString() {
		return "Loan [loanId=" + loanId + ", loanAmount=" + loanAmount + ", loanLength=" + loanLength + ", interest="
				+ interest + ", minimumDue=" + minimumDue + ", dueDate=" + dueDate + "]";
	}

}
