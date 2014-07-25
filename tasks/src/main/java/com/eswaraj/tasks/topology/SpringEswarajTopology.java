package com.eswaraj.tasks.topology;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import backtype.storm.Config;
import backtype.storm.generated.StormTopology;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.TopologyBuilder;

import com.eswaraj.tasks.bolt.LoggerBolt;
import com.eswaraj.tasks.bolt.SavedComplaintCounterBolt;
import com.eswaraj.tasks.util.TopologyRunner;

/**
 * @author Ravi Sharma
 * @data Jul 25, 2014
 */
public class SpringEswarajTopology {

	public final Logger LOG = LoggerFactory.getLogger(this.getClass());

	private static final String COMPLAINT_TOPIC_NAME = "savedComplaint";
	private static final String ZK_HOST = "192.168.1.120";

	private BrokerHosts brokerHosts;
	private String name;
	private String nimbusHostIp;
	private int nimbusHostPort;
	private int stormZkPort;
	private int numWorkers;
	private int numParallel;
	private String kafkaZookeeper;
	private List<SpringSpoutConfig> spoutConfigs;
	private List<SpringBoltConfig> boltConfigs;

	public SpringEswarajTopology() {
		
	}

	public StormTopology buildTopology() {
		SpoutConfig kafkaConfig = new SpoutConfig(brokerHosts, COMPLAINT_TOPIC_NAME, "", "complaint-test-storm");
		kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
		TopologyBuilder builder = new TopologyBuilder();
		/*
		builder.setSpout("kafka-spout",new KafkaSpout(kafkaConfig));
		builder.setBolt("complaint-bolt", new LoggerBolt()).shuffleGrouping("kafka-spout");
		builder.setBolt("complaint-counter-bolt", new SavedComplaintCounterBolt()).shuffleGrouping("kafka-spout");
		*/
		
		for(SpringSpoutConfig oneSpoutConfig : spoutConfigs){
			builder.setSpout(oneSpoutConfig.getComponentId(), oneSpoutConfig.getSpout());
		}
		BoltDeclarer boltDeclarer;
		for(SpringBoltConfig oneBoltConfig : boltConfigs){
			boltDeclarer = builder.setBolt(oneBoltConfig.getComponentId(), oneBoltConfig.getBolt());
			for(ComponentStream oneSourceComponentStream : oneBoltConfig.getSourceComponentStreams()){
				boltDeclarer.shuffleGrouping(oneSourceComponentStream.getSourceComponentId(), oneSourceComponentStream.getSourceStream());	
			}
		}
		return builder.createTopology();
	}

	public void startTopologyRemotely() throws Exception {
		brokerHosts = new ZkHosts(kafkaZookeeper);
		Config config = new Config();
		config.put(Config.TOPOLOGY_TRIDENT_BATCH_EMIT_INTERVAL_MILLIS, 2000);
		StormTopology stormTopology = buildTopology();
		LOG.info("Submitting topology to remote cluster");
		
		config.setNumWorkers(numWorkers);
		config.setMaxTaskParallelism(numParallel);
		config.put(Config.NIMBUS_HOST, nimbusHostIp);
		config.put(Config.NIMBUS_THRIFT_PORT, nimbusHostPort);
		config.put(Config.STORM_ZOOKEEPER_PORT, stormZkPort);
		config.put(Config.STORM_ZOOKEEPER_SERVERS, Arrays.asList(nimbusHostIp));
		
		TopologyRunner.runTopologyRemotely(stormTopology, name, config);
	}
	
	public void startTopologyLocally() throws Exception {
		Config config = new Config();
		config.put(Config.TOPOLOGY_TRIDENT_BATCH_EMIT_INTERVAL_MILLIS, 2000);
		StormTopology stormTopology = buildTopology();
		LOG.info("Submitting topology to local cluster");
		config.setNumWorkers(2);
		config.setMaxTaskParallelism(2);
		TopologyRunner.runTopologyLocally(stormTopology, "eswaraj-topology", config);
	}
}
