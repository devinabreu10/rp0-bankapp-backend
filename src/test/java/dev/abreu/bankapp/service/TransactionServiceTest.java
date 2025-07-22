package dev.abreu.bankapp.service;

import dev.abreu.bankapp.dao.AccountDao;
import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.dao.TransactionDao;
import dev.abreu.bankapp.entity.Account;
import dev.abreu.bankapp.entity.Transaction;
import dev.abreu.bankapp.exception.ResourceNotFoundException;
import dev.abreu.bankapp.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static dev.abreu.bankapp.util.BankappConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes =  TransactionServiceImpl.class)
class TransactionServiceTest {
	
	@MockBean
	private CustomerDao customerDao;
	
	@MockBean
	private AccountDao accountDao;
	
	@MockBean
	private TransactionDao transactionDao;
	
	@Autowired
	private TransactionService transactionService;

	@Test
	void testGetTransactionById() {
		Transaction txn = new Transaction(ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L);
		
		Mockito.when(transactionDao.findTransactionById(1L)).thenReturn(Optional.of(txn));
		
		Transaction result = transactionService.getTransactionById(1L);
		
		assertEquals(txn, result);
	}
	
	@Test
	void testGetTransactionByIdResourceNotFound() {
		Mockito.when(transactionDao.findTransactionById(1L)).thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class, 
				() -> transactionService.getTransactionById(1L));
	}

	@Test
	void testGetAllTransactionsByAcctNo() {
		Account mockAccount = new Account(12345L, CHECKING_ACCOUNT, 100.00, 1L);
		List<Transaction> txnList = List.of(
				new Transaction(ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L),
				new Transaction(ACCOUNT_WITHDRAW, 50.00, "Withdrawed $50.00", 12345L));
		
		Mockito.when(accountDao.findAccountByAcctNo(12345L)).thenReturn(Optional.of(mockAccount));
		
		Mockito.when(transactionDao.findAllTransactionsByAcctNo(12345L)).thenReturn(txnList);
		
		List<Transaction> result = transactionService.getAllTransactionsByAcctNo(12345L);
		
		assertEquals(txnList, result);	
	}
	
	@Test
	void testGetAllTransactionsByAcctNoResourceNotFound() {
		Mockito.when(accountDao.findAccountByAcctNo(12345L)).thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class, 
				() -> transactionService.getAllTransactionsByAcctNo(12345L));
	}

	@Test
	void testSaveTransaction() {
		Transaction txn = new Transaction(ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L);
		
		Mockito.when(transactionDao.saveTransaction(txn)).thenReturn(txn);
		
		Transaction result = transactionService.saveTransaction(txn);
		
		assertNotNull(result);
	}

	@Test
	void testUpdateTransactionDetails() {
		Transaction txn = new Transaction(ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L);
		Transaction txnUpdate = new Transaction(ACCOUNT_DEPOSIT, 75.00, "TEST", 12345L);
		
		Mockito.when(transactionDao.updateTransaction(txn)).thenReturn(txnUpdate);
		
		Transaction result = transactionService.updateTransactionDetails(txn);
		
		assertEquals(txnUpdate, result);
	}

	@Test
	void testDeleteTransactionById() {
		Transaction mockTxn = new Transaction();
		mockTxn.setTransactionId(1L);
		
		Mockito.when(transactionDao.findTransactionById(1L)).thenReturn(Optional.of(mockTxn));
		
		Mockito.when(transactionDao.deleteTransactionById(1L)).thenReturn(Boolean.TRUE);
		
		boolean result = transactionService.deleteTransactionById(1L);
		
		assertTrue(result);
	}
	
	@Test
	void testDeleteTransactionByIdNotFound() {
		Transaction mockTxn = new Transaction();
		mockTxn.setTransactionId(1L);
		
		Mockito.when(transactionDao.findTransactionById(1L)).thenReturn(Optional.empty());
		
		Mockito.when(transactionDao.deleteTransactionById(1L)).thenReturn(Boolean.FALSE);
		
		boolean result = transactionService.deleteTransactionById(1L);
		
		assertFalse(result);
	}

	@Test
	void testTransactionToString() {
		Transaction txn = new Transaction(ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L);

		String result = String.valueOf(txn);

		assertEquals(txn.toString(), result);
	}

	@Test
	void testGetAllTransactionsAndTransfersByCustomerId() {
		Long customerId = 1L;
		List<Transaction> mockTransactions = List.of(
			new Transaction(ACCOUNT_DEPOSIT, 100.00, "Deposit", 12345L),
			new Transaction(ACCOUNT_DEPOSIT, 200.00, "Deposit", 12346L)
		);
		Mockito.when(transactionDao.findAllTransactionsAndTransfersByCustomerId(customerId)).thenReturn(mockTransactions);
		List<Transaction> result = transactionService.getAllTransactionsAndTransfersByCustomerId(customerId);
		assertEquals(mockTransactions, result);
		Mockito.verify(transactionDao, Mockito.times(1)).findAllTransactionsAndTransfersByCustomerId(customerId);
	}

}
