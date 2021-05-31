package task_management;

import java.util.List;

import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

public class TaskClaimerImpl implements TaskClaimer{
	
	TaskService taskService;
	
	public TaskClaimerImpl(TaskService taskService) {
		super();
		this.taskService = taskService;
	}

	@Override
	public List<Task> claimTaskByName(String task_name) {
		List<Task> tasks = taskService.createTaskQuery().taskName(task_name).list();
		return tasks;
	}

	@Override
	public Task claimTaskByNameAndProcessId(String task_name, String process_id) {
		Task task = taskService.createTaskQuery().processDefinitionId(process_id).taskName(task_name).singleResult();
		return task;
	}

	@Override
	public List<Task> claimTaskByUser(String user) {
		List<Task> tasks = taskService.createTaskQuery().taskAssignee(user).list();
		return tasks;
	}

}
