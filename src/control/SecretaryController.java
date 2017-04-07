package control;

import java.util.ArrayList;

import server.SecretaryManager;
import view.DoctorMainView;
import view.SecretaryMainView;

public class SecretaryController extends CalendarProgram
{
	private SecretaryManager secretaryManager;
	
	public SecretaryController()
	{
		super(new SecretaryMainView());
		secretaryManager = new SecretaryManager();
//		setCalendarProgram(new ArrayList<CalendarProgram>());
//		int maxCtr = secretaryManager.getAllSecretary().size();
//		for(int ctr = 0; ctr < maxCtr; ctr++){
//			getCalendarProgram().add(new CalendarProgram(new SecretaryMainView()));
//		}	
	}
	
}
