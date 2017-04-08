package view;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class UpdateFrameView extends JFrame {

	private UpdateView updateView;

	public UpdateFrameView() {

		setSize(430, 250);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel(null);

		getContentPane().add(panel);

		setUpdateView(new UpdateView());

		panel.add(updateView);

		panel.setBounds(0, 0, 650, 500);

		getUpdateView().setBounds(0, 0, 650, 500);

		setResizable(false);
		setVisible(true);
	}

	public UpdateView getUpdateView() {
		return updateView;
	}

	public void setUpdateView(UpdateView updateView) {
		this.updateView = updateView;
	}
}
