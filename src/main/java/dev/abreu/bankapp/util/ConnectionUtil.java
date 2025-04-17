package dev.abreu.bankapp.util;

import static dev.abreu.bankapp.util.BankappConstants.SQL_EXCEPTION_CAUGHT;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

/**
 * This class provides utility methods for managing database connections.
 * <p>
 * Replaced by Spring DataSource configured in {@link dev.abreu.bankapp.config.ApplicationConfig}
 *
 * @author Devin Abreu
 */
public class ConnectionUtil {

	private static final Logger log = LogManager.getLogger(ConnectionUtil.class);

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Value("${spring.datasource.username}")
	private String dbUser;

	@Value("${spring.datasource.password}")
	private String dbPass;

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

		try {
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
		} catch (SQLException e) {
			log.error(SQL_EXCEPTION_CAUGHT, e.getMessage());
		}

		return conn;
	}

}
