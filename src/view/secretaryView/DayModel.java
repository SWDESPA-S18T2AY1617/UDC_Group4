package view.secretaryView;

import javax.swing.table.*;

public class DayModel extends DefaultTableModel{
	 public boolean isCellEditable(int rowIndex, int mColIndex) {
	        return true;
	    }

	    public DayModel() {
	        String[] headers = {"Time", "Appointment"}; //All headers
	        for (int i = 0; i < 2; i++) {
	            this.addColumn(headers[i]);
	        }
	        
	        this.setColumnCount(2);
	        this.setRowCount(48);
	    }
}
