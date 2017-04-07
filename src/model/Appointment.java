package model;

import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment{
	
    private int appointmentID;
	private String appointmentName;
    private LocalDate appointmentDate;
    private LocalTime timeIn;
    private LocalTime timeOut;
    private int clientID;
    private int doctorID;
    private boolean status;
    private int startRowDay;
    private int endRowDay;
    private int colWeek;
    private String color;
    
    public final static String TABLE_NAME = "appointment";
	public final static String COL_ID = "appointmentID";
	public final static String COL_APPOINTMENTNAME = "appointment_name";
	public final static String COL_DATE = "date";
	public final static String COL_TIMESTART = "time_start";
	public final static String COL_TIMEEND = "time_end";
	public final static String COL_CLIENTID = "clientID";
	public final static String COL_DOCTORID = "doctorID";
	public final static String COL_STATUS = "status";
	public final static String COL_STARTROW = "startRowDay";
	public final static String COL_ENDROW = "endRowDay";
	public final static String COL_COLWEEK = "colWeek";
	public final static String COL_COLOR = "color";
	
	public Appointment()
	{
		appointmentDate = null;
	}
	
	public Appointment(int appointmentID, String appointmentName, String color, LocalDate appointmentDate, LocalTime LocalTimeIn, LocalTime LocalTimeOut, int DoctorID){
		this.appointmentID = appointmentID;
		this.appointmentName = appointmentName;
		this.appointmentDate = appointmentDate;
		this.timeIn = LocalTimeIn;
		this.timeOut = LocalTimeOut;
		this.doctorID = DoctorID;
		this.color = color;
		this.status = false;
        this.setStartRowDay();
        this.setEndRowDay();
	}
	
	public Color getColor() {
        switch (color.toUpperCase()) {
            case "GREEN":
                return Color.GREEN;
            case "RED":
                return Color.RED;
            case "BLUE":
                return Color.BLUE;
            default:
                return Color.ORANGE;
        }
    }
    
    public void setColor(String color){
    	this.color = color;
    }
    
    public String getColorName(){
    	return color.toUpperCase();
    }
    
    public int checkSameDate(LocalDate day)
    {	
    	return this.appointmentDate.compareTo(day);
    }
    
//    public boolean checkYearMonth(int year, int month) {
//        return this.getYear() == year && this.getMonth() == month;
//    }
//
//    public boolean checkSameDate(int year, int month, int day) {
//        return this.getYear() == year && this.getMonth() == month && this.getDay() == day;
//    }
//
//	public String toString(){
//		String a = month + "/" + day + "/" + year + " "+ event + " " + shour + ":" + sminute + " - " + ehour + ":" + eminute;
//		return a;
//	}

	public int getAppointmentID() {
		return appointmentID;
	}

	public void setAppointmentID(int appointmentID) {
		this.appointmentID = appointmentID;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getAppointmentName() {
		return appointmentName;
	}

	public void setAppointmentName(String appointmentName) {
		this.appointmentName = appointmentName;
	}

	public LocalTime getLocalTimeIn() {
		return timeIn;
	}

	public void setLocalTimeIn(LocalTime timeIn) {
		this.timeIn = timeIn;
	}

	public LocalTime getLocalTimeOut() {
		return timeOut;
	}

	public void setLocalTimeOut(LocalTime timeOut) {
		this.timeOut = timeOut;
	}

	public LocalDate getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(LocalDate appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public int getDoctorID() {
		return doctorID;
	}

	public void setDoctorID(int doctorID) {
		this.doctorID = doctorID;
	}

	public int getStartRowDay() {
		return startRowDay;
	}

	public void setStartRowDay() {
		this.startRowDay = timeIn.getHour() * 2;
		if(timeIn.getMinute() == 30)
			this.startRowDay++;
	}

	public int getEndRowDay() {
		return endRowDay;
	}

	public void setEndRowDay() {
		this.endRowDay = timeOut.getHour()* 2;
		if(timeOut.getMinute() == 30)
			this.endRowDay++;
	}
	
	public int getColWeek() {
		return colWeek;
	}

	public void setColWeek(int colWeek) {
		this.colWeek = colWeek;
	}
   
}
