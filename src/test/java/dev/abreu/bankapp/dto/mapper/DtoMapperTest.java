package dev.abreu.bankapp.dto.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.abreu.bankapp.dto.CustomerDTO;
import dev.abreu.bankapp.dto.CustomerResponseDTO;
import dev.abreu.bankapp.dto.RegisterRequest;
import dev.abreu.bankapp.model.Customer;

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
		customer.setFirstName("John");
		customer.setLastName("Doe");
		customer.setUsername("johndoe");
		customer.setAddress("123 Main St");

		CustomerResponseDTO expectedDTO = new CustomerResponseDTO("John", "Doe", "johndoe", "123 Main St");
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
}
