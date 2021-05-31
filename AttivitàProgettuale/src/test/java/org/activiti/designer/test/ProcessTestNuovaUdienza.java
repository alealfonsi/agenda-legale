package org.activiti.designer.test;

import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileInputStream;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.core.annotation.Order;

import calendar.ConcreteCalendarEvent;
import entities.Adempimento;
import entities.Procedimento;
import entities.Udienza;
import service_task_handlers.ServiceTaskHandlerVerificaCostituzione;
import udienza.ConcreteProcedimento;
import udienza.UdienzaDateComparator;
import udienza.UdienzaTribunale;
import udienza.UdienzaTribunaleFactory;
import user_task_handlers.TaskHandlerImpostarePromemoriaAdempimento;
import user_task_handlers.TaskHandlerImpostarePromemoriaUdienza;
import user_task_handlers.TaskHandlerInserimentoDatiUdienza;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProcessTestNuovaUdienza {

	private String filename = "C:\\Users\\aalfo\\eclipse-workspace\\Attivit‡Progettuale\\src\\main\\resources\\diagrams\\NuovaUdienza.bpmn";
	private ProcessInstance processInstance;
	private String deployment_id;

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Before
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		deployment_id = repositoryService.createDeployment().addInputStream("nuovaUdienza.bpmn20.xml",
				new FileInputStream(filename)).deploy().getId();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		this.processInstance = runtimeService.startProcessInstanceByKey("nuovaUdienza", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
		//ProcessEngine processEngine = activitiRule.getProcessEngine();
		//repositoryService.deleteDeployment(deployment_id, true);
	}
	
	@Before
	//@Deployment(resources = {"C:\\Users\\aalfo\\eclipse-workspace\\Attivit‡Progettuale\\src\\main\\resources\\diagrams\\NuovaUdienza.bpmn"})
	public void inserimentoDatiUdienza() throws InterruptedException {
		TaskHandlerInserimentoDatiUdienza task = new TaskHandlerInserimentoDatiUdienza();
		Procedimento procedimento = new ConcreteProcedimento();
		procedimento.setUdienzaFactory(new UdienzaTribunaleFactory());
		UdienzaTribunale udienza = (UdienzaTribunale) procedimento.createUdienza();
		Calendar data = Calendar.getInstance();
		data.set(2015, 6, 26, 11, 45);
		udienza.setCalendar_event(new ConcreteCalendarEvent());
		udienza.setDate(data);
		udienza.setProcedimento(procedimento);
		task.execute(processInstance.getProcessDefinitionId(), udienza, activitiRule.getRuntimeService(), activitiRule.getTaskService());
		
		
		HistoryService historyService = activitiRule.getHistoryService();
		
		//testing user task
		System.out.println(historyService.createHistoricVariableInstanceQuery().variableName("udienza").count() + "#");
		List<HistoricVariableInstance> lette = historyService.
				createHistoricVariableInstanceQuery().
				processInstanceId(processInstance.getId()).
				variableName("udienza").
				list();
		ArrayList<Udienza> udienze_lette = new ArrayList<Udienza>();
		for(HistoricVariableInstance h : lette) {
			udienze_lette.add((Udienza) h.getValue());
		}
		
		System.out.println(udienze_lette.get(0).getDate().get(Calendar.DAY_OF_MONTH));
		
		UdienzaDateComparator comparator = new UdienzaDateComparator();
		assert(comparator.compare(udienze_lette.get(0), udienza) == 0);
		
		//activitiRule.getRepositoryService().deleteDeployment(deployment_id, true);
		
		//testing service task
		List<HistoricVariableInstance> letti = historyService.
				createHistoricVariableInstanceQuery().
				processInstanceId(processInstance.getId()).
				variableName("adempimento").
				list();
		ArrayList<Adempimento> adempimenti_letti = new ArrayList<Adempimento>();
		for(HistoricVariableInstance h : letti) {
			adempimenti_letti.add((Adempimento) h.getValue());
		}
		
		assert(adempimenti_letti.get(0).getDate().get(Calendar.DAY_OF_MONTH) == udienza.getDate().get(Calendar.DAY_OF_MONTH) - 10);
		
		/***********************************************/
		
		TaskHandlerImpostarePromemoriaAdempimento task_2 = new TaskHandlerImpostarePromemoriaAdempimento();
		task_2.execute(processInstance.getProcessDefinitionId(), false, activitiRule.getRuntimeService(), activitiRule.getTaskService(),
				11, 0);
		
		List<HistoricVariableInstance> vars = historyService.createHistoricVariableInstanceQuery().variableName("impostare").list();
		Boolean impostare = (Boolean) vars.get(vars.size() - 1).getValue();
		assert(impostare == false);
		
		vars = historyService.createHistoricVariableInstanceQuery().variableName("libero").list();
		assert((Boolean) vars.get(vars.size() - 1).getValue() == false);
		
	}
	
	/*
	@Before
	public void impostarePromemoriaAdempimento() {
		TaskHandlerImpostarePromemoriaAdempimento task = new TaskHandlerImpostarePromemoriaAdempimento();
		task.execute(processInstance.getProcessDefinitionId(), false, activitiRule.getRuntimeService(), activitiRule.getTaskService());
		
		HistoryService historyService = activitiRule.getHistoryService();
		List<HistoricVariableInstance> vars = historyService.createHistoricVariableInstanceQuery().variableName("impostare").list();
		Boolean impostare = (Boolean) vars.get(vars.size() - 1).getValue();
		assert(impostare == false);
		
		vars = historyService.createHistoricVariableInstanceQuery().variableName("libero").list();
		assert((Boolean) vars.get(vars.size() - 1).getValue() == true);
		
	}
	*/
	
	
	@Test
	public void impostarePromemoriaUdienza() throws InterruptedException {
		TaskHandlerImpostarePromemoriaUdienza task = new TaskHandlerImpostarePromemoriaUdienza();
		task.execute(processInstance.getProcessDefinitionId(), true, activitiRule.getRuntimeService(), activitiRule.getTaskService());
		
		HistoryService historyService = activitiRule.getHistoryService();
		List<HistoricVariableInstance> vars = historyService.createHistoricVariableInstanceQuery().variableName("impostare_prom_udienza").list();
		Boolean var = (Boolean) vars.get(vars.size() - 1).getValue();
		assert(var == true);
		
		System.out.println("Terminato : " + historyService.
		createHistoricProcessInstanceQuery().
		processDefinitionId(processInstance.getProcessDefinitionId()).singleResult().getEndTime());
		
		//assert(processInstance.isEnded());
	}
	
}