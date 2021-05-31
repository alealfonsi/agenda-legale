package user_task_handlers;

import java.util.ArrayList;
import java.util.Calendar;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import calendar.ConcreteCalendarEvent;
import entities.Adempimento;
import entities.CalendarEvent;
import entities.Udienza;
import udienza.ConcreteAdempimento;

public class TaskHandlerCreazioneNuovoAdempimento extends UserTaskHandler{
	
	String name = "Creazione nuovo evento adempimento";
	Task task;
	Udienza udienza;
	Calendar new_date;
	private ArrayList<Adempimento> adempimenti;
	
	@SuppressWarnings("unchecked")
	public void execute(TaskService taskService, RuntimeService runtimeService, HistoryService historyService, String process_definition_id,
			int days, int hour_of_day, String nome_adempimento, String code, String note) throws InterruptedException {
		this.setDefaultTaskHelpers(taskService);
		task = claim(name, process_definition_id);
		
		while(true) {
			try {
				this.udienza = (Udienza) runtimeService.getVariable(task.getExecutionId(), "udienza_util");
				this.new_date = (Calendar) runtimeService.getVariable(task.getExecutionId(), "new_date");
				break;
			}
			catch(Exception e) {
				System.out.println("Udienza util non pronta...");
				Thread.sleep(1000);
			}
		}
		Adempimento adempimento = new ConcreteAdempimento();
		adempimento.setCode(code);
		adempimento.setNote(note);
		String n = nome_adempimento + " per il procedimento " + udienza.getParti();
		adempimento.setName(n);
		CalendarEvent c = new ConcreteCalendarEvent();
		new_date.add(Calendar.DAY_OF_MONTH, -days);
		new_date.set(Calendar.HOUR_OF_DAY, hour_of_day);
		c.setDate(new_date);
		adempimento.setCalendar_event(c);
		
		adempimenti = (ArrayList<Adempimento>) runtimeService.getVariable(task.getExecutionId(), "adempimento");
		adempimenti.add(adempimento);
		runtimeService.setVariable(task.getExecutionId(), "adempimento", adempimenti);
		
		task_finisher.complete(task.getId());
		
	}

}
