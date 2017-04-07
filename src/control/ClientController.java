package control;

import java.util.ArrayList;

import server.ClientManager;
import view.ClientMainView;
import view.DoctorMainView;

public class ClientController extends CalendarProgram
{
	private ClientManager clientManager;
	
	public ClientController()
	{	
		super(new ClientMainView());
		clientManager = new ClientManager();
//		calendarProgram = new ArrayList<CalendarProgram>();
//		int maxCtr = clientManager.getAllClient().size();
//		for(int ctr = 0; ctr < maxCtr; ctr++){
//			calendarProgram.add(new CalendarProgram(new ClientMainView()));
//		}
	}
	

}
