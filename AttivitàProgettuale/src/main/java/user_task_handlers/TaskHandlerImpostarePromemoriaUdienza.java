package user_task_handlers;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

public class TaskHandlerImpostarePromemoriaUdienza extends UserTaskHandler{
	
	String name = "Impostare promemoria per l'udienza?";
	Task task;
	
	public void execute(String process_id, boolean impostare_prom_udienza, RuntimeService runtimeService, TaskService taskService) {
		this.setDefaultTaskHelpers(taskService);
		task = claim(name, process_id);
		runtimeService.setVariable(task.getExecutionId(), "impostare_prom_udienza", impostare_prom_udienza);
		//TO_DO
		//*****************************
		//*****************************
		//TO_DO
		task_finisher.complete(task.getId());
		
	}

}
