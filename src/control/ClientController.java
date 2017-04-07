package control;

import java.util.ArrayList;

import server.ClientManager;
import view.ClientMainView;

public class ClientController
{
	private ClientManager clientManager;
	private ArrayList<CalendarProgram> calendarProgram;
	
	public ClientController()
	{
		clientManager = new ClientManager();
		calendarProgram = new ArrayList<CalendarProgram>();
		int maxCtr = clientManager.getAllClient().size();
		for(int ctr = 0; ctr < maxCtr; ctr++){
			calendarProgram.add(new CalendarProgram(new ClientMainView()));
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
