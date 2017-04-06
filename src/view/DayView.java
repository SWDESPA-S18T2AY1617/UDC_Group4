package view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.ListSelectionModel;

public class DayView extends JPanel {

	private JTable dayTable;
	private JScrollPane dayScroll;

	/**
	 * Create the panel.
	 */
	public DayView(){
		String[] columnNames = { "Time", "Event" };
		String[][] time = { { "0:00", "" }, { "", "" }, { "1:00", "" }, { "", "" }, { "2:00", "" }, { "", "" },
				{ "3:00", "" }, { "", "" }, { "4:00", "" }, { "", "" }, { "5:00", "" }, { "", "" }, { "6:00", "" },
				{ "", "" }, { "7:00", "" }, { "", "" }, { "8:00", "" }, { "", "" }, { "9:00", "" }, { "", "" },
				{ "10:00", "" }, { "", "" }, { "11:00", "" }, { "", "" }, { "12:00", "" }, { "", "" }, { "13:00", "" },
				{ "", "" }, { "14:00", "" }, { "", "" }, { "15:00", "" }, { "", "" }, { "16:00", "" }, { "", "" },
				{ "17:00", "" }, { "", "" }, { "18:00", "" }, { "", "" }, { "19:00", "" }, { "", "" }, { "20:00", "" },
				{ "", "" }, { "21:00", "" }, { "", "" }, { "22:00", "" }, { "", "" }, { "23:00", "" }, { "", "" } };

		setDayTable(new JTable(time, columnNames));
		getDayTable().setColumnSelectionAllowed(true);
		getDayTable().setCellSelectionEnabled(true);
		getDayTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getDayTable().setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		getDayTable().setRowHeight(20);
		getDayTable().setBounds(0, 0, 400, 350);

		TableColumnModel columnModel = getDayTable().getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(50);
		columnModel.getColumn(1).setPreferredWidth(300);
		setLayout(new BorderLayout());
		

		dayScroll = new JScrollPane(getDayTable());
		dayScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		dayScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(dayScroll);
		
	}

	public JTable getDayTable() {
		return dayTable;
	}

	public void setDayTable(JTable dayTable) {
		this.dayTable = dayTable;
	}
}
