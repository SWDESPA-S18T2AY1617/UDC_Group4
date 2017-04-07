package testClasses;

import java.awt.Color;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Scanner;

import model.Appointment;

public class DateRecurTest {
	Boolean isConflict; 
	int MONTH = 10;
	int YEAR = 2017;
	int DAY = 29;
	int recur = 0;
	int sHour;
	int eHour;
	int sMin;
	int eMin;
	String months[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	
	//Temporarily store the dates ONLY 
	ArrayList<String> appointments = new ArrayList<String>();
	
	ArrayList<Appointment> resApp, //reserved appointments or open appointments, in the system. 
	                       inputApp; //the input appointments that the doctor wishes to be opened.
	int conflictCtr = 0; //just counts the conflict.
	
	//Temporary arraylist to contain the conflcit appointments (Of the input appointment by doctor)
	ArrayList<Appointment> conflictApp = new ArrayList<Appointment>();
	//Temporray arraylist to contain the non-conflicting appointments (Of the input appointment by doctor)
	ArrayList<Appointment> notConflictApp = new ArrayList<Appointment>();
	
	public DateRecurTest() {
		initialize();
		Scanner sc = new Scanner(System.in);
		
		//Temporary asking of inputs, for the input appointment
		System.out.print("Enter Month: ");
		MONTH = sc.nextInt();
		System.out.print("Enter Day: ");
		DAY = sc.nextInt();
		System.out.print("Enter Year: ");
		YEAR = sc.nextInt();
		System.out.println("Time Start(H): ");
		sHour = sc.nextInt();
		System.out.print("Time start(Min): ");
		sMin = sc.nextInt();
		System.out.println("Time end(H): ");
		eHour = sc.nextInt();
		System.out.print("Time end(Min): ");
		eMin = sc.nextInt();
		
		GregorianCalendar date = new GregorianCalendar(YEAR, MONTH, 1); //First date selected
		int dayIntervals = 0;
		
		date = new GregorianCalendar(YEAR, MONTH, 1);
		System.out.println("------------------------------------------------------");
		System.out.println("Month: " + months[MONTH]);
		System.out.println("Year: " + YEAR);
		System.out.println("Day: " + DAY);
		System.out.println("Days of " + months[MONTH] + ": " + date.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
		System.out.println("Time start: " + sHour + ":" + sMin);
		System.out.println("Time end: " + eHour + ":" + eMin);
			
			
		System.out.print("Enter # of weeks: ");
		recur = sc.nextInt();
			
		//Maximum of 4 weeks only.
		//No recurring appointment means no extra appointments to be made.
		if (recur < 0)
			recur = 0;
		else if (recur > 4)
			recur = 4;
		
		
		System.out.println("Recurance: " + recur);
		dayIntervals += recur * 7;
		System.out.println("Recurance (Day intervals): " + dayIntervals);
		
		//Store the first appointment (added by the user).
		appointments.add(months[MONTH] + "/" + DAY + "/" + YEAR);
		inputApp.add(new Appointment(YEAR, MONTH, DAY, "INPUT", "CELER", sHour, sMin, eHour, eMin));
		
		//if the recurring dates is greater than 0, adding of new appointments.
		if(dayIntervals != 0) {
			
			//Adds the recurring appointments to a temporary arraylist which holds all temporary appointments to be added or not.
			for(int index = 1; index <= dayIntervals; index++) {
				DAY++;
					
				//Check if day would go over with the Selected date.
				if(DAY > date.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)) {
				
					//Next month
					if(MONTH == 11) {
						MONTH = 0; //If month is december, reset back to january
						YEAR++; //Increment year
					}
						
					else {
						MONTH++;//Just add normally if its not december
					}
						
					//day reset
					DAY = 1; //Day resets to 1 if its the next month;
				}
					
				//Store appointment
				if(index % 7 == 0) {		
					appointments.add(/*months[MONTH]*/ MONTH + "/" + DAY + "/" + YEAR);
					inputApp.add(new Appointment(YEAR, MONTH, DAY, "INPUT", "CELER", sHour, sMin, eHour, eMin));
						
				}		
			}
		}	
			
		//initialize isConflict as false.
		isConflict = false;
			
		//Checking if there are any conflicts in the given input appointment of the doctor.
		for(int i = 0; i < inputApp.size(); i++) {
				
			for(int j = 0; j < resApp.size(); j++) {	
				//check for conflicts
				checkForConflict(resApp.get(j), inputApp.get(i));
			}
				
			//If there is no conflict add it on the appointments! (For now, just adds it on a temporay arrayList,
			//But we can add this directly to the main reserved appointments used by the system.
			if(!isConflict) {
				//
				notConflictApp.add(inputApp.get(i));
			}
				
			//if there is a conflcit, temporarily store it on an arrayList (for Testing)
			else {
				conflictApp.add(inputApp.get(i));
				isConflict = false;  //reset back to false.
			}
				
		}
		
		
		//Checks the size of the string date appointments (Just for testing)			
		System.out.println("Size of appointments(string): " + appointments.size());
		//Checks the size of the list of the appointments that the doctor wanted to add (Just for testing)
		System.out.println("Size of input appointment: " + inputApp.size());
		
		//Basically prints out the date of the appointments the doctor wants to add(Testing)
		for (String DATE : appointments) {
			System.out.println(DATE);
		}
		
		//Just for checking if it stored the correct appointments.
		System.out.println("\nConflicts: " + conflictCtr);
		for(Appointment app : conflictApp) {
			System.out.println(months[app.getMonth()] + "/" + app.getDay() + "/" + app.getYear());
			System.out.println("Time: " + app.getShour() + ":" + app.getSminute() + " -> "+ app.getEhour() + ":" + app.getEminute());
		}
		
		System.out.println("\nNon conflicts: (Will only show outputs if recurring is >= 1)");
		for(Appointment app : notConflictApp) {
			System.out.println(months[app.getMonth()] + "/" + app.getDay() + "/" + app.getYear());
			System.out.println("Time: " + app.getShour() + ":" + app.getSminute() + " -> "+ app.getEhour() + ":" + app.getEminute());
		}
		
		
	}
	
	/**
	 * This function checks if the appointment added by the doctor has a conflict or not.
	 * It also updates the iConflict variable to true if its a conflicting appointment.
	 * 
	 * @param reserved - the reserved appointment (either open or reserved by a client)
	 * @param input - the new appointment that the doctor wants to open.
	 */
	public void checkForConflict(Appointment reserved, Appointment input) {
		
		//Checks if there is conflict
		if(reserved.checkSameDate(input.getAppointmentDate()) && //<< Checks if the input appointment has the same day as the reserved appointment
		   ( 
			 //Reserverd Appointment's start hour is in between the input appointment.
		     ((reserved.getShour() > input.getShour() && reserved.getShour() < input.getEhour()) || //OR
		     //Rserverd appointments end hour is in between the input appointment.
		     (reserved.getEhour() > input.getShour() && reserved.getEhour() < input.getEhour())) 
		     
		     || //OR
		     
		     //If StartMins & Starthour of reserved is equal with the endMins & endHour of input appointment OR 
		     ((reserved.getShour() == input.getEhour() && reserved.getSminute() == input.getEminute()) || 
		    //endMins & endHour of rserved is equal with the Start mins & end Hour of the input appointment.		 
		      (reserved.getEhour() == input.getShour() && reserved.getEminute() == input.getSminute()) ||
		      reserved.getShour() == input.getShour() || reserved.getEhour() == input.getEhour())
		    
		   )) {	   
				   
			
				conflictCtr++; //Conflict counter
				System.out.println("\nCONFLICT #" + conflictCtr);
				System.out.println("*************************************");
				System.out.println("Reserved appointment: ");
				System.out.println(months[reserved.getMonth()] + "/" + reserved.getDay() + "/" + reserved.getYear());
				System.out.println("Time: " + reserved.getShour() + ":" + reserved.getSminute() + " -> "+ reserved.getEhour() + ":" + reserved.getEminute());
				System.out.println("*************************************\n");
				
				System.out.println("\n-------------------------------------");
				System.out.println("Doctor's 'wanted' appointment: ");
				System.out.println(months[input.getMonth()] + "/" + input.getDay() + "/" + input.getYear());
				System.out.println("Time: " + input.getShour() + ":" + input.getSminute() + " -> "+ input.getEhour() + ":" + input.getEminute());
				System.out.println("-------------------------------------\n");
				
				//Add the conflicts
				isConflict = true;
		}
	}
	
	
	//Initializes the reserverd appointments.
	public void initialize() {

		//month + 1
		resApp = new ArrayList<Appointment>();
		inputApp = new ArrayList<Appointment>();
		
		//April 10, 2017 10:30 -> 12:00
		resApp.add(new Appointment(2017, 3, 10, "SWEDESPA", "COLOR", 10, 30, 12, 00));
		
		//March 27	2017 9:00 -> 10:00
		resApp.add(new Appointment(2017, 2, 27, "TEST", "COLOR", 9, 00, 10, 00));
		
		//
		resApp.add(new Appointment(2017, 0, 1, "TEST2", "COLOR", 10, 30, 12, 00));
		resApp.add(new Appointment(2017, 5, 11, "TEST3", "COLOR", 21, 00, 22, 30));
		
		//Group of May start from may 4 to may 22. Start: 10:30 -> End: 12:00
		resApp.add(new Appointment(2017, 4, 1, "TEST2", "COLOR", 10, 30, 12, 00));
		resApp.add(new Appointment(2017, 4, 8, "TEST2", "COLOR", 10, 30, 12, 00));
		resApp.add(new Appointment(2017, 4, 15, "TEST2", "COLOR", 10, 30, 12, 00));
		resApp.add(new Appointment(2017, 4, 22, "TEST2", "COLOR", 10, 30, 12, 00));
	}
	
	
	public static void main(String[] args) {
		new DateRecurTest();
	}
}
