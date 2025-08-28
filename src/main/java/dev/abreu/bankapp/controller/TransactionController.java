package dev.abreu.bankapp.controller;

import dev.abreu.bankapp.dto.TransactionDTO;
import dev.abreu.bankapp.dto.TransactionResponseDTO;
import dev.abreu.bankapp.dto.UnifiedTransactionDetailDTO;
import dev.abreu.bankapp.dto.mapper.DtoMapper;
import dev.abreu.bankapp.entity.Transaction;
import dev.abreu.bankapp.service.TransactionService;
import dev.abreu.bankapp.service.UnifiedTransactionDetailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling transaction-related operations.
 */
@RestController
@RequestMapping(path = "/transaction")
public class TransactionController {

    private static final Logger log = LogManager.getLogger(TransactionController.class);

    private final TransactionService transactionService;
    private final UnifiedTransactionDetailService unifiedTransactionDetailService;
    private final DtoMapper dtoMapper;

    public TransactionController(TransactionService transactionService, 
                               UnifiedTransactionDetailService unifiedTransactionDetailService,
                               DtoMapper dtoMapper) {
        this.transactionService = transactionService;
        this.unifiedTransactionDetailService = unifiedTransactionDetailService;
        this.dtoMapper = dtoMapper;
    }

    /**
     * Retrieves a transaction or transfer by its ID and type using unified service.
     * The frontend must specify the type (TRANSACTION or TRANSFER) to fetch the correct data.
     *
     * @param type the type of item to retrieve (TRANSACTION or TRANSFER)
     * @param id the transaction or transfer ID
     * @return the transaction or transfer details with the specified ID
     */
    @GetMapping(path = "/get/{type}/{id}")
    public ResponseEntity<UnifiedTransactionDetailDTO> getTransactionById(@PathVariable String type, @PathVariable Long id) {
        log.info("Performing GET method to retrieve by id and type");
        UnifiedTransactionDetailDTO dto = unifiedTransactionDetailService.getUnifiedTransactionDetailById(id, type);
        return ResponseEntity.ok(dto);
    }

    /**
     * Retrieves all transactions associated with an account number.
     *
     * @param acctNo the account number
     * @return a list of transactions associated with the account number
     */
    @GetMapping(path = "/list/account/{acctNo}")
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
     * @param txnId          the transaction ID
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

        if (success) {
            return new ResponseEntity<>("{\"success\": \"Transaction successfully deleted...\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("{\"error\": \"Transaction could not be deleted...\"}", HttpStatus.CONFLICT);
        }
    }

    /**
     * Retrieves all transactions and transfers associated with a customer ID.
     *
     * @param customerId the customer ID
     * @return list of transactions and transfers
     */
    @GetMapping(path = "/list/customer/{customerId}")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactionsAndTransfersByCustomerId(@PathVariable("customerId") Long customerId) {
        log.info("Performing GET method to retrieve all transactions and transfers for customer id {}", customerId);
        List<Transaction> transactions = transactionService.getAllTransactionsAndTransfersByCustomerId(customerId);
        List<TransactionResponseDTO> dtos = transactions.stream()
                .map(dtoMapper::toTransactionResponseDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }
}
