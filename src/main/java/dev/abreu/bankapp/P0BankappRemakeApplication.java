package dev.abreu.bankapp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

import dev.abreu.bankapp.model.Account;
import dev.abreu.bankapp.model.Credit;
import dev.abreu.bankapp.model.Customer;
import dev.abreu.bankapp.model.Loan;
import dev.abreu.bankapp.model.Transaction;
import dev.abreu.bankapp.repository.AccountRepository;
import dev.abreu.bankapp.repository.CreditRepository;
import dev.abreu.bankapp.repository.CustomerRepository;
import dev.abreu.bankapp.repository.LoanRepository;
import dev.abreu.bankapp.repository.TransactionRepository;

@SpringBootApplication
public class P0BankappRemakeApplication {
	
	private static final Logger log = LogManager.getLogger(P0BankappRemakeApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(P0BankappRemakeApplication.class, args);
		System.out.println("\nP0 Bankapp Remake Application has STARTED...");
		//log.info("INFO Testing log in P0 Bankapp Remake");
		//log.debug("DEBUG Testing log in P0 Bankapp Remake");
	}
	
	@Bean
	CommandLineRunner commandLineRunner(CustomerRepository customer, AccountRepository accounts, LoanRepository loan, 
										CreditRepository credit, TransactionRepository transactions) {
		return args -> {
			// When id is not null it treats it as an update as opposed to inserting a new record
//			customer.save(new Customer(null, "Bobby", "Abreu", "12345 MLB Dr.", "bobby123", "password"));
//			log.info("New CUSTOMER successfully saved!");
			
			AggregateReference<Customer, Long> bobby = AggregateReference.to(customer.save(new Customer(null, "Bobby", "Account", "12345 MLB Dr.", "bobby123", "password")).getId());
			accounts.save(new Account("Checking", 100.00, bobby));
			
			AggregateReference<Customer, Long> bobbyLoan = AggregateReference.to(customer.save(new Customer(null, "Bobby", "Loan", "12345 MLB Dr.", "bobby123", "password")).getId());
			loan.save(new Loan(5000.00, 60, 0.06, bobbyLoan));
			
			AggregateReference<Customer, Long> bobbyCredit = AggregateReference.to(customer.save(new Customer(null, "Bobby", "Credit", "12345 MLB Dr.", "bobby123", "password")).getId());
			credit.save(new Credit(0.00, 2000, bobbyCredit));
			
			
			transactions.save(new Transaction()); // should customer id be linked to transaction?
			
		};
	}

	// Accidentally messed up my old project and had to restart it. I need to be mindful about pushing my code to github
	
	//Plan out things to accomplish after finishing everything above this
	// DONE 1. Create Account and Transaction models AND/OR a possible 4th model
	// DONE 2. Link all models using AggregateReference<> from Spring Data JDBC (Transaction is left to figure out)
	// TODO 3. Create Service Layer for models using Spring Data JDBC
	// TODO 4. Populate Customer/Account/Transaction DAOs w/ CRUD operations using JDBC API (don't do impl yet)
	// TODO 5. Create DTO classes for models to be used in HTTP Requests
	// TODO 6. Create Controller classes for models to configure API endpoints
	
	//Project Breakdown
	//	API Endpoint (Postman) <---> Controller <-- Service <-- Dao (First need to establish db relationships and finalize models to finish Dao)
	
}
