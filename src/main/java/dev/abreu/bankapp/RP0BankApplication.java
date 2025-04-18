package dev.abreu.bankapp;

import dev.abreu.bankapp.util.ConnectionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RP0BankApplication {

    private static final Logger log = LogManager.getLogger(RP0BankApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RP0BankApplication.class, args);
        log.info("RP0 Bank Application has STARTED...");
    }

    /**
     * This bean provides a connection utility instance that can be used throughout the application.
     *
     * @return the singleton instance of the ConnectionUtil class
     */
    @Bean
    public ConnectionUtil connUtil() {
        return ConnectionUtil.getConnectionUtil();
    }
}
