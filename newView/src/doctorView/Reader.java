package doctorView;

import java.util.ArrayList;

public abstract class Reader {

    protected ArrayList<CalendarItem> EVENTS = new ArrayList<CalendarItem>();
    public abstract void Read();
    
    public ArrayList<CalendarItem> getEvents(){
        return EVENTS;
    }

}
