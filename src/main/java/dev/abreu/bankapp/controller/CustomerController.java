package dev.abreu.bankapp.controller;

import dev.abreu.bankapp.dto.CustomerDTO;
import dev.abreu.bankapp.dto.CustomerResponseDTO;
import dev.abreu.bankapp.dto.mapper.DtoMapper;
import dev.abreu.bankapp.entity.Customer;
import dev.abreu.bankapp.service.CustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/customer")
public class CustomerController {

    private static final Logger log = LogManager.getLogger(CustomerController.class);

    private final CustomerService customerService;
    private final DtoMapper dtoMapper;

    public CustomerController(CustomerService customerService, DtoMapper dtoMapper) { //dependency injection for CustomerService
        this.customerService = customerService;
        this.dtoMapper = dtoMapper;
    }

    /**
     * Retrieves a Customer based on their username
     *
     * @param username customer username
     * @return customer
     */
    @GetMapping(path = "/get/user/{username}")
    public ResponseEntity<CustomerResponseDTO> getCustomerByUsername(@PathVariable("username") String username) {
        log.info("Performing GET method from inside getCustomerByUsername in CustomerController");

        Customer customer = customerService.getCustomerByUsername(username);

        CustomerResponseDTO dto = dtoMapper.toCustomerResponseDto(customer);

        log.info("Customer with username {} was successfully retrieved...", username);
        return ResponseEntity.ok(dto); //sends 200 status
    }


    /**
     * Retrieves a Customer based on their id
     *
     * @param id customer id
     * @return Customer
     */
    @GetMapping("/get/id/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable("id") Long id) {
        log.info("Performing GET method from inside getCustomerById in CustomerController");

        Customer customer = customerService.getCustomerById(id);

        CustomerResponseDTO dto = dtoMapper.toCustomerResponseDto(customer);

        log.info("Customer with id {} was successfully retrieved...", id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Retrieves a list of all customers available in the database
     * through a http GET method
     *
     * @return a list of customers
     */
    @GetMapping()
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        log.info("Performing GET method from inside getAllCustomers()");

        List<Customer> customers = customerService.getAllCustomers();

        List<CustomerResponseDTO> dtoList = customers.stream().map(dtoMapper::toCustomerResponseDto).toList();

        return ResponseEntity.ok(dtoList);
    }

    /**
     * Update customer details by id
     *
     * @param id customer id
     * @param customerDto customer details
     * @return customer
     */
    @PutMapping(path = "/update/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable("id") Long id, @RequestBody CustomerDTO customerDto) {
        log.info("Performing PUT method to update details for customer with id: {}", id);

        if (!customerDto.id().equals(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Customer customer = dtoMapper.toCustomer(customerDto);

        customer = customerService.updateCustomerDetails(customer);

        if (customer != null) {
            return ResponseEntity.ok(customerDto);
        } else {
            return ResponseEntity.badRequest().build();
        }

    }

    /**
     * Remove customer from database by username
     *
     * @param username customer username
     */
    @DeleteMapping(path = "/delete/user/{username}")
    public ResponseEntity<String> deleteCustomerByUsername(@PathVariable("username") String username) {
        log.info("Performing DELETE method for customer with username: {}", username);

        boolean success = customerService.deleteCustomerByUsername(username);

        if (Boolean.TRUE.equals(success)) {
            return new ResponseEntity<>("Customer successfully deleted...", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Customer could not be deleted, please check backend...", HttpStatus.CONFLICT);
        }
    }


    /**
     * Remove customer from database by id
     *
     * @param id customer id
     */
    @DeleteMapping(path = "/delete/id/{id}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable("id") Long id) {
        log.info("Performing DELETE method for customer with id: {}", id);

        boolean success = customerService.deleteCustomerById(id);

        if (Boolean.TRUE.equals(success)) {
            return new ResponseEntity<>("Customer successfully deleted...", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Customer could not be deleted, please check backend...", HttpStatus.CONFLICT);
        }
    }

}
