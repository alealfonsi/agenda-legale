package udienza;

import java.util.Calendar;

import entities.CalendarEvent;

public class UdienzaLavoro extends AbstractUdienza{
	
	public UdienzaLavoro() {
		super();
	}
	
	public UdienzaLavoro(CalendarEvent calendar_event, Calendar date){
		super();
		setCalendar_event(calendar_event);
		getCalendar_event().setDate(date);
	}

	@Override
	public int getGiorniScadenza() {
		// TODO Auto-generated method stub
		return 5;
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
