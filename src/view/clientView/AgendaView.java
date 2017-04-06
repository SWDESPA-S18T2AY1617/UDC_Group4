package view.clientView;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class AgendaView extends JPanel {

	private JLabel lblEventTime;
	private JLabel lblEventName;

	public AgendaView() {
		setLayout(null);

		setLblEventTime(new JLabel(""));
		getLblEventTime().setBounds(20, 6, 90, 270);
		add(getLblEventTime());

		setLblEventName(new JLabel("No Upcoming Events"));
		getLblEventName().setBounds(120, 6, 300, 270);
		add(getLblEventName());
	}

	public JLabel getLblEventTime() {
		return lblEventTime;
	}

	public void setLblEventTime(JLabel lblEventTime) {
		this.lblEventTime = lblEventTime;
		lblEventTime.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEventTime.setVerticalAlignment(SwingConstants.TOP);
	}

	public JLabel getLblEventName() {
		return lblEventName;
	}

	public void setLblEventName(JLabel lblEventName) {
		this.lblEventName = lblEventName;
		lblEventName.setVerticalAlignment(SwingConstants.TOP);
	}

}
