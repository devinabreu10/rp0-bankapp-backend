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

import dev.abreu.bankapp.model.Account;
import dev.abreu.bankapp.service.AccountService;

@RestController
@RequestMapping(path = "/account")
public class AccountController {
	
	private static final Logger log = LogManager.getLogger(AccountController.class);
	
	private AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	//ResourceNotFoundException being handled by @ControllerAdvice
	
	@GetMapping(path = "/get/{acctNo}")
	public ResponseEntity<Account> getAccountByAcctNo(@PathVariable("acctNo") Long acctNo) {
		log.info("Performing GET method to retrieve Account by AcctNo {}", acctNo);
		Account account = accountService.getAccountByAcctNo(acctNo);

		return ResponseEntity.ok(account);
	}
	
	@GetMapping(path = "/get/list/{username}")
	public ResponseEntity<List<Account>> getAllAccountsByUsername(@PathVariable("username") String username) {
		log.info("Performing GET method to retrieve all accounts by username");
		List<Account> accounts = accountService.getAllAccountsByUsername(username);
		
		return ResponseEntity.ok(accounts);
	}
	
	@PostMapping(path = "/save")
	public ResponseEntity<Account> saveAccount(@RequestBody Account account) {
		log.info("Performing POST method to save new Account");
		account = accountService.saveAccount(account);
		return ResponseEntity.status(HttpStatus.CREATED).body(account);
	}
	
	@PutMapping(path = "/update/{acctNo}")
	public ResponseEntity<Account> updateAccount(@PathVariable("acctNo") Long acctNo, @RequestBody Account account) {
		log.info("Performing PUT method to update details for account with acctNo: {}", acctNo);
		
		if(!account.getAccountNumber().equals(acctNo)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		
		account = accountService.updateAccount(account);
		
		if(account != null) {
			return ResponseEntity.ok(account);
		} else {
			return ResponseEntity.badRequest().build();
		}

	}
	
	@DeleteMapping(path = "/delete/{acctNo}")
	public ResponseEntity<String> deleteAccountByAcctNo(@PathVariable("acctNo") Long acctNo) {
		log.info("Performing DELETE method for account with acctNo: {}", acctNo);
		
		boolean success = accountService.deleteAccountByAcctNo(acctNo);
		
		if(Boolean.TRUE.equals(success)) {
			return new ResponseEntity<>("Account successfully deleted...", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Account could not be deleted, please check backend...", HttpStatus.CONFLICT);
		}
	}

}
