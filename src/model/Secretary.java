package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import javax.swing.table.DefaultTableModel;

public class Secretary extends Person{

    private ArrayList<Appointment> Appointments = new ArrayList<Appointment>();
    private ArrayList<Integer> itemIndex = new ArrayList<Integer>();
    private DTModel calendarModel = new DTModel();
    private DayModel dayModel = new DayModel();
    private Doctor Doctors;
    
    public final static String TABLE_NAME = "secretary";
	public final static String COL_ID = "idsecretary";
	public final static String COL_NAME = "name";

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
    
    private void updateDayTable(int month, int year,int day)
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
        	
        	if (Appointments.get(ctr).checkSameDate(year, month+1, day)) {
               index = ctr;
               event = Appointments.get(index).getEvent();
               dayRow = Appointments.get(ctr).getShour() * 2;
               if(Appointments.get(ctr).getSminute() == 30)
                   dayRow++;
               
               dayModel.setValueAt(event, dayRow, 1);
               itemIndex.add(ctr);
            }
        	
        }
            
    }
    
    private void updateDayTableSpecific(int month, int year,int day, String style)
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
        	
        	if (Appointments.get(ctr).checkSameDate(year, month+1, day)) {
               index = ctr;
               event = Appointments.get(index).getEvent();
               dayRow = Appointments.get(ctr).getShour() * 2;
               if(Appointments.get(ctr).getSminute() == 30)
                   dayRow++;
               
             /*  if(style.equalsIgnoreCase("todo") && Appointments.get(ctr) instanceof ToDo){
            	   dayModel.setValueAt(event, dayRow, 1);
            	   itemIndex.add(ctr);
               }
            	   
               else if(style.equalsIgnoreCase("event") && !(Appointments.get(ctr) instanceof ToDo)){
            	   dayModel.setValueAt(event, dayRow, 1);
            	   itemIndex.add(ctr);
               }*/ // no need for ToDo
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
    		startRowList.add(getAppointments().get(itemIndex.get(i)).getStartRow());
    		endRowList.add(getAppointments().get(itemIndex.get(i)).getEndRow());
    		colorList.add(getAppointments().get(itemIndex.get(i)).getColor());
    	}
    	
    	itemIndex.clear();
    	
    	return new DayTableRenderer(startRowList, endRowList, colorList);
    }

    public void addAppointment(int year, int month, int day, String event, int SHour, int SMinute, int EHour, int EMinute, int row) {
        row = SHour * 2;
        if(SMinute == 30)
        	row++;
    	this.getAppointments().add(new Appointment(year, month, day, event, "BLUE", SHour, SMinute, EHour, EMinute));
    }
    
    public DefaultTableModel getCalendarModel(int month, int year) {
        this.updateTable(month, year);
        return calendarModel;
    }
    
    public DefaultTableModel getDayModel(int month, int year, int day){
    	this.updateDayTable(month, year, day);
    	return dayModel;
    }
    
    public DefaultTableModel getEventDayModel(int month, int year, int day, String style)
    {
    	this.updateDayTableSpecific(month, year, day, style);
    	
    	return dayModel;
    }
    
	/* public void load(){
    	Reader temp = new CSVReader();
    	temp.Read();
    	
    	Appointments.addAll(temp.getEvents());
    }*/ //legacy loader

	public ArrayList<Appointment> getAppointments() {
		return Appointments;
	}

	public void setAppointments(ArrayList<Appointment> Appointments) {
		this.Appointments = Appointments;
	}
	
	public ArrayList<Appointment> getDayEvents(int m, int d, int y){
		ArrayList<Appointment> filtered = new ArrayList<Appointment>();
		for(int i = 0;i<Appointments.size();i++){
			if(Appointments.get(i).getDay()==d && Appointments.get(i).getYear()==y && Appointments.get(i).getMonth()==m)
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
				if(temp[j].getShour()>temp[j+1].getShour()){
					temp2 = temp[j];
					temp[j]= temp[j+1];
					temp[j+1]= temp2;
				}
			
				if(temp[j].getShour()==temp[j+1].getShour()){
					if(temp[j].getSminute()>temp[j+1].getSminute()){
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
	
	/*public String getdata(){ //legacy csv reader
		StringBuilder sb = new StringBuilder();
        ArrayList<Appointment> a = Appointments;
        for(int j=0; j<a.size();j++)
        {
        	if(a.get(j) instanceof ToDo){
        		sb.append("to do");
        	}else
        		sb.append("event");
        	sb.append(',');
        	sb.append(' ');
        	if(a.get(j) instanceof ToDo){
        		sb.append(a.get(j).getMonth());
        	}else
        		sb.append(a.get(j).getMonth());
			sb.append('/');
			sb.append(a.get(j).getDay());
			sb.append('/');
			sb.append(a.get(j).getYear());
	        sb.append(',');
			sb.append(' ');
	        sb.append(a.get(j).getEvent());
			sb.append(',');
			sb.append(' ');
	        sb.append(a.get(j).getColorName());
			sb.append(',');
			sb.append(' ');
			sb.append(a.get(j).getShour());
			sb.append(':');
			sb.append(' ');
			sb.append(a.get(j).getSminute());
			sb.append(',');
			sb.append(' ');
			sb.append(a.get(j).getEhour());
	        sb.append(':');
			sb.append(' ');
	        sb.append(a.get(j).getEminute()); 
			if(a.get(j) instanceof ToDo){
				sb.append(',');
				sb.append(' ');
				if(((ToDo)a.get(j)).isDone())
					sb.append("yes");
				else
					sb.append("no");
			}
	        sb.append('\n');
		}
		return sb.toString();
	}*/
    
}
