package view.doctorView;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class WeekTableRenderer extends DefaultTableCellRenderer{
	private ArrayList<Integer> startRowList;
	 private ArrayList<Integer> endRowList;
	 private ArrayList<Color> colorList;

	    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
	        super.getTableCellRendererComponent(table, value, selected, focused, row, column);
	        setBackground(Color.WHITE);
	            
	        for (int i = 0; i < colorList.size(); i++) {
	        	//for(int j = 0; j < 5; j++)
	        		for(int k = startRowList.get(i); k < endRowList.get(i); k++){
	        			if(k == row)
	        				setBackground(colorList.get(i));
	            }
	        }
	        
	        
	        setBorder(null);
	        setForeground(Color.black);
	        return this;
	    }

	    public WeekTableRenderer(ArrayList<Integer> startRowList, ArrayList<Integer> endRowList, ArrayList<Color> colorList) {
	        this.startRowList = startRowList;
	        this.endRowList = endRowList;
	        this.colorList = colorList;
	    }
}

