package control;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import server.DoctorManager;
import view.DoctorMainView;

public class DoctorController extends CalendarProgram
{
	private DoctorManager doctorManager;
	private int col = -1;
	private int row = -1;
//	private ArrayList<CalendarProgram> calendarProgram;
	
	public DoctorController()
	{	
		super(new DoctorMainView());
		doctorManager = new DoctorManager();
//		calendarProgram = new ArrayList<CalendarProgram>();
//		int maxCtr = doctorManager.getAllDoctor().size();
//		for(int ctr = 0; ctr < maxCtr; ctr++){
//			calendarProgram.add(new CalendarProgram());
//		}
	}
	
}
