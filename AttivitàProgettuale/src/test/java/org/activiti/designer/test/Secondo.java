package org.activiti.designer.test;

import java.util.Calendar;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import entities.Udienza;
import udienza.UdienzaTribunale;

public class Secondo implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) {
		UdienzaTribunale udienza = (UdienzaTribunale) execution.getVariable("udienza");
		System.out.println("Ho ricevuto un udienza con anno " + udienza.getDate().get(Calendar.YEAR) + " e data " + udienza.getDate().get(Calendar.DAY_OF_MONTH));
		
	}

}
