package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Client;
import server.DBConnection;

public class ClientManager {
	private DBConnection connection;
	private PreparedStatement statement;
	
	public ClientManager ()
	{
		connection = new DBConnection ();
	}
	
	public ArrayList<Client> getAllClient ()
	{
		ArrayList<Client> clients = new ArrayList<Client>();
		
		try (Connection connect = connection.getConnection())
		{
			ResultSet rs;
			String query = "SELECT * FROM " + Client.TABLE_NAME;
			
			statement = connect.prepareStatement(query);
			rs = statement.executeQuery();
			
			while(rs.next()) {
				clients.add(toClient(rs));
			}
			
			connection.close();
			
			System.out.println("[" + getClass().getName() + "] Successful SELECT from " + Client.TABLE_NAME);
			
			return clients;
			
		} catch (SQLException e) {
			System.out.println("[" + getClass().getName() + "] Unable to SELECT from " + Client.TABLE_NAME);
			e.printStackTrace();
			return null;
		}
	}
	
	public void insertToClient(Client client)
	{
		String query = "INSERT INTO " 	+ Client.TABLE_NAME + "(" 
										+ Client.COL_ID + ", " 
										+ Client.COL_NAME + ")"
										+ " VALUES (?,?);";

		try (Connection connect = connection.getConnection()) {
			statement = connect.prepareStatement(query);
			
			statement.setInt(1, client.getID());
			statement.setString(2, client.getName());

			// execute the  insert
			statement.executeUpdate();
			connection.close();
			System.out.println("[" + getClass().getName() + "] Successful INSERT into " + Client.TABLE_NAME);
		} 
		catch (SQLException e)
		{
			System.out.println("[" + getClass().getName() + "] Unable to INSERT into " + Client.TABLE_NAME);
			e.printStackTrace();
		}
	}
	
	
	public void deleteClient (int id)
	{	
		String query = "DELETE FROM " + Client.TABLE_NAME + 
						" WHERE " + Client.COL_ID + " = ?;";
		
		try (Connection connect = connection.getConnection())
		{
			statement = connect.prepareStatement(query);
			statement.setInt(1, id);
			statement.executeUpdate();
			
			System.out.println("[" + getClass().getName() + "] Successful DELETE in " + Client.TABLE_NAME);
			connection.close();
		} catch (SQLException e) {
			System.out.println("[" + getClass().getName() + "] Unable to DELETE in " + Client.TABLE_NAME);
			e.printStackTrace();
		}
	}
	
	public void updateClient (Client d)
	{
		String updateTableSQL = "UPDATE " + Client.TABLE_NAME + 
								" SET " + Client.COL_NAME + "= ?" + 
								" WHERE " + Client.COL_ID + " = ?;";

		try (Connection connect = connection.getConnection())
		{
			statement = connect.prepareStatement(updateTableSQL);

			statement.setString(1, d.getName());
			statement.setInt(2, d.getID());
			
			// execute update SQL statement
			statement.executeUpdate();
			connection.close();
			System.out.println("[" + getClass().getName() + "] Successful UPDATE in " + Client.TABLE_NAME);
		} catch (SQLException e) {
			System.out.println("[" + getClass().getName() + "] Unable to UPDATE in " + Client.TABLE_NAME);
			e.printStackTrace();

		}
	}
	
	private Client toClient(ResultSet rs) throws SQLException {
		Client client = new Client ();
		
		client.setID(rs.getInt(Client.COL_ID));
		client.setName(rs.getString(Client.COL_NAME));
		
		return client;
	}
	
	
}
