package entities;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

public interface Adempimento extends Serializable{
	
	public void setName(String name);
	
	public String getName();
	
	public void setCalendar_event(CalendarEvent calendar_event);
	
	public CalendarEvent getCalendar_event();
	
	public void setCode(String code);
	
	public String getCode();
	
	public void setNote(String note);
	
	public String getNote();
	
	public Calendar getDate();
	
	public void moveBefore(Calendar when, int days);
	
	public void moveAfter(Calendar when, int days);
	
	public ZonedDateTime getZonedDateTime();

}
