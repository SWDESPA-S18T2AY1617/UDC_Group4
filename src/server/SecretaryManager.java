package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Secretary;
import server.DBConnection;

public class SecretaryManager {
	private DBConnection connection;
	private PreparedStatement statement;
	
	public SecretaryManager ()
	{
		connection = new DBConnection ();
	}
	
	public ArrayList<Secretary> getAllSecretary ()
	{
		ArrayList<Secretary> secretarys = new ArrayList<Secretary>();
		
		try (Connection connect = connection.getConnection())
		{
			ResultSet rs;
			String query = "SELECT * FROM " + Secretary.TABLE_NAME;
			
			statement = connect.prepareStatement(query);
			rs = statement.executeQuery();
			
			while(rs.next()) {
				secretarys.add(toSecretary(rs));
			}
			
			connection.close();
			
			System.out.println("[" + getClass().getName() + "] Successful SELECT from " + Secretary.TABLE_NAME);
			
			return secretarys;
			
		} catch (SQLException e) {
			System.out.println("[" + getClass().getName() + "] Unable to SELECT from " + Secretary.TABLE_NAME);
			e.printStackTrace();
			return null;
		}
	}
	
	public void insertToSecretary(Secretary secretary)
	{
		String query = "INSERT INTO " 	+ Secretary.TABLE_NAME + "(" 
										+ Secretary.COL_ID + ", " 
										+ Secretary.COL_NAME + ")"
										+ " VALUES (?,?);";

		try (Connection connect = connection.getConnection()) {
			statement = connect.prepareStatement(query);
			
			statement.setInt(1, secretary.getID());
			statement.setString(2, secretary.getName());

			// execute the  insert
			statement.executeUpdate();
			connection.close();
			System.out.println("[" + getClass().getName() + "] Successful INSERT into " + Secretary.TABLE_NAME);
		} 
		catch (SQLException e)
		{
			System.out.println("[" + getClass().getName() + "] Unable to INSERT into " + Secretary.TABLE_NAME);
			e.printStackTrace();
		}
	}
	
	
	public void deleteSecretary (int id)
	{	
		String query = "DELETE FROM " + Secretary.TABLE_NAME + 
						" WHERE " + Secretary.COL_ID + " = ?;";
		
		try (Connection connect = connection.getConnection())
		{
			statement = connect.prepareStatement(query);
			statement.setInt(1, id);
			statement.executeUpdate();
			
			System.out.println("[" + getClass().getName() + "] Successful DELETE in " + Secretary.TABLE_NAME);
			connection.close();
		} catch (SQLException e) {
			System.out.println("[" + getClass().getName() + "] Unable to DELETE in " + Secretary.TABLE_NAME);
			e.printStackTrace();
		}
	}
	
	public void updateSecretary (Secretary d)
	{
		String updateTableSQL = "UPDATE " + Secretary.TABLE_NAME + 
								" SET " + Secretary.COL_NAME + "= ?" + 
								" WHERE " + Secretary.COL_ID + " = ?;";

		try (Connection connect = connection.getConnection())
		{
			statement = connect.prepareStatement(updateTableSQL);

			statement.setString(1, d.getName());
			statement.setInt(2, d.getID());
			
			// execute update SQL statement
			statement.executeUpdate();
			connection.close();
			System.out.println("[" + getClass().getName() + "] Successful UPDATE in " + Secretary.TABLE_NAME);
		} catch (SQLException e) {
			System.out.println("[" + getClass().getName() + "] Unable to UPDATE in " + Secretary.TABLE_NAME);
			e.printStackTrace();

		}
	}
	
	private Secretary toSecretary(ResultSet rs) throws SQLException {
		Secretary secretary = new Secretary ();
		
		secretary.setID(rs.getInt(Secretary.COL_ID));
		secretary.setName(rs.getString(Secretary.COL_NAME));
		
		return secretary;
	}
	
	
}
