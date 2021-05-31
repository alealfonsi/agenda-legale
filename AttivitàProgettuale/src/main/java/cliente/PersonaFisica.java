package cliente;

import java.util.List;

import entities.Client;

public class PersonaFisica extends Client{
	
	private String cf;
	
	public PersonaFisica() {
		super();
	}
	
	public PersonaFisica(String name, String surname, List<String> mail, List<String> pec, String phone,
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
		return cf;
	}

	@Override
	public void setID(String id) {
		this.cf = id;
	}

}
