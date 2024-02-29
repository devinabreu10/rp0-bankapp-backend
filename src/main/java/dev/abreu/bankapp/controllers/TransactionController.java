package dev.abreu.bankapp.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.abreu.bankapp.model.Transaction;
import dev.abreu.bankapp.service.TransactionService;

@RestController
@RequestMapping(path = "/transaction")
public class TransactionController {
	
	private static final Logger log = LogManager.getLogger(TransactionController.class);
	
	private TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}
	
	@GetMapping(path = "/get/{id}")
	public ResponseEntity<Transaction> getTransactionById(@PathVariable("id") Long txnId) {
		log.info("Performing GET method to retrieve Transaction by id {}", txnId);
		Transaction transaction = transactionService.getTransactionById(txnId);
		
		return ResponseEntity.ok(transaction);
	}
	
	@GetMapping(path = "/get/list/{acctNo}")
	public ResponseEntity<List<Transaction>> getAllTransactionsByAcctNo(@PathVariable("acctNo") Long acctNo) {
		log.info("Performing GET method to retrieve all transaction by account number");
		List<Transaction> transactions = transactionService.getAllTransactionsByAcctNo(acctNo);
		
		return ResponseEntity.ok(transactions);
	}
	
	@PostMapping(path = "/save")
	public ResponseEntity<Transaction> saveTransaction(@RequestBody Transaction transaction) {
		log.info("Performing POST method to save new Transaction");
		transaction = transactionService.saveTransaction(transaction);
		return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
	}
	
	@PutMapping(path = "/update/{txnId}")
	public ResponseEntity<Transaction> updateTransaction(@PathVariable("txnId") Long txnId, 
			@RequestBody Transaction transaction) {
		log.info("Performing PUT method to update details for transaction with id: {}", txnId);
		
		if(!transaction.getTransactionId().equals(txnId)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		
		transaction = transactionService.updateTransactionDetails(transaction);
		
		if(transaction != null) {
			return ResponseEntity.ok(transaction);
		} else {
			return ResponseEntity.badRequest().build();
		}

	}
	
	@DeleteMapping(path = "/delete/{txnId}")
	public ResponseEntity<String> deleteTransactionById(@PathVariable("txnId") Long txnId) {
		log.info("Performing DELETE method for transaction with id: {}", txnId);
		
		boolean success = transactionService.deleteTransactionById(txnId);
		
		if(Boolean.TRUE.equals(success)) {
			return new ResponseEntity<>("Transaction successfully deleted...", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Transaction could not be deleted, please check backend...", HttpStatus.CONFLICT);
		}
	}
	
}
