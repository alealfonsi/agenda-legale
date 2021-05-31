package gui;

import java.util.Calendar;
import java.util.HashMap;

import entities.Adempimento;
import entities.CalendarEvent;
import entities.Client;
import entities.Procedimento;
import udienza.UdienzaTribunale;
import udienza.UdienzaTribunaleFactory;

public class UdienzaGUI {
	
	private String note;
	private String giudice;
	private Client client;
	private CalendarEvent calendar_event;
	private Procedimento procedimento;
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getGiudice() {
		return giudice;
	}
	public void setGiudice(String giudice) {
		this.giudice = giudice;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public CalendarEvent getCalendar_event() {
		return calendar_event;
	}
	public void setCalendar_event(CalendarEvent calendar_event) {
		this.calendar_event = calendar_event;
	}
	public Procedimento getProcedimento() {
		return procedimento;
	}
	public void setProcedimento(Procedimento procedimento) {
		this.procedimento = procedimento;
	}
	
	public UdienzaTribunale toUdienzaTribunale() {
		UdienzaTribunaleFactory factory = new UdienzaTribunaleFactory();
		UdienzaTribunale udienza_tribunale = (UdienzaTribunale) factory.createUdienza();
		
		udienza_tribunale.setCalendar_event(this.getCalendar_event());
		udienza_tribunale.setClient(this.getClient());
		udienza_tribunale.setGiudice(this.getGiudice());
		udienza_tribunale.setNote(this.getNote());
		udienza_tribunale.setProcedimento(this.getProcedimento());

		return udienza_tribunale;
	}

}
