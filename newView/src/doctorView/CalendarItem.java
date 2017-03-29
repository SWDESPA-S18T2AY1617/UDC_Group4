package doctorView;

import java.awt.Color;

public class CalendarItem{

	private final int day;
    private final int year;
    private final int month;
    private final String event;
    private String color;
	private final int shour;
	private final int sminute;
	private final int ehour;
	private final int eminute;
    private int srow;
    private int erow;
    private int col;

    public CalendarItem(int year, int month, int day, String event, String color, int shour, int sminute, int ehour, int eminute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.event = event;
        this.color = color;
		this.shour = shour;
		this.sminute = sminute;
		this.ehour = ehour;
		this.eminute = eminute;
        this.setStartRow();
        this.setEndRow();
        this.setCol(1);
    }

    public String getEvent() {
        return event;
    }

    public Color getColor() {
        switch (color.toUpperCase()) {
            case "GREEN":
                return Color.GREEN;
            case "RED":
                return Color.RED;
            case "BLUE":
                return Color.BLUE;
            default:
                return Color.ORANGE;
        }
    }
    
    public void setColor(String color){
    	this.color = color;
    }
    
    public String getColorName(){
    	return color.toUpperCase();
    }
    
    public boolean checkYearMonth(int year, int month) {
        return this.getYear() == year && this.getMonth() == month;
    }

    public boolean checkSameDate(int year, int month, int day) {
        return this.getYear() == year && this.getMonth() == month && this.getDay() == day;
    }

	public int getDay() {
		return day;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getShour() {
		return shour;
	}

	public int getSminute() {
		return sminute;
	}

	public int getEhour() {
		return ehour;
	}

	public int getEminute() {
		return eminute;
	}

	public int getStartRow() {
		return srow;
	}

	public void setStartRow() {
		this.srow = shour * 2;
		if(sminute == 30)
			this.srow++;
	}
	
	public int getEndRow() {
		return erow;
	}

	public void setEndRow() {
		this.erow = ehour * 2;
		if(eminute == 30)
			this.erow++;
	}
	
	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
	public String toString(){
		String a = month + "/" + day + "/" + year + " "+ event + " " + shour + ":" + sminute + " - " + ehour + ":" + eminute;
		return a;
	}
   
}
