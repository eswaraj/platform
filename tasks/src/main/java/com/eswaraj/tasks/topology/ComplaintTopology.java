package com.eswaraj.tasks.topology;


/**
 * @author anuj
 * @data Jul 7, 2014
 */
public class ComplaintTopology {

/*
	public static final Logger LOG = LoggerFactory.getLogger(ComplaintTopology.class);

	private static final String COMPLAINT_TOPIC_NAME = "savedComplaint";
	private static final String ZK_HOST = "192.168.1.120";

	private final BrokerHosts brokerHosts;

	public ComplaintTopology(String kafkaZookeeper) {
		brokerHosts = new ZkHosts(kafkaZookeeper);
	}

	public StormTopology buildTopology() {
		SpoutConfig kafkaConfig = new SpoutConfig(brokerHosts, COMPLAINT_TOPIC_NAME, "", "complaint-test-storm");
		kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("kafka-spout",new KafkaSpout(kafkaConfig));
		builder.setBolt("complaint-bolt", new LoggerBolt()).shuffleGrouping("kafka-spout");
		builder.setBolt("complaint-counter-bolt", new SavedComplaintCounterBolt()).shuffleGrouping("kafka-spout");
		return builder.createTopology();
	}

	public static void main(String[] args) throws Exception {
		ComplaintTopology complaintTopology = new ComplaintTopology(ZK_HOST);
		Config config = new Config();
		config.put(Config.TOPOLOGY_TRIDENT_BATCH_EMIT_INTERVAL_MILLIS, 2000);
		StormTopology stormTopology = complaintTopology.buildTopology();
		if (args != null && args.length > 1) {
			LOG.info("Submitting topology to remote cluster");
			String name = args[1];
			String nimbusHostIp = args[2];
			int nimbusHostPort = Integer.valueOf(args[3]);
			int stormZkPort = Integer.valueOf(args[4]);
			int numWorkers = Integer.valueOf(args[5]);
			int numParallel = Integer.valueOf(args[6]);
			
			config.setNumWorkers(numWorkers);
			config.setMaxTaskParallelism(numParallel);
			config.put(Config.NIMBUS_HOST, nimbusHostIp);
			config.put(Config.NIMBUS_THRIFT_PORT, nimbusHostPort);
			config.put(Config.STORM_ZOOKEEPER_PORT, stormZkPort);
			config.put(Config.STORM_ZOOKEEPER_SERVERS, Arrays.asList(nimbusHostIp));
			
			TopologyRunner.runTopologyRemotely(stormTopology, name, config);
		} else {
			LOG.info("Submitting topology to local cluster");
			config.setNumWorkers(2);
			config.setMaxTaskParallelism(2);
			
			TopologyRunner.runTopologyLocally(stormTopology, "complaint-test-topology", config);
		}
	}
	*/
}
