package view.clientView;

import javax.swing.table.*;

public class DTModel extends DefaultTableModel {

    public boolean isCellEditable(int rowIndex, int mColIndex) {
        return true;
    }

    public DTModel() {
        String[] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; //All headers
        for (int i = 0; i < 7; i++) {
            this.addColumn(headers[i]);
        }
        
        this.setColumnCount(7);
        this.setRowCount(6);
    }
}
