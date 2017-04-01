package doctorView;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TypeView extends JPanel{
	private JCheckBox eventCheckBox;
	private JCheckBox todoCheckBox;
	private final JLabel viewCB = new JLabel("View");
	
	public TypeView()
	{
		setLayout(null);
		setEventCheckBox(new JCheckBox("Event"));
		setTodoCheckBox(new JCheckBox("To-Do"));
		
		add(viewCB);
		add(getEventCheckBox());
		add(getTodoCheckBox());
		
		viewCB.setBounds(10,10,50,30);
		getEventCheckBox().setBounds(10,30,100,30);
		getTodoCheckBox().setBounds(10,60,100,30);
	}

	public JCheckBox getEventCheckBox() {
		return eventCheckBox;
	}

	public void setEventCheckBox(JCheckBox eventCheckBox) {
		this.eventCheckBox = eventCheckBox;
	}

	public JCheckBox getTodoCheckBox() {
		return todoCheckBox;
	}

	public void setTodoCheckBox(JCheckBox todoCheckBox) {
		this.todoCheckBox = todoCheckBox;
	}
}
