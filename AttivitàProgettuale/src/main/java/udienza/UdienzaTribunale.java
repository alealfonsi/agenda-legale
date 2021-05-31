package udienza;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;

import entities.Adempimento;
import entities.CalendarEvent;
import entities.Client;
import entities.Procedimento;

public class UdienzaTribunale extends AbstractUdienza implements Serializable{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UdienzaTribunale() {
		super();
	}
	
	public UdienzaTribunale(CalendarEvent calendar_event, Calendar date){
		super();
		setCalendar_event(calendar_event);
		getCalendar_event().setDate(date);
	}

	@Override
	public int getGiorniScadenza() {
		return 20;
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
	
	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
		oos.writeObject(this.getCalendar_event());
		//oos.writeObject(this.getAllAdempimenti());
		//oos.writeObject(this.getClient());
		oos.writeObject(this.getGiudice());
		oos.writeObject(this.getProvvedimento());
		oos.writeObject(this.getNote());
		oos.writeObject(this.getProcedimento());
		
	}
	
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		CalendarEvent c = (CalendarEvent) ois.readObject();
		//HashMap<Calendar, Adempimento> adem = (HashMap<Calendar, Adempimento>) ois.readObject();
		//Client cli = (Client) ois.readObject();
		String giu = (String) ois.readObject();
		String prov = (String) ois.readObject();
		String not = (String) ois.readObject();
		ConcreteProcedimento proc = (ConcreteProcedimento) ois.readObject();
		this.setCalendar_event(c);
		//this.setAdempimenti(adem);
		//this.setClient(cli);
		this.setGiudice(giu);
		this.setProvvedimento(prov);
		this.setNote(not);
		this.setProcedimento(proc);
	}

}
