package service_task_handlers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.history.HistoricVariableInstance;

import entities.Udienza;
import udienza.UdienzaDateComparator;

public class ServiceTaskHandlerControlloSovrapposizione implements JavaDelegate{
	
	HistoryService historyService;

	@Override
	public void execute(DelegateExecution execution) {
		Map<String, ProcessEngine> engines = ProcessEngines.getProcessEngines();
		ProcessEngine engine = engines.values().iterator().next();
		historyService = engine.getHistoryService();
		
		List<HistoricVariableInstance> variables = historyService.createHistoricVariableInstanceQuery().variableName("udienza").list();
		Udienza nuova_udienza = (Udienza) variables.get(variables.size() - 1).getValue();
		variables.remove(variables.size() - 1);
		ArrayList<Udienza> udienze = new ArrayList<Udienza>();
		for(HistoricVariableInstance h : variables) {
			udienze.add((Udienza) h.getValue());
		}
		
		UdienzaDateComparator comparator = new UdienzaDateComparator();
		for(Udienza u : udienze) {
			if(comparator.compare(u, nuova_udienza) == 0) {
				execution.setVariable("libero", false);
				return;
			}
		}
		execution.setVariable("libero", true);
					
	}

}
