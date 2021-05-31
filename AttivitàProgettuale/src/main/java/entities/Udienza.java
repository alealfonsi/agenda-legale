package entities;


import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.Entity;

public interface Udienza extends Entity{
	
	/*
	 * restituisce il numero di giorni entro cui
	 * è necessario adempiere alla
	 * "Verifica costituzione in giudizio controparti"
	 */
	public int getGiorniScadenza();
	
	public void setProvvedimento(String text);
	
	public void setNote(String text);
	
	public void modifyGiudice(String giudice_name);
	
	public Map<Calendar, Adempimento> getAllAdempimenti();
	
	public Adempimento getAdempimentoByDate(Calendar date);
	
	public Client getClient();
	
	public String getParti();
	
	public Calendar getDate();
	
	public void setDate(Calendar date);
	
	public ZonedDateTime getZonedDateTime();

}
