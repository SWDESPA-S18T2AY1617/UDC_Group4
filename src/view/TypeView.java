package view;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;

public class TypeView extends JPanel {
	private JCheckBox freeCheckBox;
	private JCheckBox reservedCheckBox;
	private final JLabel viewCB = new JLabel("View");
	private JComboBox<String> comboBox = new JComboBox<String>();

	public TypeView() {
		setLayout(null);
		setReservedCheckBox(new JCheckBox("All"));
		setFreeCheckBox(new JCheckBox("Reservations"));

		add(viewCB);
		add(getReservedCheckBox());
		add(getFreeCheckBox());

		viewCB.setBounds(10, 10, 50, 30);
		getReservedCheckBox().setBounds(10, 30, 100, 30);
		getFreeCheckBox().setBounds(10, 60, 130, 30);

		getComboBox().setBounds(10, 95, 100, 27);
		add(getComboBox());
	}

	public JCheckBox getFreeCheckBox() {
		return freeCheckBox;
	}

	public void setFreeCheckBox(JCheckBox freeCheckBox) {
		this.freeCheckBox = freeCheckBox;
	}

	public JCheckBox getReservedCheckBox() {
		return reservedCheckBox;
	}

	public void setReservedCheckBox(JCheckBox reservedCheckBox) {
		this.reservedCheckBox = reservedCheckBox;
	}

	public JComboBox<String> getComboBox() {
		return comboBox;
	}

	public void setComboBox(JComboBox<String> comboBox) {
		this.comboBox = comboBox;
	}
}
