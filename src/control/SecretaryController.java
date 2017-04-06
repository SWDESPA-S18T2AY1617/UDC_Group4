package control;

import java.util.ArrayList;

import server.SecretaryManager;
import view.CalendarProgram;
import view.SecretaryMainView;

public class SecretaryController {

	private SecretaryManager secretaryManager;
	private ArrayList<CalendarProgram> calendarProgram;
	
	public SecretaryController()
	{
		secretaryManager = new SecretaryManager();
		calendarProgram = new ArrayList<CalendarProgram>();
		for(int ctr = 0; ctr<secretaryManager.getAllSecretary().size(); ctr++){
			calendarProgram.add(new CalendarProgram(new SecretaryMainView()));
		}
		
	}

}
