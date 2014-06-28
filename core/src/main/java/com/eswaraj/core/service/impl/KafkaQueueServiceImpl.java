package com.eswaraj.core.service.impl;

import java.io.Serializable;
import java.util.Properties;

import org.apache.commons.lang.SerializationUtils;

import kafka.javaapi.producer.Producer;
import kafka.message.Message;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.QueueService;

public abstract class KafkaQueueServiceImpl<KeyType,ValueType extends Serializable> implements QueueService<KeyType,ValueType> {

	private Producer<KeyType,ValueType> producer;
	
	private ProducerConfig producerConfig;
	
	public KafkaQueueServiceImpl(String kafkaBrokers){
		this(kafkaBrokers, "kafka.serializer.DefaultEncoder", "1", null);
	}

	public KafkaQueueServiceImpl(String kafkaBrokers, String serializerClass, String ack,String partitionerClass){
		Properties props = new Properties();
		props.put("metadata.broker.list", kafkaBrokers);
		props.put("serializer.class", serializerClass);
		if(partitionerClass != null){
			props.put("partitioner.class", partitionerClass);	
		}
		props.put("request.required.acks", ack);
		 
		producerConfig = new ProducerConfig(props);
		
		producer = new Producer<KeyType,ValueType>(producerConfig);
	}
	public void preDestroy(){
		//Close Producer
		producer.close();
	}
	@Override
	public void sendMessage(String topicName, ValueType value) throws ApplicationException {
		KeyedMessage<KeyType, ValueType> message = new KeyedMessage<KeyType, ValueType>(topicName, value);
		producer.send(message);
	}
	@Override
	public void sendMessage(String topicName, KeyType key, ValueType value) throws ApplicationException {
		KeyedMessage<KeyType, ValueType> message = new KeyedMessage<KeyType, ValueType>(topicName, key, value);
		producer.send(message);
	}
}
