package task_management;

import java.util.Map;

import org.activiti.engine.TaskService;

public class TaskFinisherImpl implements TaskFinisher{
	
	TaskService taskService;
	
	public TaskFinisherImpl(TaskService taskService) {
		super();
		this.taskService = taskService;
	}

	@Override
	public void complete(String task_id) {
		taskService.complete(task_id);		
	}

	@Override
	public void complete(String task_id, Map<String, Object> task_variables) {
		taskService.complete(task_id, task_variables);		
	}

}
