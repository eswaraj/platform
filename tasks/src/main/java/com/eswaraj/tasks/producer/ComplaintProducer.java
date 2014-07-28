package com.eswaraj.tasks.producer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.RandomStringUtils;

import com.eswaraj.web.dto.ComplaintDto;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * @author anuj
 * @data Jul 10, 2014
 */

public class ComplaintProducer {
	private static List<ComplaintDto> getComplaints(int number) {
		List<ComplaintDto> complaints = new ArrayList<>();
		for(int i = 0; i<number; i++) {
			ComplaintDto complaint = new ComplaintDto();
			complaint.setId((long) i);
			complaint.setTitle("Complaint".concat(RandomStringUtils.randomAlphabetic(10)));
			complaint.setDescription(RandomStringUtils.randomAlphabetic(15));
			complaint.setCategoryId(1L);
			complaints.add(complaint);
		}
		return complaints;
	}
	
	public static void main(String args[]) throws InterruptedException {
		Properties props = new Properties();
		props.put("metadata.broker.list", "192.168.1.123:9092,192.168.1.124:9092");
		props.put("serializer.class", "com.eswaraj.tasks.producer.ComplaintSerializer");
		props.put("request.required.acks", "1");
		ProducerConfig config = new ProducerConfig(props);
		Producer<String, ComplaintDto> producer = new Producer<String, ComplaintDto>(config);

		while (true) {
			for (ComplaintDto complaint: getComplaints(100)) {
				KeyedMessage<String, ComplaintDto> data = new KeyedMessage<String, ComplaintDto>("savedComplaint", complaint);
				producer.send(data);
				Thread.sleep(100);
			}
			//break;
			Thread.sleep(10000);
		}
	}
}
