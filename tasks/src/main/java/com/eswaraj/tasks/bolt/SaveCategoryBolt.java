package com.eswaraj.tasks.bolt;

import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Tuple;

import com.eswaraj.tasks.topology.EswarajBaseBolt;

public class SaveCategoryBolt extends EswarajBaseBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;
	
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
        try {
            GraphDatabase graphDatabase = new SpringRestGraphDatabase("");
            GraphDatabaseService graphDatabaseService = new RestGraphDatabase("");
            Node ode= graphDatabaseService.getNodeById(123L);

            Neo4jTemplate neo4jTemplate = new Neo4jTemplate(graphDatabaseService);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}

	@Override
	public void execute(Tuple input) {
		try{
			//List<CategoryWithChildCategoryDto> categories = appService.getAllCategories();
			//TODO write these categories to Redis
            collector.ack(input);
		}catch(Exception ex){
			collector.fail(input);
		}

	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}


}
