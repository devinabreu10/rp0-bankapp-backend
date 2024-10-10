package dev.abreu.bankapp;

import dev.abreu.bankapp.dao.AccountDao;
import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.model.Customer;
import dev.abreu.bankapp.utils.ConnectionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@SpringBootApplication
public class RP0BankApplication {

    private static final Logger log = LogManager.getLogger(RP0BankApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RP0BankApplication.class, args);
        log.info("RP0 Bank Application has STARTED...");
    }

    @Bean
    CommandLineRunner commandLineRunner(CustomerDao customerDao, AccountDao accountDao, PasswordEncoder passwordEncoder) {
        return args -> {
            // When id is not null it treats it as an update as opposed to inserting a new record
            log.info("Adding customers if they don't already exist...");
            customerDao.saveCustomer(new Customer("Bobby", "Abreu", "12345 MLB Dr.", "bobby123", passwordEncoder.encode("ph8(*445%64")));
            customerDao.saveCustomer(new Customer("Jolene", "FitzGilbert", "4 Sycamore Circle", "jfitzgilbert0", passwordEncoder.encode("bP6>#)NTV?qgg")));
            customerDao.saveCustomer(new Customer("Josefa", "Merwood", "23569 Bultman Drive", "jmerwood1", passwordEncoder.encode("pK9%b?igvw")));
            customerDao.saveCustomer(new Customer("Ronnie", "Voak", "7 Ridge Oak Terrace", "rvoak3", passwordEncoder.encode("pN9,Z7#mC")));
        };
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

    /**
     * Spring alternative to using custom ConnectionUtil singleton class
     *
     * @return DataSource
     */
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url(System.getenv("DB_URL"))
                .username(System.getenv("DB_USER"))
                .password(System.getenv("DB_PASSWORD"))
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
