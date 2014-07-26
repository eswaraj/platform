package com.eswaraj.tasks.spout;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import org.apache.commons.lang.SerializationUtils;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;

import com.eswaraj.tasks.topology.EswarajBaseSpout;
import com.eswaraj.web.dto.LocationBoundaryFileDto;

public class LocationSpout extends EswarajBaseSpout {

	private static final long serialVersionUID = 1L;

	public LocationSpout() {
	}

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		Properties props = new Properties();
        props.put("zookeeper.connect", "a_zookeeper");
        props.put("group.id", "a_groupId");
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        ConsumerConfig consumerConfig =  new ConsumerConfig(props);
        ConsumerConnector consumer = Consumer.createJavaConsumerConnector(consumerConfig);
        
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put("topic", new Integer(1));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get("topic");
        
        KafkaStream<byte[], byte[]> m_stream = streams.iterator().next();
        
        ConsumerIterator<byte[], byte[]> cit = m_stream.iterator();
        while (cit.hasNext()){
            System.out.println("Thread : " + new String(cit.next().message()));
            LocationBoundaryFileDto locationBoundaryFileDto = (LocationBoundaryFileDto)SerializationUtils.deserialize(cit.next().message());
            System.out.println(locationBoundaryFileDto);
        }
        
	}

	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
	    ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return is.readObject();
	}
	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void nextTuple() {
		// TODO Auto-generated method stub

	}

	@Override
	public void ack(Object msgId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fail(Object msgId) {
		// TODO Auto-generated method stub

	}


	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
