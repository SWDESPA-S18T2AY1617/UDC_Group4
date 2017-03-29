package doctorView;

public class ToDo extends CalendarItem
{	
	private boolean done;
	
	public ToDo(int year, int month, int day, String event, String color, int shour, int sminute, int ehour, int eminute, boolean done)
	{
		super(year, month, day, event, color, shour, sminute, ehour, eminute);
		this.done = done;
	}
	
	public boolean isDone()
	{
		return done;
	}
	
	public void setDone(boolean d){
		this.done = d;
	}
}