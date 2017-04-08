package control;

import java.util.ArrayList;

import server.AppointmentManager;

public class MainController
{
	private AppointmentManager apptManager;
	private ArrayList<CalendarProgram> AllController = new ArrayList<CalendarProgram>();
	
	public MainController()
	{
		apptManager = new AppointmentManager();
		AllController.add(new DoctorController(apptManager));
		AllController.add(new ClientController(apptManager));
		AllController.add(new ClientController(apptManager));
		AllController.add(new DoctorController(apptManager));
		AllController.add(new SecretaryController(apptManager));
		
//		for(int ctr=0;ctr<AllController.size();ctr++){
//			if(AllController.get(ctr) instanceof DoctorController){
//				AllController.get(ctr).mainView.getCreateView().getBtnSave();
//			}
//		}
	}
	
	
	
	
		
}
