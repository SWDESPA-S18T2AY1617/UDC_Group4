package server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

import control.CalendarProgram;
import model.Appointment;
import model.Appointment;
import server.DBConnection;

public class AppointmentManager {
	
	private Connection connect;
	private PreparedStatement statement;
	private ArrayList<CalendarProgram> allController;
	
	public AppointmentManager (ArrayList<CalendarProgram> ac)
	{
		DBConnection connection = new DBConnection ();
		if(connection.openConnection())
			connect = connection.getActiveConnection();
		allController = ac;
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
					", " + Appointment.COL_STATUS + 
					", " + Appointment.COL_COLOR + 
					", " + Appointment.COL_STARTROW + 
					", " + Appointment.COL_ENDROW + 
					", " + Appointment.COL_COLWEEK + ") " +
					" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			
			statement = connect.prepareStatement(query);
			
			statement.setInt(1, appointment.getAppointmentID());
			statement.setDate(2, Date.valueOf(appointment.getAppointmentDate()));
			statement.setTime(3, Time.valueOf(appointment.getLocalTimeIn()));
			statement.setTime(4, Time.valueOf(appointment.getLocalTimeOut()));
			statement.setString(5, appointment.getAppointmentName());
			statement.setNull(6, java.sql.Types.INTEGER);
			statement.setInt(7, appointment.getDoctorID());
			statement.setBoolean(8, appointment.isStatus());
			statement.setString(9, appointment.getColorName());
			statement.setInt(10, appointment.getStartRowDay());
			statement.setInt(11, appointment.getEndRowDay());
			statement.setInt(12, appointment.getColWeek());
			
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
	
	public void updateAppointment (Appointment a)
	{
		String updateTableSQL = "UPDATE " + Appointment.TABLE_NAME + 
								" SET " + Appointment.COL_APPOINTMENTNAME + "= ?, " + Appointment.COL_CLIENTID + "= ? ," + 
								Appointment.COL_COLOR + "= ?, " + Appointment.COL_STARTROW + "= ? ," + 
								Appointment.COL_ENDROW + "= ?, " + Appointment.COL_DATE + "= ? ," + 
								Appointment.COL_TIMESTART + "= ?, " + Appointment.COL_TIMEEND + "= ? ," + 
								Appointment.COL_STATUS + "= ? ," + Appointment.COL_COLWEEK + "= ? " +
								" WHERE " + Appointment.COL_ID + " = ?;";

		try
		{
			
			 statement = connect.prepareStatement(updateTableSQL);

			statement.setString(1, a.getAppointmentName());
			statement.setInt(2, a.getClientID());
			statement.setString(3, a.getColorName());
			statement.setInt(4, a.getStartRowDay());
			statement.setInt(5, a.getEndRowDay());
			statement.setDate(6, Date.valueOf(a.getAppointmentDate()));
			statement.setTime(7, Time.valueOf(a.getLocalTimeIn()));
			statement.setTime(8, Time.valueOf(a.getLocalTimeOut()));
			statement.setBoolean(9, a.isStatus());
			statement.setInt(10, a.getColWeek());
			statement.setInt(11, a.getAppointmentID());
			
			// execute update SQL statement
			statement.executeUpdate();
			System.out.println("[" + getClass().getName() + "] Successful UPDATE in " + Appointment.TABLE_NAME);
		} catch (SQLException e) {
			System.out.println("[" + getClass().getName() + "] Unable to UPDATE in " + Appointment.TABLE_NAME);
			e.printStackTrace();

		}
	}
	
	public Appointment toAppointment (ResultSet rs) throws SQLException {
		Appointment appointment = new Appointment();
			
		appointment.setAppointmentID(rs.getInt(Appointment.COL_ID));
		appointment.setLocalTimeIn(rs.getTime(Appointment.COL_TIMESTART).toLocalTime());
		appointment.setLocalTimeOut(rs.getTime(Appointment.COL_TIMEEND).toLocalTime());
		appointment.setAppointmentDate(rs.getDate(Appointment.COL_DATE).toLocalDate());
		appointment.setAppointmentName(rs.getString(Appointment.COL_APPOINTMENTNAME));
		appointment.setDoctorID(rs.getInt(Appointment.COL_DOCTORID));
		appointment.setClientID(rs.getInt(Appointment.COL_CLIENTID));
		appointment.setStatus(rs.getBoolean(Appointment.COL_STATUS));
		appointment.setColor(rs.getString(Appointment.COL_COLOR));
		appointment.setStartRowDay();
		appointment.setEndRowDay();
		
		return appointment;
	}
	
}
