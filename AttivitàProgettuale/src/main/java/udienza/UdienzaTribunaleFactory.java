package udienza;

import java.io.Serializable;
import java.util.Calendar;

import calendar.ConcreteCalendarEvent;
import entities.Udienza;

public class UdienzaTribunaleFactory implements UdienzaFactory{

	@Override
	public Udienza createUdienza() {
		Udienza udienza = new UdienzaTribunale();
		return udienza;
	}

	@Override
	public Udienza createUdienza(Calendar data) {
		Udienza udienza = new UdienzaTribunale(new ConcreteCalendarEvent(), data);
		return udienza;
	}

}
