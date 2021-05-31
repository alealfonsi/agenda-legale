package service_task_handlers;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import entities.Adempimento;

public class ServiceTaskHandlerCreazionePromemoriaAdempimento implements JavaDelegate{
	
	Calendar date;
	String event_name;
	ArrayList<Adempimento> adempimenti = null;
	Adempimento adempimento;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) {
		
		while(true) {
			try {
				Object obj = execution.getVariable("adempimento");
				if(obj instanceof ArrayList<?>) {
					this.adempimenti = (ArrayList<Adempimento>) obj;
					this.adempimento = adempimenti.get(adempimenti.size() - 1);
				}
				if(obj instanceof Adempimento) {
					this.adempimento = (Adempimento) obj;
				}
				this.date = adempimento.getDate();
				this.event_name = adempimento.getName();
				System.out.println("Adempimento pronto");
				
				int days = (Integer) execution.getVariable("days_promemoria");
				int hour_of_day = (Integer) execution.getVariable("hour_of_day_promemoria");
				date.add(Calendar.DAY_OF_MONTH, -days);
				date.set(Calendar.HOUR_OF_DAY, hour_of_day);
				
				break;
			}
			catch(Exception e) {
				try {
					System.out.println("Adempimento non ancora pronto... [" + execution.getId() + "]");
					Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
					Thread.sleep(500);
					
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
			}
		}
	
		Map<String, Object> variableMap = new HashMap<String, Object>();
		date.add(Calendar.MONTH, -1);
		String duration = DateTimeFormatter.ISO_INSTANT.format(date.toInstant());
		System.out.println(duration);
		variableMap.put("duration", duration);
		variableMap.put("event_name", event_name);
		
		System.out.println("Creazione del promemoria adempimento: " + adempimento.getName());
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getRuntimeService().signalEventReceived("new_promemoria", variableMap);
	}

}
