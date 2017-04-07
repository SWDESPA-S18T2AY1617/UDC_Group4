package control;

import java.util.ArrayList;

public class MainController
{
	private AppointmentController appointmentController;
	private ArrayList<CalendarProgram> AllController = new ArrayList<CalendarProgram>();
	
	public MainController()
	{
		AllController.add(new DoctorController());
		AllController.add(new ClientController());
		AllController.add(new ClientController());
		AllController.add(new DoctorController());
		AllController.add(new SecretaryController());
		appointmentController = new AppointmentController();
	}
	
	
	
	
		
}
