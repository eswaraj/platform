package com.eswaraj.tasks.topology;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.kafka.BrokerHosts;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.SpoutDeclarer;
import backtype.storm.topology.TopologyBuilder;

import com.eswaraj.tasks.util.TopologyRunner;

/**
 * @author Ravi Sharma
 * @data Jul 25, 2014
 */
public class SpringEswarajTopology {

	public final Logger LOG = LoggerFactory.getLogger(this.getClass());

	private BrokerHosts brokerHosts;
	private String name;
	private int numWorkers;
	private int numParallel;
    private int maxSpoutPending;
    private int messageTimeoutSeconds = 30;
	private String kafkaZookeeper;
    private List<EswarajBaseSpout> spoutConfigs;
    private List<EswarajBaseBolt> boltConfigs;
    private Map<String, Object> topologyProperties;

	public SpringEswarajTopology() {
		
	}

	public StormTopology buildTopology() {

		TopologyBuilder builder = new TopologyBuilder();
        // Create a Multiple Tree to print in logs
        for (EswarajBaseSpout oneSpout : spoutConfigs) {
            System.out.println("Building Spout id=[" + oneSpout.getComponentId() + "], output stream = [" + oneSpout.getOutputStream() + "]");
            SpoutDeclarer sd = builder.setSpout(oneSpout.getComponentId(), oneSpout, oneSpout.getParalellism());
            if (oneSpout.getMaxSpoutPending() > 0) {
                sd.setMaxSpoutPending(oneSpout.getMaxSpoutPending());
            }
        }
        BoltDeclarer boltDeclarer;
        for (EswarajBaseBolt oneBolt : boltConfigs) {
            System.out.println("Building Bolt id=[" + oneBolt.getComponentId() + "], Bolt CLass = [" + oneBolt.getClass() + "]");
            boltDeclarer = builder.setBolt(oneBolt.getComponentId(), oneBolt, oneBolt.getParalellism());
            if (oneBolt.getSourceComponentStreams() != null) {
                for (Entry<String, String> oneSourceComponentStream : oneBolt.getSourceComponentStreams().entrySet()) {
                    System.out.println("Shuffling to Spout ID =[" + oneSourceComponentStream.getKey() + "], Spout Stream = [" + oneSourceComponentStream.getValue() + "]");
                    boltDeclarer.shuffleGrouping(oneSourceComponentStream.getKey(), oneSourceComponentStream.getValue());
                }

            }
        }
		return builder.createTopology();
	}


	public void startTopologyRemotely() throws Exception {
        Config conf = new Config();
        conf.setNumWorkers(numWorkers);
        conf.setMaxTaskParallelism(numParallel);
        conf.setMaxSpoutPending(maxSpoutPending);
        // conf.setNumAckers(2);
        conf.setMessageTimeoutSecs(messageTimeoutSeconds);

        if (topologyProperties != null) {
            for (Entry<String, Object> oneProperty : topologyProperties.entrySet()) {
                System.out.println("oneProperty.getKey()=[" + oneProperty.getValue() + "]");
                conf.put(oneProperty.getKey(), oneProperty.getValue());
            }
        }
        System.out.println("messageTimeoutSeconds=[" + messageTimeoutSeconds + "]");
        StormTopology stormTopology = buildTopology();
        StormSubmitter.submitTopology(getName(), conf, stormTopology);
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

    public BrokerHosts getBrokerHosts() {
        return brokerHosts;
    }

    public void setBrokerHosts(BrokerHosts brokerHosts) {
        this.brokerHosts = brokerHosts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumWorkers() {
        return numWorkers;
    }

    public void setNumWorkers(int numWorkers) {
        this.numWorkers = numWorkers;
    }

    public int getNumParallel() {
        return numParallel;
    }

    public void setNumParallel(int numParallel) {
        this.numParallel = numParallel;
    }

    public String getKafkaZookeeper() {
        return kafkaZookeeper;
    }

    public void setKafkaZookeeper(String kafkaZookeeper) {
        this.kafkaZookeeper = kafkaZookeeper;
    }

    public List<EswarajBaseSpout> getSpoutConfigs() {
        return spoutConfigs;
    }

    public void setSpoutConfigs(List<EswarajBaseSpout> spoutConfigs) {
        this.spoutConfigs = spoutConfigs;
    }

    public List<EswarajBaseBolt> getBoltConfigs() {
        return boltConfigs;
    }

    public void setBoltConfigs(List<EswarajBaseBolt> boltConfigs) {
        this.boltConfigs = boltConfigs;
    }

    public int getMaxSpoutPending() {
        return maxSpoutPending;
    }

    public void setMaxSpoutPending(int maxSpoutPending) {
        this.maxSpoutPending = maxSpoutPending;
    }

    public int getMessageTimeoutSeconds() {
        return messageTimeoutSeconds;
    }

    public void setMessageTimeoutSeconds(int messageTimeoutSeconds) {
        this.messageTimeoutSeconds = messageTimeoutSeconds;
    }

    public Map<String, Object> getTopologyProperties() {
        return topologyProperties;
    }

    public void setTopologyProperties(Map<String, Object> topologyProperties) {
        this.topologyProperties = topologyProperties;
    }
}
