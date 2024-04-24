package dev.abreu.bankapp.utils;

import static dev.abreu.bankapp.utils.BankappConstants.SQL_EXCEPTION_CAUGHT;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class provides utility methods for managing database connections.
 * 
 * @author Devin Abreu
 */
public class ConnectionUtil {

	private static final Logger log = LogManager.getLogger(ConnectionUtil.class);

	private static ConnectionUtil connUtil;

	/**
	 * Private constructor to prevent instantiation of the class.
	 */
	private ConnectionUtil() {

	}

	/**
	 * Returns the singleton instance of the ConnectionUtil class.
	 *
	 * @return The singleton instance of ConnectionUtil.
	 */
	public static synchronized ConnectionUtil getConnectionUtil() {
		if (connUtil == null) {
			connUtil = new ConnectionUtil();
		}
		return connUtil;
	}

	/**
	 * Retrieves a database connection using the specified environment variables.
	 *
	 * @return A database connection object, or null if an exception occurs.
	 */
	public Connection getConnection() {
		Connection conn = null;

		String dbUrl = System.getenv("DB_URL");
		String dbUser = System.getenv("DB_USER");
		String dbPass = System.getenv("DB_PASSWORD");

		try {
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
		} catch (SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT, e.getMessage());
		}

		return conn;
	}

}
