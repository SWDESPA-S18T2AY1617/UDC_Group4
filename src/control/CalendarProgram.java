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
import server.DoctorManager;
import view.ClientMainView;
import view.DoctorMainView;
import view.MainView;
import view.SecretaryMainView;
import view.TableRenderer;
import model.Person;

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
	protected String theFilter;
	protected MainView mainView;
	protected AppointmentHandler eventH;
	protected ArrayList<Appointment> sortedDay;
	protected ArrayList<Appointment> sortedWeek;
	protected ArrayList<Appointment> sortedDoctor;
	protected ArrayList<Appointment> openSlots;
	protected ArrayList<Appointment> closedSlots;
	protected Person person;
	
	protected DoctorManager DManager = new DoctorManager();
	
	public CalendarProgram(MainView mainView, AppointmentManager apptManager, Person person) {
		monthToday = cal.get(GregorianCalendar.MONTH);
		yearToday = cal.get(GregorianCalendar.YEAR);
		yearBound = cal.get(GregorianCalendar.YEAR);
		dayToday = cal.get(GregorianCalendar.DAY_OF_MONTH);
		date = LocalDate.of(yearToday, monthToday + 1, dayToday);
		this.mainView = mainView;
		this.person = person;
		sIndex = 100;
		this.eventH = new AppointmentHandler(apptManager);
		theFilter = "NONE";
		setFrame();

		this.mainView.setVisible(true);
		this.mainView.getCalendarView().getCalendarTable().setModel(eventH.getCalendarModel(monthToday, yearToday));
		
		refreshDay(theFilter);
		refreshWeek(theFilter);
		refreshdAgenda(theFilter);
		refreshwAgenda();
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
				
				openSlots = eventH.getOpenSlots();
				closedSlots = eventH.getClosedSlots();
				
				if(mainView instanceof DoctorMainView || mainView instanceof SecretaryMainView) {
					
					//if free is clicked.
					if (mainView.getTypeView().getFreeCheckBox().isSelected()) {
			
						//if reserved is also clicked, print both.
						if (mainView.getTypeView().getReservedCheckBox().isSelected())
							theFilter = "NONE";
						//if not, just print free.
						else {
	    					theFilter = "FREE";
							
							showOpen();
						}
						
						refreshWeek(theFilter);
						refreshDay(theFilter);
						refreshdAgenda(theFilter);
						refreshwAgenda();
						
					} 
					
					//if free is deselected, but reserved is selected. Display reserved.
					else if (mainView.getTypeView().getReservedCheckBox().isSelected()) {
						if (mainView.getTypeView().getFreeCheckBox().isSelected())
							theFilter = "NONE" ;
						else
							theFilter = "RESERVED";
   
						showClosed();
						
					} 
					
					//If none are selected... just print both.. again
					else if ((!(mainView.getTypeView().getFreeCheckBox().isSelected()) && !(mainView.getTypeView().getReservedCheckBox().isSelected())) || 
							(mainView.getTypeView().getFreeCheckBox().isSelected() && mainView.getTypeView().getReservedCheckBox().isSelected())) {
						theFilter = "NONE";
//						System.out.println(theFilter);
						
						
//						for (int ctr = 0; ctr < openSlots.size(); ctr++) {
//							System.out.println("--" + openSlots.get(ctr).getAppointmentName());
//						}
//						
//						for (int ctr = 0; ctr < closedSlots.size(); ctr++) {
//							System.out.println("--" + closedSlots.get(ctr).getAppointmentName());
//						}
						
						
					}
					
					refreshWeek(theFilter);
					refreshDay(theFilter);
					refreshdAgenda(theFilter);
					refreshwAgenda();
				}
				
				//Else, its the "All" check box.
				else {
					//if All is clicked, just print all appointments.
					if (mainView.getTypeView().getFreeCheckBox().isSelected()) {
						if(mainView.getTypeView().getReservedCheckBox().isSelected())
							theFilter = "NONE";
						else
							theFilter = "FREE";
//						System.out.println(theFilter);
					} 
					
					//if All is deselected, but reserved is selected. Display reserved FOR THAT CLIENT ONLY (STILL NEED TO FIX).
					else if (mainView.getTypeView().getReservedCheckBox().isSelected()) {
						if(mainView.getTypeView().getFreeCheckBox().isSelected())
							theFilter = "NONE";
						else
							theFilter = "RESERVED";
//    					System.out.println(theFilter);
					} 
					
					//If none are selected... just print both.. again
					else if ((!(mainView.getTypeView().getFreeCheckBox().isSelected()) && !(mainView.getTypeView().getReservedCheckBox().isSelected())) || 
							(mainView.getTypeView().getFreeCheckBox().isSelected() && mainView.getTypeView().getReservedCheckBox().isSelected())) {
						theFilter = "NONE";
//						System.out.println(theFilter);
						
					}
					
					refreshWeek(theFilter);
					refreshDay(theFilter);
					refreshdAgenda(theFilter);
					refreshwAgenda();
					
				}
			}
		});
		mainView.getTypeView().getReservedCheckBox().addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				openSlots = eventH.getOpenSlots();
				closedSlots = eventH.getClosedSlots();
				
				System.out.println("-RESERVED CLICKED-");
				//if reserved is selected
				if (mainView.getTypeView().getReservedCheckBox().isSelected()) {
					
					//if free is also selected, print both.
					if (mainView.getTypeView().getFreeCheckBox().isSelected()) {
						theFilter = "NONE";
						
					} 
					
					//If not, then just display the reserved.
					else {
						theFilter = "RESERVED";
						
						showClosed();
					}
					
					refreshWeek(theFilter);
					refreshDay(theFilter);
					refreshdAgenda(theFilter);
					refreshwAgenda();
					
				} 
				
				//if reserved is deselected, but free is still checked. Display free.
				else if (!(mainView.getTypeView().getReservedCheckBox().isSelected())
						&& mainView.getTypeView().getFreeCheckBox().isSelected()) {
					theFilter = "FREE";
					System.out.println(theFilter);
					refreshWeek(theFilter);
					refreshDay(theFilter);
					
					for (int ctr = 0; ctr < openSlots.size(); ctr++) {
						System.out.println("--" + openSlots.get(ctr).getAppointmentName());
					}
					
					showOpen();
					
				} 
				
				//if both are deselected.. just print both
				else if (!(mainView.getTypeView().getFreeCheckBox().isSelected())
						&& !(mainView.getTypeView().getReservedCheckBox().isSelected())) {
					theFilter = "NONE";
					System.out.println(theFilter);
					
					
					for (int ctr = 0; ctr < openSlots.size(); ctr++) {
						System.out.println("--" + openSlots.get(ctr).getAppointmentName());
					}
					
					for (int ctr = 0; ctr < closedSlots.size(); ctr++) {
						System.out.println("--" + closedSlots.get(ctr).getAppointmentName());
					}
				}
				
				refreshWeek(theFilter);
				refreshDay(theFilter);
				refreshdAgenda(theFilter);
				refreshwAgenda();
			}
		});
        
		mainView.getCalendarView().getCmbYear().addActionListener(new cmbYear_Action());
		
		refreshDay(theFilter);
		refreshWeek(theFilter);
		refreshdAgenda(theFilter);
		refreshwAgenda();
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

	protected void refreshDay(String filter) {
		if(mainView instanceof DoctorMainView){
			mainView.getDayView().getDayTable().setModel(eventH.getDoctorDayModel(mainView.getAppID(), getDate(), filter));
			mainView.getDayView().getDayTable().setDefaultRenderer(mainView.getDayView().getDayTable().getColumnClass(0),
						eventH.getDoctorDayRenderer(mainView.getAppID(), filter));
		}
		else if (mainView instanceof ClientMainView) {
			mainView.getDayView().getDayTable().setModel(eventH.getClientDayModel(mainView.getAppID(), getDate(), filter));
			mainView.getDayView().getDayTable().setDefaultRenderer(mainView.getDayView().getDayTable().getColumnClass(0),
						eventH.getClientDayRenderer(mainView.getAppID(), filter));
		}
		else{
			mainView.getDayView().getDayTable().setModel(eventH.getDayModel(getDate(), filter));
			mainView.getDayView().getDayTable().setDefaultRenderer(mainView.getDayView().getDayTable().getColumnClass(0),
					eventH.getDayRenderer(filter));
		}
	}


	protected void refreshWeek(String filter) {
		if(mainView instanceof DoctorMainView){
			mainView.getWeekView().getWeekTable().setModel(eventH.getDoctorWeekModel(mainView.getAppID(), getDate(), filter));
			mainView.getWeekView().getWeekTable().setDefaultRenderer(mainView.getWeekView().getWeekTable().getColumnClass(0), eventH.getDoctorWeekRenderer(mainView.getAppID(), filter));
		}else if (mainView instanceof ClientMainView) {
			mainView.getWeekView().getWeekTable().setModel(eventH.getClientWeekModel(mainView.getAppID(), getDate(), filter));
			mainView.getWeekView().getWeekTable().setDefaultRenderer(mainView.getWeekView().getWeekTable().getColumnClass(0),
						eventH.getClientWeekRenderer(mainView.getAppID(), filter));
		}else{
			mainView.getWeekView().getWeekTable().setModel(eventH.getWeekModel(getDate(), filter));
			mainView.getWeekView().getWeekTable().setDefaultRenderer(mainView.getWeekView().getWeekTable().getColumnClass(0), eventH.getWeekRenderer(filter));
		}
	}

	protected void showOpen () {
		mainView.getAgendaView().getLblEventName().setText("No Upcoming Events");
		mainView.getAgendaView().getLblEventTime().setText("");

		if(mainView instanceof DoctorMainView){
			openSlots = eventH.getDoctorDayAppointments(mainView.getAppID(), getDate());
		}
		else
			openSlots = eventH.getDayEvents(getDate());
		
		for (int ctr = 0; ctr < openSlots.size(); ctr++) {
			System.out.println("--" + openSlots.get(ctr).getAppointmentName());
		}
		
//		for (int ctr = 0; ctr < openSlots.size(); ctr++) {
//			System.out.println("--" + openSlots.get(ctr).getAppointmentName());
//		}
		
		for (int ctr = 0; ctr < openSlots.size(); ctr++) {
			if (openSlots.get(ctr).checkSameDate(getDate()) == 0 && openSlots.get(ctr).isStatus() == false) {
				if (mainView.getAgendaView().getLblEventName().getText().equalsIgnoreCase("No Upcoming Events")) {
					mainView.getAgendaView().getLblEventName().setText("<html><font color='"
							+ openSlots.get(ctr).getColorName() + "'>" + openSlots.get(ctr).getAppointmentName() + "</font>");
					mainView.getAgendaView().getLblEventTime().setText("<html><font color='"
							+ openSlots.get(ctr).getColorName() + "'>" + openSlots.get(ctr).getLocalTimeIn() + "-" + openSlots.get(ctr).getLocalTimeOut() + "</font>");
				} else {
					mainView.getAgendaView().getLblEventName()
							.setText(mainView.getAgendaView().getLblEventName().getText() + "<br><font color='"
									+ openSlots.get(ctr).getColorName() + "'>" + openSlots.get(ctr).getAppointmentName() + "</font>");
					mainView.getAgendaView().getLblEventTime()
							.setText(mainView.getAgendaView().getLblEventTime().getText() + "<br><font color='"
									+ openSlots.get(ctr).getColorName() + "'>" + openSlots.get(ctr).getLocalTimeIn() + "-" + openSlots.get(ctr).getLocalTimeOut() + "</font>");
				}
			}
		}
	}
	
	protected void showClosed () {
		mainView.getAgendaView().getLblEventName().setText("No Upcoming Events");
		mainView.getAgendaView().getLblEventTime().setText("");
		

		if(mainView instanceof DoctorMainView){
			closedSlots = eventH.getDoctorDayAppointments(mainView.getAppID(), getDate());
		}
		else
			closedSlots = eventH.getDayEvents(getDate());
		
		for (int ctr = 0; ctr < closedSlots.size(); ctr++) {
			System.out.println("--" + closedSlots.get(ctr).getAppointmentName());
		}

//		for (int ctr = 0; ctr < closedSlots.size(); ctr++) {
//			System.out.println("--" + closedSlots.get(ctr).getAppointmentName());
//		}
		
		for (int ctr = 0; ctr < closedSlots.size(); ctr++) {
			if (closedSlots.get(ctr).checkSameDate(getDate()) == 0 && closedSlots.get(ctr).isStatus() == true) {
				if (mainView.getAgendaView().getLblEventName().getText().equalsIgnoreCase("No Upcoming Events")) {
					mainView.getAgendaView().getLblEventName().setText("<html><font color='"
							+ closedSlots.get(ctr).getColorName() + "'>" + closedSlots.get(ctr).getAppointmentName() + "</font>");
					mainView.getAgendaView().getLblEventTime().setText("<html><font color='"
							+ closedSlots.get(ctr).getColorName() + "'>" + closedSlots.get(ctr).getLocalTimeIn() + "-" + closedSlots.get(ctr).getLocalTimeOut() + "</font>");
				} else {
					mainView.getAgendaView().getLblEventName()
							.setText(mainView.getAgendaView().getLblEventName().getText() + "<br><font color='"
									+ closedSlots.get(ctr).getColorName() + "'>" + closedSlots.get(ctr).getAppointmentName() + "</font>");
					mainView.getAgendaView().getLblEventTime()
							.setText(mainView.getAgendaView().getLblEventTime().getText() + "<br><font color='"
									+ closedSlots.get(ctr).getColorName() + "'>" + closedSlots.get(ctr).getLocalTimeIn() + "-" + closedSlots.get(ctr).getLocalTimeOut() + "</font>");
				}
			}
		}
	}
	
	
	
	protected void refreshdAgenda(String filter) {
		
		ArrayList<Appointment> tempDT = new ArrayList<Appointment>();
		ArrayList<Appointment> appCopy;
		
		//Filter that states all appointment to be used.
    	if(filter.equalsIgnoreCase("NONE")) {
    	//	System.out.println("NONE, FILTER FOUND. (getDoctorDayRenderer)");
    		appCopy = eventH.getAppointments();
    	}
    	
    	//Appointments that are only free/open are given.
    	else if(filter.equalsIgnoreCase("FREE")) {
    	//	System.out.println("FREE, FILTER FOUND. (getDoctorDayRenderer)");
    		appCopy = eventH.getOpenSlots();
    	}
    	
    	//appointments that are only closed/reserved are given.
    	else if(filter.equalsIgnoreCase("RESERVED")) {
    	//	System.out.println("RESERVED, FILTER FOUND. (getDoctorDayRenderer)");
    		appCopy = eventH.getClosedSlots();
    	}
    	
    	//error.
    	else {
    	//	System.out.println("ERROR, FILTER NOT FOUND. (getDoctorDayRenderer)");
    		appCopy = eventH.getAppointments();
    	}
		
		if(mainView instanceof DoctorMainView){
			
			for(int i = 0; i < appCopy.size(); i++){
				if(appCopy.get(i).getDoctorID() == mainView.getAppID()){
					tempDT.add(appCopy.get(i));
				}
			}
			
			mainView.getAgendaView().getLblEventName().setText("No Upcoming Events");
			mainView.getAgendaView().getLblEventTime().setText("");

			for (int ctr = 0; ctr < tempDT.size(); ctr++) {
				if (tempDT.get(ctr).checkSameDate(getDate()) == 0) {
					if (mainView.getAgendaView().getLblEventName().getText().equalsIgnoreCase("No Upcoming Events")) {
						mainView.getAgendaView().getLblEventName().setText("<html><font color='"
								+ tempDT.get(ctr).getColorName() + "'>" + tempDT.get(ctr).getAppointmentName() + "</font>");
						mainView.getAgendaView().getLblEventTime().setText("<html><font color='"
								+ tempDT.get(ctr).getColorName() + "'>" + tempDT.get(ctr).getLocalTimeIn() + "-" + tempDT.get(ctr).getLocalTimeOut() + "</font>");
					} else {
						mainView.getAgendaView().getLblEventName()
								.setText(mainView.getAgendaView().getLblEventName().getText() + "<br><font color='"
										+ tempDT.get(ctr).getColorName() + "'>" + tempDT.get(ctr).getAppointmentName() + "</font>");
						mainView.getAgendaView().getLblEventTime()
								.setText(mainView.getAgendaView().getLblEventTime().getText() + "<br><font color='"
										+ tempDT.get(ctr).getColorName() + "'>" + tempDT.get(ctr).getLocalTimeIn() + "-" + tempDT.get(ctr).getLocalTimeOut() + "</font>");
					}
				}
			}
		}
		else if (mainView instanceof ClientMainView) {
			
			for(int i = 0; i < appCopy.size(); i++){
				if(appCopy.get(i).getClientID() == mainView.getAppID() || appCopy.get(i).isStatus() == false){
					tempDT.add(appCopy.get(i));
				}
			}
			
			mainView.getAgendaView().getLblEventName().setText("No Upcoming Events");
			mainView.getAgendaView().getLblEventTime().setText("");

			for (int ctr = 0; ctr < tempDT.size(); ctr++) {
				if (tempDT.get(ctr).checkSameDate(getDate()) == 0) {
					if (mainView.getAgendaView().getLblEventName().getText().equalsIgnoreCase("No Upcoming Events")) {
						mainView.getAgendaView().getLblEventName().setText("<html><font color='"
								+ tempDT.get(ctr).getColorName() + "'>" + tempDT.get(ctr).getAppointmentName() + "</font>");
						mainView.getAgendaView().getLblEventTime().setText("<html><font color='"
								+ tempDT.get(ctr).getColorName() + "'>" + tempDT.get(ctr).getLocalTimeIn() + "-" + tempDT.get(ctr).getLocalTimeOut() + "</font>");
					} else {
						mainView.getAgendaView().getLblEventName()
								.setText(mainView.getAgendaView().getLblEventName().getText() + "<br><font color='"
										+ tempDT.get(ctr).getColorName() + "'>" + tempDT.get(ctr).getAppointmentName() + "</font>");
						mainView.getAgendaView().getLblEventTime()
								.setText(mainView.getAgendaView().getLblEventTime().getText() + "<br><font color='"
										+ tempDT.get(ctr).getColorName() + "'>" + tempDT.get(ctr).getLocalTimeIn() + "-" + tempDT.get(ctr).getLocalTimeOut() + "</font>");
					}
				}
			}
			
		}
		else{
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
	}
	
	protected void refreshwAgenda(){
		mainView.getWeekAgendaView().getLblEventName().setText("No Upcoming Events");
		mainView.getWeekAgendaView().getLblEventTime().setText("");
		
		if(mainView instanceof DoctorMainView){
			sortedWeek = eventH.getDoctorWeekAppointments(mainView.getAppID(), getDate());
		}
		else
			sortedWeek = eventH.getWeekEvents(getDate());
		
		for (int ctr = 0; ctr < sortedWeek.size(); ctr++) {
			
			if (mainView.getWeekAgendaView().getLblEventName().getText().equalsIgnoreCase("No Upcoming Events")) {
				mainView.getWeekAgendaView().getLblEventName().setText("<html><font color='"
						+ sortedWeek.get(ctr).getColorName() + "'>" + sortedWeek.get(ctr).getAppointmentDate() + " " + sortedWeek.get(ctr).getAppointmentName() + "</font>");
				mainView.getWeekAgendaView().getLblEventTime().setText("<html><font color='"
						+ sortedWeek.get(ctr).getColorName() + "'>" + sortedWeek.get(ctr).getLocalTimeIn() + "-" + sortedWeek.get(ctr).getLocalTimeOut() + "</font>");
			} else {
				mainView.getWeekAgendaView().getLblEventName()
						.setText(mainView.getWeekAgendaView().getLblEventName().getText() + "<br><font color='"
								+ sortedWeek.get(ctr).getColorName() + "'>" + sortedWeek.get(ctr).getAppointmentDate() + " " + sortedWeek.get(ctr).getAppointmentName() + "</font>");
				mainView.getWeekAgendaView().getLblEventTime()
						.setText(mainView.getWeekAgendaView().getLblEventTime().getText() + "<br><font color='"
								+ sortedWeek.get(ctr).getColorName() + "'>" + sortedWeek.get(ctr).getLocalTimeIn() + "-" + sortedWeek.get(ctr).getLocalTimeOut() + "</font>");

			}
		}
	}
	
	public void refresh(ArrayList<Appointment> appointments)
	{
		eventH.setAppointments(appointments);
		
		refreshDay(theFilter);
		refreshdAgenda(theFilter);
		refreshWeek(theFilter);
		refreshwAgenda();
	}
	
	public AppointmentHandler getAppointmentHandler(){
		return eventH;
	}
	
	public Person getPerson(){
		return person;
	}

	public MainView getMainView(){
		return mainView;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	////////////////////////////////////////////////// ACTION LISTENERS /////////////////////////////////////////////////////////////
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
			refreshdAgenda(theFilter);
			refreshDay(theFilter);
			refreshwAgenda();
			refreshWeek(theFilter);
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
			refreshdAgenda(theFilter);
			refreshDay(theFilter);
			refreshwAgenda();
			refreshWeek(theFilter);
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
			
			refreshdAgenda(theFilter);
			refreshwAgenda();
			refreshDay(theFilter);
			refreshWeek(theFilter);
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
				refreshdAgenda(theFilter);
				refreshwAgenda();
				refreshDay(theFilter);
				refreshWeek(theFilter);
			}
		}
	}

	protected class btnSave_Action implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
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
//						System.out.println("Day of Event" + LocalDate.ofYearDay(date.getYear(), ctr));
//						System.out.println("Added");
					}
				}
			}
			else
				eventH.addAppointment(getDate(), "Appointment " + (eventH.getAppointments().size() + 1), LocalTime.of(shour, sminute), LocalTime.of(ehour, eminute), mainView.getAppID(), "Red");
			
			refreshDay(theFilter);
			refreshdAgenda(theFilter);
			refreshWeek(theFilter);
			refreshwAgenda();
		
			mainView.getCreateView().getTextFieldDate().setText("");
			mainView.getCreateView().getComboBoxFrom().setSelectedIndex(0);
			mainView.getCreateView().getComboBoxTo().setSelectedIndex(0);

		}
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
