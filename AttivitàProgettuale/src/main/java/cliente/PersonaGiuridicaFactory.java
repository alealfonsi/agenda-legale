package cliente;

import java.util.List;

import entities.Client;

public class PersonaGiuridicaFactory implements ClientFactory{

	@Override
	public Client createCliente() {
		PersonaGiuridica p = new PersonaGiuridica();
		return p;
	}

	@Override
	public Client createCliente(String name, String surname, List<String> mail, List<String> pec, String phone,
			String id) {
		PersonaGiuridica p = new PersonaGiuridica(name, surname, mail, pec, phone, id);
		return p;
	}

}
