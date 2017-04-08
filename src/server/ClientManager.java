package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Appointment;
import model.Client;
import server.DBConnection;

public class ClientManager {
	
	private Connection connect;
	private PreparedStatement statement;
	
	public ClientManager ()
	{
		DBConnection connection = new DBConnection ();
		if(connection.openConnection())
			connect = connection.getActiveConnection();
	}
	public ArrayList<Appointment> getClientAppointments(){
        ArrayList<Appointment> apts = new ArrayList<Appointment>();
        
        String query = "SELECT * FROM " + Client.TABLE_NAME + " c, " + Appointment.TABLE_NAME + " a WHERE c.clientID = a.clientID";
        
        AppointmentManager am = new AppointmentManager();
        
        ResultSet rs;
        
        try{
            statement = connect.prepareStatement(query);
            rs = statement.executeQuery();
            
            while(rs.next()){
               apts.add(am.toAppointment(rs));
            }
            
        }catch(Exception e){
            e.printStackTrace();
            
        }
        return apts;
    }
	public ArrayList<Client> getAllClient ()
	{
		ArrayList<Client> clients = new ArrayList<Client>();
		
		try
		{
			ResultSet rs;
			String query = "SELECT * FROM " + Client.TABLE_NAME;
			
			statement = connect.prepareStatement(query);
			rs = statement.executeQuery();
			
			while(rs.next()) {
				clients.add(toClient(rs));
			}
			
			
			
			System.out.println("[" + getClass().getName() + "] Successful SELECT from " + Client.TABLE_NAME);
			
			return clients;
			
		} catch (SQLException e) {
			System.out.println("[" + getClass().getName() + "] Unable to SELECT from " + Client.TABLE_NAME);
			e.printStackTrace();
			return null;
		}
	}
	
	public Client getClient (int id)
	{
		Client client = null;
		
		try
		{
			ResultSet rs;
			String query = "SELECT * from " + Client.TABLE_NAME + 
					" WHERE " + Client.COL_ID + " = ?";
			
			PreparedStatement statement = connect.prepareStatement(query);
			statement.setInt(1, id);
			rs = statement.executeQuery();
			
			
			if(rs.next()) {
				client = toClient(rs);
			}
			
			System.out.println("[" + getClass().getName() + "] Successful SELECT from " + Client.TABLE_NAME);
		} catch (SQLException e) {
			System.out.println("[" + getClass().getName() + "] Unable to SELECT from" + Client.TABLE_NAME);
		}
		
		return client;
	}
	
	public void insertToClient(Client client)
	{
		String query = "INSERT INTO " 	+ Client.TABLE_NAME + "(" 
										+ Client.COL_ID + ", " 
										+ Client.COL_NAME + ")"
										+ " VALUES (?,?);";

		try {
			statement = connect.prepareStatement(query);
			
			statement.setInt(1, client.getID());
			statement.setString(2, client.getName());

			// execute the  insert
			statement.executeUpdate();
			
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
		
		try
		{
			statement = connect.prepareStatement(query);
			statement.setInt(1, id);
			statement.executeUpdate();
			
			System.out.println("[" + getClass().getName() + "] Successful DELETE in " + Client.TABLE_NAME);
			
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

		try
		{
			statement = connect.prepareStatement(updateTableSQL);

			statement.setString(1, d.getName());
			statement.setInt(2, d.getID());
			
			// execute update SQL statement
			statement.executeUpdate();
			
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
