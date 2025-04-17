package dev.abreu.bankapp.service;

import static dev.abreu.bankapp.util.BankappConstants.CHECKING_ACCOUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import dev.abreu.bankapp.dao.AccountDao;
import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.dao.TransactionDao;
import dev.abreu.bankapp.exception.InsufficientFundsException;
import dev.abreu.bankapp.exception.ResourceNotFoundException;
import dev.abreu.bankapp.entity.Account;
import dev.abreu.bankapp.service.impl.AccountServiceImpl;

@SpringBootTest(classes =  AccountServiceImpl.class)
class AccountServiceTest {
	
	@MockBean
	private CustomerDao customerDao;
	
	@MockBean
	private AccountDao accountDao;
	
	@MockBean
	private TransactionDao transactionDao;
	
	@Autowired
	private AccountService accountService;

	@Test
	void testGetAccountByAcctNo() {
		Account mockAccount = new Account();
		mockAccount.setAccountNumber(12345L);
		
		Mockito.when(accountDao.findAccountByAcctNo(12345L)).thenReturn(Optional.of(mockAccount));
		
		Account result = accountService.getAccountByAcctNo(12345L);
		
		assertEquals(mockAccount, result);
	}
	
	@Test
	void testGetAccountByAcctNoResourceNotFound() {
		Mockito.when(accountDao.findAccountByAcctNo(12345L)).thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class, 
				() -> accountService.getAccountByAcctNo(12345L));
	}

	@Test
	void testGetAllAccountsByUsername() {
		String testUsername = "test";
		List<Account> mockAccountList = new ArrayList<>();
		Account mockAccount1 = new Account();
		Account mockAccount2 = new Account();
		mockAccountList.add(mockAccount1);
		mockAccountList.add(mockAccount2);
		
		Mockito.when(customerDao.existsByUsername(testUsername)).thenReturn(Boolean.TRUE);
		
		Mockito.when(accountDao.findAllAccountsByUsername(testUsername)).thenReturn(mockAccountList);
		
		List<Account> result = accountService.getAllAccountsByUsername(testUsername);
		
		assertEquals(mockAccountList, result);
	}
	
	@Test
	void testGetAllAccountsByUsernameResourceNotFound() {
		String testUsername = "test";
		
		Mockito.when(customerDao.existsByUsername(testUsername)).thenReturn(Boolean.FALSE);
		
		assertThrows(ResourceNotFoundException.class, 
				() -> accountService.getAllAccountsByUsername(testUsername));
	}

	@Test
	void testSaveAccount() {
		Account mockAccount = new Account();
		mockAccount.setAccountNumber(12345L);
		
		Mockito.when(accountDao.saveAccount(mockAccount)).thenReturn(mockAccount);
		
		Account result = accountService.saveAccount(mockAccount);
		
		assertEquals(mockAccount, result);
	}

	@Test
	void testUpdateAccount() {
		Account mockAccount = new Account();
		mockAccount.setAccountNumber(12345L);
		Account mockAccountUpdate = new Account();
		mockAccountUpdate.setAccountNumber(12345L);
		mockAccountUpdate.setAccountType(CHECKING_ACCOUNT);
		
		Mockito.when(accountDao.updateAccount(mockAccount)).thenReturn(mockAccountUpdate);
		
		Account result = accountService.updateAccount(mockAccount);
		
		assertEquals(mockAccountUpdate, result);
	}

	@Test
	void testDeleteAccountByAcctNo() {
		Account mockAccount = new Account();
		mockAccount.setAccountNumber(12345L);
		
		Mockito.when(accountDao.findAccountByAcctNo(12345L)).thenReturn(Optional.of(mockAccount));
		
		Mockito.when(accountDao.deleteAccountByAcctNo(12345L)).thenReturn(Boolean.TRUE);
		
		boolean result = accountService.deleteAccountByAcctNo(12345L);
		
		assertTrue(result);
	}
	
	@Test
	void testDeleteAccountByAcctNoNotFound() {
		Mockito.when(accountDao.findAccountByAcctNo(12345L)).thenReturn(Optional.empty());
		
		Mockito.when(accountDao.deleteAccountByAcctNo(12345L)).thenReturn(Boolean.FALSE);
		
		boolean result = accountService.deleteAccountByAcctNo(12345L);
		
		assertFalse(result);
	}

	@Test
	void testTransferFundsBetweenAccounts() throws InsufficientFundsException {
		Optional<Account> mockSourceOpt = Optional.of(new Account(12345L, CHECKING_ACCOUNT, 100.00, 1L));
		Optional<Account> mockTargetOpt = Optional.of(new Account(45678L, CHECKING_ACCOUNT, 200.00, 2L));
		
		Mockito.when(accountDao.findAccountByAcctNo(12345L)).thenReturn(mockSourceOpt);
		Mockito.when(accountDao.findAccountByAcctNo(45678L)).thenReturn(mockTargetOpt);
		
		accountService.transferFundsBetweenAccounts(12345L, 45678L, 99.00);
		
		assertEquals(299.00, mockTargetOpt.get().getAccountBalance());
	}
	
	@Test
	void testTransferFundsBetweenAccountsWithInsufficientFunds() throws InsufficientFundsException {
		Optional<Account> mockSourceOpt = Optional.of(new Account(12345L, CHECKING_ACCOUNT, 100.00, 1L));
		Optional<Account> mockTargetOpt = Optional.of(new Account(45678L, CHECKING_ACCOUNT, 200.00, 2L));
		
		Mockito.when(accountDao.findAccountByAcctNo(12345L)).thenReturn(mockSourceOpt);
		Mockito.when(accountDao.findAccountByAcctNo(45678L)).thenReturn(mockTargetOpt);
		
		Mockito.when(accountDao.updateAccount(mockSourceOpt.get())).thenReturn(mockSourceOpt.get());
		Mockito.when(accountDao.updateAccount(mockTargetOpt.get())).thenReturn(mockTargetOpt.get());
		
		accountService.transferFundsBetweenAccounts(12345L, 45678L, 99.00);
		
		assertThrows(InsufficientFundsException.class, 
				() -> accountService.transferFundsBetweenAccounts(12345L, 45678L, 101.00));
	}

	@Test
	void testDepositFundsIntoAccount() {
		Optional<Account> mockAccountOpt = Optional.of(new Account(12345L, CHECKING_ACCOUNT, 100.00, 1L));
		Account mockAccount = new Account(12345L, CHECKING_ACCOUNT, 150.00, 1L);
		
		Mockito.when(accountDao.findAccountByAcctNo(12345L)).thenReturn(mockAccountOpt);
		
		Mockito.when(accountDao.updateAccount(mockAccountOpt.get())).thenReturn(mockAccount);
		
		accountService.depositFundsIntoAccount(12345L, 50.00);
		
		assertEquals(150.00, mockAccountOpt.get().getAccountBalance());
	}

	@Test
	void testWithdrawFundsFromAccount() throws InsufficientFundsException {
		Optional<Account> mockAccountOpt = Optional.of(new Account(12345L, CHECKING_ACCOUNT, 100.00, 1L));
		Account mockAccount = new Account(12345L, CHECKING_ACCOUNT, 50.00, 1L);
		
		Mockito.when(accountDao.findAccountByAcctNo(12345L)).thenReturn(mockAccountOpt);
		
		Mockito.when(accountDao.updateAccount(mockAccountOpt.get())).thenReturn(mockAccount);
		
		accountService.withdrawFundsFromAccount(12345L, 50.00);
		
		assertEquals(50.00, mockAccountOpt.get().getAccountBalance());
	}
	
	@Test
	void testWithdrawFundsFromAccountWithInsufficientFunds() {
		Optional<Account> mockAccountOpt = Optional.of(new Account(12345L, CHECKING_ACCOUNT, 100.00, 1L));
		
		Mockito.when(accountDao.findAccountByAcctNo(12345L)).thenReturn(mockAccountOpt);
		
		assertThrows(InsufficientFundsException.class, 
				() -> accountService.withdrawFundsFromAccount(12345L, 150.00));
	}

}
