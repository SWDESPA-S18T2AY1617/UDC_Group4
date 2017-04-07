package view;

import java.awt.Color;
import java.sql.Date;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import javax.swing.table.DefaultTableModel;

import model.Appointment;

public class AppointmentHandler {

    private ArrayList<Appointment> Appointments = new ArrayList<Appointment>();
    private ArrayList<Integer> itemIndex = new ArrayList<Integer>();
    private DTModel calendarModel = new DTModel();
    private WeekModel weekModel = new WeekModel();
    private DayModel dayModel = new DayModel();

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
    
    private void updateDayTable(Date date)
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
        	
        	if (Appointments.get(ctr).checkSameDate(date)) {
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
    
    private void updateWeekTable(Date date)
    {
        int index = -1; //index of the event found
        
        for (int j = 0; j < 48; j++) {
        	if(j%2==0)
        		weekModel.setValueAt(j/2+":00", j, 0);
        	weekModel.setValueAt(null, j, 1);
        }
        
        int weekRow = 0;
        String event = "";
        
        for (int ctr = 0; ctr < Appointments.size(); ctr++) { //searches for the events for this month and year
        	
        	if (Appointments.get(ctr).checkSameDate(date)) {
               index = ctr;
               event = Appointments.get(index).getAppointmentName();
               weekRow = Appointments.get(ctr).getLocalTimeIn().getHour() * 2;
               if(Appointments.get(ctr).getLocalTimeIn().getMinute() == 30)
                   weekRow++;
               
               weekModel.setValueAt(event, weekRow, 1);
               itemIndex.add(ctr);
            }
        	
        }
            
    }
    
    private void updateDayTableSpecific(Date date)
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
        	
        	if (Appointments.get(ctr).checkSameDate(date)) {
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
    	
//    	System.out.println("Number = " +itemIndex.size());
    	
    	for(int i = 0; i < itemIndex.size();i++){
    		startRowList.add(getAppointments().get(itemIndex.get(i)).getStartRowDay());
    		endRowList.add(getAppointments().get(itemIndex.get(i)).getEndRowDay());
    	}
    	
    	itemIndex.clear();
    	
    	return new DayTableRenderer(startRowList, endRowList);
    }

    public void addAppointment(Date date, String event, LocalTime timeIn, LocalTime timeOut, int doctorID) {
    	this.getAppointments().add(new Appointment(Appointments.size(),  event, date, timeIn, timeOut, doctorID));
    }
    
    public DefaultTableModel getCalendarModel(int month, int year) {
        this.updateTable(month, year);
        return calendarModel;
    }
    
    public DefaultTableModel getDayModel(Date date){
    	this.updateDayTable(date);
    	return dayModel;
    }
    
	public DefaultTableModel getWeekModel(Date date) {
		this.updateWeekTable(date);
		return weekModel;
	}

	public void setWeekModel(WeekModel weekModel) {
		this.weekModel = weekModel;
	}
	
    public DefaultTableModel getEventDayModel(Date date)
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
	
	public ArrayList<Appointment> getDayEvents(Date date){
		ArrayList<Appointment> filtered = new ArrayList<Appointment>();
		for(int i = 0;i<Appointments.size();i++){
			if(Appointments.get(i).checkSameDate(date))
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
}
