package com.eswaraj.tasks.topology;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StormStartup {

	public static void main(String[] args) throws Exception {
		System.out.println("Creating Context");
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("storm-topology.xml");
        Map<String, SpringEswarajTopology> allToplogies = applicationContext.getBeansOfType(SpringEswarajTopology.class);
        for (Entry<String, SpringEswarajTopology> oneEntry : allToplogies.entrySet()) {
            oneEntry.getValue().startTopologyRemotely();
        }

	}
}
