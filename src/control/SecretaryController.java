package control;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import model.Appointment;
import server.SecretaryManager;
import view.SecretaryMainView;

public class SecretaryController extends CalendarProgram
{
	private SecretaryManager secretaryManager;
	
	public SecretaryController()
	{
		super(new SecretaryMainView());
		secretaryManager = new SecretaryManager();
//		setCalendarProgram(new ArrayList<CalendarProgram>());
//		int maxCtr = secretaryManager.getAllSecretary().size();
//		for(int ctr = 0; ctr < maxCtr; ctr++){
//			getCalendarProgram().add(new CalendarProgram(new SecretaryMainView()));
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
