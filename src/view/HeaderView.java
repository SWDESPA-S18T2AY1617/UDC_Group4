package view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.FlowLayout;
import java.awt.Label;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;

public class HeaderView extends JPanel {

	private final JLabel productivityLabel = new JLabel("My Productivity Tool");
	private JButton dayBtn;
	private JButton weekBtn;
	private JButton dAgendaBtn;
	private JButton wAgendaBtn;
	private Label dateLabel;

	public HeaderView() {

		setLayout(null);
		 
		productivityLabel.setHorizontalAlignment(SwingConstants.CENTER);
		setDayBtn(new JButton("Day"));
		setWeekBtn(new JButton("Week"));
		setdAgendaBtn(new JButton("Day-Agenda"));
		setwAgendaBtn(new JButton("Week-Agenda"));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(productivityLabel);

		setDateLabel(new Label(""));
		getDateLabel().setSize(230, 50);
		getDateLabel().setAlignment(Label.CENTER);
		add(getDateLabel());
		add(getDayBtn());
		add(getdAgendaBtn());
		add(getWeekBtn());
		add(getwAgendaBtn());
	}

	public Label getDateLabel() {
		return dateLabel;
	}

	public void setDateLabel(Label dateLabel) {
		this.dateLabel = dateLabel;
	}

	public JButton getDayBtn() {
		return dayBtn;
	}

	public void setDayBtn(JButton dayBtn) {
		this.dayBtn = dayBtn;
	}

	public JButton getWeekBtn() {
		return weekBtn;
	}

	public void setWeekBtn(JButton weekBtn) {
		this.weekBtn = weekBtn;
	}

	public JButton getdAgendaBtn() {
		return dAgendaBtn;
	}

	public void setdAgendaBtn(JButton dAgendaBtn) {
		this.dAgendaBtn = dAgendaBtn;
	}

	public JButton getwAgendaBtn() {
		return wAgendaBtn;
	}

	public void setwAgendaBtn(JButton wAgendaBtn) {
		this.wAgendaBtn = wAgendaBtn;
	}

}
