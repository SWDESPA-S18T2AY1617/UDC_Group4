package control;

import java.util.ArrayList;

public class MainController
{
	private ArrayList<CalendarProgram> AllController = new ArrayList<CalendarProgram>();
	
	public MainController()
	{
		AllController.add(new DoctorController());
		AllController.add(new ClientController());
		AllController.add(new ClientController());
		AllController.add(new DoctorController());
		AllController.add(new SecretaryController());
		
//		for(int ctr=0;ctr<AllController.size();ctr++){
//			if(AllController.get(ctr) instanceof DoctorController){
//				AllController.get(ctr).mainView.getCreateView().getBtnSave();
//			}
//		}
	}
	
	
	
	
		
}
