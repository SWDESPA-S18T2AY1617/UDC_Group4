package control;

import java.util.ArrayList;

import server.SecretaryManager;
import view.SecretaryMainView;

public class SecretaryController 
{
	private SecretaryManager secretaryManager;
	private ArrayList<CalendarProgram> calendarProgram;
	
	public SecretaryController()
	{
		secretaryManager = new SecretaryManager();
		setCalendarProgram(new ArrayList<CalendarProgram>());
		int maxCtr = secretaryManager.getAllSecretary().size();
		for(int ctr = 0; ctr < maxCtr; ctr++){
			getCalendarProgram().add(new CalendarProgram(new SecretaryMainView()));
		}	
	}
	
	public CalendarProgram getSpecificCalendarProgram(int value)
	{
		return calendarProgram.get(value);
	}

	public ArrayList<CalendarProgram> getCalendarProgram() {
		return calendarProgram;
	}

	public void setCalendarProgram(ArrayList<CalendarProgram> calendarProgram) {
		this.calendarProgram = calendarProgram;
	}
}
