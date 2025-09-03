package dev.abreu.bankapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.abreu.bankapp.config.ApplicationConfig;
import dev.abreu.bankapp.dao.AccountDao;
import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.dto.AccountDTO;
import dev.abreu.bankapp.dto.AccountResponseDTO;
import dev.abreu.bankapp.dto.AccountTxnRequest;
import dev.abreu.bankapp.dto.TransferRequest;
import dev.abreu.bankapp.dto.mapper.DtoMapper;
import dev.abreu.bankapp.entity.Account;
import dev.abreu.bankapp.security.JwtConfig;
import dev.abreu.bankapp.security.SecurityConfig;
import dev.abreu.bankapp.service.AccountService;
import dev.abreu.bankapp.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static dev.abreu.bankapp.util.BankappConstants.CHECKING_ACCOUNT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({SecurityConfig.class, ApplicationConfig.class})
class AccountControllerTest {

	@MockitoBean
	AccountService accountService;

	@MockitoBean
	private CustomerService customerService;

	@MockitoBean
	private CustomerDao customerDao;

	@MockitoBean
	private AccountDao accountDao;

	@MockitoBean
	JwtConfig jwtConfig;

	@MockitoBean
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
	void testGetAccountByAcctNo() throws Exception {
		Account mockAccount = new Account(12345L,CHECKING_ACCOUNT, 100.00, 1L);
		mockAccount.setNickname("my checking");
		AccountResponseDTO mockDto = new AccountResponseDTO(12345L, mockAccount.getNickname(), CHECKING_ACCOUNT, 100.00, mockAccount.getCreatedAt(), mockAccount.getUpdatedAt());

		Mockito.when(accountService.getAccountByAcctNo(12345L)).thenReturn(mockAccount);
		Mockito.when(dtoMapper.toAccountResponseDto(mockAccount)).thenReturn(mockDto);

		mockMvc.perform(get("/account/get/12345"))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonMapper.writeValueAsString(mockDto)));
	}

	@Test
	void testGetAllAccountsByUsername() throws Exception {
		String testUsername = "test";
		Account mockAccount = new Account(12345L, CHECKING_ACCOUNT, 100.00, 1L);
		mockAccount.setNickname("my checking");
		AccountResponseDTO mockDto = new AccountResponseDTO(12345L, mockAccount.getNickname(), CHECKING_ACCOUNT, 100.00, mockAccount.getCreatedAt(), mockAccount.getUpdatedAt());
		List<Account> mockAccountList = new ArrayList<>();
		mockAccountList.add(mockAccount);

		List<AccountResponseDTO> mockDtoList = new ArrayList<>();
		mockAccountList.forEach(a -> mockDtoList
				.add(new AccountResponseDTO(a.getAccountNumber(), a.getNickname(), a.getAccountType(), a.getAccountBalance(), a.getCreatedAt(), a.getUpdatedAt())));

		Mockito.when(accountService.getAllAccountsByUsername(testUsername)).thenReturn(mockAccountList);
		Mockito.when(dtoMapper.toAccountResponseDto(mockAccount)).thenReturn(mockDto);

		mockMvc.perform(get("/account/get/list/test"))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonMapper.writeValueAsString(mockDtoList)));
	}

	@Test
	void testSaveAccount() throws Exception {
		Account mockAccount = new Account(12345L, CHECKING_ACCOUNT, 100.00, 1L);
		mockAccount.setNickname("my checking");
		AccountResponseDTO mockDto = new AccountResponseDTO(12345L, mockAccount.getNickname(), CHECKING_ACCOUNT, 100.00, mockAccount.getCreatedAt(), mockAccount.getUpdatedAt());

		Mockito.when(accountService.saveAccount(mockAccount)).thenReturn(mockAccount);
		Mockito.when(dtoMapper.toAccountResponseDto(mockAccount)).thenReturn(mockDto);

		mockMvc.perform(post("/account/save")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockDto)))
				.andExpect(status().isCreated())
				.andReturn();
	}

	@Test
	void testUpdateAccount() throws Exception {
		Account mockAccount = new Account(12345L, CHECKING_ACCOUNT, 100.00, 1L);
		mockAccount.setNickname("my checking");
		AccountDTO mockDto = new AccountDTO(12345L, mockAccount.getNickname(), CHECKING_ACCOUNT, 100.00, 1L);

		Mockito.when(accountService.updateAccount(any(Account.class))).thenReturn(mockAccount);
		Mockito.when(dtoMapper.toAccount(mockDto)).thenReturn(mockAccount);

		mockMvc.perform(put("/account/update/12345")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockDto)))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonMapper.writeValueAsString(mockDto)));

		verify(accountService, times(1)).updateAccount(any(Account.class));
	}

	@Test
	void testUpdateAccountConflict() throws Exception {
		Account mockAccount = new Account(12345L, CHECKING_ACCOUNT, 100.00, 1L);
		mockAccount.setNickname("my checking");
		AccountDTO mockDto = new AccountDTO(12345L, mockAccount.getNickname(), CHECKING_ACCOUNT, 100.00, 1L);

		Mockito.when(accountService.updateAccount(any(Account.class))).thenReturn(mockAccount);
		Mockito.when(dtoMapper.toAccount(mockDto)).thenReturn(mockAccount);

		mockMvc.perform(put("/account/update/45678")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockDto)))
				.andExpect(status().isConflict());
	}

	@Test
	void testUpdateAccountBadRequest() throws Exception {
		Account mockAccount = new Account(12345L, CHECKING_ACCOUNT, 100.00, 1L);
		mockAccount.setNickname("my checking");
		AccountDTO mockDto = new AccountDTO(12345L, mockAccount.getNickname(), CHECKING_ACCOUNT, 100.00, 1L);

		Mockito.when(accountService.updateAccount(any(Account.class))).thenReturn(null);
		Mockito.when(dtoMapper.toAccount(mockDto)).thenReturn(mockAccount);

		mockMvc.perform(put("/account/update/12345")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockDto)))
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

	@Test
	void testTransferFundsBetweenAccounts() throws Exception {
		TransferRequest mockTransferReq = new TransferRequest(12345L, 45678L, 50.00, "test");

		Mockito.doNothing().when(accountService).transferFundsBetweenAccounts(mockTransferReq.sourceAccountNumber(),
				mockTransferReq.targetAccountNumber(), mockTransferReq.amount(), mockTransferReq.notes());

		mockMvc.perform(post("/account/transferFunds")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockTransferReq)))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	void testDepositFundsIntoAccount() throws Exception {
		AccountTxnRequest mockDepositReq = new AccountTxnRequest(12345L, 100.00, "test");

		Mockito.doNothing().when(accountService).depositFundsIntoAccount(mockDepositReq.accountNumber(),
				mockDepositReq.amount(), mockDepositReq.notes());

		mockMvc.perform(put("/account/deposit")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockDepositReq)))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	void testWithdrawFundsFromAccount() throws Exception {
		AccountTxnRequest mockWithdrawReq = new AccountTxnRequest(12345L, 50.00, "test");

		Mockito.doNothing().when(accountService).withdrawFundsFromAccount(mockWithdrawReq.accountNumber(),
				mockWithdrawReq.amount(), mockWithdrawReq.notes());

		mockMvc.perform(put("/account/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(mockWithdrawReq)))
				.andExpect(status().isOk())
				.andDo(print());
	}

}
