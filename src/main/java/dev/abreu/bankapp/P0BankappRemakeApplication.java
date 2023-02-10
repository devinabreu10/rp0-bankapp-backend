package dev.abreu.bankapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class P0BankappRemakeApplication {
	
	//private static final Logger log = LogManager.getLogger(P0BankappRemakeApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(P0BankappRemakeApplication.class, args);
		System.out.println("\nP0 Bankapp Remake Application has STARTED...");
		//log.info("INFO Testing log in P0 Bankapp Remake");
		//log.debug("DEBUG Testing log in P0 Bankapp Remake");
	}

	// Accidentally messed up my old project and had to restart it. I need to be mindful about pushing my code to github
	
	//Things I need to do to get back to where I was
	// 1. Create Customer model (label customer id with @Id)
	// 2. Create CustomerRespository and CustomerDao/CustomerDaoImpl (for JDBC API and Spring Data JDBC)
	// 3. Create UsernameTaken Exception
	// 4. Add command line runner bean to this class (from Spring Data JDBC Tutorial vid)
	// DONE 5. Push this project to GitHub, make sure the right files are being ignored to avoid disaster again
	//    --- .gitignore is opened in notepad++
	// DONE 6. Check if mvnw and mvnw.cmd are needed before pushin to GitHub
	
	//Plan out thins to accomplish after finishing everything above this
	// TODO 1. 
	// TODO 2.
	// TODO 3.
	
}
