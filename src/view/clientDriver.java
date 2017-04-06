package view;

import java.util.ArrayList;

public class clientDriver {
	public static void main(String[] args ){
		new CalendarProgram();
		ArrayList<MainView> views = new ArrayList<MainView>();
		views.add(new doctorMainView());
		views.add(new doctorMainView());
		views.add(new clientMainView());
		views.add(new clientMainView());
		views.add(new secretaryMainView());
	}
}
