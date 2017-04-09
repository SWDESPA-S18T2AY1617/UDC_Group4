package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Doctor;
import server.DBConnection;

public class DoctorManager {
	
	private Connection connect ;
	private PreparedStatement statement;
	
	public DoctorManager ()
	{
		DBConnection connection = new DBConnection ();
		if(connection.openConnection())
			connect = connection.getActiveConnection();
	}
	
//	public ArrayList<Appointment> getDoctorAppointment(){
//        String query = "SELECT * FROM " + Doctor.TABLE_NAME + " d, " + Appointment.TABLE_NAME +" a WHERE d.doctorID = a.doctorID";
//        
//        ResultSet rs;
//        
//        try{
//            statement = connect.prepareStatement(query);
//            rs = statement.executeQuery();
//            
//            while(rs.next()){
//               apt.add(.toAppointment(rs));
//            }
//            
//        }catch(Exception e){
//            e.printStackTrace();
//            
//        }
//        
//        
//        return apt;
//    }
	
	public ArrayList<Doctor> getAllDoctor ()
	{
		ArrayList<Doctor> doctors = new ArrayList<Doctor>();
		
		try
		{
			ResultSet rs;
			String query = "SELECT * FROM " + Doctor.TABLE_NAME;
			
			statement = connect.prepareStatement(query);
			rs = statement.executeQuery();
			
			while(rs.next()) {
				doctors.add(toDoctor(rs));
			}
			
			System.out.println("[" + getClass().getName() + "] Successful SELECT from " + Doctor.TABLE_NAME);
			return doctors;
			
		} catch (SQLException e) {
			System.out.println("[" + getClass().getName() + "] Unable to SELECT from " + Doctor.TABLE_NAME);
			e.printStackTrace();
			return null;
		}
	}
	
	public Doctor getDoctor (int id)
	{
		Doctor doctor = null;
		
		try
		{
			ResultSet rs;
			String query = "SELECT * from " + Doctor.TABLE_NAME + 
					" WHERE " + Doctor.COL_ID + " = ?";
			
			PreparedStatement statement = connect.prepareStatement(query);
			statement.setInt(1, id);
			rs = statement.executeQuery();
			
			
			if(rs.next()) {
				doctor = toDoctor(rs);
			}
			
			System.out.println("[" + getClass().getName() + "] Successful SELECT from " + Doctor.TABLE_NAME);
		} catch (SQLException e) {
			System.out.println("[" + getClass().getName() + "] Unable to SELECT from" + Doctor.TABLE_NAME);
		}
		
		return doctor;
	}
	
	public void insertToDoctor(Doctor doctor)
	{
		String query = "INSERT INTO " 	+ Doctor.TABLE_NAME + "(" 
										+ Doctor.COL_ID + ", " 
										+ Doctor.COL_NAME + ", " 
										+ Doctor.COL_SECRETARY + ")"
										+ " VALUES (?,?,?);";

		try {
			statement = connect.prepareStatement(query);
			
			statement.setInt(1, doctor.getID());
			statement.setString(2, doctor.getName());
			statement.setInt(3, doctor.getSecretaryid());

			// execute the  insert
			statement.executeUpdate();
			System.out.println("[" + getClass().getName() + "] Successful INSERT into " + Doctor.TABLE_NAME);
		} 
		catch (SQLException e)
		{
			System.out.println("[" + getClass().getName() + "] Unable to INSERT into " + Doctor.TABLE_NAME);
			e.printStackTrace();
		}
	}
	
	
	public void deleteDoctor (int id)
	{	
		String query = "DELETE FROM " + Doctor.TABLE_NAME + 
						" WHERE " + Doctor.COL_ID + " = ?;";
		
		try 
		{
			statement = connect.prepareStatement(query);
			statement.setInt(1, id);
			statement.executeUpdate();
			
			System.out.println("[" + getClass().getName() + "] Successful DELETE in " + Doctor.TABLE_NAME);
		} catch (SQLException e) {
			System.out.println("[" + getClass().getName() + "] Unable to DELETE in " + Doctor.TABLE_NAME);
			e.printStackTrace();
		}
	}
	
	public void updateDoctor (Doctor d)
	{
		String updateTableSQL = "UPDATE " + Doctor.TABLE_NAME + 
								" SET " + Doctor.COL_NAME + "= ?," + Doctor.COL_SECRETARY + "= ?," + 
								" WHERE " + Doctor.COL_ID + " = ?;";

		try 
		{
			statement = connect.prepareStatement(updateTableSQL);

			statement.setString(1, d.getName());
			statement.setInt(2, d.getSecretaryid());
			statement.setInt(3, d.getID());
			
			// execute update SQL statement
			statement.executeUpdate();
			System.out.println("[" + getClass().getName() + "] Successful UPDATE in " + Doctor.TABLE_NAME);
		} catch (SQLException e) {
			System.out.println("[" + getClass().getName() + "] Unable to UPDATE in " + Doctor.TABLE_NAME);
			e.printStackTrace();

		}
	}
	
	private Doctor toDoctor(ResultSet rs) throws SQLException {
		Doctor doctor = new Doctor ();
		
		doctor.setID(rs.getInt(Doctor.COL_ID));
		doctor.setName(rs.getString(Doctor.COL_NAME));
		doctor.setSecretaryid(rs.getInt(Doctor.COL_SECRETARY));
		
		return doctor;
	}
	
	
}
