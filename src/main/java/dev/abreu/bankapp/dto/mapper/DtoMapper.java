package dev.abreu.bankapp.dto.mapper;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import dev.abreu.bankapp.dto.CustomerDTO;
import dev.abreu.bankapp.dto.CustomerResponseDTO;
import dev.abreu.bankapp.dto.RegisterRequest;
import dev.abreu.bankapp.model.Customer;

/**
 * This class provides methods to map between different DTOs and the corresponding model classes.
 * 
 * @author Devin Abreu
 */
@Component
public class DtoMapper {

    private final PasswordEncoder passwordEncoder;

    public DtoMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Maps a Customer object to a CustomerDTO object.
     *
     * @param customer The customer object to be mapped.
     * @return The mapped CustomerDTO object.
     */
    public CustomerDTO toCustomerDto(@NonNull Customer customer) {
        return new CustomerDTO(customer.getId(), customer.getFirstName(), customer.getLastName(),
                customer.getUsername(), customer.getAddress());
    }

    /**
     * Maps a Customer object to a CustomerResponseDTO object.
     *
     * @param customer The customer object to be mapped.
     * @return The mapped CustomerResponseDTO object.
     */
    public CustomerResponseDTO toCustomerResponseDto(@NonNull Customer customer) {
        return new CustomerResponseDTO(customer.getFirstName(), customer.getLastName(), customer.getUsername(),
                customer.getAddress());
    }

    /**
     * Maps a CustomerDTO object to a Customer object.
     *
     * @param customerDto The customer DTO object to be mapped.
     * @return The mapped Customer object.
     */
    public Customer toCustomer(@NonNull CustomerDTO customerDto) {
        Customer customer = new Customer();
        customer.setId(customerDto.id());
        customer.setUsername(customerDto.username());
        customer.setFirstName(customerDto.firstName());
        customer.setLastName(customerDto.lastName());
        customer.setAddress(customerDto.address());
        return customer;
    }

    /**
     * Maps a RegisterRequest object to a Customer object.
     *
     * @param request The register request object to be mapped.
     * @return The mapped Customer object.
     */
    public Customer toCustomer(@NonNull RegisterRequest request) {
        Customer customer = new Customer();
        customer.setUsername(request.username());
        customer.setPassword(passwordEncoder.encode(request.password()));
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setAddress(request.address());
        return customer;
    }
}
