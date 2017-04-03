package clientView;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Date;
import java.awt.event.ActionEvent;

public class CreateView extends JPanel {
	private JTextField textFieldDate;
	private JComboBox<String> comboBoxFrom;
	private JComboBox<String> comboBoxTo;
	private JLabel lblTo;
	private JCheckBox weeklyCheckBox;
	private final JLabel viewCB = new JLabel("Repeat:");
//	private JRadioButton rdbtnEvent;
//	private JRadioButton rdbtnTask;
	private JButton btnSave;

	/**
	 * Create the panel.
	 */
	public CreateView() {
		setLayout(null);

//		rdbtnFree = new JRadioButton("Event");
//		rdbtnFree.setBounds(30, 70, 75, 23);
//		add(rdbtnFree);
//
//		rdbtnReserved = new JRadioButton("Task");
//		rdbtnReserved.setBounds(138, 70, 75, 23);
//		add(rdbtnReserved);
//
//		ButtonGroup group = new ButtonGroup();
//		group.add(rdbtnFree);
//		group.add(rdbtnReserved);

		setTextFieldDate(new JTextField());
		setWeeklyCheckBox(new JCheckBox("Weekly"));
		getTextFieldDate().setToolTipText("mm/dd/yyyy");
		getTextFieldDate().setBounds(40, 100, 130, 30);
		
		add(viewCB);
		add(getWeeklyCheckBox());
		add(getTextFieldDate());
		getTextFieldDate().setColumns(10);
		
		viewCB.setBounds(130,150,50,30);
		getWeeklyCheckBox().setBounds(190,150,80,30);
		setComboBoxFrom(new JComboBox<String>());
		getComboBoxFrom().setBounds(180, 100, 90, 30);
		add(getComboBoxFrom());

		setComboBoxTo(new JComboBox<String>());
		getComboBoxTo().setBounds(300, 100, 90, 30);
		add(getComboBoxTo());

		lblTo = new JLabel("to");
		lblTo.setBounds(275, 100, 25, 30);
		add(lblTo);

		setBtnSave(new JButton("SET"));
		getBtnSave().setForeground(Color.RED);
		getBtnSave().setBackground(Color.WHITE);
		getBtnSave().setBounds(285, 150, 90, 30);
		add(getBtnSave());
	}

	public JComboBox<String> getComboBoxTo() {
		return comboBoxTo;
	}

	public void setComboBoxTo(JComboBox<String> comboBoxTo) {
		this.comboBoxTo = comboBoxTo;
	}

	public JComboBox<String> getComboBoxFrom() {
		return comboBoxFrom;
	}

	public void setComboBoxFrom(JComboBox<String> comboBoxFrom) {
		this.comboBoxFrom = comboBoxFrom;
	}

//	public JRadioButton getrdBtnEvent() {
//		return rdbtnEvent;
//	}
//
//	public JRadioButton getrdBtnTask() {
//		return rdbtnTask;
//	}

	public JLabel getLabelTo() {
		return lblTo;
	}

	public JButton getBtnSave() {
		return btnSave;
	}

	public void setBtnSave(JButton btnSave) {
		this.btnSave = btnSave;
	}

	public JTextField getTextFieldDate() {
		return textFieldDate;
	}

	public void setTextFieldDate(JTextField textFieldDate) {
		this.textFieldDate = textFieldDate;
	}

	public JCheckBox getWeeklyCheckBox() {
		return weeklyCheckBox;
	}

	public void setWeeklyCheckBox(JCheckBox weeklyCheckBox) {
		this.weeklyCheckBox = weeklyCheckBox;
	}

}
