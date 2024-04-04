package dev.abreu.bankapp.dto;

public class TransferRequest {
	
	private Long sourceAccountNumber;
	private Long targetAccountNumber;
	private double amount;
	
	public Long getSourceAccountNumber() {
		return sourceAccountNumber;
	}
	public void setSourceAccountNumber(Long sourceAccountNumber) {
		this.sourceAccountNumber = sourceAccountNumber;
	}
	public Long getTargetAccountNumber() {
		return targetAccountNumber;
	}
	public void setTargetAccountNumber(Long targetAccountNumber) {
		this.targetAccountNumber = targetAccountNumber;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
}
