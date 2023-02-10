package dev.abreu.bankapp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import dev.abreu.bankapp.model.Account;
import dev.abreu.bankapp.model.Customer;
import dev.abreu.bankapp.model.Transaction;
import dev.abreu.bankapp.repository.AccountRepository;
import dev.abreu.bankapp.repository.CustomerRepository;
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
	CommandLineRunner commandLineRunner(CustomerRepository customer, AccountRepository accounts, TransactionRepository transactions) {
		return args -> {
			// When id is not null it treats it as an update as opposed to inserting a new record
			customer.save(new Customer(null, "Bobby", "Abreu", "12345 MLB Dr.", "bobby123", "password"));
			log.info("New CUSTOMER successfully saved!");
			
//			accounts.save(new Account());
//			log.info("New ACCOUNT successfully saved!");
			
//			transactions.save(new Transaction());
//			log.info("New TRANSACTION successfully saved!");
			
		};
	}

	// Accidentally messed up my old project and had to restart it. I need to be mindful about pushing my code to github
	
	//Plan out things to accomplish after finishing everything above this
	// TODO 1. Create Account and Transaction models (test saving through command runner)
	// TODO 2. Populate Customer/Account/Transaction DAOs w/ CRUD operations using JDBC API (don't do impl yet)
	// TODO 3. Link all 3 models using AggregateReference<> from Spring Data JDBC (test saving through command runner)
	
}
