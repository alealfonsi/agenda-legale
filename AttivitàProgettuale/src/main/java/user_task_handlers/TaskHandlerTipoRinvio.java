package user_task_handlers;

import java.util.Calendar;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import entities.Udienza;

public class TaskHandlerTipoRinvio extends UserTaskHandler{
	
	Task task;
	String name = "Selezionare data e tipo rinvio";
	
	public void execute(TaskService taskService, RuntimeService runtimeService,	String process_definition_id, 
			Boolean rinvio_ufficio,	Udienza udienza, Calendar new_date) {
		
		this.setDefaultTaskHelpers(taskService);
		task = claim(name, process_definition_id);
		runtimeService.setVariable(task.getExecutionId(), "udienza_util", udienza);
		runtimeService.setVariable(task.getExecutionId(), "rinvio_ufficio", rinvio_ufficio);
		runtimeService.setVariable(task.getExecutionId(), "new_date", new_date);
		task_finisher.complete(task.getId());
	}

}
