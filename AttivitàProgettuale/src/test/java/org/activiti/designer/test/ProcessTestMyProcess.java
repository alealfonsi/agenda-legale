package org.activiti.designer.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.activiti.bpmn.model.ExtensionAttribute;
import org.activiti.bpmn.model.Message;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestMyProcess {

	private String filename = "C:\\Users\\aalfo\\eclipse-workspace\\Attivit‡Progettuale\\src\\main\\resources\\diagrams\\CreazionePromemoria.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("myProcess.bpmn20.xml",
				new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		
		Map<String, Object> variableMap = new HashMap<String, Object>();
		Calendar date = Calendar.getInstance();
		date.set(2021, 4, 15, 12, 07);
		String duration = DateTimeFormatter.ISO_INSTANT.format(date.toInstant());
		System.out.println(duration);
		variableMap.put("duration", duration);
		variableMap.put("event_name", "White box unit testing bruuuuh");
		
		runtimeService.signalEventReceived("new_promemoria", variableMap);
		
		long timer_attivi = activitiRule.getManagementService().createTimerJobQuery().count();
		System.out.println("Timer attivi: " + timer_attivi);
		while(timer_attivi > 0) {
			Thread.sleep(5000);
			timer_attivi = activitiRule.getManagementService().createTimerJobQuery().count();
			System.out.println("Timer attivi: " + timer_attivi);
			List<Job> jobs = activitiRule.getManagementService().createTimerJobQuery().orderByExecutionId().asc().list();
			for(Job job : jobs) {
				Date date__ = job.getDuedate();
				System.out.println(date__);
			}
		}
	}
}