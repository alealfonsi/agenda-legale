package entities;

import java.util.Calendar;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.Entity;

import udienza.UdienzaFactory;

public interface Procedimento {
	
	public Udienza createUdienza();
	
	public Udienza createUdienza(Calendar data);
	
	public Map<Calendar, Udienza> getAllUdienze();
	
	public Udienza getUdienzaByDate(Calendar date);

	public void setParti(String string);
	
	public String getParti();
	
	public UdienzaFactory getUdienzaFactory();
	
	public void setUdienzaFactory(UdienzaFactory factory);

}
