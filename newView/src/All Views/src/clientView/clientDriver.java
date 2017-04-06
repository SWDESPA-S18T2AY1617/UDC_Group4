package clientView;

public class clientDriver {
	public static void main(String[] args ){
//		new CalendarProgram();
		clientMainView x = new clientMainView();
		x.mainView();
		doctorMainView y = new doctorMainView();
		y.mainView();
		secretaryMainView z = new secretaryMainView();
		z.mainView();
	}
}
