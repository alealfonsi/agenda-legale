package udienza;

import java.io.Serializable;
import java.util.Calendar;

import entities.Udienza;

public interface UdienzaFactory extends Serializable{
	
	public Udienza createUdienza();
	
	public Udienza createUdienza(Calendar date);

}
