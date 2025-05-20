package dev.abreu.bankapp.util;

import java.util.Random;

/**
 * Utility class for generating random 8-digit account numbers.
 */
public class AccountNumberGenerator {
    
    private static final Random random = new Random();
    
    private AccountNumberGenerator() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Generates a random 8-digit account number.
     * 
     * @return a random 8-digit account number as Long
     */
    public static Long generateAccountNumber() {
        // Generate a random number between 10000000 and 99999999 (8 digits)
        return 10000000L + random.nextInt(90000000);
    }
}