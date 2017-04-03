package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Appointment;
import server.DBConnection;

public class AppointmentManager {
	
	private Connection connect;
	private PreparedStatement statement;
	
	public AppointmentManager ()
	{
		DBConnection connection = new DBConnection ();
		if(connection.openConnection())
			connect = connection.getActiveConnection();
	}
	
	public ArrayList <Appointment> getAllAppointments ()
	{
		ArrayList <Appointment> appointments = new ArrayList<Appointment> ();
		try{
			String query = "SELECT * FROM " + Appointment.TABLE_NAME;
			
			statement = connect.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			
			while(rs.next()) {
				appointments.add(toAppointment(rs));
			}
			
			System.out.println("[" + getClass().getName() + "] Successful SELECT from " + Appointment.TABLE_NAME);
		} catch (SQLException e) {
			System.out.println("[" + getClass().getName() + "] Unable to SELECT from " + Appointment.TABLE_NAME);
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return appointments;
	}
	
	public Appointment getAppointment (int id)
	{
		Appointment appointment = null;
		
		try
		{
			ResultSet rs;
			String query = "SELECT * from " + Appointment.TABLE_NAME + 
					" WHERE " + Appointment.COL_ID + " = ?";
			
			PreparedStatement statement = connect.prepareStatement(query);
			statement.setInt(1, id);
			rs = statement.executeQuery();
			
			
			if(rs.next()) {
				appointment = toAppointment(rs);
			}
			
			System.out.println("[" + getClass().getName() + "] Successful SELECT from " + Appointment.TABLE_NAME);
		} catch (SQLException e) {
			System.out.println("[" + getClass().getName() + "] Unable to SELECT from" + Appointment.TABLE_NAME);
		}
		
		return appointment;
	}
	
	public ArrayList<Appointment> getAppointmentWithDoctorID(int id)
	{
		ArrayList <Appointment> appointments = new ArrayList<Appointment> ();
		
		try{
			String query = "SELECT * FROM " + Appointment.TABLE_NAME
					+ " WHERE " + Appointment.COL_DOCTORID + " = " + id + ";";
			
			statement = connect.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			
			while(rs.next()) {
				appointments.add(toAppointment(rs));
			}
			
			System.out.println("[" + getClass().getName() + "] Successful SELECT from " + Appointment.TABLE_NAME);
		} catch (SQLException e) {
			System.out.println("[" + getClass().getName() + "] Unable to SELECT from " + Appointment.TABLE_NAME);
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return appointments;
	}
	
	public void addAppointment(Appointment appointment)
	{
		try {
			String query = "INSERT INTO " + Appointment.TABLE_NAME +  
					" (" + Appointment.COL_ID + 
					", " + Appointment.COL_DATE + 
					", " + Appointment.COL_TIMESTART + 
					", " + Appointment.COL_TIMEEND +
					", " + Appointment.COL_APPOINTMENTNAME +
					", " + Appointment.COL_CLIENTID +
					", " + Appointment.COL_DOCTORID + 
					", " + Appointment.COL_STATUS + ") " +
					" VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
			
			statement = connect.prepareStatement(query);
			
			statement.setInt(1, appointment.getAppointmentID());
			statement.setDate(2, appointment.getAppointmentDate());
			statement.setTime(3, appointment.getTimeIn());
			statement.setTime(4, appointment.getTimeOut());
			statement.setString(5, appointment.getAppointmentName());
			statement.setInt(6, appointment.getClientID());
			statement.setInt(7, appointment.getDoctorID());
			statement.setBoolean(8, appointment.isStatus());
			
			statement.executeUpdate();
			System.out.println("[" + getClass().getName() + "] Successful INSERT to " + Appointment.TABLE_NAME);
		} catch (SQLException e) {
			System.out.println("[" + getClass().getName() + "] Unable to INSERT to " + Appointment.TABLE_NAME);
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void deleteAppointment (int id)
	{	
		String query = "DELETE FROM " + Appointment.TABLE_NAME + 
						" WHERE " + Appointment.COL_ID + " = ?;";
		
		try
		{
			statement = connect.prepareStatement(query);
			statement.setInt(1, id);
			
			statement.executeUpdate();
			
			System.out.println("[" + getClass().getName() + "] Successful DELETE in " + Appointment.TABLE_NAME);
		} catch (SQLException e) {
			System.out.println("[" + getClass().getName() + "] Unable to DELETE in " + Appointment.TABLE_NAME);
			e.printStackTrace();
		}
	}
	
	public Appointment toAppointment (ResultSet rs) throws SQLException {
		Appointment appointment = new Appointment();
			
		appointment.setAppointmentID(rs.getInt(Appointment.COL_ID));
		appointment.setTimeIn(rs.getTime(Appointment.COL_TIMESTART));
		appointment.setTimeOut(rs.getTime(Appointment.COL_TIMEEND));
		appointment.setAppointmentDate(rs.getDate(Appointment.COL_DATE));
		appointment.setAppointmentName(rs.getString(Appointment.COL_APPOINTMENTNAME));
		appointment.setDoctorID(rs.getInt(Appointment.COL_DOCTORID));
		appointment.setClientID(rs.getInt(Appointment.COL_CLIENTID));
		appointment.setStatus(rs.getBoolean(Appointment.COL_STATUS));
		
		return appointment;
	}
	
}
