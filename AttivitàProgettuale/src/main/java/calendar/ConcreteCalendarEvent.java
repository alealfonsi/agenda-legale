package calendar;

import java.io.Serializable;
import java.util.Calendar;

import entities.CalendarEvent;

public class ConcreteCalendarEvent implements CalendarEvent, Serializable{
	
	private Calendar calendar;
	
	public ConcreteCalendarEvent() {
		super();
		this.calendar = Calendar.getInstance();
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	@Override
	public void setDate(Calendar date) {
		calendar.set(date.get(Calendar.YEAR),
				date.get(Calendar.MONTH),
				date.get(Calendar.DAY_OF_MONTH),
				date.get(Calendar.HOUR_OF_DAY),
				date.get(Calendar.MINUTE));
	}
	
	@Override
	public Calendar getDate() {
		return calendar;
	}

	@Override
	public void moveBefore(Calendar when, int days) {
		when.add(Calendar.DAY_OF_MONTH, 0 - days);
		setCalendar(when);
	}

	@Override
	public void moveAfter(Calendar when, int days) {
		when.add(Calendar.DAY_OF_MONTH, days);
		setCalendar(when);
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isInserted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setInserted(boolean inserted) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isUpdated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setUpdated(boolean updated) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDeleted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDeleted(boolean deleted) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getPersistentState() {
		// TODO Auto-generated method stub
		return null;
	}

}
