package application_login;

import java.sql.DriverManager;
import java.sql.Connection;

public class DB {
	private static final String jdbcURL = "URL";
	private static final String username = "USERNAME";
	private static final String password = "PASSWORD";

	public static Connection getConnection() throws Exception {
		return DriverManager.getConnection(jdbcURL, username, password);
	}

}
