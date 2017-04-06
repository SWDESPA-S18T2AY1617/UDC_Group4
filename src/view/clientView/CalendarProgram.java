package view.clientView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.util.*;
import javax.swing.JOptionPane;

public class CalendarProgram {

	private GregorianCalendar cal = new GregorianCalendar();
	private int monthToday;
	private int yearToday;
	private int dayToday;
	private int sIndex = 100;
	private int yearBound;
	private int col = -1;
	private int row = -1;
	private CalendarItemHandler eventH;
	private clientMainView mainView;
	private Writer writer;
	private ArrayList<CalendarItem> sortedDay;

	public CalendarProgram() {
		monthToday = cal.get(GregorianCalendar.MONTH);
		yearToday = cal.get(GregorianCalendar.YEAR);
		yearBound = cal.get(GregorianCalendar.YEAR);
		dayToday = cal.get(GregorianCalendar.DAY_OF_MONTH);

		sIndex = 100;
		this.mainView = new clientMainView();
		this.eventH = new CalendarItemHandler();
		this.writer = new Writer();
		eventH.load();

		for (int i = 0; i < eventH.getCalendarItems().size(); i++) {
			System.out.println(eventH.getCalendarItems().get(i).toString());
		}

		setFrame();

		this.mainView.setVisible(true);
		this.mainView.getCalendarView().getCalendarTable().setModel(eventH.getCalendarModel(monthToday, yearToday));
		this.mainView.getDayView().getDayTable().setModel(eventH.getDayModel(monthToday, yearToday, dayToday));
		this.mainView.getWeekView().getWeekTable().setModel(eventH.getWeekModel(monthToday,yearToday,dayToday));
		refreshCalendar();
	}

	private void setFrame() { // sets the values of the buttons in the frame

		for (int i = yearBound - 100; i <= yearBound + 100; i++) {
			mainView.getCalendarView().getCmbYear().addItem(String.valueOf(i)); // adds																			// box
		}

//		for (int i = 0; i < 24; i++) {
//			mainView.getCreateView().getComboBoxFrom().addItem(i + ":00");
//			mainView.getCreateView().getComboBoxFrom().addItem(i + ":30");
//			mainView.getCreateView().getComboBoxTo().addItem(i + ":30");
//			mainView.getCreateView().getComboBoxTo().addItem((i + 1) + ":00");
//		}

		mainView.getCalendarView().getBtnPrev().addActionListener(new btnPrev_Action()); // adds
																							// action
																							// listener
		mainView.getCalendarView().getBtnNext().addActionListener(new btnNext_Action()); // adds
																							// action
																							// listener
//		mainView.getCalendarView().getBtnCreate().addActionListener(new btnCreate_Action());
		mainView.getCalendarView().getCalendarTable().addMouseListener(new scrollPanelCal_Action());
		mainView.getHeaderView().getDayBtn().addActionListener(new btnDay_Action());
		mainView.getHeaderView().getWeekBtn().addActionListener(new btnWeek_Action());
		mainView.getHeaderView().getAgendaBtn().addActionListener(new btnAgenda_Action());
//		mainView.getCreateView().getrdBtnEvent().addActionListener(new rdBtnEvent_Action());
//		mainView.getCreateView().getrdBtnTask().addActionListener(new rdBtnTask_Action());
//		mainView.getCreateView().getBtnSave().addActionListener(new btnSave_Action());
//		mainView.getCreateView().getBtnDiscard().addActionListener(new btnDiscard_Action());
//		mainView.getDayView().getDayTable().addMouseListener(new scrollPanelDay_Action());
		mainView.getTypeView().getFreeCheckBox().addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (mainView.getTypeView().getFreeCheckBox().isSelected()) {
					if (mainView.getTypeView().getReservedCheckBox().isSelected()) {
						refreshAgenda();
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
					refreshAgenda();
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
						refreshAgenda();
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
					refreshAgenda();
					refreshDay();
				}
			}
		});

		mainView.getCalendarView().getCmbYear().addActionListener(new cmbYear_Action());
		// Initialize Contents
		mainView.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				try {
					writer.write(eventH.getdata());
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		refreshAgenda();
		refreshDay();
	}

	private void refreshCalendar() { // sets the view based on the current month
										// and year
		String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };

		mainView.getHeaderView().getDateLabel().setText(months[monthToday] + " " + dayToday + ", " + yearToday);
		mainView.getCalendarView().getBtnPrev().setEnabled(true); // enables the
																	// previous
																	// button
		mainView.getCalendarView().getBtnNext().setEnabled(true); // enables the
																	// next
																	// button
		if (monthToday == 0 && yearToday <= yearBound - 100)
			mainView.getCalendarView().getBtnPrev().setEnabled(false); // disables
																		// the
																		// previous
																		// button
		if (monthToday == 11 && yearToday >= yearBound + 100)
			mainView.getCalendarView().getBtnNext().setEnabled(false); // disables
																		// the
																		// next
																		// button

		mainView.getCalendarView().getMonthLabel().setText(months[monthToday]); // changes
																				// the
																				// month
																				// label
																				// based
																				// on
																				// the
																				// current
																				// month
		mainView.getCalendarView().getMonthLabel().setBounds(20, 25, 100, 30);

		mainView.getCalendarView().getCmbYear().setSelectedIndex(sIndex); // changes
																			// the
																			// selected
																			// index
																			// in
																			// the
																			// combo
																			// box
																			// based
																			// on
																			// the
																			// current
																			// year
		mainView.getCalendarView().getCalendarTable().setModel(eventH.getCalendarModel(monthToday, yearToday)); // updates
																												// the
																												// model
																												// being
																												// used
		mainView.getCalendarView().getCalendarTable().setDefaultRenderer(
				mainView.getCalendarView().getCalendarTable().getColumnClass(0), new TableRenderer()); // updates
																										// the
																										// renderer
																										// used
																										// to
																										// change
																										// the
																										// color
																										// of
																										// a
																										// cell
	}

	private void refreshDay() {

		mainView.getDayView().getDayTable().setModel(eventH.getDayModel(monthToday, yearToday, dayToday));
		mainView.getDayView().getDayTable().setDefaultRenderer(mainView.getDayView().getDayTable().getColumnClass(0),
				eventH.getDayRenderer());
	}

	private void refreshDaySpecific(String style) {
		mainView.getDayView().getDayTable().setModel(eventH.getEventDayModel(monthToday, yearToday, dayToday, style));
		mainView.getDayView().getDayTable().setDefaultRenderer(mainView.getDayView().getDayTable().getColumnClass(0),
				eventH.getDayRenderer());
	}

	private void refreshAgenda() {
		mainView.getAgendaView().getLblEventName().setText("No Upcoming Events");
		mainView.getAgendaView().getLblEventTime().setText("");

		sortedDay = eventH.getDayEvents(monthToday + 1, dayToday, yearToday);

		for (int ctr = 0; ctr < sortedDay.size(); ctr++) {
			if (dayToday == sortedDay.get(ctr).getDay() && (monthToday + 1) == sortedDay.get(ctr).getMonth()
					&& yearToday == sortedDay.get(ctr).getYear()) {
				if (mainView.getAgendaView().getLblEventName().getText().equalsIgnoreCase("No Upcoming Events")) {
					mainView.getAgendaView().getLblEventName().setText("<html><font color='"
							+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getEvent());
					mainView.getAgendaView().getLblEventTime().setText("<html><font color='"
							+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getShour() + ":");
				} else {
					mainView.getAgendaView().getLblEventName()
							.setText(mainView.getAgendaView().getLblEventName().getText() + "<br><font color='"
									+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getEvent());
					mainView.getAgendaView().getLblEventTime()
							.setText(mainView.getAgendaView().getLblEventTime().getText() + "<br><font color='"
									+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getShour() + ":");
				}

				if (sortedDay.get(ctr).getSminute() < 10)
					mainView.getAgendaView().getLblEventTime()
							.setText(mainView.getAgendaView().getLblEventTime().getText() + "0"
									+ Integer.toString(sortedDay.get(ctr).getSminute()));
				else
					mainView.getAgendaView().getLblEventTime()
							.setText(mainView.getAgendaView().getLblEventTime().getText()
									+ Integer.toString(sortedDay.get(ctr).getSminute()));

				mainView.getAgendaView().getLblEventTime().setText(mainView.getAgendaView().getLblEventTime().getText()
						+ "-" + sortedDay.get(ctr).getEhour() + ":");

				if (sortedDay.get(ctr).getEminute() < 10)
					mainView.getAgendaView().getLblEventTime()
							.setText(mainView.getAgendaView().getLblEventTime().getText() + "0"
									+ Integer.toString(sortedDay.get(ctr).getEminute()));
				else
					mainView.getAgendaView().getLblEventTime()
							.setText(mainView.getAgendaView().getLblEventTime().getText()
									+ Integer.toString(sortedDay.get(ctr).getEminute()));

				mainView.getAgendaView().getLblEventName()
						.setText(mainView.getAgendaView().getLblEventName().getText() + "</font>");
				mainView.getAgendaView().getLblEventTime()
						.setText(mainView.getAgendaView().getLblEventTime().getText() + "</font>");

			}
		}
	}

	private void refreshEventAgenda() {
		mainView.getAgendaView().getLblEventName().setText("No Upcoming Events");
		mainView.getAgendaView().getLblEventTime().setText("");

		sortedDay = eventH.getDayEvents(monthToday + 1, dayToday, yearToday);

		for (int ctr = 0; ctr < sortedDay.size(); ctr++) {
			if (!(sortedDay.get(ctr) instanceof ToDo) && dayToday == sortedDay.get(ctr).getDay()
					&& (monthToday + 1) == sortedDay.get(ctr).getMonth() && yearToday == sortedDay.get(ctr).getYear()) {
				if (mainView.getAgendaView().getLblEventName().getText().equalsIgnoreCase("No Upcoming Events")) {
					mainView.getAgendaView().getLblEventName().setText("<html><font color='"
							+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getEvent());
					mainView.getAgendaView().getLblEventTime().setText("<html><font color='"
							+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getShour() + ":");
				} else {
					mainView.getAgendaView().getLblEventName()
							.setText(mainView.getAgendaView().getLblEventName().getText() + "<br><font color='"
									+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getEvent());
					mainView.getAgendaView().getLblEventTime()
							.setText(mainView.getAgendaView().getLblEventTime().getText() + "<br><font color='"
									+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getShour() + ":");
				}

				if (sortedDay.get(ctr).getSminute() < 10)
					mainView.getAgendaView().getLblEventTime()
							.setText(mainView.getAgendaView().getLblEventTime().getText() + "0"
									+ Integer.toString(sortedDay.get(ctr).getSminute()));
				else
					mainView.getAgendaView().getLblEventTime()
							.setText(mainView.getAgendaView().getLblEventTime().getText()
									+ Integer.toString(sortedDay.get(ctr).getSminute()));

				mainView.getAgendaView().getLblEventTime().setText(mainView.getAgendaView().getLblEventTime().getText()
						+ "-" + sortedDay.get(ctr).getEhour() + ":");

				if (sortedDay.get(ctr).getEminute() < 10)
					mainView.getAgendaView().getLblEventTime()
							.setText(mainView.getAgendaView().getLblEventTime().getText() + "0"
									+ Integer.toString(sortedDay.get(ctr).getEminute()));
				else
					mainView.getAgendaView().getLblEventTime()
							.setText(mainView.getAgendaView().getLblEventTime().getText()
									+ Integer.toString(sortedDay.get(ctr).getEminute()));

				mainView.getAgendaView().getLblEventName()
						.setText(mainView.getAgendaView().getLblEventName().getText() + "</font>");
				mainView.getAgendaView().getLblEventTime()
						.setText(mainView.getAgendaView().getLblEventTime().getText() + "</font>");

			}
		}
	}

	private void refreshTaskAgenda() {
		mainView.getAgendaView().getLblEventName().setText("No Upcoming Events");
		mainView.getAgendaView().getLblEventTime().setText("");

		sortedDay = eventH.getDayEvents(monthToday + 1, dayToday, yearToday);

		for (int ctr = 0; ctr < sortedDay.size(); ctr++) {
			if (sortedDay.get(ctr) instanceof ToDo && dayToday == sortedDay.get(ctr).getDay()
					&& (monthToday + 1) == sortedDay.get(ctr).getMonth() && yearToday == sortedDay.get(ctr).getYear()) {
				if (mainView.getAgendaView().getLblEventName().getText().equalsIgnoreCase("No Upcoming Events")) {
					mainView.getAgendaView().getLblEventName().setText("<html><font color='"
							+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getEvent());
					mainView.getAgendaView().getLblEventTime().setText("<html><font color='"
							+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getShour() + ":");
				} else {
					mainView.getAgendaView().getLblEventName()
							.setText(mainView.getAgendaView().getLblEventName().getText() + "<br><font color='"
									+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getEvent());
					mainView.getAgendaView().getLblEventTime()
							.setText(mainView.getAgendaView().getLblEventTime().getText() + "<br><font color='"
									+ sortedDay.get(ctr).getColorName() + "'>" + sortedDay.get(ctr).getShour() + ":");
				}

				if (sortedDay.get(ctr).getSminute() < 10)
					mainView.getAgendaView().getLblEventTime()
							.setText(mainView.getAgendaView().getLblEventTime().getText() + "0"
									+ Integer.toString(sortedDay.get(ctr).getSminute()));
				else
					mainView.getAgendaView().getLblEventTime()
							.setText(mainView.getAgendaView().getLblEventTime().getText()
									+ Integer.toString(sortedDay.get(ctr).getSminute()));

				mainView.getAgendaView().getLblEventTime().setText(mainView.getAgendaView().getLblEventTime().getText()
						+ "-" + sortedDay.get(ctr).getEhour() + ":");

				if (sortedDay.get(ctr).getEminute() < 10)
					mainView.getAgendaView().getLblEventTime()
							.setText(mainView.getAgendaView().getLblEventTime().getText() + "0"
									+ Integer.toString(sortedDay.get(ctr).getEminute()));
				else
					mainView.getAgendaView().getLblEventTime()
							.setText(mainView.getAgendaView().getLblEventTime().getText()
									+ Integer.toString(sortedDay.get(ctr).getEminute()));

				mainView.getAgendaView().getLblEventName()
						.setText(mainView.getAgendaView().getLblEventName().getText() + "</font>");
				mainView.getAgendaView().getLblEventTime()
						.setText(mainView.getAgendaView().getLblEventTime().getText() + "</font>");

			}
		}
	}

	//////////////////////////// ACTION LISTENERS////////////////////////////
	private class btnPrev_Action implements ActionListener {

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

			refreshCalendar();
			refreshAgenda();
			refreshDay();
		}
	}

	private class btnNext_Action implements ActionListener {

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

			refreshCalendar();
			refreshAgenda();
			refreshDay();
		}
	}

//	private class btnCreate_Action implements ActionListener {
//		public void actionPerformed(ActionEvent e) {
//			mainView.getDayView().setVisible(false);
//			mainView.getAgendaView().setVisible(false);
//			mainView.getWeekView().setVisible(false);
//			mainView.getCreateView().setVisible(true);
//		}
//	}

	private class btnDay_Action implements ActionListener {
		public void actionPerformed(ActionEvent e) {
//			mainView.getCreateView().setVisible(false);
			mainView.getAgendaView().setVisible(false);
			mainView.getWeekView().setVisible(false);
			mainView.getDayView().setVisible(true);
		}
	}
	
	private class btnWeek_Action implements ActionListener{
		public void actionPerformed(ActionEvent e){
//			mainView.getCreateView().setVisible(false);
			mainView.getDayView().setVisible(false);
			mainView.getAgendaView().setVisible(false);
			mainView.getWeekView().setVisible(true);
		}
	}

	private class btnAgenda_Action implements ActionListener {
		public void actionPerformed(ActionEvent e) {
//			mainView.getCreateView().setVisible(false);
			mainView.getAgendaView().setVisible(true);
			mainView.getWeekView().setVisible(false);
			mainView.getDayView().setVisible(false);
		}
	}

	private class scrollPanelCal_Action implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
					"October", "November", "December" };

			col = mainView.getCalendarView().getCalendarTable().getSelectedColumn();
			row = mainView.getCalendarView().getCalendarTable().getSelectedRow();
			if (mainView.getCalendarView().getCalendarTable().getValueAt(row, col) != null)
				dayToday = (Integer) mainView.getCalendarView().getCalendarTable().getValueAt(row, col);

			mainView.getHeaderView().getDateLabel().setText(months[monthToday] + " " + dayToday + ", " + yearToday);
//			mainView.getCreateView().getTextFieldDate().setText((monthToday+1) + "/" + dayToday + "/" + yearToday);
			refreshAgenda();
			refreshDay();
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

//	private class btnToday_Action implements ActionListener {
//		public void actionPerformed(ActionEvent e) {
//			monthToday = cal.get(GregorianCalendar.MONTH);
//			yearToday = cal.get(GregorianCalendar.YEAR);
//			dayToday = cal.get(GregorianCalendar.DAY_OF_MONTH);
//			sIndex = 100;
//
//			mainView.getDayView().setVisible(true);
//			mainView.getAgendaView().setVisible(false);
//			mainView.getCreateView().setVisible(false);
//
//			refreshCalendar();
//			refreshAgenda();
//			refreshDay();
//		}
//	}

//	private class rdBtnEvent_Action implements ActionListener {
//		public void actionPerformed(ActionEvent e) {
//			mainView.getCreateView().getComboBoxFrom().setVisible(true);
//			mainView.getCreateView().getComboBoxTo().setVisible(true);
//			mainView.getCreateView().getLabelTo().setVisible(true);
//		}
//	}

//	private class rdBtnTask_Action implements ActionListener {
//		public void actionPerformed(ActionEvent e) {
//			mainView.getCreateView().getComboBoxFrom().setVisible(true);
//			mainView.getCreateView().getComboBoxTo().setVisible(false);
//			mainView.getCreateView().getLabelTo().setVisible(false);
//		}
//	}

	private class cmbYear_Action implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (mainView.getCalendarView().getCmbYear().getSelectedItem() != null) {
				String selectedYear = mainView.getCalendarView().getCmbYear().getSelectedItem().toString();
				yearToday = Integer.parseInt(selectedYear);
				sIndex = mainView.getCalendarView().getCmbYear().getSelectedIndex();
				refreshCalendar();
				refreshAgenda();
				refreshDay();
			}
		}
	}

//	private class btnSave_Action implements ActionListener {
//		public void actionPerformed(ActionEvent e) {
//			int i = 0;
//			String[] dates = mainView.getCreateView().getTextFieldDate().getText().split("/");
//			int month = Integer.parseInt(dates[0]);
//			int day = Integer.parseInt(dates[1]);
//			int year = Integer.parseInt(dates[2]);
//			String[] stime = mainView.getCreateView().getComboBoxFrom().getSelectedItem().toString().split(":");
//			int shour = Integer.parseInt(stime[0]);
//			int sminute = Integer.parseInt(stime[1]);
//			String[] etime = mainView.getCreateView().getComboBoxTo().getSelectedItem().toString().split(":");
//			int ehour = Integer.parseInt(etime[0]);
//			int eminute = Integer.parseInt(etime[1]);
//			if (mainView.getCreateView().getrdBtnTask().isSelected()) {
//				// determines type of input before adding
//
//				if (sminute == 0) {
//					ehour = shour;
//					eminute = 30;
//				} else {
//					ehour = shour + 1;
//					eminute = 0;
//				}
//
//				for (i = 0; i < eventH.getCalendarItems().size(); i++) {
//					if (year == eventH.getCalendarItems().get(i).getYear()
//							&& month == eventH.getCalendarItems().get(i).getMonth()
//							&& day == eventH.getCalendarItems().get(i).getDay()
//							&& ((shour == eventH.getCalendarItems().get(i).getShour()
//									&& sminute == eventH.getCalendarItems().get(i).getSminute())
//									|| (ehour == eventH.getCalendarItems().get(i).getEhour()
//											&& eminute == eventH.getCalendarItems().get(i).getEminute()))) {
//						JOptionPane.showMessageDialog(null, "Conflict arose.");
//						break;
//					}
//				}
//
//				if (i == eventH.getCalendarItems().size()) {
//					eventH.addTask(year, month, day, mainView.getCreateView().getTextFieldEvent().getText(), shour,
//							sminute, ehour, eminute, false);
//
//					JOptionPane.showMessageDialog(null, "Done Adding a Task!");
//
//					refreshAgenda();
//					refreshDay();
//				}
//
//			} else {
//				for (i = 0; i < eventH.getCalendarItems().size(); i++) {
//					if (year == eventH.getCalendarItems().get(i).getYear()
//							&& month == eventH.getCalendarItems().get(i).getMonth()
//							&& day == eventH.getCalendarItems().get(i).getDay()
//							&& ((shour == eventH.getCalendarItems().get(i).getShour()
//									&& sminute == eventH.getCalendarItems().get(i).getSminute())
//									|| (ehour == eventH.getCalendarItems().get(i).getEhour()
//											&& eminute == eventH.getCalendarItems().get(i).getEminute())
//									|| ((shour >= eventH.getCalendarItems().get(i).getShour())
//											&& (shour <= eventH.getCalendarItems().get(i).getEhour())
//											|| (ehour >= eventH.getCalendarItems().get(i).getShour())
//													&& (ehour <= eventH.getCalendarItems().get(i).getEhour()))
//									|| ((eventH.getCalendarItems().get(i).getShour() >= shour)
//											&& (eventH.getCalendarItems().get(i).getShour() <= ehour)
//											|| (eventH.getCalendarItems().get(i).getEhour() >= shour)
//													&& (eventH.getCalendarItems().get(i).getEhour() <= ehour)))) {
//						JOptionPane.showMessageDialog(null, "Conflict arose.");
//						break;
//					}
//				}
//
//				if (i == eventH.getCalendarItems().size()) {
//					eventH.addCalendarItem(year, month, day, "",shour, sminute, ehour, eminute, row);
//
//					JOptionPane.showMessageDialog(null, "Done Setting for Appointments!");
//
//					refreshAgenda();
//					refreshDay();
//				}
//			}

//			mainView.getCreateView().getTextFieldDate().setText("");
//			mainView.getCreateView().getTextFieldEvent().setText("");
//			mainView.getCreateView().getComboBoxFrom().setSelectedIndex(0);
//			mainView.getCreateView().getComboBoxTo().setSelectedIndex(0);

		}
//	}

//	private class btnDiscard_Action implements ActionListener {
//		public void actionPerformed(ActionEvent e) {
//			mainView.getCreateView().getTextFieldDate().setText("");
//			mainView.getCreateView().getTextFieldEvent().setText("");
//			mainView.getCreateView().getComboBoxFrom().setSelectedIndex(0);
//			mainView.getCreateView().getComboBoxTo().setSelectedIndex(0);
//		}
//	}

//	private void markAsDone(String event) {
//		for (CalendarItem cI : eventH.getCalendarItems()) {
//			if (cI.getEvent().equalsIgnoreCase(event)) {
//				if (cI instanceof ToDo) {
//					if (((ToDo) cI).isDone()) {
//						int decide = JOptionPane.showConfirmDialog(null, "Delete?");
//						if (decide == JOptionPane.YES_OPTION) {
//							eventH.getCalendarItems().remove(cI);
//						}
//					} else {
//						int decide = JOptionPane.showConfirmDialog(null, "Mark as Done?");
//						if (decide == JOptionPane.YES_OPTION) {
//							cI.setColor("GRAY");
//							((ToDo) cI).setDone(true);
//						}
//					}
//					refreshDay();
//					refreshAgenda();
//				}
//				break;
//			}
//		}
//	}
	
//	private class scrollPanelDay_Action implements MouseListener {
//
//		@Override
//		public void mouseClicked(MouseEvent arg0) {
//			int rowDay = mainView.getDayView().getDayTable().getSelectedRow();
//			markAsDone(mainView.getDayView().getDayTable().getValueAt(rowDay, 1).toString());
//		}
//		
//		@Override
//		public void mouseEntered(MouseEvent arg0) {}
//		@Override
//		public void mouseExited(MouseEvent arg0) {}
//		@Override
//		public void mousePressed(MouseEvent arg0) {}
//		@Override
//		public void mouseReleased(MouseEvent arg0) {}
//
//	}
//}
