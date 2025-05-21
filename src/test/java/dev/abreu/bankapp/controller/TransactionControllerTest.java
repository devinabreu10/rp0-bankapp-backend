package dev.abreu.bankapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.abreu.bankapp.config.ApplicationConfig;
import dev.abreu.bankapp.dao.AccountDao;
import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.dao.TransactionDao;
import dev.abreu.bankapp.dto.TransactionDTO;
import dev.abreu.bankapp.dto.TransactionResponseDTO;
import dev.abreu.bankapp.dto.mapper.DtoMapper;
import dev.abreu.bankapp.entity.Transaction;
import dev.abreu.bankapp.security.JwtConfig;
import dev.abreu.bankapp.security.SecurityConfig;
import dev.abreu.bankapp.service.CustomerService;
import dev.abreu.bankapp.service.TransactionService;
import dev.abreu.bankapp.util.BankappConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({SecurityConfig.class, ApplicationConfig.class})
class TransactionControllerTest {
	
	@MockBean
	TransactionService transactionService;
	
	@MockBean
	private CustomerService customerService;
	
	@MockBean
	private CustomerDao customerDao;
	
	@MockBean
	private AccountDao accountDao;
	
	@MockBean
	private TransactionDao transactionDao;
	
	@MockBean
	JwtConfig jwtConfig;

	@MockBean
	DtoMapper dtoMapper;

	@Autowired
	private MockMvc mockMvc;
	
	private final ObjectMapper jsonMapper = new ObjectMapper();
	
	@BeforeEach
	void setup() {
		jsonMapper.findAndRegisterModules();
		jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Test
	void testGetTransactionById() throws Exception {
		Transaction mockTxn = new Transaction(BankappConstants.ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L);
		TransactionResponseDTO mockDto = new TransactionResponseDTO(1L, BankappConstants.ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", mockTxn.getCreatedAt());
		
		Mockito.when(transactionService.getTransactionById(1L)).thenReturn(mockTxn);
		Mockito.when(dtoMapper.toTransactionResponseDto(mockTxn)).thenReturn(mockDto);

		mockMvc.perform(get("/transaction/get/1"))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonMapper.writeValueAsString(mockDto)));
	}

	@Test
	void testGetAllTransactionsByAcctNo() throws Exception {
		List<Transaction> txnList = List.of(
				new Transaction(BankappConstants.ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L),
				new Transaction(BankappConstants.ACCOUNT_WITHDRAW, 50.00, "Withdraw $50.00", 12345L));

		List<TransactionResponseDTO> mockDtoList = new ArrayList<>();
		txnList.forEach(t -> mockDtoList
				.add(new TransactionResponseDTO(1L, t.getTransactionType(), t.getTransactionAmount(), t.getTransactionNotes(), t.getCreatedAt())));
		
		Mockito.when(transactionService.getAllTransactionsByAcctNo(12345L)).thenReturn(txnList);
		Mockito.when(dtoMapper.toTransactionResponseDto(txnList.get(0))).thenReturn(mockDtoList.get(0));
		Mockito.when(dtoMapper.toTransactionResponseDto(txnList.get(1))).thenReturn(mockDtoList.get(1));
		
		mockMvc.perform(get("/transaction/get/list/12345"))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonMapper.writeValueAsString(mockDtoList)));
	}

	@Test
	void testSaveTransaction() throws Exception {
		Transaction mockTxn = new Transaction(BankappConstants.ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L);
		TransactionResponseDTO mockDto = new TransactionResponseDTO(1L, BankappConstants.ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", mockTxn.getCreatedAt());
		
		Mockito.when(transactionService.saveTransaction(mockTxn)).thenReturn(mockTxn);
		Mockito.when(dtoMapper.toTransactionResponseDto(mockTxn)).thenReturn(mockDto);
		
		mockMvc.perform(post("/transaction/save")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockDto)))
				.andExpect(status().isCreated())
				.andReturn();
	}

	@Test
	void testUpdateTransaction() throws Exception {
		Transaction mockTxn = new Transaction(BankappConstants.ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L);
		TransactionDTO mockDto = new TransactionDTO(1L, BankappConstants.ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L);
		mockTxn.setTransactionId(1L);
		
		Mockito.when(transactionService.updateTransactionDetails(any(Transaction.class))).thenReturn(mockTxn);
		Mockito.when(dtoMapper.toTransaction(mockDto)).thenReturn(mockTxn);

		mockMvc.perform(put("/transaction/update/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockDto)))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonMapper.writeValueAsString(mockDto)));
		
		verify(transactionService, times(1)).updateTransactionDetails(any(Transaction.class));
	}
	
	@Test
	void testUpdateTransactionConflict() throws Exception {
		Transaction mockTxn = new Transaction(BankappConstants.ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L);
		TransactionDTO mockDto = new TransactionDTO(1L, BankappConstants.ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L);
		mockTxn.setTransactionId(1L);
		
		Mockito.when(transactionService.updateTransactionDetails(any(Transaction.class))).thenReturn(mockTxn);
		Mockito.when(dtoMapper.toTransaction(mockDto)).thenReturn(mockTxn);

		mockMvc.perform(put("/transaction/update/2")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockTxn)))
				.andExpect(status().isConflict());
	}
	
	@Test
	void testUpdateTransactionBadRequest() throws Exception {
		Transaction mockTxn = new Transaction(BankappConstants.ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L);
		TransactionDTO mockDto = new TransactionDTO(1L, BankappConstants.ACCOUNT_DEPOSIT, 100.00, "Deposited $100.00", 12345L);
		mockTxn.setTransactionId(1L);
		
		Mockito.when(transactionService.updateTransactionDetails(any(Transaction.class))).thenReturn(null);
		Mockito.when(dtoMapper.toTransaction(mockDto)).thenReturn(mockTxn);

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
