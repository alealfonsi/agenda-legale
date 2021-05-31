package udienza;

import java.util.Calendar;

import calendar.ConcreteCalendarEvent;
import entities.Udienza;

public class UdienzaLavoroFactory implements UdienzaFactory{

	@Override
	public Udienza createUdienza() {
		Udienza udienza = new UdienzaLavoro();
		return udienza;
	}

	@Override
	public Udienza createUdienza(Calendar date) {
		Udienza udienza = new UdienzaLavoro(new ConcreteCalendarEvent(), date);
		return udienza;
	}

	

}
