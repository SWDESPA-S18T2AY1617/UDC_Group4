package doctorView;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class doctorMainView extends JFrame {

	private DayView dayView;
	private AgendaView agendaView;
	private CalendarView calendarView;
	private TypeView typeView;
	private CreateView createView;
	private HeaderView headerView;
	private WeekView weekView;

	public doctorMainView() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(650, 530);
		getContentPane().setLayout(null);

		
		// initializing the panels
		JPanel mainPanel = new JPanel(null);
		getContentPane().add(mainPanel);
		setTitle("Doctor's Calendar");
		setCalendarView(new CalendarView());
		getCalendarView().getBtnCreate().setLocation(70, 180);
		setTypeView(new TypeView());
		setCreateView(new CreateView());
		setHeaderView(new HeaderView());
		setDayView(new DayView());
		setWeekView(new WeekView());
		setAgendaView(new AgendaView());
		
		// adding to the main panel
		mainPanel.add(getCalendarView());
		mainPanel.add(getTypeView());
		mainPanel.add(getCreateView());
		mainPanel.add(getHeaderView());
		mainPanel.add(getAgendaView());
		mainPanel.add(getWeekView());
		mainPanel.add(getDayView());

		// setting up the panels
		mainPanel.setBounds(0, 0, 650, 500);
		getHeaderView().setBounds(10, 0, 630, 100);
		getCalendarView().setBounds(10, 110, 230, 230);
		getTypeView().setBounds(20, 352, 100, 100);
		getCreateView().setBounds(240, 110, 400, 350);
		getAgendaView().setBounds(240, 110, 400, 350);
		getDayView().setBounds(240, 110, 400, 350);
		getWeekView().setBounds(240,110,400,350);

		// Panels
		//getDayView().setVisible(false);
		getCreateView().setVisible(false);
		getWeekView().setVisible(false);
		getAgendaView().setVisible(false);

		setResizable(false);
		setVisible(true);
	}

	public CalendarView getCalendarView() {
		return calendarView;
	}

	public CreateView getCreateView() {
		return createView;
	}

	public void setCreateView(CreateView createView) {
		this.createView = createView;
	}

	public void setCalendarView(CalendarView calendarView) {
		this.calendarView = calendarView;
	}

	public TypeView getTypeView() {
		return typeView;
	}

	public void setTypeView(TypeView typeView) {
		this.typeView = typeView;
	}

	public DayView getDayView() {
		return dayView;
	}

	public void setDayView(DayView dayView) {
		this.dayView = dayView;
	}

	public HeaderView getHeaderView() {
		return headerView;
	}

	public void setHeaderView(HeaderView headerView) {
		this.headerView = headerView;
	}

	public AgendaView getAgendaView() {
		return agendaView;
	}

	public void setAgendaView(AgendaView agendaView) {
		this.agendaView = agendaView;
	}

	public WeekView getWeekView() {
		return weekView;
	}

	public void setWeekView(WeekView weekView) {
		this.weekView = weekView;
	}

}