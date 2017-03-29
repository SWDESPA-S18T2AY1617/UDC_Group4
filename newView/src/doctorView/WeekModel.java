package doctorView;

import javax.swing.table.DefaultTableModel;

public class WeekModel extends DefaultTableModel{
	 public boolean isCellEditable(int rowIndex, int mColIndex) {
	        return true;
	    }

	    public WeekModel() {
	        String[] headers = {"", "M", "T", "W", "H", "F"}; //All headers
	        for (int i = 0; i < 6; i++) {
	            this.addColumn(headers[i]);
	        }
	        
	        this.setColumnCount(6);
	        this.setRowCount(48);
	    }
}