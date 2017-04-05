package view.clientView;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.ListSelectionModel;


public class WeekView extends JPanel{
	private JTable weekTable;
	private JScrollPane weekScroll;

	/**
	 * Create the panel.
	 */
	public WeekView(){
		String[] columnNames = {"", "M", "T", "W", "H", "F"};
		String[][] time = { { "0:00", "" }, { "", "" }, { "1:00", "" }, { "", "" }, { "2:00", "" }, { "", "" },
				{ "3:00", "" }, { "", "" }, { "4:00", "" }, { "", "" }, { "5:00", "" }, { "", "" }, { "6:00", "" },
				{ "", "" }, { "7:00", "" }, { "", "" }, { "8:00", "" }, { "", "" }, { "9:00", "" }, { "", "" },
				{ "10:00", "" }, { "", "" }, { "11:00", "" }, { "", "" }, { "12:00", "" }, { "", "" }, { "13:00", "" },
				{ "", "" }, { "14:00", "" }, { "", "" }, { "15:00", "" }, { "", "" }, { "16:00", "" }, { "", "" },
				{ "17:00", "" }, { "", "" }, { "18:00", "" }, { "", "" }, { "19:00", "" }, { "", "" }, { "20:00", "" },
				{ "", "" }, { "21:00", "" }, { "", "" }, { "22:00", "" }, { "", "" }, { "23:00", "" }, { "", "" } };

		setWeekTable(new JTable(time, columnNames));
		getWeekTable().setColumnSelectionAllowed(true);
		getWeekTable().setCellSelectionEnabled(true);
		getWeekTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getWeekTable().setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		getWeekTable().setRowHeight(20);
		getWeekTable().setBounds(0, 0, 400, 350);

		TableColumnModel columnModel = getWeekTable().getColumnModel();
		for(int i = 0; i<2; i++)
			columnModel.getColumn(i).setPreferredWidth(50);
		//columnModel.getColumn(1).setPreferredWidth(300);
		setLayout(new BorderLayout());
		

		weekScroll = new JScrollPane(getWeekTable());
		weekScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		weekScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(weekScroll);
		
	}

	public JTable getWeekTable() {
		return weekTable;
	}

	public void setWeekTable(JTable weekTable) {
		this.weekTable = weekTable;
	}
}
