package dev.abreu.bankapp.dto.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import dev.abreu.bankapp.dto.*;
import dev.abreu.bankapp.entity.Account;
import dev.abreu.bankapp.entity.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.abreu.bankapp.entity.Customer;

import java.time.LocalDateTime;

@SuppressWarnings("WriteOnlyObject")
@SpringBootTest(classes =  DtoMapper.class)
class DtoMapperTest {

	@Autowired
	private DtoMapper dtoMapper;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@Test
	void testToCustomerDto() {
		Customer customer = new Customer();
		customer.setId(1L);
		customer.setFirstName("John");
		customer.setLastName("Doe");
		customer.setUsername("johndoe");
		customer.setAddress("123 Main St");

		CustomerDTO expectedDTO = new CustomerDTO(1L, "John", "Doe", "johndoe", "123 Main St");
		assertEquals(expectedDTO, dtoMapper.toCustomerDto(customer));
	}

	@Test
	void testToCustomerResponseDto() {
		Customer customer = new Customer();
		customer.setId(1L);
		customer.setFirstName("John");
		customer.setLastName("Doe");
		customer.setUsername("johndoe");
		customer.setAddress("123 Main St");

		CustomerResponseDTO expectedDTO = new CustomerResponseDTO(1L,"John", "Doe", "johndoe", "123 Main St", "ABC123");
		assertEquals(expectedDTO, dtoMapper.toCustomerResponseDto(customer, "ABC123"));
	}

	@Test
	void testToCustomerResponseDtoWithoutToken() {
		Customer customer = new Customer();
		customer.setId(1L);
		customer.setFirstName("John");
		customer.setLastName("Doe");
		customer.setUsername("johndoe");
		customer.setAddress("123 Main St");

		CustomerResponseDTO expectedDTO = new CustomerResponseDTO(1L, "John", "Doe", "johndoe", "123 Main St", null);
		assertEquals(expectedDTO, dtoMapper.toCustomerResponseDto(customer));
	}

	@Test
	void testToCustomer() {
		CustomerDTO customerDTO = new CustomerDTO(1L, "John", "Doe", "johndoe", "123 Main St");

		Customer expectedCustomer = new Customer();
		expectedCustomer.setId(customerDTO.id());
		expectedCustomer.setFirstName(customerDTO.firstName());
		expectedCustomer.setLastName(customerDTO.lastName());
		expectedCustomer.setUsername(customerDTO.username());
		expectedCustomer.setAddress(customerDTO.address());

		assertNotNull(dtoMapper.toCustomer(customerDTO));
	}

	@Test
	void testToCustomerWithRegisterRequest() {
		RegisterRequest request = new RegisterRequest("johndoe", "password", "John", "Doe", "123 Main St");

		Customer expectedCustomer = new Customer();
		expectedCustomer.setUsername(request.username());
		expectedCustomer.setPassword("encodedPassword");
		expectedCustomer.setFirstName(request.firstName());
		expectedCustomer.setLastName(request.lastName());
		expectedCustomer.setAddress(request.address());

		when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");

		assertNotNull(dtoMapper.toCustomer(request));
	}

	@Test
	void testToAccountDto() {
		Account account = new Account();
		account.setAccountNumber(123L);
		account.setNickname("my checking");
		account.setAccountType("checking");
		account.setAccountBalance(100.00);
		account.setCustomerId(1L);

		AccountDTO expectedDTO = new AccountDTO(123L, "my checking", "checking", 100.00, 1L);
		assertEquals(expectedDTO, dtoMapper.toAccountDto(account));
	}

	@Test
	void testToAccount() {
		AccountDTO accountDTO = new AccountDTO(123L, "my checking", "checking", 100.00, 1L);

		Account expectedAccount = new Account();
		expectedAccount.setAccountNumber(accountDTO.accountNumber());
		expectedAccount.setAccountType(accountDTO.accountType());
		expectedAccount.setAccountBalance(accountDTO.accountBalance());
		expectedAccount.setCustomerId(accountDTO.customerId());

		assertNotNull(dtoMapper.toAccount(accountDTO));
	}

	@Test
	void testToAccountResponseDto() {
		Account account = new Account();
		account.setAccountNumber(123L);
		account.setNickname("my checking");
		account.setAccountType("checking");
		account.setAccountBalance(100.00);
		account.setCreatedAt(LocalDateTime.parse("2022-01-01T00:00:00"));
		account.setUpdatedAt(LocalDateTime.parse("2022-01-01T00:00:00"));
		account.setCustomerId(1L);

		AccountResponseDTO expectedDTO = new AccountResponseDTO(123L, "my checking", "checking", 100.00, LocalDateTime.parse("2022-01-01T00:00:00"), LocalDateTime.parse("2022-01-01T00:00:00"));
		assertEquals(expectedDTO, dtoMapper.toAccountResponseDto(account));
	}

	@Test
	void testToTransactionDto() {
		Transaction transaction = new Transaction();
		transaction.setTransactionId(1L);
		transaction.setTransactionType("deposit");
		transaction.setTransactionAmount(100.00);
		transaction.setTransactionNotes("initial deposit");
		transaction.setCreatedAt(LocalDateTime.parse("2022-01-01T00:00:00"));
		transaction.setAccountNumber(123L);

		TransactionDTO expectedDTO = new TransactionDTO(1L, "deposit", 100.00, "initial deposit", 123L);
		assertEquals(expectedDTO, dtoMapper.toTransactionDto(transaction));
	}

	@Test
	void testToTransaction() {
		TransactionDTO transactionDTO = new TransactionDTO(1L, "deposit", 100.00, "initial deposit", 123L);

		Transaction expectedTransaction = new Transaction();
		expectedTransaction.setTransactionId(transactionDTO.transactionId());
		expectedTransaction.setTransactionType(transactionDTO.transactionType());
		expectedTransaction.setTransactionAmount(transactionDTO.transactionAmount());
		expectedTransaction.setTransactionNotes(transactionDTO.transactionNotes());
		expectedTransaction.setAccountNumber(transactionDTO.accountNumber());

		assertNotNull(dtoMapper.toTransaction(transactionDTO));
	}

	@Test
	void testToTransactionResponseDto() {
		Transaction transaction = new Transaction();
		transaction.setTransactionId(1L);
		transaction.setTransactionType("deposit");
		transaction.setTransactionAmount(100.00);
		transaction.setTransactionNotes("initial deposit");
		transaction.setCreatedAt(LocalDateTime.parse("2022-01-01T00:00:00"));
		transaction.setAccountNumber(123L);

		TransactionResponseDTO expectedDTO = new TransactionResponseDTO(1L, "deposit", 100.00, "initial deposit", LocalDateTime.parse("2022-01-01T00:00:00"));
		assertEquals(expectedDTO, dtoMapper.toTransactionResponseDto(transaction));
	}
}
