package dev.abreu.bankapp.controllers;

import java.util.List;

import dev.abreu.bankapp.dto.TransactionDTO;
import dev.abreu.bankapp.dto.TransactionResponseDTO;
import dev.abreu.bankapp.dto.mapper.DtoMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.abreu.bankapp.model.Transaction;
import dev.abreu.bankapp.service.TransactionService;

/**
 * Controller for handling transaction-related operations.
 */
@RestController
@CrossOrigin
@RequestMapping(path = "/transaction")
public class TransactionController {

    private static final Logger log = LogManager.getLogger(TransactionController.class);

    private final TransactionService transactionService;
    private final DtoMapper dtoMapper;

    public TransactionController(TransactionService transactionService, DtoMapper dtoMapper) {
        this.transactionService = transactionService;
        this.dtoMapper = dtoMapper;
    }

    /**
     * Retrieves a transaction by its ID.
     *
     * @param txnId the transaction ID
     * @return the transaction with the specified ID
     */
    @GetMapping(path = "/get/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable("id") Long txnId) {
        log.info("Performing GET method to retrieve Transaction by id {}", txnId);
        Transaction transaction = transactionService.getTransactionById(txnId);
        TransactionResponseDTO dto = dtoMapper.toTransactionResponseDto(transaction);
        return ResponseEntity.ok(dto);
    }

    /**
     * Retrieves all transactions associated with an account number.
     *
     * @param acctNo the account number
     * @return a list of transactions associated with the account number
     */
    @GetMapping(path = "/get/list/{acctNo}")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactionsByAcctNo(@PathVariable("acctNo") Long acctNo) {
        log.info("Performing GET method to retrieve all transaction by account number");
        List<Transaction> transactions = transactionService.getAllTransactionsByAcctNo(acctNo);
        List<TransactionResponseDTO> dtoList = transactions.stream()
                .map(dtoMapper::toTransactionResponseDto).toList();
        return ResponseEntity.ok(dtoList);
    }

    /**
     * Saves a new transaction.
     *
     * @param transactionDto the transaction to save
     * @return the saved transaction
     */
    @PostMapping(path = "/save")
    public ResponseEntity<TransactionResponseDTO> saveTransaction(@RequestBody TransactionDTO transactionDto) {
        log.info("Performing POST method to save new Transaction");
        Transaction transaction = transactionService.saveTransaction(dtoMapper.toTransaction(transactionDto));
        TransactionResponseDTO dto = dtoMapper.toTransactionResponseDto(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Updates an existing transaction.
     *
     * @param txnId the transaction ID
     * @param transactionDto the transaction details to update
     * @return the updated transaction
     */
    @PutMapping(path = "/update/{txnId}")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable("txnId") Long txnId,
                                                                    @RequestBody TransactionDTO transactionDto) {
        log.info("Performing PUT method to update details for transaction with id: {}", txnId);

        if (!transactionDto.transactionId().equals(txnId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Transaction transaction = dtoMapper.toTransaction(transactionDto);
        transaction = transactionService.updateTransactionDetails(transaction);

        if (transaction != null) {
            return ResponseEntity.ok(transactionDto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Deletes a transaction by its ID.
     *
     * @param txnId the transaction ID
     * @return a response entity indicating the result of the operation
     */
    @DeleteMapping(path = "/delete/{txnId}")
    public ResponseEntity<String> deleteTransactionById(@PathVariable("txnId") Long txnId) {
        log.info("Performing DELETE method for transaction with id: {}", txnId);

        boolean success = transactionService.deleteTransactionById(txnId);

        if (Boolean.TRUE.equals(success)) {
            return new ResponseEntity<>("Transaction successfully deleted...", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Transaction could not be deleted, please check backend...", HttpStatus.CONFLICT);
        }
    }

}
