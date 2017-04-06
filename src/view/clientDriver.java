package view;

import java.util.ArrayList;

public class clientDriver {
	public static void main(String[] args ){
		new CalendarProgram();
		ArrayList<MainView> views = new ArrayList<MainView>();
		views.add(new DoctorMainView());
		views.add(new DoctorMainView());
		views.add(new ClientMainView());
		views.add(new ClientMainView());
		views.add(new SecretaryMainView());
	}
}
