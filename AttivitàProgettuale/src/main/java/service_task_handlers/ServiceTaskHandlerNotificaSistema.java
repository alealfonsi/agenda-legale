package service_task_handlers;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class ServiceTaskHandlerNotificaSistema implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) {
		System.out.println("Notifica sistema.");
		
	}

}
