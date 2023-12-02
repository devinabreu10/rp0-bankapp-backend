package dev.abreu.bankapp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionUtil {
	
	private static final Logger log = LogManager.getLogger(ConnectionUtil.class);
	
	private static ConnectionUtil connUtil;
	private Properties props;
	
	private ConnectionUtil() {
		props = new Properties();
		
		InputStream propsFile = ConnectionUtil.class.getClassLoader().getResourceAsStream("application.properties");
		
		try {
			props.load(propsFile);
		} catch (IOException e) {
			log.error("IOException Thrown: " + e.getMessage());
		}
	}
	
	public static synchronized ConnectionUtil getConnectionUtil() {
		if(connUtil == null) {
			connUtil = new ConnectionUtil();
		}
		return connUtil;
	}
	
	public Connection getConnection() {
		Connection conn = null;
		
		String dbUrl = props.getProperty("spring.datasource.url");
		String dbUser = props.getProperty("spring.datasource.username");
		String dbPass = props.getProperty("spring.datasource.password");
		
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
			
		} catch (SQLException | ClassNotFoundException e) {
			log.error("Exception Thrown: " + e.getMessage());
		}
		
		return conn;
		
	}

}
