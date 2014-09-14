package com.eswaraj.api;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.eswaraj.core.service.ComplaintService;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("eswaraj-neo4j-db-context.xml", "eswaraj-core.xml");
        ComplaintService cm = applicationContext.getBean(ComplaintService.class);
        System.out.println("cm = " + cm);

	}

}
