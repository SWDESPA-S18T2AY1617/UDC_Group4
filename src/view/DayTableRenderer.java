package view;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DayTableRenderer extends DefaultTableCellRenderer {
	 private ArrayList<Integer> startRowList;
	 private ArrayList<Integer> endRowList;

	    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
	        super.getTableCellRendererComponent(table, value, selected, focused, row, column);
	        setBackground(Color.WHITE);

	        setBorder(null);
	        setForeground(Color.black);
	        return this;
	    }

	    public DayTableRenderer(ArrayList<Integer> startRowList, ArrayList<Integer> endRowList) {
	        this.startRowList = startRowList;
	        this.endRowList = endRowList;
	    }
}
