package control;

import java.util.ArrayList;

import server.ClientManager;
import view.CalendarProgram;
import view.ClientMainView;

public class ClientController
{
	private ClientManager clientManager;
	private ArrayList<CalendarProgram> calendarProgram;
	
	public ClientController()
	{
		clientManager = new ClientManager();
		calendarProgram = new ArrayList<CalendarProgram>();
		for(int ctr = 0; ctr<clientManager.getAllClient().size(); ctr++){
			calendarProgram.add(new CalendarProgram(new ClientMainView()));
		}
	}
	
}
