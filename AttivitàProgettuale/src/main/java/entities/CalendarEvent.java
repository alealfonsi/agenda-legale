package entities;

import java.io.Serializable;
import java.util.Calendar;

import org.activiti.engine.impl.persistence.entity.Entity;

public interface CalendarEvent extends Entity, Serializable{
	
	public void setDate(Calendar date);
	
	public Calendar getDate();
	
	public void moveBefore(Calendar when, int days);
	
	public void moveAfter(Calendar when, int days);
}
