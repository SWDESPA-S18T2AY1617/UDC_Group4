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
    private DTModel calendarModel = new DTModel();
    private WeekModel weekModel = new WeekModel();
    private DayModel dayModel = new DayModel();

    public AppointmentHandler()
    {
    	appointmentManager = new AppointmentManager();
    	Appointments = appointmentManager.getAllAppointments();
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
    
    private void updateDayTable(LocalDate date)
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
    
    private void updateWeekTable(LocalDate date)
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
        	
        	LocalDate date1 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value1);
        	LocalDate date2 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value2);
        	
        	if (Appointments.get(ctr).getAppointmentDate().isAfter(date1) && Appointments.get(ctr).getAppointmentDate().isBefore(date2) && !(Appointments.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Sunday")) && !(Appointments.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Saturday"))){
               index = ctr;
               event = Appointments.get(index).getAppointmentName();
               dayRow = Appointments.get(ctr).getLocalTimeIn().getHour() * 2;
               if(Appointments.get(ctr).getLocalTimeIn().getMinute() == 30)
                   dayRow++;
               
               System.out.println(date);
               System.out.println(date.getDayOfWeek().name());
               
               if(Appointments.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Monday"))
            	   weekCol = 1;
               else if(Appointments.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Tuesday"))
            	   weekCol = 2;
               else if(Appointments.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Wednesday"))
            	   weekCol = 3;
               else if(Appointments.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Thursday"))
            	   weekCol = 4;
               else if(Appointments.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Friday"))
            	   weekCol = 5;
               
               System.out.println("Week Column" + weekCol);
               
               Appointments.get(ctr).setColWeek(weekCol);
               
               weekModel.setValueAt(event, dayRow, weekCol);
               itemIndex.add(ctr);
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
    
    public DayTableRenderer getDayRenderer(){
        
    	ArrayList<Integer> startRowList = new ArrayList<Integer>();
    	ArrayList<Integer> endRowList = new ArrayList<Integer>();
    	ArrayList<Color> colorList = new ArrayList<Color>();
    	
//    	System.out.println("Number = " +itemIndex.size());
    	
    	for(int i = 0; i < itemIndex.size();i++){
    		startRowList.add(getAppointments().get(itemIndex.get(i)).getStartRowDay());
    		endRowList.add(getAppointments().get(itemIndex.get(i)).getEndRowDay());
    		colorList.add(getAppointments().get(itemIndex.get(i)).getColor());
    	}
    	
    	itemIndex.clear();
    	
    	return new DayTableRenderer(startRowList, endRowList,colorList);
    }
    
    public WeekTableRenderer getWeekRenderer(){
        
    	ArrayList<Integer> startRowList = new ArrayList<Integer>();
    	ArrayList<Integer> endRowList = new ArrayList<Integer>();
    	ArrayList<Color> colorList = new ArrayList<Color>();
    	ArrayList<Integer> columnList = new ArrayList<Integer>();
    	
    	
    	for(int i = 0; i < itemIndex.size();i++){
    		startRowList.add(getAppointments().get(itemIndex.get(i)).getStartRowDay());
    		endRowList.add(getAppointments().get(itemIndex.get(i)).getEndRowDay());
    		colorList.add(getAppointments().get(itemIndex.get(i)).getColor());
    		columnList.add(getAppointments().get(itemIndex.get(i)).getColWeek());
    	}
    	
    	itemIndex.clear();
    	
    	return new WeekTableRenderer(startRowList, endRowList,colorList, columnList);
    }

    public void addAppointment(LocalDate date, String event, LocalTime timeIn, LocalTime timeOut, int doctorID, String color) {
    	
    	Appointment wantedAppointment = new Appointment(Appointments.size(), event, color, date, timeIn, timeOut, doctorID);

		if(!checkForConflicts(wantedAppointment)) {
			System.out.println(checkForConflicts(wantedAppointment));
			System.out.println("**ADDED!**\n");
			getAppointments().add(wantedAppointment);
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
    
    public DefaultTableModel getCalendarModel(int month, int year) {
        this.updateTable(month, year);
        return calendarModel;
    }
    
    public DefaultTableModel getDayModel(LocalDate date){
    	this.updateDayTable(date);
    	return dayModel;
    }
    
	public DefaultTableModel getWeekModel(LocalDate date) {
		this.updateWeekTable(date);
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

	public ArrayList<Appointment> getAppointments() {
		return Appointments;
	}

	public void setAppointments(ArrayList<Appointment> Appointments) {
		this.Appointments = Appointments;
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
//			System.out.println(temp[i].toString());
			filtered.add(temp[i]);	
		}
		
		return filtered;	
	}
	
	public void sync(int i){
        for(; i < this.getAppointments().size() ; i++){
            appointmentManager.addAppointment(this.getAppointments().get(i));
        }
    }
}
