package entities;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.impl.persistence.entity.Entity;

public abstract class Client implements Serializable{
	
	String name;
	String surname;
	List<String> mail;
	List<String> pec;
	String phone;
	List<Procedimento> procedimenti;
	
	public abstract String getID();
	
	public abstract void setID(String id);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<String> getMail() {
		return mail;
	}

	public void setMail(List<String> mail) {
		this.mail = mail;
	}

	public List<String> getPec() {
		return pec;
	}

	public void setPec(List<String> pec) {
		this.pec = pec;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<Procedimento> getProcedimenti() {
		return procedimenti;
	}

	public void setProcedimenti(List<Procedimento> procedimenti) {
		this.procedimenti = procedimenti;
	}

}
