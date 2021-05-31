package cliente;

import java.util.List;

import entities.Client;

public class PersonaGiuridica extends Client{
	
	private String p_iva;
	
	public PersonaGiuridica() {
		super();
	}
	
	public PersonaGiuridica(String name, String surname, List<String> mail, List<String> pec, String phone,
			String id) {
		super();
		setName(name);
		setSurname(surname);
		setMail(mail);
		setPec(pec);
		setPhone(phone);
		setID(id);
	}

	@Override
	public String getID() {
		return p_iva;
	}

	@Override
	public void setID(String id) {
		this.p_iva = id;
	}

}
