package udienza;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

import entities.Adempimento;
import entities.CalendarEvent;

public class ConcreteAdempimento implements Adempimento{
	
	private CalendarEvent calendar_event;
	private String code;
	private String note;
	private String name;
	
	public ConcreteAdempimento() {
		super();
	}
	
	public ConcreteAdempimento(CalendarEvent calendar_event, Calendar date) {
		super();
		this.calendar_event = calendar_event;
		this.calendar_event.setDate(date); 
	}

	public CalendarEvent getCalendar_event() {
		return calendar_event;
	}

	public void setCalendar_event(CalendarEvent calendar_event) {
		this.calendar_event = calendar_event;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public Calendar getDate() {
		return calendar_event.getDate();
	}
	
	@Override
	public void moveBefore(Calendar when, int days) {
		calendar_event.moveBefore(when, days);		
	}

	@Override
	public void moveAfter(Calendar when, int days) {
		calendar_event.moveAfter(when, days);		
	}

	@Override
	public void setName(String name) {
		this.name = name;
		
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public ZonedDateTime getZonedDateTime() {
		ZonedDateTime out = ZonedDateTime.of(
				this.getDate().get(Calendar.YEAR),
				this.getDate().get(Calendar.MONTH),
				this.getDate().get(Calendar.DAY_OF_MONTH),
				this.getDate().get(Calendar.HOUR_OF_DAY),
				this.getDate().get(Calendar.MINUTE),
				0,
				0,
				ZoneId.systemDefault());
		return out;
	}

}
