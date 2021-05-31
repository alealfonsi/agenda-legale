package udienza;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import entities.CalendarEvent;
import entities.Procedimento;
import entities.Udienza;

public class ConcreteProcedimento implements Procedimento, Serializable{
	
	private UdienzaFactory udienza_factory;
	private HashMap<Calendar, Udienza> udienze;
	private Udienza no_date;
	String numero_ruolo;
	String parti;
	String autoria_giudiziaria;
	String tipo;
	
	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
		oos.writeObject(this.getUdienza_factory());
		//oos.writeObject(this.getAllUdienze());
		oos.writeObject(this.getNo_date());
		oos.writeObject(this.getNumero_ruolo());
		oos.writeObject(this.getParti());
		oos.writeObject(this.getAutoria_giudiziaria());
		oos.writeObject(this.getTipo());
	}
	
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		UdienzaFactory u = (UdienzaFactory) ois.readObject();
		//HashMap<Calendar, Udienza> udi = (HashMap<Calendar, Udienza>) ois.readObject();
		Udienza u_no_date = (Udienza) ois.readObject();
		String num = (String) ois.readObject();
		String par = (String) ois.readObject();
		String aut = (String) ois.readObject();
		String tip = (String) ois.readObject();
		this.setUdienza_factory(u);
		//this.setUdienze(udi);
		this.setNo_date(u_no_date);
		this.setNumero_ruolo(num);
		this.setParti(par);
		this.setAutoria_giudiziaria(aut);
		this.setTipo(tip);
	}

	public ConcreteProcedimento() {
		super();
	}
	
	public ConcreteProcedimento(UdienzaFactory udienza_factory) {
		super();
		this.udienza_factory = udienza_factory;
	}
	
	public HashMap<Calendar, Udienza> getUdienze() {
		return udienze;
	}

	public void setUdienze(HashMap<Calendar, Udienza> udienze) {
		this.udienze = udienze;
	}

	public String getNumero_ruolo() {
		return numero_ruolo;
	}

	public void setNumero_ruolo(String numero_ruolo) {
		this.numero_ruolo = numero_ruolo;
	}

	public String getAutoria_giudiziaria() {
		return autoria_giudiziaria;
	}

	public void setAutoria_giudiziaria(String autoria_giudiziaria) {
		this.autoria_giudiziaria = autoria_giudiziaria;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getParti() {
		return parti;
	}

	public void setParti(String parti) {
		this.parti = parti;
	}

	@Override
	public Udienza createUdienza() {
		Udienza new_udienza = udienza_factory.createUdienza();
		this.no_date = new_udienza;
		return new_udienza;
	}

	@Override
	public Udienza createUdienza(Calendar date) {
		Udienza new_udienza = udienza_factory.createUdienza(date);
		udienze.put(date, new_udienza);
		return new_udienza;
	}

	@Override
	public Map<Calendar, Udienza> getAllUdienze() {
		return udienze;
	}

	@Override
	public Udienza getUdienzaByDate(Calendar date) {
		return udienze.get(date);
	}
	
	public Udienza getNo_date() {
		return no_date;
	}

	public void setNo_date(Udienza no_date) {
		this.no_date = no_date;
	}

	public UdienzaFactory getUdienza_factory() {
		return udienza_factory;
	}
	
	public void setUdienza_factory(UdienzaFactory udienza_factory) {
		this.udienza_factory = udienza_factory;
	}

	@Override
	public UdienzaFactory getUdienzaFactory() {
		return this.udienza_factory;
	}

	@Override
	public void setUdienzaFactory(UdienzaFactory factory) {
		this.udienza_factory = factory;		
	}
	
	
	
}
