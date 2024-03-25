package dev.abreu.bankapp.controllers;

import static dev.abreu.bankapp.utils.BankappConstants.CHECKING_ACCOUNT;
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

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.abreu.bankapp.dao.AccountDao;
import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.model.Account;
import dev.abreu.bankapp.service.AccountService;

@WebMvcTest(controllers = AccountController.class)
class AccountControllerTest {
	
	@MockBean
	AccountService accountService;
	
	@MockBean
	private CustomerDao customerDao;
	
	@MockBean
	private AccountDao accountDao;

	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper jsonMapper = new ObjectMapper();

	@Test
	void testGetAccountByAcctNo() throws JsonProcessingException, Exception {
		Account mockAccount = new Account(12345L, CHECKING_ACCOUNT, 100.00, 1L);
		
		Mockito.when(accountService.getAccountByAcctNo(12345L)).thenReturn(mockAccount);
		
		mockMvc.perform(get("/account/get/12345"))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonMapper.writeValueAsString(mockAccount)));
	}

	@Test
	void testGetAllAccountsByUsername() throws JsonProcessingException, Exception {
		String testUsername = "test";
		List<Account> mockAccountList = new ArrayList<>();
		Account mockAccount = new Account(12345L, CHECKING_ACCOUNT, 100.00, 1L);
		mockAccountList.add(mockAccount);
		
		Mockito.when(accountService.getAllAccountsByUsername(testUsername)).thenReturn(mockAccountList);
		
		mockMvc.perform(get("/account/get/list/test"))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonMapper.writeValueAsString(mockAccountList)));
	}

	@Test
	void testSaveAccount() throws JsonProcessingException, Exception {
		Account mockAccount = new Account(12345L, CHECKING_ACCOUNT, 100.00, 1L);
		
		Mockito.when(accountService.saveAccount(mockAccount)).thenReturn(mockAccount);
		
		mockMvc.perform(post("/account/save")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockAccount)))
				.andExpect(status().isCreated())
				.andReturn();
	}

	@Test
	void testUpdateAccount() throws JsonProcessingException, Exception {
		Account mockAccount = new Account(12345L, CHECKING_ACCOUNT, 100.00, 1L);
		
		Mockito.when(accountService.updateAccount(any(Account.class))).thenReturn(mockAccount);

		mockMvc.perform(put("/account/update/12345")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockAccount)))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonMapper.writeValueAsString(mockAccount)));
		
		verify(accountService, times(1)).updateAccount(any(Account.class));
	}
	
	@Test
	void testUpdateAccountConflict() throws JsonProcessingException, Exception {
		Account mockAccount = new Account(12345L, CHECKING_ACCOUNT, 100.00, 1L);
		
		Mockito.when(accountService.updateAccount(any(Account.class))).thenReturn(mockAccount);

		mockMvc.perform(put("/account/update/45678")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockAccount)))
				.andExpect(status().isConflict());
	}
	
	@Test
	void testUpdateAccountBadRequest() throws JsonProcessingException, Exception {
		Account mockAccount = new Account(12345L, CHECKING_ACCOUNT, 100.00, 1L);
		
		Mockito.when(accountService.updateAccount(any(Account.class))).thenReturn(null);

		mockMvc.perform(put("/account/update/12345")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockAccount)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testDeleteAccountByAcctNoSuccess() throws Exception {
		Account mockAccount = new Account(12345L, CHECKING_ACCOUNT, 100.00, 1L);
		
		Mockito.when(accountService.deleteAccountByAcctNo(mockAccount.getAccountNumber())).thenReturn(Boolean.TRUE);
		
		mockMvc.perform(delete("/account/delete/12345"))
				.andExpect(status().isOk())
				.andDo(print());
	}
	
	@Test
	void testDeleteAccountByAcctNoUnsuccessful() throws Exception {
		Account mockAccount = new Account(12345L, CHECKING_ACCOUNT, 100.00, 1L);
		
		Mockito.when(accountService.deleteAccountByAcctNo(mockAccount.getAccountNumber())).thenReturn(Boolean.FALSE);
		
		mockMvc.perform(delete("/account/delete/12345"))
				.andExpect(status().isConflict())
				.andDo(print());
	}
	
//	@Test
//	void testTransferFundsBetweenAccounts() {
//		
//	}
	
	@Test
	void testDepositFundsIntoAccount() throws Exception {
		Mockito.doNothing().when(accountService).depositFundsIntoAccount(12345L, 100.00);
		
		mockMvc.perform(put("/account/12345/deposit/100"))
				.andExpect(status().isOk())
				.andDo(print());
	}
	
	@Test
	void testWithdrawFundsFromAccount() throws Exception {
		Mockito.doNothing().when(accountService).withdrawFundsFromAccount(12345L, 50.00);
		
		mockMvc.perform(put("/account/12345/withdraw/100"))
				.andExpect(status().isOk())
				.andDo(print());
	}

}
