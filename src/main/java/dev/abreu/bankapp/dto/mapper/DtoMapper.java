package dev.abreu.bankapp.dto.mapper;

import dev.abreu.bankapp.dto.*;
import dev.abreu.bankapp.model.Account;
import dev.abreu.bankapp.model.Transaction;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
                customer.getAddress(), null);
    }

    /**
     * Maps a Customer object to a CustomerResponseDTO object with Jwt token.
     *
     * @param customer The customer object to be mapped.
     * @param token    The JWT token.
     * @return The mapped CustomerResponseDTO object.
     */
    public CustomerResponseDTO toCustomerResponseDto(@NonNull Customer customer, String token) {
        return new CustomerResponseDTO(customer.getFirstName(), customer.getLastName(), customer.getUsername(),
                customer.getAddress(), token);
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

    /**
     * Maps an Account object to an AccountDTO object.
     *
     * @param account The account object to be mapped.
     * @return The mapped AccountDTO object.
     */
    public AccountDTO toAccountDto(@NonNull Account account) {
        return new AccountDTO(account.getAccountNumber(), account.getAccountType(), account.getAccountBalance(),
                account.getCustomerId());
    }

    /**
     * Maps an Account object to an AccountResponseDTO object.
     *
     * @param account The account object to be mapped.
     * @return The mapped AccountResponseDTO object.
     */
    public AccountResponseDTO toAccountResponseDto(@NonNull Account account) {
        return new AccountResponseDTO(account.getAccountNumber(), account.getAccountType(),
                account.getAccountBalance());
    }

    /**
     * Maps an AccountDTO object to an Account object.
     *
     * @param accountDto The account DTO object to be mapped.
     * @return The mapped Account object.
     */
    public Account toAccount(@NonNull AccountDTO accountDto) {
        Account account = new Account();
        account.setAccountNumber(accountDto.accountNumber());
        account.setAccountType(accountDto.accountType());
        account.setAccountBalance(accountDto.accountBalance());
        account.setCustomerId(accountDto.customerId());
        return account;
    }

    /**
     * Maps a Transaction object to an TransactionDTO object.
     *
     * @param transaction The transaction object to be mapped.
     * @return The mapped TransactionDTO object.
     */
    public TransactionDTO toTransactionDto(@NonNull Transaction transaction) {
        return new TransactionDTO(transaction.getTransactionId(),
                transaction.getTransactionType(), transaction.getTransactionAmount(),
                transaction.getTransactionNotes(), transaction.getTransactionDate(), transaction.getAccountNumber());
    }

    /**
     * Maps a Transaction object to a TransactionResponseDTO object.
     *
     * @param transaction The transaction object to be mapped.
     * @return The mapped TransactionResponseDTO object.
     */
    public TransactionResponseDTO toTransactionResponseDto(@NonNull Transaction transaction) {
        return new TransactionResponseDTO(transaction.getTransactionId(),
                transaction.getTransactionType(), transaction.getTransactionAmount(),
                transaction.getTransactionNotes(), transaction.getTransactionDate());
    }

    /**
     * Maps a TransactionDTO object to a Transaction object.
     *
     * @param transactionDto The transaction DTO object to be mapped.
     * @return The mapped Transaction object.
     */
    public Transaction toTransaction(@NonNull TransactionDTO transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionDto.transactionId());
        transaction.setTransactionType(transactionDto.transactionType());
        transaction.setTransactionAmount(transactionDto.transactionAmount());
        transaction.setTransactionNotes(transactionDto.transactionNotes());
        transaction.setTransactionDate(transactionDto.transactionDate());
        transaction.setAccountNumber(transactionDto.accountNumber());
        return transaction;
    }
}
