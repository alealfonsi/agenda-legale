package cliente;

import java.util.List;

import entities.Client;

public interface ClientFactory {
	
	public Client createCliente();
	
	public Client createCliente(String name, String surname, List<String> mail, List<String> pec, String phone, String id);

}
