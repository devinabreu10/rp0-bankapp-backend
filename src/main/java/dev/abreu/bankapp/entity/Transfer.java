package dev.abreu.bankapp.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Represents a Transfer entity.
 * 
 * @author Devin Abreu
 */
@Table(name = "transfers")
public class Transfer {

	private @Id Long transferId;
	private Long sourceAccountNumber;
	private Long targetAccountNumber;
	private double transferAmount;
	private String transferNotes;
	private LocalDateTime createdAt;

	public Transfer() {
		
	}

	public Transfer(Long sourceAccountNumber, Long targetAccountNumber, double transferAmount, String transferNotes) {
		this.sourceAccountNumber = sourceAccountNumber;
		this.targetAccountNumber = targetAccountNumber;
		this.transferAmount = transferAmount;
		this.transferNotes = transferNotes;
		this.createdAt = LocalDateTime.now();
	}
	
	public Transfer(Long transferId, Long sourceAccountNumber, Long targetAccountNumber, double transferAmount, String transferNotes) {
		this.transferId = transferId;
		this.sourceAccountNumber = sourceAccountNumber;
		this.targetAccountNumber = targetAccountNumber;
		this.transferAmount = transferAmount;
		this.transferNotes = transferNotes;
		this.createdAt = LocalDateTime.now();
	}

	public Long getTransferId() {
		return transferId;
	}

	public void setTransferId(Long transferId) {
		this.transferId = transferId;
	}

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

	public double getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(double transferAmount) {
		this.transferAmount = transferAmount;
	}

	public String getTransferNotes() {
		return transferNotes;
	}

	public void setTransferNotes(String transferNotes) {
		this.transferNotes = transferNotes;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Transfer [transferId=" + transferId + ", sourceAccountNumber=" + sourceAccountNumber
				+ ", targetAccountNumber=" + targetAccountNumber + ", transferAmount=" + transferAmount
				+ ", transferNotes=" + transferNotes + ", createdAt=" + createdAt + "]";
	}

}