package service_task_handlers;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class ServiceTaskHandlerNotificaUtente implements JavaDelegate{
	
	String notifica = "Attenzione! Nel giorno selezionato è già presente un'altra udienza. (L'evento è stato comunque creato)";

	@Override
	public void execute(DelegateExecution execution) {
		System.out.println(notifica);
		//TO-DO
	}
	

}
