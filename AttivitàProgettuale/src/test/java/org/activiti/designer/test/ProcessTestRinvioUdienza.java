package org.activiti.designer.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileInputStream;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import calendar.ConcreteCalendarEvent;
import cliente.PersonaFisica;
import entities.Adempimento;
import entities.Client;
import entities.Procedimento;
import entities.Udienza;
import udienza.ConcreteProcedimento;
import udienza.UdienzaDateComparator;
import udienza.UdienzaTribunale;
import udienza.UdienzaTribunaleFactory;
import user_task_handlers.TaskHandlerAggiungereAdempimento;
import user_task_handlers.TaskHandlerCreazioneNuovoAdempimento;
import user_task_handlers.TaskHandlerImpostarePromemoriaAdempimento;
import user_task_handlers.TaskHandlerTipoRinvio;

public class ProcessTestRinvioUdienza {

	private String filename = "C:\\Users\\aalfo\\eclipse-workspace\\Attivit‡Progettuale\\src\\main\\resources\\diagrams\\RinvioUdienza.bpmn";
	private ProcessInstance processInstance;
	private Boolean rinvio_ufficio;
	private UdienzaTribunale udienza;
	private Calendar new_date;
	private Boolean aggiungere_adempimento;
	private UdienzaDateComparator comparator;
	private Boolean impostare;
	private String adempimento_name = "Notificare teste";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Before
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("rinvioUdienza.bpmn20.xml",
				new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		ArrayList<Adempimento> adempimenti = new ArrayList<Adempimento>();
		variableMap.put("adempimento", adempimenti);
		processInstance = runtimeService.startProcessInstanceByKey("rinvioUdienza", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
		UdienzaTribunaleFactory factory = new UdienzaTribunaleFactory();
		Calendar data = Calendar.getInstance();
		data.set(2021, 5, 17, 16, 0);
		this.udienza = (UdienzaTribunale) factory.createUdienza(data);
		Procedimento procedimento = new ConcreteProcedimento();
		procedimento.setParti("Alfonsi-Mancini");
		udienza.setProcedimento(procedimento);
		Client client = new PersonaFisica();
		client.setName("Alfonsi");
		udienza.setClient(client);
		new_date = Calendar.getInstance();
		new_date.set(2021, 5, 19, 9, 30);
		comparator = new UdienzaDateComparator();
		
		/*
		ManagementService managementService = activitiRule.getManagementService();
		List<Job> jobs = managementService.createJobQuery().orderByJobId().asc().list();
		for(Job job : jobs) {
			try {
				managementService.deleteJob(job.getId());
			}
			catch(Exception e) {
				managementService.executeJob(job.getId());
			}
		}
		*/
	}
	
	
	@Test
	public void loop() throws Exception {
		
		this.adempimento_name = "Notificare teste";
		tipoRinvioFalse();
		creazioneNuovoAdempimento();
		impostarePromemoriaTrue();
		
		Thread.sleep(1000);
		this.adempimento_name = "loooooop baby";
		creazioneNuovoAdempimento();
		impostarePromemoriaTrue();
		
		HistoryService historyService = activitiRule.getHistoryService();
		List<HistoricVariableInstance> vars = historyService.createHistoricVariableInstanceQuery().variableName("adempimento").list();
		ArrayList<Adempimento> adempimenti = (ArrayList<Adempimento>) vars.get(vars.size() - 1).getValue();
		for(Adempimento var : adempimenti) {
			System.out.println(var.getName());
		}
		assert("loooooop baby per il procedimento Alfonsi-Mancini".equals(adempimenti.get(adempimenti.size() - 1).getName()));
		
	}
	
	
	//@Test
	public void impostarePromemoriaTrue() throws InterruptedException {
		//ATTENZIONE: se si abilita il testing decommentando il tag, bisogna anche decommentare la seguente riga!!!
		//aggiungereAdempimentoTrue();
		
		this.impostare = true;
		TaskHandlerImpostarePromemoriaAdempimento task_handler = new TaskHandlerImpostarePromemoriaAdempimento();
		task_handler.execute(processInstance.getProcessDefinitionId(), impostare, activitiRule.getRuntimeService(), activitiRule.getTaskService(),
				2, 15);
		
		HistoryService historyService = activitiRule.getHistoryService();
		List<HistoricVariableInstance> vars = historyService.createHistoricVariableInstanceQuery().variableName("impostare").list();
		assertTrue((Boolean) vars.get(vars.size() - 1).getValue());
		
		vars = historyService.createHistoricVariableInstanceQuery().variableName("days_promemoria").list();
		assert(2 == (Integer) vars.get(vars.size() - 1).getValue());
		
		vars = historyService.createHistoricVariableInstanceQuery().variableName("hour_of_day_promemoria").list();
		assert(15 == (Integer) vars.get(vars.size() - 1).getValue());
		
		/*********************/
		
		ManagementService managementService = activitiRule.getManagementService();
		List<Job> jobs = managementService.createTimerJobQuery().list();
		System.out.println(jobs.get(jobs.size() - 1).getDuedate());
	}
	
	//@Test
	public void impostarePromemoriaFalse() throws InterruptedException {
		aggiungereAdempimentoTrue();
		
		this.impostare = false;
		TaskHandlerImpostarePromemoriaAdempimento task_handler = new TaskHandlerImpostarePromemoriaAdempimento();
		task_handler.execute(processInstance.getProcessDefinitionId(), impostare, activitiRule.getRuntimeService(), activitiRule.getTaskService(),
				15, 0);
		
		HistoryService historyService = activitiRule.getHistoryService();
		List<HistoricVariableInstance> vars = historyService.createHistoricVariableInstanceQuery().variableName("impostare").list();
		assertFalse((Boolean) vars.get(vars.size() - 1).getValue());
	}
	
	//@Test
	public void creazioneNuovoAdempimento() throws InterruptedException {
		aggiungereAdempimentoTrue();
		
		TaskHandlerCreazioneNuovoAdempimento task_handler = new TaskHandlerCreazioneNuovoAdempimento();
		task_handler.execute(activitiRule.getTaskService(), activitiRule.getRuntimeService(), activitiRule.getHistoryService(),
				processInstance.getProcessDefinitionId(), -5, 18, adempimento_name, "123", "adempimento di prova per testing");
		
		HistoryService historyService = activitiRule.getHistoryService();
		List<HistoricVariableInstance> vars = historyService.createHistoricVariableInstanceQuery().variableName("adempimento").list();
		int d1 = new_date.get(Calendar.DAY_OF_MONTH) - 5;
		ArrayList<Adempimento> ade_temp = (ArrayList<Adempimento>) vars.get(vars.size() - 1).getValue();
		int d2 = ade_temp.get(ade_temp.size() - 1).getDate().get(Calendar.DAY_OF_MONTH);
		assert(d1 == d2);
	}
	
	//@Test
	public void tipoRinvioTrue() {
		TaskHandlerTipoRinvio task_handler = new TaskHandlerTipoRinvio();
		
		this.rinvio_ufficio = true;
		
				
		task_handler.execute(activitiRule.getTaskService(), activitiRule.getRuntimeService(), processInstance.getProcessDefinitionId(),
				rinvio_ufficio, udienza, new_date);
		
		/********/
		
		HistoryService historyService = activitiRule.getHistoryService();
		
		List<HistoricVariableInstance> vars = historyService.createHistoricVariableInstanceQuery().variableName("udienza_util").list();
		assert(comparator.compare(udienza, vars.get(vars.size() - 1).getValue()) == 0);
		
		vars = historyService.createHistoricVariableInstanceQuery().variableName("rinvio_ufficio").list();
		assert(rinvio_ufficio == vars.get(vars.size() - 1).getValue());
		
		vars = historyService.createHistoricVariableInstanceQuery().variableName("new_date").list();
		assert(new_date.get(Calendar.DAY_OF_MONTH) == ((Calendar) vars.get(vars.size() - 1).getValue()).get(Calendar.DAY_OF_MONTH));
		
		vars = historyService.createHistoricVariableInstanceQuery().variableName("udienza").list();
		udienza.setDate(new_date);
		assert(comparator.compare(udienza, vars.get(vars.size() - 1).getValue()) == 0);
	}
	
	//@Test
	public void tipoRinvioFalse() throws InterruptedException {
		TaskHandlerTipoRinvio task_handler = new TaskHandlerTipoRinvio();
		
		this.rinvio_ufficio = false;
		
				
		task_handler.execute(activitiRule.getTaskService(), activitiRule.getRuntimeService(), processInstance.getProcessDefinitionId(),
				rinvio_ufficio, udienza, new_date);
		
		/********/
		
		HistoryService historyService = activitiRule.getHistoryService();
		
		List<HistoricVariableInstance> vars = historyService.createHistoricVariableInstanceQuery().variableName("udienza_util").list();
		assert(comparator.compare(udienza, vars.get(vars.size() - 1).getValue()) == 0);
		
		vars = historyService.createHistoricVariableInstanceQuery().variableName("rinvio_ufficio").list();
		assert(rinvio_ufficio == vars.get(vars.size() - 1).getValue());
		
		vars = historyService.createHistoricVariableInstanceQuery().variableName("new_date").list();
		assert(new_date.get(Calendar.DAY_OF_MONTH) == ((Calendar) vars.get(vars.size() - 1).getValue()).get(Calendar.DAY_OF_MONTH));
		
		//ATTENZIONE
		/*
		Thread.sleep(5000);
		vars = historyService.createHistoricVariableInstanceQuery().variableName("udienza").list();
		udienza.setDate(new_date);
		assert(comparator.compare(udienza, vars.get(vars.size() - 1).getValue()) == 0);
		*/
	}
	
	//@Test
	public void aggiungereAdempimentoTrue() throws InterruptedException {
		//ATTENZIONE
		//tipoRinvioFalse();
		this.aggiungere_adempimento = true;
		
		TaskHandlerAggiungereAdempimento task_handler = new TaskHandlerAggiungereAdempimento();
		
		task_handler.execute(activitiRule.getTaskService(), activitiRule.getRuntimeService(),
				processInstance.getProcessDefinitionId(), aggiungere_adempimento);
		
		HistoryService historyService = activitiRule.getHistoryService();
		
		List<HistoricVariableInstance> vars = historyService.createHistoricVariableInstanceQuery().variableName("aggiungere_adempimento").list();
		assertTrue((Boolean) vars.get(vars.size() - 1).getValue());
		
	}
	
	//@Test
	public void aggiungereAdempimentoFalse() throws InterruptedException {
		tipoRinvioFalse();
		this.aggiungere_adempimento = false;
		
		TaskHandlerAggiungereAdempimento task_handler = new TaskHandlerAggiungereAdempimento();
		
		task_handler.execute(activitiRule.getTaskService(), activitiRule.getRuntimeService(),
				processInstance.getProcessDefinitionId(), aggiungere_adempimento);
		
		HistoryService historyService = activitiRule.getHistoryService();
		
		List<HistoricVariableInstance> vars = historyService.createHistoricVariableInstanceQuery().variableName("aggiungere_adempimento").list();
		assertFalse((Boolean) vars.get(vars.size() - 1).getValue());
		
		vars = historyService.createHistoricVariableInstanceQuery().variableName("udienza").list();
		udienza.setDate(new_date);
		assert(comparator.compare(udienza, vars.get(vars.size() - 1).getValue()) == 0);
		
	}
	
}