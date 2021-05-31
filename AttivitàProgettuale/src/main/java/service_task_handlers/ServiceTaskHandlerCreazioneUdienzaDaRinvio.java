package service_task_handlers;

import java.util.Calendar;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import entities.Udienza;

public class ServiceTaskHandlerCreazioneUdienzaDaRinvio implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) {
		
		Boolean rinvio_ufficio = (Boolean) execution.getVariable("rinvio_ufficio");
		Udienza udienza = (Udienza) execution.getVariable("udienza_util");
		Calendar new_date = (Calendar) execution.getVariable("new_date");
		
		udienza.setDate(new_date);
		execution.setVariable("udienza", udienza);
		
		/*
		if(rinvio_ufficio == true) {
			udienza.setDate(new_date);
			execution.setVariable("udienza", udienza);
		}
		*/
		
	}

}
