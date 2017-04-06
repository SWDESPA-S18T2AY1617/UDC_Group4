package view;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader extends Reader {

    private final String csvFile = "Test2.csv";
    private BufferedReader br = null;
    private String line = "";
    private final String SplitByDate = "/";
    private final String SplitByComma = ", ";
    private final String SplitByColon = ": ";

    @Override
    public void Read() {
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
				boolean value = true;//for to do 
                String[] details = line.split(SplitByComma); //splits the string by comma
                String[] date = details[1].split(SplitByDate); //m,d,y
                String[] stime = details[4].split(SplitByColon);//split start time
            	String[] etime = details[5].split(SplitByColon);//split end time
				 
				if(details[0].equals("event")){
					CalendarItem temp = new CalendarItem(Integer.parseInt(date[2]), Integer.parseInt(date[0]) , Integer.parseInt(date[1]), details[2], details[3],Integer.parseInt(stime[0]),Integer.parseInt(stime[1]),Integer.parseInt(etime[0]),Integer.parseInt(etime[1])); //y,m,d,e,c
	                EVENTS.add(temp);
				}
				else{
					
					if(details[0].equals("to do")){
						System.out.println(details[6]);
						if(details[6].equals("yes"))
							value = true;
						else 
							value = false;
						ToDo temp2 = new ToDo(Integer.parseInt(date[2]), Integer.parseInt(date[0]), Integer.parseInt(date[1]), details[2], details[3],Integer.parseInt(stime[0]),Integer.parseInt(stime[1]),Integer.parseInt(etime[0]),Integer.parseInt(etime[1]),value);
						EVENTS.add(temp2);
					}
				}
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {

                }
            }
        }
    }
}
