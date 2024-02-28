package dev.abreu.bankapp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import dev.abreu.bankapp.dao.AccountDao;
import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.model.Customer;

@SpringBootApplication
public class RP0BankApplication {
	
	private static final Logger log = LogManager.getLogger(RP0BankApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RP0BankApplication.class, args);
		log.info("\nRP0 Bank Application has STARTED...");
		
	}
	
	@Bean
	CommandLineRunner commandLineRunner(CustomerDao customerDao, AccountDao accountDao) {
		return args -> {
			// When id is not null it treats it as an update as opposed to inserting a new record
			log.info("Adding customers if they don't already exist...");
			customerDao.saveCustomer(new Customer(null, "Bobby", "Abreu", "12345 MLB Dr.", "bobby123", "password"));
			customerDao.saveCustomer(new Customer(null, "Jolene", "FitzGilbert", "4 Sycamore Circle", "jfitzgilbert0", "bP6>#)NTV?qgg"));
			customerDao.saveCustomer(new Customer(null, "Josefa", "Merwood", "23569 Bultman Drive", "jmerwood1", "pK9%b?igvw"));
			customerDao.saveCustomer(new Customer(null, "Ronnie", "Voak", "7 Ridge Oak Terrace", "rvoak3", "pN9,Z7#mC"));
		};
	}
	
}
