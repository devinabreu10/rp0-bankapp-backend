package dev.abreu.bankapp.utils;

import static dev.abreu.bankapp.utils.BankappConstants.SQL_EXCEPTION_CAUGHT;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionUtil {
	
	private static final Logger log = LogManager.getLogger(ConnectionUtil.class);
	
	private static ConnectionUtil connUtil;
	
	private ConnectionUtil() {
		
	}
	
	public static synchronized ConnectionUtil getConnectionUtil() {
		if(connUtil == null) {
			connUtil = new ConnectionUtil();
		}
		return connUtil;
	}
	
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
