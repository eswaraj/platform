package com.eswaraj.core.service.impl;

import java.util.Date;
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

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.LocationBoundaryFileDto;

public class TestLocationBoundaryTopicManager {
    public static void main(String[] args) throws ApplicationException, InterruptedException {
        try {

            LocationBoundaryTopicManager locationBoundaryTopicManager = new LocationBoundaryTopicManager("localhost:9092", "test");
            LocationBoundaryFileDto locationBoundaryFileDto = new LocationBoundaryFileDto();
            locationBoundaryFileDto.setFileNameAndPath("Test Path");
            locationBoundaryFileDto.setId(10L);
            locationBoundaryFileDto.setLocationId(102L);
            locationBoundaryFileDto.setStatus("Pending");
            locationBoundaryFileDto.setUploadDate(new Date());
            // locationBoundaryTopicManager.sendBoundaryfileMessage(locationBoundaryFileDto);

            StringTopicManager stringTopicManager = new StringTopicManager("localhost:9092", "test");
            stringTopicManager.sendBoundaryfileMessage("Hello Kafka World");

            Thread.sleep(1000);

            ConsumerConfig consumerConfig = createConsumerConfig("localhost:2181", "a_groupId");
            ConsumerConnector consumer = Consumer.createJavaConsumerConnector(consumerConfig);
            Map<String, Integer> topicCountMap = new HashMap<String, Integer>();

            topicCountMap.put("test", new Integer(1));
            System.out.println("topicCountMap : " + topicCountMap);
            Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
            System.out.println("consumerMap : " + consumerMap);
            List<KafkaStream<byte[], byte[]>> streams = consumerMap.get("test");
            System.out.println("streams : " + streams);

            KafkaStream<byte[], byte[]> m_stream = streams.iterator().next();

            System.out.println("m_stream : " + m_stream);

            ConsumerIterator<byte[], byte[]> cit = m_stream.iterator();
            System.out.println("m_stream : " + m_stream);
            while (cit.hasNext()) {
                System.out.println("Thread : " + new String(cit.next().message()));
                LocationBoundaryFileDto locationBoundaryFileDto2 = (LocationBoundaryFileDto) SerializationUtils.deserialize(cit.next().message());
                System.out.println("locationBoundaryFileDto2=" + locationBoundaryFileDto2);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static ConsumerConfig createConsumerConfig(String a_zookeeper, String a_groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", a_zookeeper);
        props.put("group.id", a_groupId);
        props.put("auto.offset.reset", "smallest");
        props.put("zookeeper.session.timeout.ms", "10000");
        
        props.put("fetch.message.max.bytes", "1000000000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.enable", "false");

        return new ConsumerConfig(props);
    }

}
