package dev.abreu.bankapp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.dao.impl.CustomerDaoImpl;
import dev.abreu.bankapp.model.Customer;

@SpringBootApplication
public class RP0BankApplication {
	
	private static final Logger log = LogManager.getLogger(RP0BankApplication.class);
	
	public static CustomerDao customerDao = new CustomerDaoImpl();

	public static void main(String[] args) {
		SpringApplication.run(RP0BankApplication.class, args);
		System.out.println("\nRP0 Bank Application has STARTED...");
		log.info("INFO Testing log in P0 Bankapp Remake");
		
	}
	
	@Bean
	CommandLineRunner commandLineRunner(CustomerDao customerDao) {
		return args -> {
			// When id is not null it treats it as an update as opposed to inserting a new record
			customerDao.saveCustomer(new Customer(null, "Bobby", "Abreu", "12345 MLB Dr.", "bobby123", "password"));
			customerDao.saveCustomer(new Customer(null, "Jolene", "FitzGilbert", "4 Sycamore Circle", "jfitzgilbert0", "bP6>#)NTV?qgg"));
			customerDao.saveCustomer(new Customer(null, "Josefa", "Merwood", "23569 Bultman Drive", "jmerwood1", "pK9%b?igvw"));
			customerDao.saveCustomer(new Customer(null, "Ronnie", "Voak", "7 Ridge Oak Terrace", "rvoak3", "pN9,Z7#mC"));
			log.info("New customers created!");
		};
	}

	
/* Plan out things to accomplish after finishing everything above this */
	// DONE 1. Create Account and Transaction models AND/OR a possible 4th model
	// DONE 2a. Link all models using AggregateReference<> from Spring Data JDBC (Transaction is left to figure out)
	// DONE 2b. Create Dao layer for DB transactions between Customers, Accounts, and Transactions
	// DONE 2c. Tested Dao layer for DB transactions between Customers
	// TODO 3. Create Service Layer for models using Spring JDBC Dao Methods
	// TODO 3a. Create Service Layer Unit Tests for Customer/Account/Transaction
	// TODO 4. Populate Customer/Account/Transaction DAOs w/ CRUD operations using JDBC API
	// TODO 5. Create DTO classes for models to be used in HTTP Requests
	// TODO 6. Create Controller classes for models to configure API endpoints
	
	//Project Breakdown
	//	API Endpoint (Postman) <---> Controller <-- Service <-- Dao (First need to establish db relationships and finalize models to finish Dao)
	
}
