package control;

import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import model.Appointment;
import server.AppointmentManager;
import view.DTModel;
import view.DayModel;
import view.DayTableRenderer;
import view.WeekModel;
import view.WeekTableRenderer;

public class AppointmentHandler {
	
	private AppointmentManager appointmentManager;
    private ArrayList<Appointment> Appointments = new ArrayList<Appointment>();
    private ArrayList<Integer> itemIndex = new ArrayList<Integer>();
    private ArrayList<Integer> itemIndexWeek = new ArrayList<Integer>();
    private DTModel calendarModel = new DTModel();
    private WeekModel weekModel = new WeekModel();
    private DayModel dayModel = new DayModel();

    public AppointmentHandler(AppointmentManager apptManager)
    {
    	setAppointmentManager(apptManager); 
    	Appointments = getAppointmentManager().getAllAppointments();
    }
    
    private void updateTable(int month, int year) {
        GregorianCalendar cal = new GregorianCalendar(year, month, 1);
        
        int som = cal.get(GregorianCalendar.DAY_OF_WEEK); //start of month
        int nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH); //number of days
        ArrayList<Integer> temporaryHolder = new ArrayList<Integer>();

        for (int i=0 ; i< itemIndex.size(); i++)
        {
        	temporaryHolder.add(itemIndex.get(i));
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                calendarModel.setValueAt(null, i, j);
            }
        }
        
        for (int day = 1; day <= nod; day++) {
            int row = (day + som - 2) / 7;
            int column = (day + som - 2) % 7;

            calendarModel.setValueAt(day, row, column);
        }
    }
    
    private void updateDayTable(LocalDate date, String filter)
    {	
        int index = -1; //index of the event found
        
        for (int j = 0; j < 48; j++) {
        	if(j%2==0)
        		dayModel.setValueAt(j/2+":00", j, 0);
        	dayModel.setValueAt(null, j, 1);
        }
        
        int dayRow = 0;
        String event = "";
        
        //All appointments will be displayed.
        if(filter.equalsIgnoreCase("NONE")) {
	        for (int ctr = 0; ctr < Appointments.size(); ctr++) { //searches for the events for this month and year
	        	if (Appointments.get(ctr).checkSameDate(date) == 0) {
	               index = ctr;
	               event = Appointments.get(index).getAppointmentName();
	               dayRow = Appointments.get(ctr).getLocalTimeIn().getHour() * 2;
	               if(Appointments.get(ctr).getLocalTimeIn().getMinute() == 30)
	                   dayRow++;
	               
	               dayModel.setValueAt(event, dayRow, 1);
	               itemIndex.add(ctr);
	            }
	        }
        }
        
        //View free ONLY.
        else if(filter.equalsIgnoreCase("FREE")) {
        	//Temporary arrayList
        	ArrayList<Appointment> open = getOpenSlots ();
        	
	        for (int ctr = 0; ctr < open.size(); ctr++) { //searches for the events for this month and year
	        	if (open.get(ctr).checkSameDate(date) == 0) {
	               index = ctr;
	               event = open.get(index).getAppointmentName();
	               dayRow = open.get(ctr).getLocalTimeIn().getHour() * 2;
	               if(open.get(ctr).getLocalTimeIn().getMinute() == 30)
	                   dayRow++;
	               
	               dayModel.setValueAt(event, dayRow, 1);
	               itemIndex.add(ctr);
	            }
	        }
        }
        
        //View reserved ONLY.
        else if (filter.equalsIgnoreCase("RESERVED")){
        	//Temporary arrayList
        	ArrayList<Appointment> closed = getClosedSlots();
        	
	        for (int ctr = 0; ctr < closed.size(); ctr++) { //searches for the events for this month and year
	        	if (closed.get(ctr).checkSameDate(date) == 0) {
	               index = ctr;
	               event = closed.get(index).getAppointmentName();
	               dayRow = closed.get(ctr).getLocalTimeIn().getHour() * 2;
	               if(closed.get(ctr).getLocalTimeIn().getMinute() == 30)
	                   dayRow++;
	               
	               dayModel.setValueAt(event, dayRow, 1);
	               itemIndex.add(ctr);
	            }
	        }
        }
        
        else {
        	System.out.println("ERROR IN PUTTNG VALUES INSIDE TABLE (appointment handler, updateDayTable) FILTER NOT FOUND.");
        }
            
    }
    
    private void updateDoctorDayTable(int ID, LocalDate date, String filter)
    {	
		ArrayList<Appointment> tempDT = new ArrayList<Appointment>();
		ArrayList<Appointment> appCopy = new ArrayList<Appointment>(); //copy of the appointment list needed based on filter.
		
		//use normal appointment list stored in the application/db.
        if(filter.equalsIgnoreCase("NONE")) {
        	appCopy = Appointments;
        }
        
        //Use appointmint list that only contains RESERVED appointments
        else if(filter.equalsIgnoreCase("RESERVED")) {
        	appCopy = this.getClosedSlots();
        }
        
        //Use appointmint list that only contains FREE appointments
        else if(filter.equalsIgnoreCase("FREE")) {
        	appCopy = this.getOpenSlots();
        }
        
        
        else {
        	System.out.println("ERROR, FILTER NOT FOUND (updateDoctorDayTable)");
        	appCopy = Appointments;
        }
		
		for(int i = 0; i < appCopy.size(); i++){
			if(appCopy.get(i).getDoctorID() == ID){
				tempDT.add(appCopy.get(i));
			}
		}   	
    	
        int index = -1; //index of the event found
        
        for (int j = 0; j < 48; j++) {
        	if(j%2==0)
        		dayModel.setValueAt(j/2+":00", j, 0);
        	dayModel.setValueAt(null, j, 1);
        }
        
        int dayRow = 0;
        String event = "";
        
        
        
        for (int ctr = 0; ctr < tempDT.size(); ctr++) { //searches for the events for this month and year
        	if (tempDT.get(ctr).checkSameDate(date) == 0) {
               index = ctr;
               event = tempDT.get(index).getAppointmentName();
               dayRow = tempDT.get(ctr).getLocalTimeIn().getHour() * 2;
               if(tempDT.get(ctr).getLocalTimeIn().getMinute() == 30)
                   dayRow++;
               
               dayModel.setValueAt(event, dayRow, 1);
               itemIndex.add(ctr);
            }
        }     
    }
    
    private void updateClientDayTable(int ID, LocalDate date, String filter)
    {	
		ArrayList<Appointment> tempDT = new ArrayList<Appointment>();
		ArrayList<Appointment> appCopy = new ArrayList<Appointment>(); //copy of the appointment list needed based on filter.
		
		//use normal appointment list stored in the application/db.
        if(filter.equalsIgnoreCase("NONE")) {
        	appCopy = Appointments;
        }
        
        //Use appointmint list that only contains RESERVED appointments
        else if(filter.equalsIgnoreCase("RESERVED")) {
        	appCopy = this.getClosedSlots();
        }
        else if(filter.equalsIgnoreCase("FREE")) {
            	appCopy = this.getOpenSlots();
        }

        else {
        	System.out.println("ERROR, FILTER NOT FOUND (updateClientDayTable)");
        	appCopy = Appointments;
        }
		
		for(int i = 0; i < appCopy.size(); i++){
			if(appCopy.get(i).getClientID() == ID || appCopy.get(i).isStatus() == false){
				tempDT.add(appCopy.get(i));
			}
		}   	
    	
        int index = -1; //index of the event found
        
        for (int j = 0; j < 48; j++) {
        	if(j%2==0)
        		dayModel.setValueAt(j/2+":00", j, 0);
        	dayModel.setValueAt(null, j, 1);
        }
        
        int dayRow = 0;
        String event = "";
        
        
        
        for (int ctr = 0; ctr < tempDT.size(); ctr++) { //searches for the events for this month and year
        	if (tempDT.get(ctr).checkSameDate(date) == 0) {
               index = ctr;
               event = tempDT.get(index).getAppointmentName();
               dayRow = tempDT.get(ctr).getLocalTimeIn().getHour() * 2;
               if(tempDT.get(ctr).getLocalTimeIn().getMinute() == 30)
                   dayRow++;
               
               dayModel.setValueAt(event, dayRow, 1);
               itemIndex.add(ctr);
            }
        }     
    }
    
    public ArrayList<Appointment> getWeekEvents(LocalDate date)
    {
    	ArrayList<Appointment> weekAppointments = new ArrayList<Appointment>();
    	ArrayList<Appointment> temp = new ArrayList<Appointment>();
    	int value1 = 0;
        int value2 = 0;
        
        LocalDate date1 = null;
    	LocalDate date2;
        
    	for (int ctr = 0; ctr < Appointments.size(); ctr++) { //searches for the events for this month and year
        	
        	if(date.getDayOfWeek().name().equalsIgnoreCase("Monday")){
        		value1 = -2;
        		value2 = 6;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Tuesday")){
        		value1 = -3;
        		value2 = 5;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Wednesday")){
        		value1 = -4;
        		value2 = 4;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Thursday")){
        		value1 = -5;
        		value2 = 3;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Friday")){
        		value1 = -6;
        		value2 = 2;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Saturday")){
        		value1 = -7;
        		value2 = 1;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Sunday")){
        		value1 = -1;
        		value2 = 7;
        	}
        	
        	
        	if(date.getDayOfYear() > 7){
        		date1 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value1);
	        	date2 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value2);
        	}
	        else{
	        	date1 = LocalDate.ofYearDay(date.getYear() - 1, 365 + 1 + value1);
        		date2 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value2);
	        }
	        	
        	
        	if (Appointments.get(ctr).getAppointmentDate().isAfter(date1) && Appointments.get(ctr).getAppointmentDate().isBefore(date2) && !(Appointments.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Sunday")) && !(Appointments.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Saturday"))){
               
               weekAppointments.add(Appointments.get(ctr));
        	}
        }       
    	
    	for(int ctr2 = 1 ; ctr2 < 7 ; ctr2 ++){
    		for(int ctr3 = 0 ; ctr3 < weekAppointments.size(); ctr3++){
    			if(date1.plusDays(ctr2).compareTo(weekAppointments.get(ctr3).getAppointmentDate()) == 0){
    				temp.add(weekAppointments.get(ctr3));
    			}
    		}
    			
    	}
    	
    	return temp;
    }
    
    public ArrayList<Appointment> getDoctorWeekAppointments(int ID, LocalDate date)
    {
		ArrayList<Appointment> tempW = new ArrayList<Appointment>();
		
		for(int i = 0; i < Appointments.size(); i++){
			if(Appointments.get(i).getDoctorID() == ID){
				tempW.add(Appointments.get(i));
			}
		}
    	
    	ArrayList<Appointment> weekAppointments = new ArrayList<Appointment>();
    	ArrayList<Appointment> temp = new ArrayList<Appointment>();
    	int value1 = 0;
        int value2 = 0;
        
        LocalDate date1 = null;
    	LocalDate date2;
        
    	for (int ctr = 0; ctr < tempW.size(); ctr++) { //searches for the events for this month and year
        	
        	if(date.getDayOfWeek().name().equalsIgnoreCase("Monday")){
        		value1 = -2;
        		value2 = 6;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Tuesday")){
        		value1 = -3;
        		value2 = 5;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Wednesday")){
        		value1 = -4;
        		value2 = 4;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Thursday")){
        		value1 = -5;
        		value2 = 3;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Friday")){
        		value1 = -6;
        		value2 = 2;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Saturday")){
        		value1 = -7;
        		value2 = 1;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Sunday")){
        		value1 = -1;
        		value2 = 7;
        	}
        	
        	
        	if(date.getDayOfYear() > 7){
        		date1 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value1);
	        	date2 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value2);
        	}
	        else{
	        	date1 = LocalDate.ofYearDay(date.getYear() - 1, 365 + 1 + value1);
        		date2 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value2);
	        }
	        	
        	
        	if (tempW.get(ctr).getAppointmentDate().isAfter(date1) && tempW.get(ctr).getAppointmentDate().isBefore(date2) && !(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Sunday")) && !(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Saturday"))){
               
               weekAppointments.add(tempW.get(ctr));
        	}
        }       
    	
    	for(int ctr2 = 1 ; ctr2 < 7 ; ctr2 ++){
    		for(int ctr3 = 0 ; ctr3 < weekAppointments.size(); ctr3++){
    			if(date1.plusDays(ctr2).compareTo(weekAppointments.get(ctr3).getAppointmentDate()) == 0){
    				temp.add(weekAppointments.get(ctr3));
    			}
    		}
    			
    	}
    	
    	return temp;
    }
    
    public ArrayList<Appointment> getClientWeekAppointments(int ID, LocalDate date)
    {
		ArrayList<Appointment> tempW = new ArrayList<Appointment>();
		
		for(int i = 0; i < Appointments.size(); i++){
			if(Appointments.get(i).getClientID() == ID || Appointments.get(i).isStatus() == false){
				tempW.add(Appointments.get(i));
			}
		}
    	
    	ArrayList<Appointment> weekAppointments = new ArrayList<Appointment>();
    	ArrayList<Appointment> temp = new ArrayList<Appointment>();
    	int value1 = 0;
        int value2 = 0;
        
        LocalDate date1 = null;
    	LocalDate date2;
        
    	for (int ctr = 0; ctr < tempW.size(); ctr++) { //searches for the events for this month and year
        	
        	if(date.getDayOfWeek().name().equalsIgnoreCase("Monday")){
        		value1 = -2;
        		value2 = 6;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Tuesday")){
        		value1 = -3;
        		value2 = 5;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Wednesday")){
        		value1 = -4;
        		value2 = 4;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Thursday")){
        		value1 = -5;
        		value2 = 3;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Friday")){
        		value1 = -6;
        		value2 = 2;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Saturday")){
        		value1 = -7;
        		value2 = 1;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Sunday")){
        		value1 = -1;
        		value2 = 7;
        	}
        	
        	
        	if(date.getDayOfYear() > 7){
        		date1 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value1);
	        	date2 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value2);
        	}
	        else{
	        	date1 = LocalDate.ofYearDay(date.getYear() - 1, 365 + 1 + value1);
        		date2 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value2);
	        }
	        	
        	
        	if (tempW.get(ctr).getAppointmentDate().isAfter(date1) && tempW.get(ctr).getAppointmentDate().isBefore(date2) && !(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Sunday")) && !(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Saturday"))){
               
               weekAppointments.add(tempW.get(ctr));
        	}
        }       
    	
    	for(int ctr2 = 1 ; ctr2 < 7 ; ctr2 ++){
    		for(int ctr3 = 0 ; ctr3 < weekAppointments.size(); ctr3++){
    			if(date1.plusDays(ctr2).compareTo(weekAppointments.get(ctr3).getAppointmentDate()) == 0){
    				temp.add(weekAppointments.get(ctr3));
    			}
    		}
    			
    	}
    	
    	return temp;
    }
    
    private void updateWeekTable(LocalDate date, String filter)
    {
        int index = -1; //index of the event found
        
        for (int j = 0; j < 48; j++) {
        	if(j%2 == 0)
        		weekModel.setValueAt(j/2+":00", j, 0);
        	
        	for(int i=1; i<=5;i ++){
        		weekModel.setValueAt(null, j, i);
        	}
        }
        
        int dayRow = 0;
        int weekCol = 0;
        int value1 = 0;
        int value2 = 0;
        String event = "";
        
        //Determine what list to use.
        ArrayList<Appointment> appointmentToUse;
        
        //use normal appointment list stored in the application/db.
        if(filter.equalsIgnoreCase("NONE")) {
        	appointmentToUse = Appointments;
        }
        
        //Use appointmint list that only contains RESERVED appointments
        else if(filter.equalsIgnoreCase("RESERVED")) {
        	appointmentToUse = this.getClosedSlots();
        }
        
        //Use appointmint list that only contains FREE appointments
        else if(filter.equalsIgnoreCase("FREE")) {
        	appointmentToUse = this.getOpenSlots();
        }
        
        else {
        	System.out.println("ERROR, FILTER NOT FOUND (updateWeekTable)");
        	appointmentToUse = Appointments;
        }
        
        for (int ctr = 0; ctr < appointmentToUse.size(); ctr++) { //searches for the events for this month and year
        	
        	if(date.getDayOfWeek().name().equalsIgnoreCase("Monday")){
        		value1 = -2;
        		value2 = 6;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Tuesday")){
        		value1 = -3;
        		value2 = 5;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Wednesday")){
        		value1 = -4;
        		value2 = 4;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Thursday")){
        		value1 = -5;
        		value2 = 3;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Friday")){
        		value1 = -6;
        		value2 = 2;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Saturday")){
        		value1 = -7;
        		value2 = 1;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Sunday")){
        		value1 = -1;
        		value2 = 7;
        	}
        	
        	LocalDate date1 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value1);
        	LocalDate date2 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value2);
        	
        	if (appointmentToUse.get(ctr).getAppointmentDate().isAfter(date1) && appointmentToUse.get(ctr).getAppointmentDate().isBefore(date2) && !(Appointments.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Sunday")) && !(Appointments.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Saturday"))){
               index = ctr;
               event = appointmentToUse.get(index).getAppointmentName();
               dayRow = appointmentToUse.get(ctr).getLocalTimeIn().getHour() * 2;
               if(appointmentToUse.get(ctr).getLocalTimeIn().getMinute() == 30)
                   dayRow++;
               
               if(appointmentToUse.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Monday"))
            	   weekCol = 1;
               else if(appointmentToUse.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Tuesday"))
            	   weekCol = 2;
               else if(appointmentToUse.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Wednesday"))
            	   weekCol = 3;
               else if(appointmentToUse.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Thursday"))
            	   weekCol = 4;
               else if(appointmentToUse.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Friday"))
            	   weekCol = 5;
               
               weekModel.setValueAt(event, dayRow, weekCol);
               itemIndexWeek.add(ctr);
            }
        	
        }
            
    }
    
    private void updateDoctorWeekTable(int ID, LocalDate date, String filter)
    {
		ArrayList<Appointment> tempW = new ArrayList<Appointment>();
		//Determine what list to use.
        ArrayList<Appointment> appCopy;
        
        //use normal appointment list stored in the application/db.
        if(filter.equalsIgnoreCase("NONE")) {
        	appCopy = Appointments;
        }
        
        //Use appointmint list that only contains RESERVED appointments
        else if(filter.equalsIgnoreCase("RESERVED")) {
        	appCopy = this.getClosedSlots();
        }
        
        //Use appointmint list that only contains FREE appointments
        else if(filter.equalsIgnoreCase("FREE")) {
        	appCopy = this.getOpenSlots();
        }
        
        //error, use the original list.
        else {
        	System.out.println("ERROR, FILTER NOT FOUND (updateDoctorWeekTable)");
        	appCopy = Appointments;
        }
		
        //Doctor seeing their own appointment filter
		for(int i = 0; i < appCopy.size(); i++){
			if(appCopy.get(i).getDoctorID() == ID){
				tempW.add(appCopy.get(i));
			}
		}
    	
        int index = -1; //index of the event found
        
        for (int j = 0; j < 48; j++) {
        	if(j%2 == 0)
        		weekModel.setValueAt(j/2+":00", j, 0);
        	
        	for(int i=1; i<=5;i ++){
        		weekModel.setValueAt(null, j, i);
        	}
        }
        
        int dayRow = 0;
        int weekCol = 0;
        int value1 = 0;
        int value2 = 0;
        String event = "";
        
        for (int ctr = 0; ctr < tempW.size(); ctr++) { //searches for the events for this month and year
        	
        	if(date.getDayOfWeek().name().equalsIgnoreCase("Monday")){
        		value1 = -2;
        		value2 = 6;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Tuesday")){
        		value1 = -3;
        		value2 = 5;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Wednesday")){
        		value1 = -4;
        		value2 = 4;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Thursday")){
        		value1 = -5;
        		value2 = 3;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Friday")){
        		value1 = -6;
        		value2 = 2;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Saturday")){
        		value1 = -7;
        		value2 = 1;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Sunday")){
        		value1 = -1;
        		value2 = 7;
        	}
        	
        	LocalDate date1;
        	LocalDate date2;
        	
        	if(date.getDayOfYear() > 7){
        		date1 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value1);
	        	date2 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value2);
        	}
	        else{
	        	date1 = LocalDate.ofYearDay(date.getYear() - 1, 365 + 1 + value1);
        		date2 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value2);
	        }
        	
        	if (tempW.get(ctr).getAppointmentDate().isAfter(date1) && tempW.get(ctr).getAppointmentDate().isBefore(date2) && !(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Sunday")) && !(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Saturday"))){
               index = ctr;
               event = tempW.get(index).getAppointmentName();
               dayRow = tempW.get(ctr).getLocalTimeIn().getHour() * 2;
               if(tempW.get(ctr).getLocalTimeIn().getMinute() == 30)
                   dayRow++;
               
               if(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Monday"))
            	   weekCol = 1;
               else if(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Tuesday"))
            	   weekCol = 2;
               else if(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Wednesday"))
            	   weekCol = 3;
               else if(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Thursday"))
            	   weekCol = 4;
               else if(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Friday"))
            	   weekCol = 5;
               
               weekModel.setValueAt(event, dayRow, weekCol);
               itemIndexWeek.add(ctr);
            }
        	
        }
            
    }
    
    private void updateClientWeekTable(int ID, LocalDate date, String filter)
    {
		ArrayList<Appointment> tempW = new ArrayList<Appointment>();
		//Determine what list to use.
        ArrayList<Appointment> appCopy;
        
      //use normal appointment list stored in the application/db.
        if(filter.equalsIgnoreCase("NONE")) {
        	appCopy = Appointments;
        }
        
        //Use appointmint list that only contains RESERVED appointments
        else if(filter.equalsIgnoreCase("RESERVED")) {
        	appCopy = this.getClosedSlots();
        }
        
        else if(filter.equalsIgnoreCase("FREE")) {
            	appCopy = this.getOpenSlots();
        }

        else {
        	System.out.println("ERROR, FILTER NOT FOUND (updateClientDayTable)");
        	appCopy = Appointments;
        }
		
        //Doctor seeing their own appointment filter
		for(int i = 0; i < appCopy.size(); i++){
			if(appCopy.get(i).getClientID() == ID || appCopy.get(i).isStatus() == false){
				tempW.add(appCopy.get(i));
			}
		}
    	
        int index = -1; //index of the event found
        
        for (int j = 0; j < 48; j++) {
        	if(j%2 == 0)
        		weekModel.setValueAt(j/2+":00", j, 0);
        	
        	for(int i=1; i<=5;i ++){
        		weekModel.setValueAt(null, j, i);
        	}
        }
        
        int dayRow = 0;
        int weekCol = 0;
        int value1 = 0;
        int value2 = 0;
        String event = "";
        
        for (int ctr = 0; ctr < tempW.size(); ctr++) { //searches for the events for this month and year
        	
        	if(date.getDayOfWeek().name().equalsIgnoreCase("Monday")){
        		value1 = -2;
        		value2 = 6;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Tuesday")){
        		value1 = -3;
        		value2 = 5;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Wednesday")){
        		value1 = -4;
        		value2 = 4;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Thursday")){
        		value1 = -5;
        		value2 = 3;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Friday")){
        		value1 = -6;
        		value2 = 2;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Saturday")){
        		value1 = -7;
        		value2 = 1;
        	}
            else if(date.getDayOfWeek().name().equalsIgnoreCase("Sunday")){
        		value1 = -1;
        		value2 = 7;
        	}
        	
        	LocalDate date1;
        	LocalDate date2;
        	
        	if(date.getDayOfYear() > 7){
        		date1 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value1);
	        	date2 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value2);
        	}
	        else{
	        	date1 = LocalDate.ofYearDay(date.getYear() - 1, 365 + 1 + value1);
        		date2 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value2);
	        }
        	
        	if (tempW.get(ctr).getAppointmentDate().isAfter(date1) && tempW.get(ctr).getAppointmentDate().isBefore(date2) && !(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Sunday")) && !(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Saturday"))){
               index = ctr;
               event = tempW.get(index).getAppointmentName();
               dayRow = tempW.get(ctr).getLocalTimeIn().getHour() * 2;
               if(tempW.get(ctr).getLocalTimeIn().getMinute() == 30)
                   dayRow++;
               
               if(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Monday"))
            	   weekCol = 1;
               else if(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Tuesday"))
            	   weekCol = 2;
               else if(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Wednesday"))
            	   weekCol = 3;
               else if(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Thursday"))
            	   weekCol = 4;
               else if(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Friday"))
            	   weekCol = 5;
               
               weekModel.setValueAt(event, dayRow, weekCol);
               itemIndexWeek.add(ctr);
            }
        	
        }
            
    }
    
    private void updateDayTableSpecific(LocalDate date)
    {
        int index = -1; //index of the event found
        
        for (int j = 0; j < 48; j++) {
        	if(j%2==0)
        		dayModel.setValueAt(j/2+":00", j, 0);
        	dayModel.setValueAt(null, j, 1);
        }
        
        
        int dayRow = 0;
        String event = "";
        
        for (int ctr = 0; ctr < Appointments.size(); ctr++) { //searches for the events for this month and year
        	
        	if (Appointments.get(ctr).checkSameDate(date) == 0) {
               index = ctr;
               event = Appointments.get(index).getAppointmentName();
               dayRow = Appointments.get(ctr).getLocalTimeIn().getHour() * 2;
               if(Appointments.get(ctr).getLocalTimeIn().getMinute() == 30)
                   dayRow++;
               
        	   dayModel.setValueAt(event, dayRow, 1);
        	   itemIndex.add(ctr);
             	   
            }
        	
        }
            
    }
    
    private void updateDoctorDayTableSpecific(int ID, LocalDate date)
    {
		ArrayList<Appointment> tempDT = new ArrayList<Appointment>();
		
		for(int i = 0; i < Appointments.size(); i++){
			if(Appointments.get(i).getDoctorID() == ID){
				tempDT.add(Appointments.get(i));
			}
		} 
    	
        int index = -1; //index of the event found
        
        for (int j = 0; j < 48; j++) {
        	if(j%2==0)
        		dayModel.setValueAt(j/2+":00", j, 0);
        	dayModel.setValueAt(null, j, 1);
        }
        
        int dayRow = 0;
        String event = "";
        
        for (int ctr = 0; ctr < tempDT.size(); ctr++) { //searches for the events for this month and year
        	
        	if (tempDT.get(ctr).checkSameDate(date) == 0) {
               index = ctr;
               event = tempDT.get(index).getAppointmentName();
               dayRow = tempDT.get(ctr).getLocalTimeIn().getHour() * 2;
               if(tempDT.get(ctr).getLocalTimeIn().getMinute() == 30)
                   dayRow++;
               
        	   dayModel.setValueAt(event, dayRow, 1);
        	   itemIndex.add(ctr);
             	   
            }
        	
        }
            
    }
    
    private void updateClientDayTableSpecific(int ID, LocalDate date)
    {
		ArrayList<Appointment> tempDT = new ArrayList<Appointment>();
		
		for(int i = 0; i < Appointments.size(); i++){
			if(Appointments.get(i).getClientID() == ID || Appointments.get(i).isStatus() == false){
				tempDT.add(Appointments.get(i));
			}
		} 
    	
        int index = -1; //index of the event found
        
        for (int j = 0; j < 48; j++) {
        	if(j%2==0)
        		dayModel.setValueAt(j/2+":00", j, 0);
        	dayModel.setValueAt(null, j, 1);
        }
        
        int dayRow = 0;
        String event = "";
        
        for (int ctr = 0; ctr < tempDT.size(); ctr++) { //searches for the events for this month and year
        	
        	if (tempDT.get(ctr).checkSameDate(date) == 0) {
               index = ctr;
               event = tempDT.get(index).getAppointmentName();
               dayRow = tempDT.get(ctr).getLocalTimeIn().getHour() * 2;
               if(tempDT.get(ctr).getLocalTimeIn().getMinute() == 30)
                   dayRow++;
               
        	   dayModel.setValueAt(event, dayRow, 1);
        	   itemIndex.add(ctr);
             	   
            }
        	
        }
            
    }

    
    public DayTableRenderer getDayRenderer(String filter){
        
    	ArrayList<Integer> startRowList = new ArrayList<Integer>();
    	ArrayList<Integer> endRowList = new ArrayList<Integer>();
    	ArrayList<Color> colorList = new ArrayList<Color>();
    	
    	
    	if(filter.equalsIgnoreCase("NONE")) {
	    	for(int i = 0; i < itemIndex.size();i++){
	    		startRowList.add(getAppointments().get(itemIndex.get(i)).getStartRowDay());
	    		endRowList.add(getAppointments().get(itemIndex.get(i)).getEndRowDay());
	    		colorList.add(getAppointments().get(itemIndex.get(i)).getColor());
	    	}
    	}
    	
    	else if(filter.equalsIgnoreCase("FREE")) {
    		//temp arraylist again.
    		ArrayList<Appointment> open = this.getOpenSlots();
	    	for(int i = 0; i < itemIndex.size();i++){
	    		startRowList.add(open.get(itemIndex.get(i)).getStartRowDay());
	    		endRowList.add(open.get(itemIndex.get(i)).getEndRowDay());
	    		colorList.add(open.get(itemIndex.get(i)).getColor());
	    	}
    	}
    	
    	else if(filter.equalsIgnoreCase("RESERVED")) {
    		ArrayList<Appointment> closed = this.getClosedSlots();
	    	for(int i = 0; i < itemIndex.size();i++){
	    		startRowList.add(closed.get(itemIndex.get(i)).getStartRowDay());
	    		endRowList.add(closed.get(itemIndex.get(i)).getEndRowDay());
	    		colorList.add(closed.get(itemIndex.get(i)).getColor());
	    	}
    	}
    	
    	else
    		System.out.println("ERROR, FILTER NOT FOUND. (getDayRenderer)");
    	
    	itemIndex.clear();
    	
    	return new DayTableRenderer(startRowList, endRowList,colorList);
    }
    
    public DayTableRenderer getDoctorDayRenderer(int ID, String filter){
		ArrayList<Appointment> tempDT = new ArrayList<Appointment>();
		
		//Choose what list to use.
    	ArrayList<Appointment> appCopy;
    	
    	//Filter that states all appointment to be used.
    	if(filter.equalsIgnoreCase("NONE")) {
    		appCopy = getAppointments();
    	}
    	
    	//Appointments that are only free/open are given.
    	else if(filter.equalsIgnoreCase("FREE")) {
    		appCopy = this.getOpenSlots();
    	}
    	
    	//appointments that are only closed/reserved are given.
    	else if(filter.equalsIgnoreCase("RESERVED")) {
    		appCopy = this.getClosedSlots();
    	}
    	
    	//error.
    	else {
    		System.out.println("ERROR, FILTER NOT FOUND. (getDoctorDayRenderer)");
    		appCopy = getAppointments();
    	}
		
		for(int i = 0; i < appCopy.size(); i++){
			if(appCopy.get(i).getDoctorID() == ID){
				tempDT.add(appCopy.get(i));
			}
		}   
    	
    	ArrayList<Integer> startRowList = new ArrayList<Integer>();
    	ArrayList<Integer> endRowList = new ArrayList<Integer>();
    	ArrayList<Color> colorList = new ArrayList<Color>();
    	
    	for(int i = 0; i < itemIndex.size();i++){
    		startRowList.add(tempDT.get(itemIndex.get(i)).getStartRowDay());
    		endRowList.add(tempDT.get(itemIndex.get(i)).getEndRowDay());
    		colorList.add(tempDT.get(itemIndex.get(i)).getColor());
    	}
    	
    	itemIndex.clear();
    	
    	return new DayTableRenderer(startRowList, endRowList,colorList);
    }
    
    public WeekTableRenderer getWeekRenderer(String filter){
        
    	ArrayList<Integer> startRowList = new ArrayList<Integer>();
    	ArrayList<Integer> endRowList = new ArrayList<Integer>();
    	ArrayList<Color> colorList = new ArrayList<Color>();
    	ArrayList<Integer> columnList = new ArrayList<Integer>();
    	
    	//Choose what list to use.
    	ArrayList<Appointment> appointmentList;
    	if(filter.equalsIgnoreCase("NONE")) {
    		appointmentList = getAppointments();
    	}
    	
    	else if(filter.equalsIgnoreCase("FREE")) {
    		appointmentList = this.getOpenSlots();
    	}
    	
    	else if(filter.equalsIgnoreCase("RESERVED")) {
    		appointmentList = this.getClosedSlots();
    	}
    	
    	else {
    		System.out.println("ERROR, FILTER NOT FOUND. (getWeekRenderer)");
       		appointmentList = getAppointments();
    	}
    	
    	
    	for(int i = 0; i < itemIndexWeek.size();i++){
    		startRowList.add(appointmentList.get(itemIndexWeek.get(i)).getStartRowDay());
    		endRowList.add(appointmentList.get(itemIndexWeek.get(i)).getEndRowDay());
    		colorList.add(appointmentList.get(itemIndexWeek.get(i)).getColor());
    		columnList.add(appointmentList.get(itemIndexWeek.get(i)).getColWeek());
    	}
    	
    	itemIndexWeek.clear();
    	
    	return new WeekTableRenderer(startRowList, endRowList,colorList, columnList);
    }
    
    public WeekTableRenderer getDoctorWeekRenderer(int ID, String filter){
        
		ArrayList<Appointment> tempW = new ArrayList<Appointment>();
		
		//Choose what list to use.
    	ArrayList<Appointment> appCopy;
    	
    	//Filter that states all appointment to be used.
    	if(filter.equalsIgnoreCase("NONE")) {
    		appCopy = getAppointments();
    	}
    	
    	//Appointments that are only free/open are given.
    	else if(filter.equalsIgnoreCase("FREE")) {
    		appCopy = this.getOpenSlots();
    	}
    	
    	//appointments that are only closed/reserved are given.
    	else if(filter.equalsIgnoreCase("RESERVED")) {
    		appCopy = this.getClosedSlots();
    	}
    	
    	//error.
    	else {
    		System.out.println("ERROR, FILTER NOT FOUND. (getDoctorWeekRenderer)");
    		appCopy = getAppointments();
    	}
		
		for(int i = 0; i < appCopy.size(); i++){
			if(appCopy.get(i).getDoctorID() == ID){
				tempW.add(appCopy.get(i));
			}
		}  
    	
    	ArrayList<Integer> startRowList = new ArrayList<Integer>();
    	ArrayList<Integer> endRowList = new ArrayList<Integer>();
    	ArrayList<Color> colorList = new ArrayList<Color>();
    	ArrayList<Integer> columnList = new ArrayList<Integer>();
    	
    	
    	for(int i = 0; i < itemIndexWeek.size();i++){
    		startRowList.add(tempW.get(itemIndexWeek.get(i)).getStartRowDay());
    		endRowList.add(tempW.get(itemIndexWeek.get(i)).getEndRowDay());
    		colorList.add(tempW.get(itemIndexWeek.get(i)).getColor());
    		columnList.add(tempW.get(itemIndexWeek.get(i)).getColWeek());
    	}
    	
    	itemIndexWeek.clear();
    	
    	return new WeekTableRenderer(startRowList, endRowList,colorList, columnList);
    }
    
    /**This is the client day table renderer**/
    public DayTableRenderer getClientDayRenderer(int ID, String filter){
		ArrayList<Appointment> tempDT = new ArrayList<Appointment>();
		
		//Choose what list to use.
    	ArrayList<Appointment> appCopy;
    	
    	//use normal appointment list stored in the application/db.
        if(filter.equalsIgnoreCase("NONE")) {
        	appCopy = Appointments;
        }
        
        //Use appointmint list that only contains RESERVED appointments
        else if(filter.equalsIgnoreCase("RESERVED")) {
        	appCopy = this.getClosedSlots();
        }
        else if(filter.equalsIgnoreCase("FREE")) {
            	appCopy = this.getOpenSlots();
        }

        else {
        	System.out.println("ERROR, FILTER NOT FOUND (updateClientDayTable)");
        	appCopy = Appointments;
        }
		
		for(int i = 0; i < appCopy.size(); i++){
			
			//If the filter is "RESERVED, then only display the reserved appointment by THAT client."
			if(appCopy.get(i).getClientID() == ID || appCopy.get(i).isStatus() == false){
				tempDT.add(appCopy.get(i));
			}
		}     
    	
    	ArrayList<Integer> startRowList = new ArrayList<Integer>();
    	ArrayList<Integer> endRowList = new ArrayList<Integer>();
    	ArrayList<Color> colorList = new ArrayList<Color>();
    	
    	for(int i = 0; i < itemIndex.size();i++){
    		startRowList.add(tempDT.get(itemIndex.get(i)).getStartRowDay());
    		endRowList.add(tempDT.get(itemIndex.get(i)).getEndRowDay());
    		colorList.add(tempDT.get(itemIndex.get(i)).getColor());
    	}
    	
    	itemIndex.clear();
    	
    	return new DayTableRenderer(startRowList, endRowList,colorList);
    }
    
    /**This is the week table renderer for the client.**/
    public WeekTableRenderer getClientWeekRenderer(int ID, String filter){
        
		ArrayList<Appointment> tempW = new ArrayList<Appointment>();
		
		//Choose what list to use.
    	ArrayList<Appointment> appCopy;
    	
    	//use normal appointment list stored in the application/db.
        if(filter.equalsIgnoreCase("NONE")) {
        	appCopy = Appointments;
        }
        
        //Use appointmint list that only contains RESERVED appointments
        else if(filter.equalsIgnoreCase("RESERVED")) {
        	appCopy = this.getClosedSlots();
        }
        else if(filter.equalsIgnoreCase("FREE")) {
            	appCopy = this.getOpenSlots();
        }

        else {
        	System.out.println("ERROR, FILTER NOT FOUND (updateClientDayTable)");
        	appCopy = Appointments;
        }
		
		for(int i = 0; i < appCopy.size(); i++){
			
			//If the filter is "RESERVED, then only display the reserved appointment by THAT client."	
			if(appCopy.get(i).getClientID() == ID || appCopy.get(i).isStatus() == false){
				tempW.add(appCopy.get(i));
			}
		}     
    	
    	ArrayList<Integer> startRowList = new ArrayList<Integer>();
    	ArrayList<Integer> endRowList = new ArrayList<Integer>();
    	ArrayList<Color> colorList = new ArrayList<Color>();
    	ArrayList<Integer> columnList = new ArrayList<Integer>();
    	
    	
    	for(int i = 0; i < itemIndexWeek.size();i++){
    		startRowList.add(tempW.get(itemIndexWeek.get(i)).getStartRowDay());
    		endRowList.add(tempW.get(itemIndexWeek.get(i)).getEndRowDay());
    		colorList.add(tempW.get(itemIndexWeek.get(i)).getColor());
    		columnList.add(tempW.get(itemIndexWeek.get(i)).getColWeek());
    	}
    	
    	itemIndexWeek.clear();
    	
    	return new WeekTableRenderer(startRowList, endRowList,colorList, columnList);
    }

    public void addAppointment(LocalDate date, String event, LocalTime timeIn, LocalTime timeOut, int doctorID, String color) {
    	
    	Appointment wantedAppointment = new Appointment(event, color, date, timeIn, timeOut, doctorID);

		if(!checkForConflicts(wantedAppointment)) {
			System.out.println("**ADDED!**\n");
			getAppointments().add(wantedAppointment);
			sync(wantedAppointment);
		}
		
		else {
			System.out.println("**NOT ADDED!**\n");
			//For viewing tests
			JOptionPane.showMessageDialog(null, "Not added:: Date: " + date +" | Time: " + timeIn + " -> " + timeOut);
		}
    	
//		--Original Code--    	
//    	this.getAppointments().add(new Appointment(Appointments.size(), event, color, date, timeIn, timeOut, doctorID));
//    	
    }

    public boolean checkForConflicts(Appointment wantedAppointment) {
		int index = 0;
		
		Appointment tempAppointment;
		
		while(index < Appointments.size()) {

			//Check if same date.
			tempAppointment = Appointments.get(index);
			if(tempAppointment.checkSameDate(wantedAppointment.getAppointmentDate()) == 0) {
				System.out.println("EQUAL DAY");
				System.out.println("Wanted appointment time: " + wantedAppointment.getLocalTimeIn() + " -> " + wantedAppointment.getLocalTimeOut());
				System.out.println("Stored appointment: " + tempAppointment.getLocalTimeIn() + " -> " + tempAppointment.getLocalTimeOut());
				System.out.println("Is it conflicting? : " + tempAppointment.checkConflictTime(wantedAppointment.getLocalTimeIn(), wantedAppointment.getLocalTimeOut()));
				//Check if the time is conflicting.
				if(tempAppointment.checkConflictTime(wantedAppointment.getLocalTimeIn(), wantedAppointment.getLocalTimeOut())) {
					System.out.println("************CONFLICT**********");
					System.out.println("Date: " + tempAppointment.getAppointmentDate());
					System.out.println("Start: " + tempAppointment.getLocalTimeIn());
					System.out.println("End: " + tempAppointment.getLocalTimeOut());
					System.out.println("*******************************");
					return true;
				}

			}
			
			index++;
		}
		
		return false;
	}
	
	public ArrayList<Appointment> getDayEvents(LocalDate date){
		ArrayList<Appointment> filtered = new ArrayList<Appointment>();
		for(int i = 0;i<Appointments.size();i++){
			if(Appointments.get(i).checkSameDate(date) == 0)
				filtered.add(Appointments.get(i));
		}
		
		int temp1 = filtered.size();
		Appointment temp2;
		Appointment [] temp = new Appointment[temp1];
		for (int i = 0; i<temp1;i++){
			temp[i]=filtered.get(i);
		}
		
		for(int i = 0;i<temp1;i++)
			for(int j = 0;j<temp1-1;j++){
				if(temp[j].getLocalTimeIn().getHour()>temp[j+1].getLocalTimeIn().getHour()){
					temp2 = temp[j];
					temp[j]= temp[j+1];
					temp[j+1]= temp2;
				}
			
				if(temp[j].getLocalTimeIn().getHour()==temp[j+1].getLocalTimeIn().getHour()){
					if(temp[j].getLocalTimeIn().getMinute() > temp[j+1].getLocalTimeIn().getMinute()){
						temp2 = temp[j];
						temp[j]= temp[j+1];
						temp[j+1]= temp2;
					}
				}
			}
		
		filtered.clear();
		
		for(int i = 0;i<temp1;i++){
			filtered.add(temp[i]);	
		}
		
		return filtered;	
	}
	
	public ArrayList<Appointment> getDoctorDayAppointments(int ID, LocalDate date){
		ArrayList<Appointment> tempD = new ArrayList<Appointment>();
		
		for(int i = 0; i < Appointments.size(); i++){
			if(Appointments.get(i).getDoctorID() == ID){
				tempD.add(Appointments.get(i));
			}
		}
		
		ArrayList<Appointment> filtered = new ArrayList<Appointment>();
		for(int i = 0;i<tempD.size();i++){
			if(tempD.get(i).checkSameDate(date) == 0)
				filtered.add(tempD.get(i));
		}
		
		int temp1 = filtered.size();
		Appointment temp2;
		Appointment [] temp = new Appointment[temp1];
		for (int i = 0; i<temp1;i++){
			temp[i]=filtered.get(i);
		}
		
		for(int i = 0;i<temp1;i++)
			for(int j = 0;j<temp1-1;j++){
				if(temp[j].getLocalTimeIn().getHour()>temp[j+1].getLocalTimeIn().getHour()){
					temp2 = temp[j];
					temp[j]= temp[j+1];
					temp[j+1]= temp2;
				}
			
				if(temp[j].getLocalTimeIn().getHour()==temp[j+1].getLocalTimeIn().getHour()){
					if(temp[j].getLocalTimeIn().getMinute() > temp[j+1].getLocalTimeIn().getMinute()){
						temp2 = temp[j];
						temp[j]= temp[j+1];
						temp[j+1]= temp2;
					}
				}
			}
		
		filtered.clear();
		
		for(int i = 0;i<temp1;i++){
			filtered.add(temp[i]);	
		}
		
		return filtered;
	}
	
	public ArrayList<Appointment> getOpenSlots () {
    	ArrayList<Appointment> open = new ArrayList<Appointment>();
    	
    	for (int ctr = 0; ctr < Appointments.size(); ctr++) {
    		if (Appointments.get(ctr).isStatus() == false) 
    			open.add(Appointments.get(ctr));
    	}
  
    	return open;
    }
    
    public ArrayList<Appointment> getClosedSlots () {
    	ArrayList<Appointment> closed = new ArrayList<Appointment>();
    	
    	for (int ctr = 0; ctr < Appointments.size(); ctr++) {
    		if (Appointments.get(ctr).isStatus() == true) 
    			closed.add(Appointments.get(ctr));
    	}
    	
    	return closed;
    }
    
    public void sync(Appointment a){
		getAppointmentManager().addAppointment(a);
    }

	public AppointmentManager getAppointmentManager() {
		return appointmentManager;
	}

	public void setAppointmentManager(AppointmentManager appointmentManager) {
		this.appointmentManager = appointmentManager;
	}
    
    public DefaultTableModel getCalendarModel(int month, int year) {
        this.updateTable(month, year);
        return calendarModel;
    }
    
    public DefaultTableModel getDayModel(LocalDate date, String filter){
    	this.updateDayTable(date, filter);
    	return dayModel;
    }
    
    public DefaultTableModel getDoctorDayModel(int ID, LocalDate date, String filter){
    	this.updateDoctorDayTable(ID, date, filter);
    	return dayModel;
    }
    
    public DefaultTableModel getClientDayModel(int ID, LocalDate date, String filter){
    	this.updateClientDayTable(ID, date, filter);
    	return dayModel;
    }
    
	public DefaultTableModel getWeekModel(LocalDate date, String filter) {
		this.updateWeekTable(date, filter);
		return weekModel;
	}

	public DefaultTableModel getDoctorWeekModel(int ID, LocalDate date, String filter){
		this.updateDoctorWeekTable(ID, date, filter);
		return weekModel;
	}
	
	public DefaultTableModel getClientWeekModel(int ID, LocalDate date, String filter){
		this.updateClientWeekTable(ID, date, filter);
		return weekModel;
	}
	
	public DefaultTableModel getDoctorModel(int ID, LocalDate date, String filter){
		this.updateDoctorWeekTable(ID, date, filter);
		return weekModel;
	}
	
	public void setWeekModel(WeekModel weekModel) {
		this.weekModel = weekModel;
	}
	
    public DefaultTableModel getEventDayModel(LocalDate date)
    {
    	this.updateDayTableSpecific(date);
    	return dayModel;
    }

    public DefaultTableModel getDoctorEventDayModel(int ID, LocalDate date)
    {
    	this.updateDoctorDayTableSpecific(ID, date);
    	return dayModel;
    }
    
	public ArrayList<Appointment> getAppointments() {
		return Appointments;
	}

	public void setAppointments(ArrayList<Appointment> Appointments) {
		this.Appointments = Appointments;
	}
    
}
