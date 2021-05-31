package udienza;

import java.util.Calendar;

import calendar.ConcreteCalendarEvent;
import entities.Udienza;

public class UdienzaGiudiceDiPaceFactory implements UdienzaFactory{

	@Override
	public Udienza createUdienza() {
		Udienza udienza = new UdienzaGiudiceDiPace();
		return udienza;
	}

	@Override
	public Udienza createUdienza(Calendar date) {
		Udienza udienza = new UdienzaGiudiceDiPace(new ConcreteCalendarEvent(), date);
		return udienza;
	}

}
