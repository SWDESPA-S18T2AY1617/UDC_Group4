package clientView;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TypeView extends JPanel{
	private JCheckBox freeCheckBox;
	private JCheckBox reservedCheckBox;
	private final JLabel viewCB = new JLabel("View");
	
	public TypeView()
	{
		setLayout(null);
		setReservedCheckBox(new JCheckBox("All"));
		setFreeCheckBox(new JCheckBox("Reservations"));
		
		add(viewCB);
		add(getReservedCheckBox());
		add(getFreeCheckBox());
		
		viewCB.setBounds(10,10,50,30);
		getReservedCheckBox().setBounds(10,30,100,30);
		getFreeCheckBox().setBounds(10,60,130,30);
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
}
