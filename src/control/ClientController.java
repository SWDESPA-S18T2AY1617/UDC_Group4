package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.Appointment;
import server.AppointmentManager;
import server.ClientManager;
import view.ClientMainView;

import model.Client;
public class ClientController extends CalendarProgram
{
	private ClientManager clientManager;
	
	public ClientController(AppointmentManager apptManager)
	{	
		super(new ClientMainView(), apptManager, new Client());
                
		clientManager = new ClientManager();
		Client c = clientManager.getClient(super.getMainView().getAppID());
		super.getPerson().setID(c.getID());
        	super.getPerson().setName(c.getName());
		
//		calendarProgram = new ArrayList<CalendarProgram>();
//		int maxCtr = clientManager.getAllClient().size();
//		for(int ctr = 0; ctr < maxCtr; ctr++){
//			calendarProgram.add(new CalendarProgram(new ClientMainView()));
//		}
        	
		mainView.getTypeView().getComboBox().addItem("All Doctors");
		for(int ctr=0;ctr < apptManager.getAllController().size(); ctr++)
			if(apptManager.getAllController().get(ctr) instanceof DoctorController)	{
				mainView.getTypeView().getComboBox().addItem(apptManager.getAllController().get(ctr).getPerson().getName()+" "+apptManager.getAllController().get(ctr).getPerson().getID());
			}
		mainView.getTypeView().getComboBox().setSelectedIndex(-1);
    		
    	mainView.getTypeView().getComboBox().addActionListener(new cmbView_Action());		
		mainView.getDayView().getDayTable().addMouseListener(new scrollPanelDay_Action());
		mainView.getWeekView().getWeekTable().addMouseListener(new scrollPanelWeek_Action());
	}
	
	protected void modify(String event) {
		
		for (int ctr= 0 ;ctr< eventH.getAppointments().size(); ctr++) {
			Appointment cI = eventH.getAppointments().get(ctr);
			if (cI.getAppointmentName().equalsIgnoreCase(event)) {
				if(cI.isStatus() == false){
					String name = JOptionPane.showInputDialog("Appointment name?");
					if (name != null){	
						eventH.getAppointments().get(ctr).setAppointmentName(name);
						eventH.getAppointments().get(ctr).setClientID(mainView.getAppID());
						eventH.getAppointments().get(ctr).setColor("Blue");
						eventH.getAppointments().get(ctr).setStatus(true);
						eventH.getAppointmentManager().updateAppointment(eventH.getAppointments().get(ctr));
					}
				}
				else
				{
					int decide = JOptionPane.showConfirmDialog(null, "Cancel?");
					if (decide == JOptionPane.YES_OPTION) {
						eventH.getAppointments().get(ctr).setAppointmentName("Free Appointment");
						eventH.getAppointments().get(ctr).setClientID(1);
						eventH.getAppointments().get(ctr).setColor("Red");
						eventH.getAppointments().get(ctr).setStatus(false);
						eventH.getAppointmentManager().cancelAppointment(eventH.getAppointments().get(ctr));
					}
				}
			}
			
		}
		
		refreshDay(theFilter);
		refreshWeek(theFilter);
		refreshdAgenda(theFilter);
		refreshwAgenda(theFilter);
		
	}

	protected class scrollPanelDay_Action implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			int rowDay = mainView.getDayView().getDayTable().getSelectedRow();
			modify(mainView.getDayView().getDayTable().getValueAt(rowDay, 1).toString());
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
	
	protected class scrollPanelWeek_Action implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			int rowDay = mainView.getWeekView().getWeekTable().getSelectedRow();
			int colDay = mainView.getWeekView().getWeekTable().getSelectedColumn();
			modify(mainView.getWeekView().getWeekTable().getValueAt(rowDay, colDay).toString());
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
					eventH.setAppointments(eventH.getAppointmentManager().getAllAppointments());
					refreshCalendar();
					refreshdAgenda("FREE");
					refreshwAgenda("FREE");
					refreshDay("FREE");
					refreshWeek("FREE");
				}else if(selectedDoctor.equalsIgnoreCase("Doctor 1 1")){
					ArrayList<Appointment> doctor1 = new ArrayList<Appointment>();
					eventH.setAppointments(eventH.getAppointmentManager().getAllAppointments());
					
					for(int i=0;i<getAppointmentHandler().getAppointments().size();i++){
						if(getAppointmentHandler().getAppointments().get(i).getDoctorID() == 1 && getAppointmentHandler().getAppointments().get(i).isStatus() == false)
							doctor1.add(getAppointmentHandler().getAppointments().get(i));
					}
					refresh(doctor1);
				}else{
					ArrayList<Appointment> doctor2 = new ArrayList<Appointment>();
					eventH.setAppointments(eventH.getAppointmentManager().getAllAppointments());
					
					for(int i=0;i<getAppointmentHandler().getAppointments().size();i++){
						if(getAppointmentHandler().getAppointments().get(i).getDoctorID() == 2 && getAppointmentHandler().getAppointments().get(i).isStatus() == false)
							doctor2.add(getAppointmentHandler().getAppointments().get(i));
					}
					refresh(doctor2);
				}
				getAppointmentHandler().setAppointments(holdAllAppointments);
			}	
		}
	}


}
