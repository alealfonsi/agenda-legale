package service_task_handlers;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import entities.Udienza;

public class ServiceTaskHandlerCreazionePromemoriaUdienza implements JavaDelegate{

	Calendar date;
	String event_name;

	@Override
	public void execute(DelegateExecution execution) {
		System.out.println("Creazione del promemoria udienza");
		Udienza udienza = (Udienza) execution.getVariable("udienza");
		this.date = udienza.getDate();
		this.event_name = udienza.getParti();
		
		Map<String, Object> variableMap = new HashMap<String, Object>();
		date.add(Calendar.DAY_OF_MONTH, -1);
		date.add(Calendar.MONTH, -1);
		String duration = DateTimeFormatter.ISO_INSTANT.format(date.toInstant());
		System.out.println(duration);
		variableMap.put("duration", duration);
		variableMap.put("event_name", event_name);
		
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getRuntimeService().signalEventReceived("new_promemoria", variableMap);
		
	}

}
