package view;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DoctorMainView extends MainView{

	private static int appIDTracker = 0;
	
	public DoctorMainView() {
		
		setAppID(++appIDTracker);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(650, 530);
		getContentPane().setLayout(null);

		
		// initializing the panels
		JPanel mainPanel = new JPanel(null);
		getContentPane().add(mainPanel);
		setTitle("Doctor's Calendar #" + getAppID());
		setCalendarView(new CalendarView());
		getCalendarView().getBtnCreate().setLocation(70, 180);
		setTypeView(new TypeView());
		setCreateView(new CreateView());
		setHeaderView(new HeaderView());
		setDayView(new DayView());
		setWeekView(new WeekView());
		setWeekAgendaView(new WeekAgendaView());
		setAgendaView(new AgendaView());
		
		// adding to the main panel
		mainPanel.add(getCalendarView());
		mainPanel.add(getTypeView());
		mainPanel.add(getCreateView());
		mainPanel.add(getHeaderView());
		mainPanel.add(getAgendaView());
		mainPanel.add(getWeekView());
		mainPanel.add(getDayView());
		mainPanel.add(getWeekAgendaView());

		// setting up the panels
		mainPanel.setBounds(0, 0, 650, 500);
		getHeaderView().setBounds(10, 0, 630, 100);
		getCalendarView().setBounds(10, 110, 230, 230);
		getTypeView().setBounds(20, 352,  120, 150);
		getCreateView().setBounds(240, 110, 400, 350);
		getAgendaView().setBounds(240, 110, 400, 350);
		getDayView().setBounds(240, 110, 400, 350);
		getWeekView().setBounds(240,110,400,350);
		getWeekAgendaView().setBounds(240,110,400,350);
		
		//Modify necessary objects
		getTypeView().getFreeCheckBox().setText("Free");
		getTypeView().getReservedCheckBox().setText("Reserved");
		// Panels
		getCreateView().setVisible(false);
		getWeekView().setVisible(false);
		getAgendaView().setVisible(false);
		getWeekAgendaView().setVisible(false);
		getTypeView().getComboBox().setVisible(false);

		setResizable(false);
		setVisible(true);
	}
}	