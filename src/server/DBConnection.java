package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	
	private Connection connection;
	private boolean open;
	private static String DRIVER_NAME;
	private static String URL;
	private static String USERNAME;
	private static String PASSWORD;
	private static String DATABASE = "clinic";
	
	public DBConnection () {
		open = false;
		String file = "src/config/db.config";
		try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(file)))) {
			DRIVER_NAME = br.readLine();
			URL = br.readLine();
			USERNAME = br.readLine();
			PASSWORD = br.readLine();
			
		} catch (IOException e) {
			System.err.println("Error! " + file + " not loaded!");
		}	
	}
	
	public boolean openConnection () {
		if(!open) {
			try {
				Class.forName(DRIVER_NAME);
				connection = DriverManager.getConnection(URL + DATABASE + "?autoReconnect=true&useSSL=false", USERNAME, PASSWORD);
				System.out.println("[" + getClass().getName() + "] Connection to Database successful!");
				open = true;
				return true;
			} catch (Exception ex) {
				System.out.println("[" + getClass().getName() + "] Exception Caught! Unable to connect to " + DATABASE);
				return false;
			}
		} else {
			System.err.println("[" + getClass().getName() + "] Connection already open!");
			return false;
		}
	}
	
	public Connection getActiveConnection () {
		if (open) {
			return connection;
		} else {
			System.err.println("[" + getClass().getName() + "] Connection not open!");
			return null;
		}
	}
	
	public void closeConnection() {
        try {
        	open = false;
            connection.close();
            System.out.println("[" + getClass().getName() + "] Closed Connection to Database");
        } catch (SQLException e) {
           System.out.println("[" + getClass().getName() + "] Cannot close connection");
        } catch (NullPointerException n) {
        	System.out.println("[" + getClass().getName() + "] No connection open");
        }
	}

	public boolean isOpen() {
		return open;
	}
}