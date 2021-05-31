package user_task_handlers;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

public class TaskHandlerAggiungereAdempimento extends UserTaskHandler{
	
	Task task;
	String name = "Aggiungere adempimento?";
	
	public void execute(TaskService taskService, RuntimeService runtimeService,
			String process_definition_id, Boolean aggiungere_adempimento) throws InterruptedException {
		this.setDefaultTaskHelpers(taskService);
		while(true) {
			try {
				task = claim(name, process_definition_id);
				runtimeService.setVariable(task.getExecutionId(), "aggiungere_adempimento", aggiungere_adempimento);
				break;
			}
			catch(Exception e) {
				System.out.println("Non sono riuscito a fare il claim del task aggiungere adempimento...");
				Thread.sleep(1000);
			}
			
		}
		
		task_finisher.complete(task.getId());
	}
	
	

}
