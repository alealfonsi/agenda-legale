package task_management;

import org.activiti.engine.TaskService;

public interface TaskHandler {
	
	public void setDefaultTaskHelpers(TaskService taskService);
	
	public void setTaskClaimer(TaskClaimer claimer);
	
	public void setTaskFinisher(TaskFinisher finisher);

}
