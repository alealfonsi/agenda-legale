package user_task_handlers;

import java.util.List;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import task_management.TaskClaimer;
import task_management.TaskClaimerImpl;
import task_management.TaskFinisher;
import task_management.TaskFinisherImpl;
import task_management.TaskHandler;

public abstract class UserTaskHandler implements TaskHandler{
	
	TaskClaimer task_claimer;
	TaskFinisher task_finisher;

	@Override
	public void setDefaultTaskHelpers(TaskService taskService) {
		this.task_claimer = new TaskClaimerImpl(taskService);
		this.task_finisher = new TaskFinisherImpl(taskService);
		
	}

	@Override
	public void setTaskClaimer(TaskClaimer claimer) {
		this.task_claimer = claimer;		
	}

	@Override
	public void setTaskFinisher(TaskFinisher finisher) {
		this.task_finisher = finisher;		
	}
	
	public Task claim(String name, String process_id) {
		
		if(process_id != null) {
			return task_claimer.claimTaskByNameAndProcessId(name, process_id);
		}
		else {
			List<Task> task_list = task_claimer.claimTaskByName(name);
			return task_list.get(task_list.size() - 1);
		}
	}

}
