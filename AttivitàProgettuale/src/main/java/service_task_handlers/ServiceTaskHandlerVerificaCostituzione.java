package service_task_handlers;

import java.util.Calendar;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import calendar.ConcreteCalendarEvent;
import entities.Udienza;
import udienza.ConcreteAdempimento;
import udienza.UdienzaTribunale;

public class ServiceTaskHandlerVerificaCostituzione implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) {
		Udienza udienza = (Udienza) execution.getVariable("udienza");	
		int days = udienza.getGiorniScadenza();
		
		Calendar data_udienza = udienza.getDate();
		ConcreteAdempimento adempimento = new ConcreteAdempimento();
		Calendar data_adempimento = Calendar.getInstance();
		data_adempimento.set(data_udienza.get(Calendar.YEAR),
				data_udienza.get(Calendar.MONTH),
				data_udienza.get(Calendar.DAY_OF_MONTH),
				data_udienza.get(Calendar.HOUR_OF_DAY),
				data_udienza.get(Calendar.MINUTE));
		data_adempimento.add(Calendar.DAY_OF_MONTH, -days);
		ConcreteCalendarEvent c = new ConcreteCalendarEvent();
		c.setCalendar(data_adempimento);
		adempimento.setCalendar_event(c);
		adempimento.setName("Verifica costituzione in giudizio per procedimento: " + udienza.getParti());
		
		execution.setVariable("adempimento", adempimento);
	}

}
