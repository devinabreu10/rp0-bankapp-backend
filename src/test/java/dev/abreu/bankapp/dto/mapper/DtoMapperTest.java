package dev.abreu.bankapp.dto.mapper;

import dev.abreu.bankapp.dto.*;
import dev.abreu.bankapp.entity.Account;
import dev.abreu.bankapp.entity.Customer;
import dev.abreu.bankapp.entity.Transaction;
import dev.abreu.bankapp.entity.Transfer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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

		TransactionResponseDTO expectedDTO = new TransactionResponseDTO(1L, "deposit", 100.00, "initial deposit", LocalDateTime.parse("2022-01-01T00:00:00"), 123L);
		assertEquals(expectedDTO, dtoMapper.toTransactionResponseDto(transaction));
	}

	@Test
	void testToUnifiedTransactionDetailDtoFromTransaction() {
		Transaction transaction = new Transaction();
		transaction.setTransactionId(1L);
		transaction.setTransactionType("deposit");
		transaction.setTransactionAmount(100.00);
		transaction.setTransactionNotes("initial deposit");
		transaction.setCreatedAt(LocalDateTime.parse("2022-01-01T00:00:00"));
		transaction.setAccountNumber(123L);

		Map<String, Object> expectedAdditionalDetails = new HashMap<>();
		UnifiedTransactionDetailDTO expectedDTO = new UnifiedTransactionDetailDTO(
				1L, 
				"deposit", 
				100.00, 
				"initial deposit", 
				LocalDateTime.parse("2022-01-01T00:00:00"), 
				123L, 
				"TRANSACTION", 
				expectedAdditionalDetails
		);
		
		UnifiedTransactionDetailDTO actualDTO = dtoMapper.toUnifiedTransactionDetailDto(transaction);
		assertEquals(expectedDTO, actualDTO);
		assertEquals("TRANSACTION", actualDTO.itemType());
		assertTrue(actualDTO.additionalDetails().isEmpty());
	}

	@Test
	void testToUnifiedTransactionDetailDtoFromTransfer() {
		Transfer transfer = new Transfer();
		transfer.setTransferId(2L);
		transfer.setSourceAccountNumber(123L);
		transfer.setTargetAccountNumber(456L);
		transfer.setTransferAmount(250.00);
		transfer.setTransferNotes("monthly transfer");
		transfer.setCreatedAt(LocalDateTime.parse("2022-01-02T10:30:00"));

		Map<String, Object> expectedAdditionalDetails = new HashMap<>();
		expectedAdditionalDetails.put("targetAccountNumber", 456L);
		
		UnifiedTransactionDetailDTO expectedDTO = new UnifiedTransactionDetailDTO(
				2L, 
				"Account Transfer", 
				250.00, 
				"monthly transfer", 
				LocalDateTime.parse("2022-01-02T10:30:00"), 
				123L, 
				"TRANSFER", 
				expectedAdditionalDetails
		);
		
		UnifiedTransactionDetailDTO actualDTO = dtoMapper.toUnifiedTransactionDetailDto(transfer);
		assertEquals(expectedDTO, actualDTO);
		assertEquals("TRANSFER", actualDTO.itemType());
		assertEquals(456L, actualDTO.additionalDetails().get("targetAccountNumber"));
		assertEquals(1, actualDTO.additionalDetails().size());
	}

	@Test
	void testUnifiedTransactionDetailDtoFieldMappingAccuracy() {
		// Test Transaction mapping field accuracy
		Transaction transaction = new Transaction();
		transaction.setTransactionId(10L);
		transaction.setTransactionType("withdrawal");
		transaction.setTransactionAmount(75.50);
		transaction.setTransactionNotes("ATM withdrawal");
		transaction.setCreatedAt(LocalDateTime.parse("2022-03-15T14:20:30"));
		transaction.setAccountNumber(789L);

		UnifiedTransactionDetailDTO transactionDTO = dtoMapper.toUnifiedTransactionDetailDto(transaction);
		
		assertEquals(transaction.getTransactionId(), transactionDTO.id());
		assertEquals(transaction.getTransactionType(), transactionDTO.type());
		assertEquals(transaction.getTransactionAmount(), transactionDTO.amount());
		assertEquals(transaction.getTransactionNotes(), transactionDTO.notes());
		assertEquals(transaction.getCreatedAt(), transactionDTO.createdAt());
		assertEquals(transaction.getAccountNumber(), transactionDTO.accountNumber());
		assertEquals("TRANSACTION", transactionDTO.itemType());

		// Test Transfer mapping field accuracy
		Transfer transfer = new Transfer();
		transfer.setTransferId(20L);
		transfer.setSourceAccountNumber(111L);
		transfer.setTargetAccountNumber(222L);
		transfer.setTransferAmount(500.75);
		transfer.setTransferNotes("rent payment");
		transfer.setCreatedAt(LocalDateTime.parse("2022-03-16T09:15:45"));

		UnifiedTransactionDetailDTO transferDTO = dtoMapper.toUnifiedTransactionDetailDto(transfer);
		
		assertEquals(transfer.getTransferId(), transferDTO.id());
		assertEquals("Account Transfer", transferDTO.type());
		assertEquals(transfer.getTransferAmount(), transferDTO.amount());
		assertEquals(transfer.getTransferNotes(), transferDTO.notes());
		assertEquals(transfer.getCreatedAt(), transferDTO.createdAt());
		assertEquals(transfer.getSourceAccountNumber(), transferDTO.accountNumber());
		assertEquals("TRANSFER", transferDTO.itemType());
		assertEquals(transfer.getTargetAccountNumber(), transferDTO.additionalDetails().get("targetAccountNumber"));
	}

	@Test
	void testUnifiedTransactionDetailDtoWithNullNotes() {
		// Test Transaction with null notes
		Transaction transaction = new Transaction();
		transaction.setTransactionId(1L);
		transaction.setTransactionType("deposit");
		transaction.setTransactionAmount(100.00);
		transaction.setTransactionNotes(null);
		transaction.setCreatedAt(LocalDateTime.parse("2022-01-01T00:00:00"));
		transaction.setAccountNumber(123L);

		UnifiedTransactionDetailDTO transactionDTO = dtoMapper.toUnifiedTransactionDetailDto(transaction);
        assertNull(transactionDTO.notes());
		assertEquals("TRANSACTION", transactionDTO.itemType());
		assertTrue(transactionDTO.additionalDetails().isEmpty());

		// Test Transfer with null notes
		Transfer transfer = new Transfer();
		transfer.setTransferId(2L);
		transfer.setSourceAccountNumber(123L);
		transfer.setTargetAccountNumber(456L);
		transfer.setTransferAmount(250.00);
		transfer.setTransferNotes(null);
		transfer.setCreatedAt(LocalDateTime.parse("2022-01-02T10:30:00"));

		UnifiedTransactionDetailDTO transferDTO = dtoMapper.toUnifiedTransactionDetailDto(transfer);
        assertNull(transferDTO.notes());
		assertEquals("TRANSFER", transferDTO.itemType());
		assertEquals(456L, transferDTO.additionalDetails().get("targetAccountNumber"));
	}

	@Test
	void testUnifiedTransactionDetailDtoWithEmptyNotes() {
		// Test Transaction with empty notes
		Transaction transaction = new Transaction();
		transaction.setTransactionId(1L);
		transaction.setTransactionType("withdrawal");
		transaction.setTransactionAmount(50.00);
		transaction.setTransactionNotes("");
		transaction.setCreatedAt(LocalDateTime.parse("2022-01-01T00:00:00"));
		transaction.setAccountNumber(123L);

		UnifiedTransactionDetailDTO transactionDTO = dtoMapper.toUnifiedTransactionDetailDto(transaction);
		assertEquals("", transactionDTO.notes());
		assertEquals("TRANSACTION", transactionDTO.itemType());

		// Test Transfer with empty notes
		Transfer transfer = new Transfer();
		transfer.setTransferId(2L);
		transfer.setSourceAccountNumber(123L);
		transfer.setTargetAccountNumber(456L);
		transfer.setTransferAmount(75.00);
		transfer.setTransferNotes("");
		transfer.setCreatedAt(LocalDateTime.parse("2022-01-02T10:30:00"));

		UnifiedTransactionDetailDTO transferDTO = dtoMapper.toUnifiedTransactionDetailDto(transfer);
		assertEquals("", transferDTO.notes());
		assertEquals("TRANSFER", transferDTO.itemType());
	}

	@Test
	void testUnifiedTransactionDetailDtoWithZeroAmount() {
		// Test Transaction with zero amount
		Transaction transaction = new Transaction();
		transaction.setTransactionId(1L);
		transaction.setTransactionType("adjustment");
		transaction.setTransactionAmount(0.0);
		transaction.setTransactionNotes("balance adjustment");
		transaction.setCreatedAt(LocalDateTime.parse("2022-01-01T00:00:00"));
		transaction.setAccountNumber(123L);

		UnifiedTransactionDetailDTO transactionDTO = dtoMapper.toUnifiedTransactionDetailDto(transaction);
		assertEquals(0.0, transactionDTO.amount());
		assertEquals("TRANSACTION", transactionDTO.itemType());

		// Test Transfer with zero amount
		Transfer transfer = new Transfer();
		transfer.setTransferId(2L);
		transfer.setSourceAccountNumber(123L);
		transfer.setTargetAccountNumber(456L);
		transfer.setTransferAmount(0.0);
		transfer.setTransferNotes("test transfer");
		transfer.setCreatedAt(LocalDateTime.parse("2022-01-02T10:30:00"));

		UnifiedTransactionDetailDTO transferDTO = dtoMapper.toUnifiedTransactionDetailDto(transfer);
		assertEquals(0.0, transferDTO.amount());
		assertEquals("TRANSFER", transferDTO.itemType());
	}

	@Test
	void testUnifiedTransactionDetailDtoWithNegativeAmount() {
		// Test Transaction with negative amount
		Transaction transaction = new Transaction();
		transaction.setTransactionId(1L);
		transaction.setTransactionType("fee");
		transaction.setTransactionAmount(-25.00);
		transaction.setTransactionNotes("overdraft fee");
		transaction.setCreatedAt(LocalDateTime.parse("2022-01-01T00:00:00"));
		transaction.setAccountNumber(123L);

		UnifiedTransactionDetailDTO transactionDTO = dtoMapper.toUnifiedTransactionDetailDto(transaction);
		assertEquals(-25.00, transactionDTO.amount());
		assertEquals("TRANSACTION", transactionDTO.itemType());

		// Test Transfer with negative amount (edge case)
		Transfer transfer = new Transfer();
		transfer.setTransferId(2L);
		transfer.setSourceAccountNumber(123L);
		transfer.setTargetAccountNumber(456L);
		transfer.setTransferAmount(-100.00);
		transfer.setTransferNotes("reversal");
		transfer.setCreatedAt(LocalDateTime.parse("2022-01-02T10:30:00"));

		UnifiedTransactionDetailDTO transferDTO = dtoMapper.toUnifiedTransactionDetailDto(transfer);
		assertEquals(-100.00, transferDTO.amount());
		assertEquals("TRANSFER", transferDTO.itemType());
	}

	@Test
	void testUnifiedTransactionDetailDtoWithLargeAmount() {
		// Test Transaction with large amount
		Transaction transaction = new Transaction();
		transaction.setTransactionId(1L);
		transaction.setTransactionType("deposit");
		transaction.setTransactionAmount(999999.99);
		transaction.setTransactionNotes("large deposit");
		transaction.setCreatedAt(LocalDateTime.parse("2022-01-01T00:00:00"));
		transaction.setAccountNumber(123L);

		UnifiedTransactionDetailDTO transactionDTO = dtoMapper.toUnifiedTransactionDetailDto(transaction);
		assertEquals(999999.99, transactionDTO.amount());
		assertEquals("TRANSACTION", transactionDTO.itemType());

		// Test Transfer with large amount
		Transfer transfer = new Transfer();
		transfer.setTransferId(2L);
		transfer.setSourceAccountNumber(123L);
		transfer.setTargetAccountNumber(456L);
		transfer.setTransferAmount(1000000.00);
		transfer.setTransferNotes("large transfer");
		transfer.setCreatedAt(LocalDateTime.parse("2022-01-02T10:30:00"));

		UnifiedTransactionDetailDTO transferDTO = dtoMapper.toUnifiedTransactionDetailDto(transfer);
		assertEquals(1000000.00, transferDTO.amount());
		assertEquals("TRANSFER", transferDTO.itemType());
	}

	@Test
	void testUnifiedTransactionDetailDtoWithSameSourceAndTargetAccount() {
		// Test Transfer where source and target accounts are the same (edge case)
		Transfer transfer = new Transfer();
		transfer.setTransferId(1L);
		transfer.setSourceAccountNumber(123L);
		transfer.setTargetAccountNumber(123L);
		transfer.setTransferAmount(100.00);
		transfer.setTransferNotes("internal transfer");
		transfer.setCreatedAt(LocalDateTime.parse("2022-01-01T10:00:00"));

		UnifiedTransactionDetailDTO transferDTO = dtoMapper.toUnifiedTransactionDetailDto(transfer);
		assertEquals(transfer.getTransferId(), transferDTO.id());
		assertEquals("Account Transfer", transferDTO.type());
		assertEquals(transfer.getTransferAmount(), transferDTO.amount());
		assertEquals(transfer.getTransferNotes(), transferDTO.notes());
		assertEquals(transfer.getCreatedAt(), transferDTO.createdAt());
		assertEquals(transfer.getSourceAccountNumber(), transferDTO.accountNumber());
		assertEquals("TRANSFER", transferDTO.itemType());
		assertEquals(123L, transferDTO.additionalDetails().get("targetAccountNumber"));
		assertEquals(1, transferDTO.additionalDetails().size());
	}

	@Test
	void testUnifiedTransactionDetailDtoAdditionalDetailsImmutability() {
		// Test that additionalDetails map is properly populated and isolated
		Transfer transfer1 = new Transfer();
		transfer1.setTransferId(1L);
		transfer1.setSourceAccountNumber(123L);
		transfer1.setTargetAccountNumber(456L);
		transfer1.setTransferAmount(100.00);
		transfer1.setTransferNotes("first transfer");
		transfer1.setCreatedAt(LocalDateTime.parse("2022-01-01T10:00:00"));

		Transfer transfer2 = new Transfer();
		transfer2.setTransferId(2L);
		transfer2.setSourceAccountNumber(789L);
		transfer2.setTargetAccountNumber(101L);
		transfer2.setTransferAmount(200.00);
		transfer2.setTransferNotes("second transfer");
		transfer2.setCreatedAt(LocalDateTime.parse("2022-01-02T10:00:00"));

		UnifiedTransactionDetailDTO dto1 = dtoMapper.toUnifiedTransactionDetailDto(transfer1);
		UnifiedTransactionDetailDTO dto2 = dtoMapper.toUnifiedTransactionDetailDto(transfer2);

		// Verify each DTO has its own additionalDetails map
		assertEquals(456L, dto1.additionalDetails().get("targetAccountNumber"));
		assertEquals(101L, dto2.additionalDetails().get("targetAccountNumber"));
		
		// Verify maps are independent
		assertNotNull(dto1.additionalDetails());
		assertNotNull(dto2.additionalDetails());
		assertEquals(1, dto1.additionalDetails().size());
		assertEquals(1, dto2.additionalDetails().size());
	}
}
