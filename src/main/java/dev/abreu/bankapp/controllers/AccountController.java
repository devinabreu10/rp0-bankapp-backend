package dev.abreu.bankapp.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.abreu.bankapp.service.AccountService;

@RestController
@RequestMapping(path = "/account")
public class AccountController {
	
	private static final Logger log = LogManager.getLogger(AccountController.class);
	
	private AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	

}
