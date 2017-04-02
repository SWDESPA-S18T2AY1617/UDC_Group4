package view.doctorView;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Date;
import java.awt.event.ActionEvent;

public class CreateView extends JPanel {
	private JTextField textFieldEvent;
	private JTextField textFieldDate;
	private JComboBox<String> comboBoxFrom;
	private JComboBox<String> comboBoxTo;
	private JLabel lblTo;
//	private JRadioButton rdbtnEvent;
//	private JRadioButton rdbtnTask;
	private JButton btnSave;
	private JButton btnDiscard;

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

		setTextFieldEvent(new JTextField());
		getTextFieldEvent().setToolTipText("Event Name");
		getTextFieldEvent().setBounds(30, 20, 300, 40);
		add(getTextFieldEvent());
		getTextFieldEvent().setColumns(10);

		setTextFieldDate(new JTextField());
		getTextFieldDate().setToolTipText("mm/dd/yyyy");
		getTextFieldDate().setBounds(40, 100, 130, 30);
		add(getTextFieldDate());
		getTextFieldDate().setColumns(10);

		setComboBoxFrom(new JComboBox<String>());
		getComboBoxFrom().setBounds(180, 100, 90, 30);
		add(getComboBoxFrom());
		getComboBoxFrom().setVisible(false);

		setComboBoxTo(new JComboBox<String>());
		getComboBoxTo().setBounds(300, 100, 90, 30);
		add(getComboBoxTo());
		getComboBoxTo().setVisible(false);

		lblTo = new JLabel("to");
		lblTo.setBounds(275, 100, 25, 30);
		add(lblTo);
		getLabelTo().setVisible(false);

		setBtnSave(new JButton("SET"));
		getBtnSave().setForeground(Color.RED);
		getBtnSave().setBackground(Color.WHITE);
		getBtnSave().setBounds(190, 150, 90, 30);
		add(getBtnSave());

		setBtnDiscard(new JButton("REMOVE"));
		getBtnDiscard().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		getBtnDiscard().setBounds(285, 150, 90, 30);
		add(getBtnDiscard());

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

	public JButton getBtnDiscard() {
		return btnDiscard;
	}

	public void setBtnDiscard(JButton btnDiscard) {
		this.btnDiscard = btnDiscard;
	}

	public JTextField getTextFieldDate() {
		return textFieldDate;
	}

	public void setTextFieldDate(JTextField textFieldDate) {
		this.textFieldDate = textFieldDate;
	}

	public JTextField getTextFieldEvent() {
		return textFieldEvent;
	}

	public void setTextFieldEvent(JTextField textFieldEvent) {
		this.textFieldEvent = textFieldEvent;
	}
}
