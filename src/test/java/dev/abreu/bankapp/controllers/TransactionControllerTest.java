package dev.abreu.bankapp.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.abreu.bankapp.dao.AccountDao;
import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.dao.TransactionDao;
import dev.abreu.bankapp.model.Transaction;
import dev.abreu.bankapp.service.TransactionService;
import dev.abreu.bankapp.utils.BankappConstants;

@WebMvcTest(controllers = TransactionController.class)
class TransactionControllerTest {
	
	@MockBean
	TransactionService transactionService;
	
	@MockBean
	private CustomerDao customerDao;
	
	@MockBean
	private AccountDao accountDao;
	
	@MockBean
	private TransactionDao transactionDao;

	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	@BeforeEach
	void setup() {
		jsonMapper.findAndRegisterModules();
		jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Test
	void testGetTransactionById() throws JsonProcessingException, Exception {
		Transaction mockTxn = new Transaction(BankappConstants.ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L);
		
		Mockito.when(transactionService.getTransactionById(1L)).thenReturn(mockTxn);

		mockMvc.perform(get("/transaction/get/1"))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonMapper.writeValueAsString(mockTxn)));
	}

	@Test
	void testGetAllTransactionsByAcctNo() throws JsonProcessingException, Exception {
		List<Transaction> txnList = List.of(
				new Transaction(BankappConstants.ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L),
				new Transaction(BankappConstants.ACCOUNT_WITHDRAW, 50.00, "Withdrawed $50.00", 12345L));
		
		Mockito.when(transactionService.getAllTransactionsByAcctNo(12345L)).thenReturn(txnList);
		
		mockMvc.perform(get("/transaction/get/list/12345"))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonMapper.writeValueAsString(txnList)));
	}

	@Test
	void testSaveTransaction() throws JsonProcessingException, Exception {
		Transaction mockTxn = new Transaction(BankappConstants.ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L);
		
		Mockito.when(transactionService.saveTransaction(mockTxn)).thenReturn(mockTxn);
		
		mockMvc.perform(post("/transaction/save")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockTxn)))
				.andExpect(status().isCreated())
				.andReturn();
	}

	@Test
	void testUpdateTransaction() throws JsonProcessingException, Exception {
		Transaction mockTxn = new Transaction(BankappConstants.ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L);
		mockTxn.setTransactionId(1L);
		
		Mockito.when(transactionService.updateTransactionDetails(any(Transaction.class))).thenReturn(mockTxn);

		mockMvc.perform(put("/transaction/update/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockTxn)))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonMapper.writeValueAsString(mockTxn)));
		
		verify(transactionService, times(1)).updateTransactionDetails(any(Transaction.class));
	}
	
	@Test
	void testUpdateTransactionConflict() throws JsonProcessingException, Exception {
		Transaction mockTxn = new Transaction(BankappConstants.ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L);
		mockTxn.setTransactionId(1L);
		
		Mockito.when(transactionService.updateTransactionDetails(any(Transaction.class))).thenReturn(mockTxn);

		mockMvc.perform(put("/transaction/update/2")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockTxn)))
				.andExpect(status().isConflict());
	}
	
	@Test
	void testUpdateTransactionBadRequest() throws JsonProcessingException, Exception {
		Transaction mockTxn = new Transaction(BankappConstants.ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L);
		mockTxn.setTransactionId(1L);
		
		Mockito.when(transactionService.updateTransactionDetails(any(Transaction.class))).thenReturn(null);

		mockMvc.perform(put("/transaction/update/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockTxn)))
				.andExpect(status().isBadRequest());
	}


	@Test
	void testDeleteTransactionByIdSuccess() throws Exception {
		Transaction mockTxn = new Transaction(BankappConstants.ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L);
		mockTxn.setTransactionId(1L);
		
		Mockito.when(transactionService.deleteTransactionById(1L)).thenReturn(Boolean.TRUE);
		
		mockMvc.perform(delete("/transaction/delete/1"))
				.andExpect(status().isOk())
				.andDo(print());
	}
	
	@Test
	void testDeleteTransactionByIdUnsuccessful() throws Exception {
		Transaction mockTxn = new Transaction(BankappConstants.ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L);
		mockTxn.setTransactionId(1L);
		
		Mockito.when(transactionService.deleteTransactionById(1L)).thenReturn(Boolean.FALSE);
		
		mockMvc.perform(delete("/transaction/delete/1"))
				.andExpect(status().isConflict())
				.andDo(print());
	}

}
