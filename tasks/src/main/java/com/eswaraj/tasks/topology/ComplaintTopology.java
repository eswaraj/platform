package com.eswaraj.tasks.topology;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;

import com.eswaraj.core.service.impl.ComplaintTopicManager;
import com.eswaraj.tasks.bolt.ComplaintBolt;
import com.eswaraj.web.dto.ComplaintDto;

/**
 * @author anuj
 * @data Jul 7, 2014
 */
public class ComplaintTopology {

	final static int MAX_ALLOWED_TO_RUN_MILLISECS = 1000 * 90 /* seconds */;

	CountDownLatch topologyStartedLatch = new CountDownLatch(1);

	private static int STORM_KAFKA_FROM_READ_FROM_START = -2;
	private static final String COMPLAINT_TOPIC_NAME = "savedComplaint";

	private static final int SECOND = 1000;
	private static List<ComplaintDto> complaints = new ArrayList<ComplaintDto>();

	volatile static boolean finishedCollecting = false;

	private ComplaintTopicManager complaintTopicManager = new ComplaintTopicManager("", COMPLAINT_TOPIC_NAME);
	
	private static final String ZK_HOST = "192.168.1.120";
	private static final String NIMBUS_HOST = "192.168.1.120";

	public static void recordRecievedMessage(ComplaintDto complaint) {
		synchronized (ComplaintTopology.class) {
			complaints.add(complaint);
		}
	}


	public static void main(String[] args) {
		ComplaintTopology topology = new ComplaintTopology();
		topology.complaintTest();
	}

	private void complaintTest() {
		ComplaintTestUtils.checkZkServer(ZK_HOST, 2181, 5 * SECOND);
		try {
			setupComplaintTestTopology(ZK_HOST);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ComplaintTestUtils.countDown(topologyStartedLatch);
			awaitResults();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (AlreadyAliveException e1) {
			e1.printStackTrace();
		} catch (InvalidTopologyException e1) {
			e1.printStackTrace();
		}
		verifyResults();
	}



	private void awaitResults() {
		while (!finishedCollecting) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("after await");
	}

	private void verifyResults() {
		synchronized (ComplaintTopology.class) {
			for (ComplaintDto complaint : complaints) {
				System.out.println("Complaint Saved: " + complaint.getTitle());
			}
		}
	}

	private void setupComplaintTestTopology(String zkHost) throws InterruptedException, AlreadyAliveException, InvalidTopologyException {
		BrokerHosts brokerHosts = new ZkHosts(zkHost);
		
		TopologyBuilder builder = new TopologyBuilder();
		SpoutConfig kafkaConfig = new SpoutConfig(brokerHosts, COMPLAINT_TOPIC_NAME, "", "complaint-test-storm");
		kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
		
		builder.setSpout("kafka-spout",new KafkaSpout(kafkaConfig));
		builder.setBolt("complaint-bolt", new ComplaintBolt()).shuffleGrouping("kafka-spout");

		//Configuration
		Config config = new Config();
		config.setDebug(true);
		config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
		config.put(Config.NIMBUS_HOST, NIMBUS_HOST);
		
		StormSubmitter.submitTopology("complaint-test", config, builder.createTopology());
	}
}
