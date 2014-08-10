package com.eswaraj.tasks.topology;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import backtype.storm.generated.TopologySummary;
import backtype.storm.utils.NimbusClient;
import backtype.storm.utils.Utils;

public class StormStartup {

	public static void main(String[] args) throws Exception {
		System.out.println("Creating Context");
        System.out.println("Utils.readDefaultConfig()=" + Utils.readDefaultConfig());
        NimbusClient nimbusClient = NimbusClient.getConfiguredClient(Utils.readDefaultConfig());
        List<TopologySummary> topologySummaries = nimbusClient.getClient().getClusterInfo().get_topologies();
        System.out.println("Already Running Toplogies");
        for(TopologySummary oneTopologySummary: topologySummaries){
            System.out.println("Name" + oneTopologySummary.get_name());
            System.out.println("     Id" + oneTopologySummary.get_id());
            System.out.println("     Status" + oneTopologySummary.get_status());
        }

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("storm-topology.xml");
        Map<String, SpringEswarajTopology> allToplogies = applicationContext.getBeansOfType(SpringEswarajTopology.class);
        for (Entry<String, SpringEswarajTopology> oneEntry : allToplogies.entrySet()) {
            try {
                nimbusClient.getClient().killTopology(oneEntry.getValue().getName());
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                // Sleep for 35 seconds
                Thread.sleep(35000);
            }
            oneEntry.getValue().startTopologyRemotely();
        }
        
	}
}
