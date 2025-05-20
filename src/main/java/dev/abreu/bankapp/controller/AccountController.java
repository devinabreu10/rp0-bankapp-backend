package dev.abreu.bankapp.controller;

import dev.abreu.bankapp.dto.AccountDTO;
import dev.abreu.bankapp.dto.AccountResponseDTO;
import dev.abreu.bankapp.dto.TransferRequest;
import dev.abreu.bankapp.dto.mapper.DtoMapper;
import dev.abreu.bankapp.entity.Account;
import dev.abreu.bankapp.exception.InsufficientFundsException;
import dev.abreu.bankapp.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling account-related operations.
 */
@RestController
@RequestMapping(path = "/account")
public class AccountController {

	private static final Logger log = LogManager.getLogger(AccountController.class);

	private final AccountService accountService;
	private final DtoMapper dtoMapper;

	public AccountController(AccountService accountService, DtoMapper dtoMapper) {
		this.accountService = accountService;
        this.dtoMapper = dtoMapper;
    }

	/**
	 * Retrieves an account by its account number.
	 *
	 * @param acctNo the account number
	 * @return the account with the specified account number
	 */
	@GetMapping(path = "/get/{acctNo}")
	public ResponseEntity<AccountResponseDTO> getAccountByAcctNo(@PathVariable("acctNo") Long acctNo) {
		log.info("Performing GET method to retrieve Account by AcctNo {}", acctNo);
		Account account = accountService.getAccountByAcctNo(acctNo);

		AccountResponseDTO dto = dtoMapper.toAccountResponseDto(account);

		return ResponseEntity.ok(dto);
	}

	/**
	 * Retrieves all accounts associated with a username.
	 *
	 * @param username the username
	 * @return a list of accounts associated with the username
	 */
	@GetMapping(path = "/get/list/{username}")
	public ResponseEntity<List<AccountResponseDTO>> getAllAccountsByUsername(@PathVariable("username") String username) {
		log.info("Performing GET method to retrieve all accounts by username");
		List<Account> accounts = accountService.getAllAccountsByUsername(username);

		List<AccountResponseDTO> dtoList = accounts.stream().map(dtoMapper::toAccountResponseDto).toList();

		return ResponseEntity.ok(dtoList);
	}

	/**
	 * Saves a new account.
	 *
	 * @param accountDto the account to save
	 * @return the saved account
	 */
	@PostMapping(path = "/save")
	public ResponseEntity<AccountResponseDTO> saveAccount(@RequestBody AccountDTO accountDto) {
		log.info("Performing POST method to save new Account");
		Account account = accountService.saveAccount(dtoMapper.toAccount(accountDto));
		AccountResponseDTO dto = dtoMapper.toAccountResponseDto(account);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	/**
     * Updates an existing account.
     *
     * @param acctNo the account number
     * @param accountDto the account details to update
     * @return the updated account
     */
	@PutMapping(path = "/update/{acctNo}")
	public ResponseEntity<AccountDTO> updateAccount(@PathVariable("acctNo") Long acctNo,
															@RequestBody AccountDTO accountDto) {
		log.info("Performing PUT method to update details for account with acctNo: {}", acctNo);

		if(!accountDto.accountNumber().equals(acctNo)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}

		Account account = dtoMapper.toAccount(accountDto);
		account = accountService.updateAccount(account);

		if(account != null) {
			return ResponseEntity.ok(accountDto);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	/**
     * Deletes an account by its account number.
     *
     * @param acctNo the account number
     * @return a response entity indicating the result of the operation
     */
	@DeleteMapping(path = "/delete/{acctNo}")
	public ResponseEntity<String> deleteAccountByAcctNo(@PathVariable("acctNo") Long acctNo) {
		log.info("Performing DELETE method for account with acctNo: {}", acctNo);

		boolean success = accountService.deleteAccountByAcctNo(acctNo);

		if(Boolean.TRUE.equals(success)) {
			return new ResponseEntity<>("{\"success\": \"Account successfully deleted...\"}", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("{\"error\": \"Account could not be deleted...\"}", HttpStatus.CONFLICT);
		}
	}

	/**
     * Transfers funds between accounts.
     *
     * @param transferRequest the transfer request details
     * @return a response entity indicating the result of the operation
     * @throws InsufficientFundsException if there are insufficient funds in the source account
     */
	@PostMapping(path = "/transferFunds")
	public ResponseEntity<String> transferFundsBetweenAccounts(@RequestBody TransferRequest transferRequest) throws InsufficientFundsException {
		log.info("Performing POST method to transfer funds between Account # {} and Account # {}",
					transferRequest.sourceAccountNumber(), transferRequest.targetAccountNumber());

        accountService.transferFundsBetweenAccounts(transferRequest.sourceAccountNumber(),
        		transferRequest.targetAccountNumber(), transferRequest.amount());

        return new ResponseEntity<>("Successfully transferred funds between accounts...", HttpStatus.OK);
	}

	/**
     * Deposits funds into an account.
     *
     * @param acctNo the account number
     * @param amount the amount to deposit
     * @return a response entity indicating the result of the operation
     */
	@PutMapping(path = "/{acctNo}/deposit/{amount}")
	public ResponseEntity<String> depositFundsIntoAccount(@PathVariable("acctNo") Long acctNo, @PathVariable("amount") double amount) {
		log.info("Performing PUT method to deposit funds into Account with acctNo: {}", acctNo);
		accountService.depositFundsIntoAccount(acctNo, amount);

		return new ResponseEntity<>("Successfully deposited funds into account...", HttpStatus.OK);
	}

	/**
     * Withdraws funds from an account.
     *
     * @param acctNo the account number
     * @param amount the amount to withdraw
     * @return a response entity indicating the result of the operation
     * @throws InsufficientFundsException if there are insufficient funds in the account
     */
	@PutMapping(path = "/{acctNo}/withdraw/{amount}")
	public ResponseEntity<String> withdrawFundsFromAccount(@PathVariable("acctNo") Long acctNo, @PathVariable("amount") double amount) throws InsufficientFundsException {
		log.info("Performing PUT method to withdraw funds from Account with acctNo: {}", acctNo);
		accountService.withdrawFundsFromAccount(acctNo, amount);

		return new ResponseEntity<>("Successfully withdrew funds from account...", HttpStatus.OK);
	}
}
