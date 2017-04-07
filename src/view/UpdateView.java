package view;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class UpdateView extends JPanel {

	private JTextField textFieldDate;
	private JComboBox<String> comboBoxFrom;
	private JComboBox<String> comboBoxTo;
	private JLabel lblTo;
	private JButton btnSave;
	private JLabel lblNewLabel;

	/**
	 * Create the panel.
	 */
	public UpdateView() {
		setLayout(null);

		// rdbtnFree = new JRadioButton("Event");
		// rdbtnFree.setBounds(30, 70, 75, 23);
		// add(rdbtnFree);
		//
		// rdbtnReserved = new JRadioButton("Task");
		// rdbtnReserved.setBounds(138, 70, 75, 23);
		// add(rdbtnReserved);
		//
		// ButtonGroup group = new ButtonGroup();
		// group.add(rdbtnFree);
		// group.add(rdbtnReserved);

		setTextFieldDate(new JTextField());
		getTextFieldDate().setToolTipText("mm/dd/yyyy");
		getTextFieldDate().setBounds(40, 100, 130, 30);

		add(getTextFieldDate());
		getTextFieldDate().setColumns(10);

		setComboBoxFrom(new JComboBox<String>());
		getComboBoxFrom().setBounds(180, 100, 90, 30);
		add(getComboBoxFrom());

		setComboBoxTo(new JComboBox<String>());
		getComboBoxTo().setBounds(300, 100, 90, 30);
		add(getComboBoxTo());

		lblTo = new JLabel("to");
		lblTo.setBounds(275, 100, 25, 30);
		add(lblTo);

		setBtnSave(new JButton("UPDATE"));
		getBtnSave().setForeground(Color.RED);
		getBtnSave().setBackground(Color.WHITE);
		getBtnSave().setBounds(285, 150, 90, 30);
		add(getBtnSave());
		{
			setLblNewLabel(new JLabel(""));
			getLblNewLabel().setBounds(40, 40, 350, 50);
			add(getLblNewLabel());
		}

		for (int i = 0; i < 24; i++) {
			getComboBoxFrom().addItem(i + ":00");
			getComboBoxFrom().addItem(i + ":30");
			getComboBoxTo().addItem(i + ":30");
			getComboBoxTo().addItem((i + 1) + ":00");
		}
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

	public JLabel getLblNewLabel() {
		return lblNewLabel;
	}

	public void setLblNewLabel(JLabel lblNewLabel) {
		this.lblNewLabel = lblNewLabel;
	}
}
