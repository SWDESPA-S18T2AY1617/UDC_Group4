package view;

import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class MainView extends JFrame {

	private DayView dayView;
	private AgendaView agendaView;
	private CalendarView calendarView;
	private TypeView typeView;
	private CreateView createView;
	private HeaderView headerView;
	private WeekView weekView;
	
	public abstract void mainView();
	
	public CalendarView getCalendarView() {
		return calendarView;
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

	public CreateView getCreateView() {
		return createView;
	}

	public void setCreateView(CreateView createView) {
		this.createView = createView;
	}

}

