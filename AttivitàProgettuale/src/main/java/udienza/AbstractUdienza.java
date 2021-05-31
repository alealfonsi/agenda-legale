package udienza;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import calendar.ConcreteCalendarEvent;
import entities.Adempimento;
import entities.Client;
import entities.Procedimento;
import entities.CalendarEvent;
import entities.Udienza;

public abstract class AbstractUdienza implements Udienza{
	
	private String provvedimento;
	private String note;
	private String giudice;
	private HashMap<Calendar, Adempimento> adempimenti;
	private Client client;
	private CalendarEvent calendar_event;
	private Procedimento procedimento;

	public CalendarEvent getCalendar_event() {
		return calendar_event;
	}

	public void setCalendar_event(CalendarEvent calendar_event) {
		this.calendar_event = calendar_event;
	}

	@Override
	public void setProvvedimento(String text) {
		this.provvedimento = text;
		
	}

	@Override
	public void setNote(String text) {
		this.note = text;
		
	}

	@Override
	public void modifyGiudice(String giudice_name) {
		this.giudice = giudice_name;
		
	}

	@Override
	public Map<Calendar, Adempimento> getAllAdempimenti() {
		return adempimenti;
	}

	@Override
	public Adempimento getAdempimentoByDate(Calendar data) {
		Adempimento adempimento = adempimenti.get(data);
		return adempimento;
	}
	
	public void setAdempimenti(HashMap<Calendar, Adempimento> adempimenti) {
		this.adempimenti = adempimenti;
	}

	@Override
	public Client getClient() {
		return client;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}

	public String getProvvedimento() {
		return provvedimento;
	}

	public String getNote() {
		return note;
	}

	public String getGiudice() {
		return giudice;
	}
	
	@Override
	public Calendar getDate() {
		return calendar_event.getDate();
	}
	
	@Override
	public void setDate(Calendar date) {
		if(calendar_event == null) {
			calendar_event = new ConcreteCalendarEvent();
		}
		calendar_event.setDate(date);
	}
	
	public String getParti() {
		return procedimento.getParti();
	}
	
	public Procedimento getProcedimento() {
		return procedimento;
	}
	
	public void setProcedimento(Procedimento p) {
		this.procedimento = p;
	}
	
	public void setGiudice(String giudice) {
		this.giudice = giudice;
	}
	
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
