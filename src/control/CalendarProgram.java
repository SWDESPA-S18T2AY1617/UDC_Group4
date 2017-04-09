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
	protected Person person;
	
	public CalendarProgram(MainView mainView, AppointmentManager apptManager, Person person) {
		monthToday = cal.get(GregorianCalendar.MONTH);
		yearToday = cal.get(GregorianCalendar.YEAR);
		yearBound = cal.get(GregorianCalendar.YEAR);
		dayToday = cal.get(GregorianCalendar.DAY_OF_MONTH);
		date = LocalDate.of(yearToday, monthToday + 1, dayToday);
		this.theFilter = "NONE";
		this.mainView = mainView;
		this.person = person;
		sIndex = 100;
		this.eventH = new AppointmentHandler(apptManager);
		setFrame();
		
		this.mainView.setVisible(true);
		this.mainView.getCalendarView().getCalendarTable().setModel(eventH.getCalendarModel(monthToday, yearToday));
		
        refreshDay(theFilter);
        refreshdAgenda(theFilter);
		refreshwAgenda(theFilter);
		refreshWeek(theFilter);
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
				if(mainView instanceof DoctorMainView || mainView instanceof SecretaryMainView) {
					
					//if free is clicked.
					if (mainView.getTypeView().getFreeCheckBox().isSelected()) {
			
						//if reserved is also clicked, print both.
						if (mainView.getTypeView().getReservedCheckBox().isSelected())
							theFilter = "NONE";
						//if not, just print free.
						else
	    					theFilter = "FREE";
						
						refreshWeek(theFilter);
						refreshDay(theFilter);
						refreshdAgenda(theFilter);
						refreshwAgenda(theFilter);
						
					} 
					
					//if free is deselected, but reserved is selected. Display reserved.
					else if (mainView.getTypeView().getReservedCheckBox().isSelected()) {
						if (mainView.getTypeView().getFreeCheckBox().isSelected())
							theFilter = "NONE" ;
						else
							theFilter = "RESERVED";
   
						
					} 
					
					//If none are selected... just print both.. again
					else if ((!(mainView.getTypeView().getFreeCheckBox().isSelected()) && !(mainView.getTypeView().getReservedCheckBox().isSelected())) || 
							(mainView.getTypeView().getFreeCheckBox().isSelected() && mainView.getTypeView().getReservedCheckBox().isSelected())) {
						theFilter = "NONE";
						
					}
					
					refreshWeek(theFilter);
					refreshDay(theFilter);
					refreshdAgenda(theFilter);
					refreshwAgenda(theFilter);
				}
				
				//Else, its the "All" check box.
				else {
					//if All is clicked, just print all appointments.
					if (mainView.getTypeView().getFreeCheckBox().isSelected()) {
						if(mainView.getTypeView().getReservedCheckBox().isSelected())
							theFilter = "NONE";
						else
							theFilter = "FREE";
					} 
					
					//if All is deselected, but reserved is selected. Display reserved FOR THAT CLIENT ONLY (STILL NEED TO FIX).
					else if (mainView.getTypeView().getReservedCheckBox().isSelected()) {
						if(mainView.getTypeView().getFreeCheckBox().isSelected())
							theFilter = "NONE";
						else
							theFilter = "RESERVED";
					} 
					
					//If none are selected... just print both.. again
					else if ((!(mainView.getTypeView().getFreeCheckBox().isSelected()) && !(mainView.getTypeView().getReservedCheckBox().isSelected())) || 
							(mainView.getTypeView().getFreeCheckBox().isSelected() && mainView.getTypeView().getReservedCheckBox().isSelected())) {
						theFilter = "NONE";
						
					}
					
					refreshWeek(theFilter);
					refreshDay(theFilter);
					refreshdAgenda(theFilter);
					refreshwAgenda(theFilter);
					
				}
			}
		});
		mainView.getTypeView().getReservedCheckBox().addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				//if reserved is selected
				if (mainView.getTypeView().getReservedCheckBox().isSelected()) {
					
					//if free is also selected, print both.
					if (mainView.getTypeView().getFreeCheckBox().isSelected()) {
						theFilter = "NONE";
						
					} 
					
					//If not, then just display the reserved.
					else {
						theFilter = "RESERVED";
						
					}
					
				} 
				
				//if reserved is deselected, but free is still checked. Display free.
				else if (!(mainView.getTypeView().getReservedCheckBox().isSelected())
						&& mainView.getTypeView().getFreeCheckBox().isSelected()) {
					theFilter = "FREE";
					
				} 
				
				//if both are deselected.. just print both
				else if (!(mainView.getTypeView().getFreeCheckBox().isSelected())
						&& !(mainView.getTypeView().getReservedCheckBox().isSelected())) {
					theFilter = "NONE";
					
				}
				
				refreshWeek(theFilter);
				refreshDay(theFilter);
				refreshdAgenda(theFilter);
				refreshwAgenda(theFilter);
			}
		});
        mainView.getTypeView().getComboBox().addActionListener(new cmbView_Action());
		mainView.getCalendarView().getCmbYear().addActionListener(new cmbYear_Action());
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
	
	protected void refreshdAgenda(String filter) {
		
		ArrayList<Appointment> tempDT = new ArrayList<Appointment>();
		ArrayList<Appointment> appCopy;
		
		//Filter that states all appointment to be used.
    	if(filter.equalsIgnoreCase("NONE")) {
    		appCopy = eventH.getAppointments();
    	}
    	
    	//Appointments that are only free/open are given.
    	else if(filter.equalsIgnoreCase("FREE")) {
    		appCopy = eventH.getOpenSlots();
    	}
    	
    	//appointments that are only closed/reserved are given.
    	else if(filter.equalsIgnoreCase("RESERVED")) {
    		appCopy = eventH.getClosedSlots();
    	}
    	
    	//error.
    	else {
    		appCopy = eventH.getAppointments();
    	}
    	
    	mainView.getAgendaView().getLblEventName().setText("No Upcoming Events");
		mainView.getAgendaView().getLblEventTime().setText("");
		
		if(mainView instanceof DoctorMainView){
			
			for(int i = 0; i < appCopy.size(); i++){
				if(appCopy.get(i).getDoctorID() == mainView.getAppID()){
					tempDT.add(appCopy.get(i));
				}
			}

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
			for (int ctr = 0; ctr < appCopy.size(); ctr++) {
				if (appCopy.get(ctr).checkSameDate(getDate()) == 0) {
					if (mainView.getAgendaView().getLblEventName().getText().equalsIgnoreCase("No Upcoming Events")) {
						mainView.getAgendaView().getLblEventName().setText("<html><font color='"
								+ appCopy.get(ctr).getColorName() + "'>" + appCopy.get(ctr).getAppointmentName() + "</font>");
						mainView.getAgendaView().getLblEventTime().setText("<html><font color='"
								+ appCopy.get(ctr).getColorName() + "'>" + appCopy.get(ctr).getLocalTimeIn() + "-" + appCopy.get(ctr).getLocalTimeOut() + "</font>");
					} else {
						mainView.getAgendaView().getLblEventName()
								.setText(mainView.getAgendaView().getLblEventName().getText() + "<br><font color='"
										+ appCopy.get(ctr).getColorName() + "'>" + appCopy.get(ctr).getAppointmentName() + "</font>");
						mainView.getAgendaView().getLblEventTime()
								.setText(mainView.getAgendaView().getLblEventTime().getText() + "<br><font color='"
										+ appCopy.get(ctr).getColorName() + "'>" + appCopy.get(ctr).getLocalTimeIn() + "-" + appCopy.get(ctr).getLocalTimeOut() + "</font>");
					}
				}
			}
		}
	}
	
	protected void refreshwAgenda(String filter){
		
		ArrayList<Appointment> tempW = new ArrayList<Appointment>();
		ArrayList<Appointment> appCopy;
		
		//Filter that states all appointment to be used.
    	if(filter.equalsIgnoreCase("NONE")) {
    		appCopy = eventH.getAppointments();
    	}
    	
    	//Appointments that are only free/open are given.
    	else if(filter.equalsIgnoreCase("FREE")) {
    		appCopy = eventH.getOpenSlots();
    	}
    	
    	//appointments that are only closed/reserved are given.
    	else if(filter.equalsIgnoreCase("RESERVED")) {
    		appCopy = eventH.getClosedSlots();
    	}
    	
    	//error.
    	else {
    		appCopy = eventH.getAppointments();
    	}
    	
    	int value1 = 0;
		int value2 = 0;
		
		if(date.getDayOfWeek().name().equalsIgnoreCase("Monday")){
    		value1 = -2;
    		value2 = 6;
    	}
        else if(date.getDayOfWeek().name().equalsIgnoreCase("Tuesday")){
    		value1 = -3;
    		value2 = 5;
    	}
        else if(date.getDayOfWeek().name().equalsIgnoreCase("Wednesday")){
    		value1 = -4;
    		value2 = 4;
    	}
        else if(date.getDayOfWeek().name().equalsIgnoreCase("Thursday")){
    		value1 = -5;
    		value2 = 3;
    	}
        else if(date.getDayOfWeek().name().equalsIgnoreCase("Friday")){
    		value1 = -6;
    		value2 = 2;
    	}
        else if(date.getDayOfWeek().name().equalsIgnoreCase("Saturday")){
    		value1 = -7;
    		value2 = 1;
    	}
        else if(date.getDayOfWeek().name().equalsIgnoreCase("Sunday")){
    		value1 = -1;
    		value2 = 7;
    	}
		
		LocalDate date1;
    	LocalDate date2;
    	
    	if(date.getDayOfYear() > 7){
    		date1 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value1);
        	date2 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value2);
    	}
        else{
        	date1 = LocalDate.ofYearDay(date.getYear() - 1, 365 + 1 + value1);
    		date2 = LocalDate.ofYearDay(date.getYear(), date.getDayOfYear() + value2);
        }
    	
    	mainView.getWeekAgendaView().getLblEventName().setText("No Upcoming Events");
		mainView.getWeekAgendaView().getLblEventTime().setText("");
    	
    	if (mainView instanceof DoctorMainView){
    		for(int i = 0; i < appCopy.size(); i++){
    			if(appCopy.get(i).getDoctorID() == mainView.getAppID()){
    				tempW.add(appCopy.get(i));
    			}
    		}
    		
    		for (int ctr = 0; ctr < tempW.size(); ctr++) { //searches for the events for this month and year
            	
            	if (tempW.get(ctr).getAppointmentDate().isAfter(date1) && tempW.get(ctr).getAppointmentDate().isBefore(date2) && !(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Sunday")) && !(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Saturday"))){
            		
            		if (mainView.getWeekAgendaView().getLblEventName().getText().equalsIgnoreCase("No Upcoming Events")) {
        				mainView.getWeekAgendaView().getLblEventName().setText("<html><font color='"
        						+ tempW.get(ctr).getColorName() + "'>" + tempW.get(ctr).getAppointmentDate() + " " + tempW.get(ctr).getAppointmentName() + "</font>");
        				mainView.getWeekAgendaView().getLblEventTime().setText("<html><font color='"
        						+ tempW.get(ctr).getColorName() + "'>" + tempW.get(ctr).getLocalTimeIn() + "-" + tempW.get(ctr).getLocalTimeOut() + "</font>");
        			} else {
        				mainView.getWeekAgendaView().getLblEventName()
        						.setText(mainView.getWeekAgendaView().getLblEventName().getText() + "<br><font color='"
        								+ tempW.get(ctr).getColorName() + "'>" + tempW.get(ctr).getAppointmentDate() + " " + tempW.get(ctr).getAppointmentName() + "</font>");
        				mainView.getWeekAgendaView().getLblEventTime()
        						.setText(mainView.getWeekAgendaView().getLblEventTime().getText() + "<br><font color='"
        								+ tempW.get(ctr).getColorName() + "'>" + tempW.get(ctr).getLocalTimeIn() + "-" + tempW.get(ctr).getLocalTimeOut() + "</font>");

        			}
            	}
    		}
    	}
    	else if(mainView instanceof ClientMainView) {
    		for(int i = 0; i < appCopy.size(); i++){
    			if(appCopy.get(i).getClientID() == mainView.getAppID() || appCopy.get(i).isStatus() == false){
    				tempW.add(appCopy.get(i));
    			}
    		}

    		for (int ctr = 0; ctr < tempW.size(); ctr++) { //searches for the events for this month and year
            	
            	if (tempW.get(ctr).getAppointmentDate().isAfter(date1) && tempW.get(ctr).getAppointmentDate().isBefore(date2) && !(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Sunday")) && !(tempW.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Saturday"))){
            		if (mainView.getWeekAgendaView().getLblEventName().getText().equalsIgnoreCase("No Upcoming Events")) {
        				mainView.getWeekAgendaView().getLblEventName().setText("<html><font color='"
        						+ tempW.get(ctr).getColorName() + "'>" + tempW.get(ctr).getAppointmentDate() + " " + tempW.get(ctr).getAppointmentName() + "</font>");
        				mainView.getWeekAgendaView().getLblEventTime().setText("<html><font color='"
        						+ tempW.get(ctr).getColorName() + "'>" + tempW.get(ctr).getLocalTimeIn() + "-" + tempW.get(ctr).getLocalTimeOut() + "</font>");
        			} else {
        				mainView.getWeekAgendaView().getLblEventName()
        						.setText(mainView.getWeekAgendaView().getLblEventName().getText() + "<br><font color='"
        								+ tempW.get(ctr).getColorName() + "'>" + tempW.get(ctr).getAppointmentDate() + " " + tempW.get(ctr).getAppointmentName() + "</font>");
        				mainView.getWeekAgendaView().getLblEventTime()
        						.setText(mainView.getWeekAgendaView().getLblEventTime().getText() + "<br><font color='"
        								+ tempW.get(ctr).getColorName() + "'>" + tempW.get(ctr).getLocalTimeIn() + "-" + tempW.get(ctr).getLocalTimeOut() + "</font>");

        			}
            	}
    		}
    	}
    	else{
    		for (int ctr = 0; ctr < appCopy.size(); ctr++) { //searches for the events for this month and year
            	
            	if (appCopy.get(ctr).getAppointmentDate().isAfter(date1) && appCopy.get(ctr).getAppointmentDate().isBefore(date2) && !(appCopy.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Sunday")) && !(appCopy.get(ctr).getAppointmentDate().getDayOfWeek().name().equalsIgnoreCase("Saturday"))){
    			
	    			if (mainView.getWeekAgendaView().getLblEventName().getText().equalsIgnoreCase("No Upcoming Events")) {
	    				mainView.getWeekAgendaView().getLblEventName().setText("<html><font color='"
	    						+ appCopy.get(ctr).getColorName() + "'>" + appCopy.get(ctr).getAppointmentDate() + " " + appCopy.get(ctr).getAppointmentName() + "</font>");
	    				mainView.getWeekAgendaView().getLblEventTime().setText("<html><font color='"
	    						+ appCopy.get(ctr).getColorName() + "'>" + appCopy.get(ctr).getLocalTimeIn() + "-" + appCopy.get(ctr).getLocalTimeOut() + "</font>");
	    			} else {
	    				mainView.getWeekAgendaView().getLblEventName()
	    						.setText(mainView.getWeekAgendaView().getLblEventName().getText() + "<br><font color='"
	    								+ appCopy.get(ctr).getColorName() + "'>" + appCopy.get(ctr).getAppointmentDate() + " " + appCopy.get(ctr).getAppointmentName() + "</font>");
	    				mainView.getWeekAgendaView().getLblEventTime()
	    						.setText(mainView.getWeekAgendaView().getLblEventTime().getText() + "<br><font color='"
	    								+ appCopy.get(ctr).getColorName() + "'>" + appCopy.get(ctr).getLocalTimeIn() + "-" + appCopy.get(ctr).getLocalTimeOut() + "</font>");
	    			}

    			}
    		}
    	}
		
	}
	
	public void refresh(ArrayList<Appointment> appointments)
	{
		eventH.setAppointments(appointments);
		
		refreshDay(theFilter);
		refreshdAgenda(theFilter);
		refreshWeek(theFilter);
		refreshwAgenda(theFilter);
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
			refreshwAgenda(theFilter);
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
			refreshwAgenda(theFilter);
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
			refreshwAgenda(theFilter);
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
	
	protected class cmbView_Action implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (mainView.getTypeView().getComboBox().getSelectedItem() != null) {
				ArrayList<Appointment> holdAllAppointments = getAppointmentHandler().getAppointments();
				String selectedDoctor = mainView.getTypeView().getComboBox().getSelectedItem().toString();

				if(selectedDoctor.equalsIgnoreCase("all")){
					refreshCalendar();
					refreshdAgenda("FREE");
					refreshwAgenda("FREE");
					refreshDay("FREE");
					refreshWeek("FREE");
				}else if(selectedDoctor.equalsIgnoreCase("Doctor 1 1")){
					ArrayList<Appointment> doctor1 = new ArrayList<Appointment>();
					for(int i=0;i<getAppointmentHandler().getAppointments().size();i++){
						if(getAppointmentHandler().getAppointments().get(i).getDoctorID() == 1)
							doctor1.add(getAppointmentHandler().getAppointments().get(i));
					}
					refresh(doctor1);
					refreshCalendar();
					refreshdAgenda("FREE");
					refreshwAgenda("FREE");
					refreshDay("FREE");
					refreshWeek("FREE");
				}else{
					ArrayList<Appointment> doctor2 = new ArrayList<Appointment>();
					for(int i=0;i<getAppointmentHandler().getAppointments().size();i++){
						if(getAppointmentHandler().getAppointments().get(i).getDoctorID() == 2)
							doctor2.add(getAppointmentHandler().getAppointments().get(i));
					}
					refresh(doctor2);
					refreshCalendar();
					refreshdAgenda("FREE");
					refreshwAgenda("FREE");
					refreshDay("FREE");
					refreshWeek("FREE");
				}
				getAppointmentHandler().setAppointments(holdAllAppointments);
			}	
		}
	}
	protected class cmbYear_Action implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (mainView.getCalendarView().getCmbYear().getSelectedItem() != null) {
				String selectedYear = mainView.getCalendarView().getCmbYear().getSelectedItem().toString();
				yearToday = Integer.parseInt(selectedYear);
				sIndex = mainView.getCalendarView().getCmbYear().getSelectedIndex();
				
				refreshCalendar();
				refreshdAgenda(theFilter);
				refreshwAgenda(theFilter);
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
					}
				}
			}
			else
				eventH.addAppointment(getDate(), "Appointment " + (eventH.getAppointments().size() + 1), LocalTime.of(shour, sminute), LocalTime.of(ehour, eminute), mainView.getAppID(), "Red");
			
			refreshDay(theFilter);
			refreshdAgenda(theFilter);
			refreshWeek(theFilter);
			refreshwAgenda(theFilter);
		
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
