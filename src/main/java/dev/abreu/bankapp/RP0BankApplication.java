package dev.abreu.bankapp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.dao.impl.CustomerDaoImpl;

@SpringBootApplication
public class RP0BankApplication {
	
	private static final Logger log = LogManager.getLogger(RP0BankApplication.class);
	
	public static CustomerDao customerDao = new CustomerDaoImpl();

	public static void main(String[] args) {
		SpringApplication.run(RP0BankApplication.class, args);
		System.out.println("\nRP0 Bank Application has STARTED...");
		log.info("INFO Testing log in P0 Bankapp Remake");
//		log.debug("DEBUG Testing log in P0 Bankapp Remake");
//		log.error("ERROR Testing log in P0 Bankapp Remake");
		
	}
	
//	@Bean
//	CommandLineRunner commandLineRunner(CustomerRepository customer, AccountRepository accounts, LoanRepository loan, 
//										CreditRepository credit, TransactionRepository transactions) {
//		return args -> {
//			// When id is not null it treats it as an update as opposed to inserting a new record
////			customer.save(new Customer(null, "Bobby", "Abreu", "12345 MLB Dr.", "bobby123", "password"));
//			log.info("New CUSTOMER successfully saved!");
//			
//			AggregateReference<Customer, Long> bobby = AggregateReference.to(customer.save(new Customer(null, "Bobby", "Account", "12345 MLB Dr.", "bobby123", "password")).getId());
//			accounts.save(new Account("Checking", 100.00, bobby));
//			
//			AggregateReference<Customer, Long> bobbyLoan = AggregateReference.to(customer.save(new Customer(null, "Bobby", "Loan", "12345 MLB Dr.", "bobby123", "password")).getId());
//			loan.save(new Loan(5000.00, 60, 0.06, bobbyLoan));
//			
//			AggregateReference<Customer, Long> bobbyCredit = AggregateReference.to(customer.save(new Customer(null, "Bobby", "Credit", "12345 MLB Dr.", "bobby123", "password")).getId());
//			credit.save(new Credit(0.00, 2000, bobbyCredit));
//			
//			
//			transactions.save(new Transaction()); // should customer id be linked to transaction?
//			
//		};
//	}
	
	//Plan out things to accomplish after finishing everything above this
	// DONE 1. Create Account and Transaction models AND/OR a possible 4th model
	// DONE 2a. Link all models using AggregateReference<> from Spring Data JDBC (Transaction is left to figure out)
	// DONE 2b. Create Dao layer for DB transactions between Customers, Accounts, and Transactions
	// DONE 2c. Tested Dao layer for DB transactions between Customers
	// TODO 3. Create Service Layer for models using Spring JDBC Dao Methods
	// TODO 4. Populate Customer/Account/Transaction DAOs w/ CRUD operations using JDBC API
	// TODO 5. Create DTO classes for models to be used in HTTP Requests
	// TODO 6. Create Controller classes for models to configure API endpoints
	
	//Project Breakdown
	//	API Endpoint (Postman) <---> Controller <-- Service <-- Dao (First need to establish db relationships and finalize models to finish Dao)
	
}
