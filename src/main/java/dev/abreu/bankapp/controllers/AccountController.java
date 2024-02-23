package dev.abreu.bankapp.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@GetMapping(path = "/get/{acctNo}")
	public ResponseEntity<Account> getAccountByAcctNo(@PathVariable("acctNo") Long acctNo) {
		
		Account account = accountService.getAccountByAcctNo(acctNo);
//		
//		if(account != null) {
//			log.info("Account successfully retrieved...");
//			return ResponseEntity.ok(account);
//		} else {
//			log.error("The requested account with acctNo {} does not exist", acctNo);
//			throw new ResourceNotFoundException(ResourceType.ACCOUNT, acctNo);
//		}
		
		return ResponseEntity.ok(account);
	}
	
	

}
