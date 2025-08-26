package dev.abreu.bankapp;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableCaching
public class RP0BankApplication {

    private static final Logger log = LogManager.getLogger(RP0BankApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RP0BankApplication.class, args);
        log.info("RP0 Bank Application has STARTED...");
    }

    @Bean
    public DataSource dataSource(
            @Value("${spring.datasource.url}") String dbUrl,
            @Value("${spring.datasource.username}") String dbUser,
            @Value("${spring.datasource.password}") String dbPassword,
            @Value("${spring.datasource.driver-class-name}") String driverClassName
    ) {
        return DataSourceBuilder.create()
                .url(dbUrl)
                .username(dbUser)
                .password(dbPassword)
                .driverClassName(driverClassName)
                .build();
    }

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "customer", "account", "transaction", "transfer", "auth-token", "unified-transaction-detail"
        );
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(60, TimeUnit.MINUTES) // TTL
                        .maximumSize(500) // limit entries
        );
        return cacheManager;
    }
}
