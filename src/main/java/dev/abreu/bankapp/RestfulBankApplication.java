package dev.abreu.bankapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import dev.abreu.bankapp.model.Customer;
import dev.abreu.bankapp.repository.CustomerRepository;

@SpringBootApplication
public class RestfulBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestfulBankApplication.class, args);
		System.out.println(
				"  ,------.  ,-------.   ,-----.                     ,--.         ,---.                   \r\n"
				+ "|  .--. ' `--.   /    |  |) /_   ,--,--. ,--,--,  |  |,-.     /  O  \\  ,---.   ,---.  \r\n"
				+ "|  '--' |   /   /     |  .-.  \\ ' ,-.  | |      \\ |    /   | .-.   | | .-. | | .-. | \r\n"
				+ "|  | --'   /   `--.   |  '--' / \\ '-'  | |  ||  | |   \\    | |  |  | | '-' ' | '-' ' \r\n"
				+ "`--'      `-------'   `------'   `--`--' `--''--' `--'`--'   `--' `--' |  |-'  |  |-'  \r\n"
				+ "                                                                       `--'    `--'    "
				
				);
	}
	
	@Bean
	CommandLineRunner commandLineRunner(CustomerRepository customers) {
		return args -> {
			customers.save(new Customer(null, "Bobby", "Abreu", "12345 Fairfallow Drive", "bobbyDaGoat", "password"));
		};
	}
	
	/* Check list for completing RestfulBank API */
	// TODO 1. 
	// TODO 2.
	// TODO 3.

}
