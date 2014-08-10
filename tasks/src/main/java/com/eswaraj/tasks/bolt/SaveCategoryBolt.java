package com.eswaraj.tasks.bolt;

import backtype.storm.tuple.Tuple;

import com.eswaraj.tasks.topology.EswarajBaseBolt;

public class SaveCategoryBolt extends EswarajBaseBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        try {
            GraphDatabase graphDatabase = new SpringRestGraphDatabase("");
            GraphDatabaseService graphDatabaseService = new RestGraphDatabase("");
            Node ode= graphDatabaseService.getNodeById(123L);

            Neo4jTemplate neo4jTemplate = new Neo4jTemplate(graphDatabaseService);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	*/

	@Override
	public void execute(Tuple input) {
		try{
			//List<CategoryWithChildCategoryDto> categories = appService.getAllCategories();
			//TODO write these categories to Redis
            acknowledgeTuple(input);
		}catch(Exception ex){
            failTuple(input);
        }

	}

}
