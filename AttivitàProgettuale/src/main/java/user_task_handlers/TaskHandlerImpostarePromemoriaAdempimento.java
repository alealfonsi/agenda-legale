package user_task_handlers;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

public class TaskHandlerImpostarePromemoriaAdempimento extends UserTaskHandler{
	
	String name = "Impostare promemoria per l'adempimento?";
	Task task;
	
	public void execute(String process_id, boolean impostare, RuntimeService runtimeService, TaskService taskService,
			int days, int hour_of_day) throws InterruptedException {
		this.setDefaultTaskHelpers(taskService);
		
		while(true) {
			try {
				task = claim(name, process_id);
				runtimeService.setVariable(task.getExecutionId(), "impostare", impostare);
				if(impostare == true) {
					runtimeService.setVariable(task.getExecutionId(), "days_promemoria", days);
					runtimeService.setVariable(task.getExecutionId(), "hour_of_day_promemoria", hour_of_day);
				}
				break;
			}
			catch (Exception e){
				Thread.sleep(500);
			}
		}
		
		task_finisher.complete(task.getId());
		
	}

}
