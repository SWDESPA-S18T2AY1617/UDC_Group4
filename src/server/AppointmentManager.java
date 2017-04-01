package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Appointment;
import server.DBConnection;

public class AppointmentManager {
	
	private DBConnection connection;
	private PreparedStatement statement;
	
	public AppointmentManager ()
	{
		connection = new DBConnection ();
	}
	
	public ArrayList <Appointment> getAllAppointments ()
	{
		ArrayList <Appointment> appointments = new ArrayList<Appointment> ();
		try (Connection connect = connection.getConnection()) {
			String query = "SELECT * FROM " + Appointment.TABLE_NAME;
			
			statement = connect.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			
			while(rs.next()) {
				appointments.add(toAppointment(rs));
			}
			
			connection.close();
			System.out.println("[" + getClass().getName() + "] Successful SELECT from " + Appointment.TABLE_NAME);
		} catch (SQLException e) {
			System.out.println("[" + getClass().getName() + "] Unable to SELECT from " + Appointment.TABLE_NAME);
			e.printStackTrace();
		}
		
		return appointments;
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
