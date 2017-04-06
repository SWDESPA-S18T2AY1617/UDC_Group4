package control;

public class MainController
{
	private ClientController clientController;
	private DoctorController doctorController;
	private SecretaryController secretaryController;
	
	public MainController()
	{
		clientController = new ClientController();
	    doctorController = new DoctorController();
	    secretaryController = new SecretaryController();
	}
	
	
		
}
