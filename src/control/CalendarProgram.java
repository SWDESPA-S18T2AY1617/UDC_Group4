package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

import java.time.LocalDate;
import java.time.LocalTime;

import model.Appointment;
import server.AppointmentManager;
import view.ClientMainView;
import view.DoctorMainView;
import view.MainView;
import view.SecretaryMainView;
import view.TableRenderer;

public class CalendarProgram {

	protected GregorianCalendar cal = new GregorianCalendar();
	protected int monthToday;
	protected int yearToday;
	protected int dayToday;
	protected int sIndex = 100;
	protected int yearBound;
	protected LocalDate date;
	protected int col = -1;
	protected int row = -1;
	
	protected MainView mainView;
	protected AppointmentHandler eventH;
	protected ArrayList<Appointment> sortedDay;
	protected ArrayList<Appointment> sortedWeek;

	public CalendarProgram(MainView mainView, AppointmentManager apptManager) {
		monthToday = cal.get(GregorianCalendar.MONTH);
		yearToday = cal.get(GregorianCalendar.YEAR);
		yearBound = cal.get(GregorianCalendar.YEAR);
		dayToday = cal.get(GregorianCalendar.DAY_OF_MONTH);
		date = LocalDate.of(yearToday, monthToday + 1, dayToday);
		this.mainView = mainView;
		
		sIndex = 100;
		this.eventH = new AppointmentHandler(apptManager);
		
		setFrame();

		this.mainView.setVisible(true);
		this.mainView.getCalendarView().getCalendarTable().setModel(eventH.getCalendarModel(monthToday, yearToday));
		refreshDay();
		refreshWeek();
		refreshdAgenda();
		refreshCalendar();
	}

	protected void setFrame() { // sets the values of the buttons in the frame

		for (int i = yearBound - 100; i <= yearBound + 100; i++) {
			mainView.getCalendarView().getCmbYear().addItem(String.valueOf(i)); // adds																			// box
		}

		if(mainView instanceof DoctorMainView)
			for (int i = 0; i < 24; i++) {
				mainView.getCreateView().getComboBoxFrom().addItem(i + ":00");
				mainView.getCreateView().getComboBoxFrom().addItem(i + ":30");
				mainView.getCreateView().getComboBoxTo().addItem(i + ":30");
				mainView.getCreateView().getComboBoxTo().addItem((i + 1) + ":00");
			}

		mainView.getCalendarView().getBtnPrev().addActionListener(new btnPrev_Action()); // adds
																							// action
																							// listener
		mainView.getCalendarView().getBtnNext().addActionListener(new btnNext_Action()); // adds
																							// action
																							// listener
		mainView.getCalendarView().getBtnCreate().addActionListener(new btnCreate_Action());
		mainView.getCalendarView().getCalendarTable().addMouseListener(new scrollPanelCal_Action());
		mainView.getHeaderView().getDayBtn().addActionListener(new btnDay_Action());
		mainView.getHeaderView().getWeekBtn().addActionListener(new btnWeek_Action());
		mainView.getHeaderView().getdAgendaBtn().addActionListener(new btndAgenda_Action());
		mainView.getHeaderView().getwAgendaBtn().addActionListener(new btnwAgenda_Action());
//		mainView.getCreateView().getrdBtnEvent().addActionListener(new rdBtnEvent_Action());
//		mainView.getCreateView().getrdBtnTask().addActionListener(new rdBtnTask_Action());
		if(mainView instanceof DoctorMainView)
			mainView.getCreateView().getBtnSave().addActionListener(new btnSave_Action());
//		mainView.getCreateView().getBtnDiscard().addActionListener(new btnDiscard_Action());
		mainView.getTypeView().getFreeCheckBox().addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (mainView.getTypeView().getFreeCheckBox().isSelected()) {
					if (mainView.getTypeView().getReservedCheckBox().isSelected()) {
						refreshdAgenda();
						refreshDay();
					} else {
						refreshEventAgenda();
						refreshDaySpecific("event");
					}
				} else if (!(mainView.getTypeView().getFreeCheckBox().isSelected())
						&& mainView.getTypeView().getReservedCheckBox().isSelected()) {
					refreshTaskAgenda();
					refreshDaySpecific("todo");
				} else if (!(mainView.getTypeView().getFreeCheckBox().isSelected())
						&& !(mainView.getTypeView().getReservedCheckBox().isSelected())) {
					refreshdAgenda();
					refreshDay();
				}
			}
		});
		mainView.getTypeView().getReservedCheckBox().addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if (mainView.getTypeView().getReservedCheckBox().isSelected()) {
					if (mainView.getTypeView().getFreeCheckBox().isSelected()) {
						refreshdAgenda();
						refreshDay();
					} else {
						refreshTaskAgenda();
						refreshDaySpecific("todo");
					}
				} else if (!(mainView.getTypeView().getReservedCheckBox().isSelected())
						&& mainView.getTypeView().getFreeCheckBox().isSelected()) {
					refreshEventAgenda();
					refreshDaySpecific("event");
				} else if (!(mainView.getTypeView().getFreeCheckBox().isSelected())
						&& !(mainView.getTypeView().getReservedCheckBox().isSelected())) {
					refreshdAgenda();
					refreshDay();
				}
			}
		});

		mainView.getCalendarView().getCmbYear().addActionListener(new cmbYear_Action());

		refreshdAgenda();
		refreshDay();
		refreshwAgenda();
		refreshWeek();
	}

	protected void refreshCalendar() { // sets the view based on the current month
										// and year
		String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };

		mainView.getHeaderView().getDateLabel().setText(months[monthToday] + " " + dayToday + ", " + yearToday);
		mainView.getCalendarView().getBtnPrev().setEnabled(true); // enables the previous button
		mainView.getCalendarView().getBtnNext().setEnabled(true); // enables the next button 
		if (monthToday == 0 && yearToday <= yearBound - 100)
			mainView.getCalendarView().getBtnPrev().setEnabled(false); // disables the previous button
		if (monthToday == 11 && yearToday >= yearBound + 100)
			mainView.getCalendarView().getBtnNext().setEnabled(false); // disables the next button 

		mainView.getCalendarView().getMonthLabel().setText(months[monthToday]); // changes the month label based on the current month 
		mainView.getCalendarView().getMonthLabel().setBounds(20, 25, 100, 30);

		mainView.getCalendarView().getCmbYear().setSelectedIndex(sIndex); // changes  the selected index in the combo box based on the current year 
		mainView.getCalendarView().getCalendarTable().setModel(eventH.getCalendarModel(monthToday, yearToday)); // updates the model being used 
		mainView.getCalendarView().getCalendarTable().setDefaultRenderer(
				mainView.getCalendarView().getCalendarTable().getColumnClass(0), new TableRenderer()); // updates the renderer used to change the color of a cell 
	}

	protected void refreshDay() {

		mainView.getDayView().getDayTable().setModel(eventH.getDayModel(getDate()));
		mainView.getDayView().getDayTable().setDefaultRenderer(mainView.getDayView().getDayTable().getColumnClass(0),
				eventH.getDayRenderer());
	}

	protected void refreshDaySpecific(String style) {
		mainView.getDayView().getDayTable().setModel(eventH.getEventDayModel(getDate()));
		mainView.getDayView().getDayTable().setDefaultRenderer(mainView.getDayView().getDayTable().getColumnClass(0),
				eventH.getDayRenderer());
	}
	
	protected void refreshWeek() {
		mainView.getWeekView().getWeekTable().setModel(eventH.getWeekModel(getDate()));
		mainView.getWeekView().getWeekTable().setDefaultRenderer(mainView.getWeekView().getWeekTable().getColumnClass(0), eventH.getWeekRenderer());
	}

	protected void refreshdAgenda() {
		mainView.getAgendaView().getLblEventName().setText("No Upcoming Events");
		mainView.getAgendaView().getLblEventTime().setText("");

		sortedDay = eventH.getDayEvents(getDate());

		for (int ctr = 0; ctr < sortedDay.size(); ctr++) {
			if (sortedDay.get(ctr).checkSameDate(getDate()) == 0) {
				if (mainView.getAgendaView().getLblEventName().getText().equalsIgnoreCase("No Upcoming Events")) {
					mainView.getAgendaView().getLblEventName().setText("<html><font color='"
							+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getAppointmentName() + "</font>");
					mainView.getAgendaView().getLblEventTime().setText("<html><font color='"
							+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getLocalTimeIn() + "-" + sortedDay.get(ctr).getLocalTimeOut() + "</font>");
				} else {
					mainView.getAgendaView().getLblEventName()
							.setText(mainView.getAgendaView().getLblEventName().getText() + "<br><font color='"
									+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getAppointmentName() + "</font>");
					mainView.getAgendaView().getLblEventTime()
							.setText(mainView.getAgendaView().getLblEventTime().getText() + "<br><font color='"
									+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getLocalTimeIn() + "-" + sortedDay.get(ctr).getLocalTimeOut() + "</font>");
				}
			}
		}
	}
	
	protected void refreshwAgenda(){
		mainView.getWeekAgendaView().getLblEventName().setText("No Upcoming Events");
		mainView.getWeekAgendaView().getLblEventTime().setText("");

		sortedWeek = eventH.getWeekEvents(getDate());
		
		for (int ctr = 0; ctr < sortedWeek.size(); ctr++) {
			System.out.println("My Size" + sortedWeek.size());
			if (sortedWeek.get(ctr).checkSameDate(getDate()) == 0) {
				if (mainView.getWeekAgendaView().getLblEventName().getText().equalsIgnoreCase("No Upcoming Events")) {
					mainView.getWeekAgendaView().getLblEventName().setText("<html><font color='"
							+ sortedWeek.get(ctr).getColorName() + "'>" + sortedWeek.get(ctr).getAppointmentName() + "</font>");
					mainView.getWeekAgendaView().getLblEventTime().setText("<html><font color='"
							+ sortedWeek.get(ctr).getColorName() + "'>" + sortedWeek.get(ctr).getLocalTimeIn() + "-" + sortedWeek.get(ctr).getLocalTimeOut() + "</font>");
				} else {
					mainView.getWeekAgendaView().getLblEventName()
							.setText(mainView.getWeekAgendaView().getLblEventName().getText() + "<br><font color='"
									+ sortedWeek.get(ctr).getColorName() + "'>" + sortedWeek.get(ctr).getAppointmentName() + "</font>");
					mainView.getWeekAgendaView().getLblEventTime()
							.setText(mainView.getWeekAgendaView().getLblEventTime().getText() + "<br><font color='"
									+ sortedWeek.get(ctr).getColorName() + "'>" + sortedWeek.get(ctr).getLocalTimeIn() + "-" + sortedWeek.get(ctr).getLocalTimeOut() + "</font>");
	
				}
			}
		}
	}	
	
	protected void refreshEventAgenda() {
		mainView.getAgendaView().getLblEventName().setText("No Upcoming Events");
		mainView.getAgendaView().getLblEventTime().setText("");

		sortedDay = eventH.getDayEvents(getDate());

		for (int ctr = 0; ctr < sortedDay.size(); ctr++) {
//			if (!(sortedDay.get(ctr) instanceof ToDo) && dayToday == sortedDay.get(ctr).getDay()
//					&& (monthToday + 1) == sortedDay.get(ctr).getMonth() && yearToday == sortedDay.get(ctr).getYear()) {
//				if (mainView.getAgendaView().getLblEventName().getText().equalsIgnoreCase("No Upcoming Events")) {
//					mainView.getAgendaView().getLblEventName().setText("<html><font color='"
//							+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getEvent());
//					mainView.getAgendaView().getLblEventTime().setText("<html><font color='"
//							+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getShour() + ":");
//				} else {
//					mainView.getAgendaView().getLblEventName()
//							.setText(mainView.getAgendaView().getLblEventName().getText() + "<br><font color='"
//									+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getEvent());
//					mainView.getAgendaView().getLblEventTime()
//							.setText(mainView.getAgendaView().getLblEventTime().getText() + "<br><font color='"
//									+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getShour() + ":");
//				}
//
//				if (sortedDay.get(ctr).getSminute() < 10)
//					mainView.getAgendaView().getLblEventTime()
//							.setText(mainView.getAgendaView().getLblEventTime().getText() + "0"
//									+ Integer.toString(sortedDay.get(ctr).getSminute()));
//				else
//					mainView.getAgendaView().getLblEventTime()
//							.setText(mainView.getAgendaView().getLblEventTime().getText()
//									+ Integer.toString(sortedDay.get(ctr).getSminute()));
//
//				mainView.getAgendaView().getLblEventTime().setText(mainView.getAgendaView().getLblEventTime().getText()
//						+ "-" + sortedDay.get(ctr).getEhour() + ":");
//
//				if (sortedDay.get(ctr).getEminute() < 10)
//					mainView.getAgendaView().getLblEventTime()
//							.setText(mainView.getAgendaView().getLblEventTime().getText() + "0"
//									+ Integer.toString(sortedDay.get(ctr).getEminute()));
//				else
//					mainView.getAgendaView().getLblEventTime()
//							.setText(mainView.getAgendaView().getLblEventTime().getText()
//									+ Integer.toString(sortedDay.get(ctr).getEminute()));
//
//				mainView.getAgendaView().getLblEventName()
//						.setText(mainView.getAgendaView().getLblEventName().getText() + "</font>");
//				mainView.getAgendaView().getLblEventTime()
//						.setText(mainView.getAgendaView().getLblEventTime().getText() + "</font>");
//
//			}
		}
	}

	protected void refreshTaskAgenda() {
		mainView.getAgendaView().getLblEventName().setText("No Upcoming Events");
		mainView.getAgendaView().getLblEventTime().setText("");

		sortedDay = eventH.getDayEvents(getDate());

		for (int ctr = 0; ctr < sortedDay.size(); ctr++) {
//			if (sortedDay.get(ctr) instanceof ToDo && dayToday == sortedDay.get(ctr).getDay()
//					&& (monthToday + 1) == sortedDay.get(ctr).getMonth() && yearToday == sortedDay.get(ctr).getYear()) {
//				if (mainView.getAgendaView().getLblEventName().getText().equalsIgnoreCase("No Upcoming Events")) {
//					mainView.getAgendaView().getLblEventName().setText("<html><font color='"
//							+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getEvent());
//					mainView.getAgendaView().getLblEventTime().setText("<html><font color='"
//							+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getShour() + ":");
//				} else {
//					mainView.getAgendaView().getLblEventName()
//							.setText(mainView.getAgendaView().getLblEventName().getText() + "<br><font color='"
//									+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getEvent());
//					mainView.getAgendaView().getLblEventTime()
//							.setText(mainView.getAgendaView().getLblEventTime().getText() + "<br><font color='"
//									+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getShour() + ":");
//				}
//
//				if (sortedDay.get(ctr).getSminute() < 10)
//					mainView.getAgendaView().getLblEventTime()
//							.setText(mainView.getAgendaView().getLblEventTime().getText() + "0"
//									+ Integer.toString(sortedDay.get(ctr).getSminute()));
//				else
//					mainView.getAgendaView().getLblEventTime()
//							.setText(mainView.getAgendaView().getLblEventTime().getText()
//									+ Integer.toString(sortedDay.get(ctr).getSminute()));
//
//				mainView.getAgendaView().getLblEventTime().setText(mainView.getAgendaView().getLblEventTime().getText()
//						+ "-" + sortedDay.get(ctr).getEhour() + ":");
//
//				if (sortedDay.get(ctr).getEminute() < 10)
//					mainView.getAgendaView().getLblEventTime()
//							.setText(mainView.getAgendaView().getLblEventTime().getText() + "0"
//									+ Integer.toString(sortedDay.get(ctr).getEminute()));
//				else
//					mainView.getAgendaView().getLblEventTime()
//							.setText(mainView.getAgendaView().getLblEventTime().getText()
//									+ Integer.toString(sortedDay.get(ctr).getEminute()));
//
//				mainView.getAgendaView().getLblEventName()
//						.setText(mainView.getAgendaView().getLblEventName().getText() + "</font>");
//				mainView.getAgendaView().getLblEventTime()
//						.setText(mainView.getAgendaView().getLblEventTime().getText() + "</font>");
//
//			}
		}
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	//////////////////////////// ACTION LISTENERS////////////////////////////
	protected class btnPrev_Action implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			dayToday = 1;
			if (monthToday == 0) {
				monthToday = 11;
				yearToday -= 1;
				sIndex--;
				mainView.getCalendarView().getCmbYear().setSelectedIndex(sIndex);
			} else {
				monthToday -= 1;
				sIndex = mainView.getCalendarView().getCmbYear().getSelectedIndex();
			}
			
			date = LocalDate.of(yearToday, monthToday + 1, dayToday);
			
			refreshCalendar();
			refreshdAgenda();
			refreshDay();
			refreshwAgenda();
			refreshWeek();
		}
	}

	protected class btnNext_Action implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			dayToday = 1;
			if (monthToday == 11) {
				monthToday = 0;
				yearToday += 1;
				sIndex++;
				mainView.getCalendarView().getCmbYear().setSelectedIndex(sIndex);
			} else {
				monthToday += 1;
				sIndex = mainView.getCalendarView().getCmbYear().getSelectedIndex();
			}

			date = LocalDate.of(yearToday, monthToday + 1, dayToday);
			
			refreshCalendar();
			refreshdAgenda();
			refreshDay();
			refreshwAgenda();
			refreshWeek();
		}
	}

	protected class btnCreate_Action implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			mainView.getDayView().setVisible(false);
			mainView.getAgendaView().setVisible(false);
			mainView.getWeekView().setVisible(false);
			mainView.getWeekAgendaView().setVisible(false);
			mainView.getCreateView().setVisible(true);
			
		}
	}

	protected class btnDay_Action implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(mainView instanceof DoctorMainView)
				mainView.getCreateView().setVisible(false);
			mainView.getAgendaView().setVisible(false);
			mainView.getWeekAgendaView().setVisible(false);
			mainView.getWeekView().setVisible(false);
			mainView.getDayView().setVisible(true);
		}
	}
	
	protected class btnWeek_Action implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(mainView instanceof DoctorMainView)
				mainView.getCreateView().setVisible(false);
			mainView.getDayView().setVisible(false);
			mainView.getWeekAgendaView().setVisible(false);
			mainView.getAgendaView().setVisible(false);
			mainView.getWeekView().setVisible(true);
		}
	}

	protected class btndAgenda_Action implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(mainView instanceof DoctorMainView)
				mainView.getCreateView().setVisible(false);
			mainView.getAgendaView().setVisible(true);
			mainView.getWeekAgendaView().setVisible(false);
			mainView.getWeekView().setVisible(false);
			mainView.getDayView().setVisible(false);
		}
	}
	
	protected class btnwAgenda_Action implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(mainView instanceof DoctorMainView)
				mainView.getCreateView().setVisible(false);
			mainView.getAgendaView().setVisible(false);
			mainView.getWeekAgendaView().setVisible(true);
			mainView.getWeekView().setVisible(false);
			mainView.getDayView().setVisible(false);
		}
	}

	protected class scrollPanelCal_Action implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
					"October", "November", "December" };

			col = mainView.getCalendarView().getCalendarTable().getSelectedColumn();
			row = mainView.getCalendarView().getCalendarTable().getSelectedRow();
			if (mainView.getCalendarView().getCalendarTable().getValueAt(row, col) != null)
				dayToday = (Integer) mainView.getCalendarView().getCalendarTable().getValueAt(row, col);
			
			date = LocalDate.of(yearToday, monthToday + 1, dayToday);
			
			mainView.getHeaderView().getDateLabel().setText(months[monthToday] + " " + dayToday + ", " + yearToday);
			if(mainView instanceof DoctorMainView)
				mainView.getCreateView().getTextFieldDate().setText((monthToday+1) + "/" + dayToday + "/" + yearToday);
			refreshdAgenda();
			refreshDay();
			refreshWeek();
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {}
		@Override
		public void mouseExited(MouseEvent arg0) {}
		@Override
		public void mousePressed(MouseEvent arg0) {}
		@Override
		public void mouseReleased(MouseEvent arg0) {}

	}
	
	protected class cmbYear_Action implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (mainView.getCalendarView().getCmbYear().getSelectedItem() != null) {
				String selectedYear = mainView.getCalendarView().getCmbYear().getSelectedItem().toString();
				yearToday = Integer.parseInt(selectedYear);
				sIndex = mainView.getCalendarView().getCmbYear().getSelectedIndex();
				refreshCalendar();
				refreshdAgenda();
				refreshDay();
			}
		}
	}

	protected class btnSave_Action implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {

			int i = 0;
			int startIndex  = eventH.getAppointments().size();
			
			String[] dates = mainView.getCreateView().getTextFieldDate().getText().split("/");
			
			date = LocalDate.of(Integer.parseInt(dates[2]), Integer.parseInt(dates[0]), Integer.parseInt(dates[1]));
			
			String[] stime = mainView.getCreateView().getComboBoxFrom().getSelectedItem().toString().split(":");
			int shour = Integer.parseInt(stime[0]);
			int sminute = Integer.parseInt(stime[1]);
			String[] etime = mainView.getCreateView().getComboBoxTo().getSelectedItem().toString().split(":");
			int ehour = Integer.parseInt(etime[0]);
			int eminute = Integer.parseInt(etime[1]);
			
			if(mainView.getCreateView().getWeeklyCheckBox().isSelected()){
				for(int ctr=date.getDayOfYear(); ctr<365;ctr++){
					if(LocalDate.ofYearDay(date.getYear(), ctr).getDayOfWeek().name().equalsIgnoreCase(date.getDayOfWeek().name())){
						eventH.addAppointment(LocalDate.ofYearDay(date.getYear(), ctr), "Appointment " + (eventH.getAppointments().size() + 1), LocalTime.of(shour, sminute), LocalTime.of(ehour, eminute), mainView.getAppID(), "Red");
						System.out.println("Day of Event" + LocalDate.ofYearDay(date.getYear(), ctr));
						System.out.println("Added");
					}
				}
			}
			else
				eventH.addAppointment(getDate(), "Appointment " + (eventH.getAppointments().size() + 1), LocalTime.of(shour, sminute), LocalTime.of(ehour, eminute), mainView.getAppID(), "Red");
			

			eventH.sync(startIndex);
			
			refreshDay();
			refreshdAgenda();
			refreshWeek();
		
//			for (i = 0; i < eventH.getAppointments().size(); i++) {
//				if (year == eventH.getAppointments().get(i).getYear()
//						&& month == eventH.getAppointments().get(i).getMonth()
//						&& day == eventH.getAppointments().get(i).getDay()
//						&& ((shour == eventH.getAppointments().get(i).getShour()
//								&& sminute == eventH.getAppointments().get(i).getSminute())
//								|| (ehour == eventH.getAppointments().get(i).getEhour()
//										&& eminute == eventH.getAppointments().get(i).getEminute())
//								|| ((shour >= eventH.getAppointments().get(i).getShour())
//										&& (shour <= eventH.getAppointments().get(i).getEhour())
//										|| (ehour >= eventH.getAppointments().get(i).getShour())
//												&& (ehour <= eventH.getAppointments().get(i).getEhour()))
//								|| ((eventH.getAppointments().get(i).getShour() >= shour)
//										&& (eventH.getAppointments().get(i).getShour() <= ehour)
//										|| (eventH.getAppointments().get(i).getEhour() >= shour)
//												&& (eventH.getAppointments().get(i).getEhour() <= ehour)))) {
//					JOptionPane.showMessageDialog(null, "Conflict arose.");
//					break;
//				}
//			}

//			if (i == eventH.getAppointments().size()) {
//				eventH.addCalendarItem(year, month, day, "",shour, sminute, ehour, eminute, row);
//
//				JOptionPane.showMessageDialog(null, "Done Setting for Appointments!");
//
//				refreshAgenda();
//				refreshDay();
//			}

			mainView.getCreateView().getTextFieldDate().setText("");
			mainView.getCreateView().getComboBoxFrom().setSelectedIndex(0);
			mainView.getCreateView().getComboBoxTo().setSelectedIndex(0);

		}
	}
	public AppointmentHandler getAppointmentHandler(){
		return eventH;
	}
	
	public void refresh(ArrayList<Appointment> appointments)
	{
		eventH.setAppointments(appointments);
		
		refreshDay();
		refreshdAgenda();
		refreshWeek();
	}
	
//	protected class btnDiscard_Action implements ActionListener {
//		public void actionPerformed(ActionEvent e) {
//			mainView.getCreateView().getTextFieldDate().setText("");
//			mainView.getCreateView().getTextFieldEvent().setText("");
//			mainView.getCreateView().getComboBoxFrom().setSelectedIndex(0);
//			mainView.getCreateView().getComboBoxTo().setSelectedIndex(0);
//		}
//	}

}
