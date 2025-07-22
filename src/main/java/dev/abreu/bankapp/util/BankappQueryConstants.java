package dev.abreu.bankapp.util;

/**
 * Constants that will be used for SQL queries
 *
 * @author Devin Abreu
 */
public class BankappQueryConstants {

    private static final String RP0_BANK_SCHEMA = "\"rp0-bankapp\"";
    private static final String CUSTOMERS_TABLE = RP0_BANK_SCHEMA + ".customers";
    private static final String ACCOUNTS_TABLE = RP0_BANK_SCHEMA + ".accounts";
    private static final String TRANSACTIONS_TABLE = RP0_BANK_SCHEMA + ".transactions";

    private static final String SELECT_ALL_FROM = "SELECT * FROM ";
    private static final String INSERT_INTO = "INSERT into ";
    private static final String DELETE_FROM = "DELETE from ";
    private static final String UPDATE = "UPDATE ";
    private static final String WHERE_ACCOUNT_NUMBER = " WHERE account_number=?";

    private BankappQueryConstants() {
    }

    public static final String SELECT_CUSTOMERS_BY_USERNAME_QUERY = "SELECT customer_id,first_name,last_name,address,username,passwrd FROM " + CUSTOMERS_TABLE + " WHERE username=?";

    public static final String SELECT_CUSTOMERS_BY_ID_QUERY = "SELECT customer_id,first_name,last_name,address,username,passwrd FROM " + CUSTOMERS_TABLE + " WHERE customer_id=?";

    public static final String SELECT_ALL_CUSTOMERS_QUERY = SELECT_ALL_FROM + CUSTOMERS_TABLE;

    public static final String CREATE_CUSTOMER_QUERY = INSERT_INTO + CUSTOMERS_TABLE + " (customer_id,first_name,last_name,address,username,passwrd) VALUES (default,?,?,?,?,?)";

    public static final String UPDATE_CUSTOMER_QUERY = UPDATE + CUSTOMERS_TABLE + " SET first_name=?,last_name=?,address=?,username=? WHERE customer_id=?";

    public static final String DELETE_CUSTOMER_BY_USERNAME_QUERY = DELETE_FROM + CUSTOMERS_TABLE + " WHERE username=?";

    public static final String DELETE_CUSTOMER_BY_ID_QUERY = DELETE_FROM + CUSTOMERS_TABLE + " WHERE customer_id=?";

    public static final String SELECT_ACCOUNTS_BY_ACCTNO_QUERY = SELECT_ALL_FROM + ACCOUNTS_TABLE + WHERE_ACCOUNT_NUMBER + " AND is_active=true";

    public static final String SELECT_ALL_ACCOUNTS_BY_USERNAME_QUERY = "SELECT b.username,a.customer_id,a.account_number,a.nickname,a.account_type,a.account_balance,a.created_at,a.updated_at "
            + "FROM " + ACCOUNTS_TABLE + " a JOIN " + CUSTOMERS_TABLE + " b ON a.customer_id = b.customer_id WHERE b.username=? AND a.is_active=true ORDER BY a.created_at DESC";

    public static final String CREATE_NEW_ACCOUNT_QUERY = INSERT_INTO + ACCOUNTS_TABLE + " (account_number,nickname,account_type,account_balance,created_at,updated_at,customer_id) VALUES (?,?,?,?,?,?,?)";

    public static final String UPDATE_ACCOUNT_QUERY = UPDATE + ACCOUNTS_TABLE + " SET account_type=?,nickname=?,account_balance=?,updated_at=?" + WHERE_ACCOUNT_NUMBER;

    public static final String DELETE_ACCOUNT_BY_ACCTNO_QUERY = DELETE_FROM + ACCOUNTS_TABLE + WHERE_ACCOUNT_NUMBER;

    public static final String SOFT_DELETE_ACCOUNT_BY_ACCTNO_QUERY = UPDATE + ACCOUNTS_TABLE + " SET is_active=?, updated_at=?" + WHERE_ACCOUNT_NUMBER;

    public static final String SELECT_TRANSACTIONS_BY_ID_QUERY = SELECT_ALL_FROM + TRANSACTIONS_TABLE + " WHERE transaction_id=?";

    public static final String SELECT_ALL_TRANSACTIONS_BY_ACCTNO_QUERY = SELECT_ALL_FROM + TRANSACTIONS_TABLE + WHERE_ACCOUNT_NUMBER;

    public static final String SELECT_ALL_TRANSACTIONS_AND_TRANSFERS_BY_CUSTOMER_ID_QUERY =
        "SELECT t.transaction_id AS id, " +
        "t.transaction_type AS type, " +
        "t.transaction_amount AS amount, " +
        "t.transaction_notes AS notes, " +
        "t.created_at, " +
        "t.account_number " +
        "FROM " + TRANSACTIONS_TABLE + " t " +
        "JOIN " + ACCOUNTS_TABLE + " a ON t.account_number = a.account_number " +
        "WHERE a.customer_id=? " +
        "UNION ALL " +
        "SELECT tr.transfer_id AS id, " +
        "'Account Transfer' AS type, " +
        "tr.transfer_amount AS amount, " +
        "tr.transfer_notes AS notes, " +
        "tr.created_at, " +
        "tr.source_acct_num AS account_number " +
        "FROM " + RP0_BANK_SCHEMA + ".transfers tr " +
        "JOIN " + ACCOUNTS_TABLE + " a ON tr.source_acct_num = a.account_number " +
        "WHERE a.customer_id=? " +
        "ORDER BY created_at DESC";

    public static final String CREATE_NEW_TRANSACTION_QUERY = INSERT_INTO + TRANSACTIONS_TABLE + " (transaction_id,transaction_type,transaction_amount,transaction_notes,created_at,account_number) VALUES (default,?,?,?,?,?)";

    public static final String UPDATE_TRANSACTION_QUERY = UPDATE + TRANSACTIONS_TABLE + " SET transaction_type=?,transaction_amount=?,transaction_notes=? WHERE transaction_id=?";

    public static final String DELETE_TRANSACTION_BY_ID_QUERY = DELETE_FROM + TRANSACTIONS_TABLE + " WHERE transaction_id=?";

    public static final String TRANSFER_ACCOUNT_FUNDS_STORED_PROC = "CALL " + RP0_BANK_SCHEMA + ".transfer_account_funds(?, ?, ?, ?)";
}
