package user_task_handlers;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import entities.Udienza;

public class TaskHandlerInserimentoDatiUdienza extends UserTaskHandler{
	
	String name = "Inserimento dati udienza";
	Task task;
	String variable_name = "udienza";

	public void execute(String process_definition_id, Udienza udienza, RuntimeService runtimeService, TaskService taskService) {
		
		this.setDefaultTaskHelpers(taskService);
		task = claim(name, process_definition_id);
		runtimeService.setVariable(task.getExecutionId(), variable_name, udienza);
		task_finisher.complete(task.getId());
		
	}
	
}
