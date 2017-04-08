package control;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;

import model.Appointment;
import server.AppointmentManager;
import server.ClientManager;
import view.ClientMainView;

public class ClientController extends CalendarProgram
{
	private ClientManager clientManager;
	
	public ClientController(AppointmentManager apptManager)
	{	
		super(new ClientMainView(), apptManager);
		clientManager = new ClientManager();
		super.getAppointmentHandler().setAppointments(clientManager.getClientAppointments());
//		calendarProgram = new ArrayList<CalendarProgram>();
//		int maxCtr = clientManager.getAllClient().size();
//		for(int ctr = 0; ctr < maxCtr; ctr++){
//			calendarProgram.add(new CalendarProgram(new ClientMainView()));
//		}
		mainView.getDayView().getDayTable().addMouseListener(new scrollPanelDay_Action());
		mainView.getWeekView().getWeekTable().addMouseListener(new scrollPanelWeek_Action());
	}
	
	protected void modify(String event) {
		
		for (int ctr= 0 ;ctr< eventH.getAppointments().size(); ctr++) {
			Appointment cI = eventH.getAppointments().get(ctr);
			if (cI.getAppointmentName().equalsIgnoreCase(event) && cI.isStatus() == false) {
				String name = JOptionPane.showInputDialog("Appointment name?");
				if (name != null){	
					eventH.getAppointments().get(ctr).setAppointmentName(name);
					eventH.getAppointments().get(ctr).setColor("Blue");
					cI.setStatus(true);
				}
			}
			else
			{
				int decide = JOptionPane.showConfirmDialog(null, "Cancel?");
				if (decide == JOptionPane.YES_OPTION) {
					eventH.getAppointments().get(ctr).setAppointmentName("Free Appointment");
					eventH.getAppointments().get(ctr).setColor("Red");
					cI.setStatus(false);
				}
			}
		}
		
		refreshDay();
		refreshWeek();
		refreshAgenda();
		
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
	

}
