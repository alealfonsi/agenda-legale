package task_management;

import java.util.Map;

public interface TaskFinisher {
	
	public void complete(String task_id);
	
	public void complete(String task_id, Map<String, Object> task_variables);

}
