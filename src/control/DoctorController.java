package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.JOptionPane;

import model.Appointment;
import server.AppointmentManager;
import server.DoctorManager;
import view.DoctorMainView;
import view.UpdateFrameView;

import model.Doctor;
public class DoctorController extends CalendarProgram
{
	private DoctorManager doctorManager;
	
	public DoctorController(AppointmentManager apptManager)
	{	
		super(new DoctorMainView(), apptManager, new Doctor());
                
		doctorManager = new DoctorManager();
		Doctor d = doctorManager.getDoctor(super.getMainView().getAppID());
		super.getPerson().setID(d.getID());
        	super.getPerson().setName(d.getName());
		
//		calendarProgram = new ArrayList<CalendarProgram>();
//		int maxCtr = doctorManager.getAllDoctor().size();
//		for(int ctr = 0; ctr < maxCtr; ctr++){
//			calendarProgram.add(new CalendarProgram());
//		}
		
		mainView.getDayView().getDayTable().addMouseListener(new scrollPanelDay_Action());
		mainView.getWeekView().getWeekTable().addMouseListener(new scrollPanelWeek_Action());
	}
	
	protected void modify(String event) {
		String[] options = {"Free Up Slot","Change Date or Time"};
		int choice = JOptionPane.showOptionDialog(null, //Component parentComponent
                "", //Object message,
                "Choose an option", //String title
                JOptionPane.YES_NO_OPTION, //int optionType
                JOptionPane.INFORMATION_MESSAGE, //int messageType
                null, //Icon icon,
                options, //Object[] options,
                "Free Up Slot");//Object initialValue 
		if(choice == 0 ){
			for (Appointment cI : eventH.getAppointments()) {
				if (cI.getAppointmentName().equalsIgnoreCase(event)) {
					if(cI.isStatus() == false){
						eventH.getAppointmentManager().deleteAppointment(cI.getAppointmentID());
						break;
					}
					else{
						JOptionPane.showMessageDialog(null, "Cannot Delete Reserved Appointmnet");
						break;
					}
				}
			}
		}
		else{
			for (int ctr= 0 ;ctr< eventH.getAppointments().size(); ctr++) {
				Appointment cI = eventH.getAppointments().get(ctr);
				if (cI.getAppointmentName().equalsIgnoreCase(event)) {
					
					int value = ctr;
					UpdateFrameView ufv = new UpdateFrameView();
					ufv.getUpdateView().getLblNewLabel().setText(cI.getAppointmentDate() + "/" + cI.getLocalTimeIn() + "-" + cI.getLocalTimeOut() + " " + cI.getAppointmentName());
					ufv.getUpdateView().getBtnSave().addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							String[] dates = ufv.getUpdateView().getTextFieldDate().getText().split("/");
							String[] stime = ufv.getUpdateView().getComboBoxFrom().getSelectedItem().toString().split(":");
							int shour = Integer.parseInt(stime[0]);
							int sminute = Integer.parseInt(stime[1]);
							String[] etime = ufv.getUpdateView().getComboBoxTo().getSelectedItem().toString().split(":");
							int ehour = Integer.parseInt(etime[0]);
							int eminute = Integer.parseInt(etime[1]);
							
							eventH.getAppointments().get(value).setAppointmentDate(LocalDate.of(Integer.parseInt(dates[2]), Integer.parseInt(dates[0]), Integer.parseInt(dates[1])));
							eventH.getAppointments().get(value).setLocalTimeIn(LocalTime.of(shour, sminute));
							eventH.getAppointments().get(value).setLocalTimeOut(LocalTime.of(ehour, eminute));
							eventH.getAppointments().get(value).setStartRowDay();
							eventH.getAppointments().get(value).setEndRowDay();
							eventH.getAppointments().get(value).setColWeek();
							eventH.getAppointmentManager().updateAppointment(eventH.getAppointments().get(value));
							
							ufv.dispose();
						}
					});
				}
			}
		}
		
		
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
