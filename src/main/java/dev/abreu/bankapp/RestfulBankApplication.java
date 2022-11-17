package dev.abreu.bankapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestfulBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestfulBankApplication.class, args);
		System.out.println(
				",------.  ,-------.   ,-----.                     ,--.         ,---.                   \r\n"
				+ "|  .--. ' `--.   /    |  |) /_   ,--,--. ,--,--,  |  |,-.     /  O  \\   ,---.   ,---.  \r\n"
				+ "|  '--' |   /   /     |  .-.  \\ ' ,-.  | |      \\ |     /    |  .-.  | | .-. | | .-. | \r\n"
				+ "|  | --'   /   `--.   |  '--' / \\ '-'  | |  ||  | |  \\  \\    |  | |  | | '-' ' | '-' ' \r\n"
				+ "`--'      `-------'   `------'   `--`--' `--''--' `--'`--'   `--' `--' |  |-'  |  |-'  \r\n"
				+ "                                                                       `--'    `--'    "
				
				);
	} // need to figure out the database connection with Oracle SQL, I think I shut down the instance last time and 
	  // don't remember to start it back up. After this you should setup a DAO class to test the DB connection with JDBC

}
