package designchallenge1;

public class Doctor
{
	public Doctor(String name, int ID)
	{
		this.Name = name;
		this.ID = ID;
	}
	
	public void setSecretary(Secretary secretary)
	{
		this.Secretary = secretary;
	}
	
	public String getName()
	{
		return this.Name;
	}
	
	public int getID()
	{
		return this.ID;
	}
	
	public Secretary getSecretary()
	{
		return this.Secretary;
	}
	
	private final String Name;
	private int ID;
	private Secretary Secretary;
}