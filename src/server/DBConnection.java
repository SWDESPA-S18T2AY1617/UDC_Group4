package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	
	public Connection getConnection ()
	{
		try {
			Class.forName(driverName);
			connection = DriverManager.getConnection(url + database + "?autoReconnect=true&useSSL=false", username, password);
			System.out.println("[" + getClass().getName() + "] Connection to Database successful!");
			return connection;
		} catch (Exception ex) {
			System.out.println("[" + getClass().getName() + "] Exception Caught! Unable to connect to " + database);
			return null;
		}
		
	}
	
	public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
           System.out.println("[" + getClass().getName() + "] Cannot close connection");
        }
	}
	
	private Connection connection;
	private final static String driverName = "com.mysql.jdbc.Driver";
	private final static String localHost = "127.0.0.1";
	private final static String	url = "jdbc:mysql://" + localHost + ":3306/";
	private final static String	database = "clinic";
	private final static String	username = "root";
	private final static String	password = "root";
}