package view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class CalendarView extends JPanel {
	private final JLabel monthLabel;
	private final JLabel yearLabel;
	private final JButton btnPrev;
	private final JButton btnNext;
	private final JButton btnCreate;
	private final JScrollPane scrollCalendarTable;
	private final JComboBox<String> cmbYear;
	private final JTable calendarTable;

	public void initialize() {
		setBorder(BorderFactory.createTitledBorder("Calendar"));

		add(getMonthLabel());
		add(getYearLabel());
		add(getBtnPrev());
		add(getBtnNext());
		add(getBtnCreate());
		add(getCmbYear());
		add(scrollCalendarTable);

		getCalendarTable().getParent().setBackground(getCalendarTable().getBackground()); // Set
																							// background
		getCalendarTable().getTableHeader().setResizingAllowed(false);
		getCalendarTable().getTableHeader().setReorderingAllowed(false);

		getCalendarTable().setColumnSelectionAllowed(true);
		getCalendarTable().setRowSelectionAllowed(true);
		getCalendarTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getCalendarTable().setRowHeight(15);

		getCmbYear().setBounds(120, 25, 90, 30);
		getBtnPrev().setBounds(15, 183, 50, 30);
		getBtnNext().setBounds(155, 183, 50, 30);
		scrollCalendarTable.setBounds(20, 60, 195, 113);
		getBtnCreate().setBounds(20, 100, 80, 35);
	}

	public CalendarView() {
		monthLabel = new JLabel("try");
		yearLabel = new JLabel();
		btnPrev = new JButton("<<");
		btnNext = new JButton(">>");
		btnCreate = new JButton("CREATE");
		calendarTable = new JTable();
		cmbYear = new JComboBox<String>();
		scrollCalendarTable = new JScrollPane(getCalendarTable());

		this.initialize();

		this.setSize(250, 220);
		this.setLayout(null);
	}

	public JLabel getMonthLabel() {
		return monthLabel;
	}

	public JLabel getYearLabel() {
		return yearLabel;
	}

	public JButton getBtnPrev() {
		return btnPrev;
	}

	public JButton getBtnNext() {
		return btnNext;
	}

	public JButton getBtnCreate() {
		return btnCreate;
	}

	public JComboBox<String> getCmbYear() {
		return cmbYear;
	}

	public JTable getCalendarTable() {
		return calendarTable;
	}

}
