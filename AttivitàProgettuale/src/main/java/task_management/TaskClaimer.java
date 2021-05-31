package task_management;

import java.util.List;

import org.activiti.engine.task.Task;

public interface TaskClaimer {
	
	public List<Task> claimTaskByName(String task_name);
	
	public Task claimTaskByNameAndProcessId(String task_name, String process_id);
	
	public List<Task> claimTaskByUser(String user);

}
