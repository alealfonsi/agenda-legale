package cliente;

import java.util.List;

import entities.Client;

public class PersonaFisicaFactory implements ClientFactory{

	@Override
	public Client createCliente() {
		PersonaFisica p = new PersonaFisica();
		return p;
	}

	@Override
	public Client createCliente(String name, String surname, List<String> mail, List<String> pec, String phone,
			String id) {
		PersonaFisica p = new PersonaFisica(name, surname, mail, pec, phone, id);
		return p;
	}

}
