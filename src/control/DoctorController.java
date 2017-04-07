package control;

import java.util.ArrayList;

import server.DoctorManager;
import view.DoctorMainView;

public class DoctorController 
{
	private DoctorManager doctorManager;
	private ArrayList<CalendarProgram> calendarProgram;
	
	public DoctorController()
	{
		doctorManager = new DoctorManager();
		calendarProgram = new ArrayList<CalendarProgram>();
		int maxCtr = doctorManager.getAllDoctor().size();
		for(int ctr = 0; ctr < maxCtr; ctr++){
			calendarProgram.add(new CalendarProgram(new DoctorMainView()));
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
