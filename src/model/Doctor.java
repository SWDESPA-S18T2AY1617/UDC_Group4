package model;

public class Doctor extends Person
{
	private int secretaryid;
	
	public final static String TABLE_NAME = "doctor";
	public final static String COL_ID = "iddoctor";	
	public final static String COL_SECRETARY = "idsecretary";	
	public final static String COL_ROOM = "room";
	public final static String COL_NAME = "name";
	
	public int getSecretaryid() {
		return secretaryid;
	}
	
	public void setSecretaryid(int secretaryid) {
		this.secretaryid = secretaryid;
	}
	
}
