package com.eswaraj.tasks.spout;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import com.eswaraj.tasks.topology.ComplaintTestUtils;
import com.eswaraj.web.dto.ComplaintDto;

public class ComplaintSpout {

    private final String topicName;


    CountDownLatch topologyStartedLatch;
    public CountDownLatch producerFinishedInitialBatchLatch = new CountDownLatch(1);


    Producer<String, String> producer;
    
    private List<ComplaintDto> complaints;

    ComplaintSpout(List<ComplaintDto> complaints, String topicName, CountDownLatch topologyStartedLatch) {
    	this.complaints = complaints;
        this.topicName = topicName;
        this.topologyStartedLatch = topologyStartedLatch;
    }

    public Thread startProducer() {
        Thread sender = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        emitBatch();
                        ComplaintTestUtils.countDown(producerFinishedInitialBatchLatch);
                        ComplaintTestUtils.await(topologyStartedLatch);
                        emitBatch();  // emit second batch after we know topology is up
                    }
                },
                "producerThread"
        );
        sender.start();
        return sender;
    }

    private void emitBatch() {
        Properties props = new Properties();
        props.put("metadata.broker.list", "localhost:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");
        ProducerConfig config = new ProducerConfig(props);
        Producer<String, ComplaintDto> producer = new Producer<String, ComplaintDto>(config);

        for (ComplaintDto complaint : complaints) {
            KeyedMessage<String, ComplaintDto> data = new KeyedMessage<String, ComplaintDto>(topicName, complaint);
            producer.send(data);
        }
        producer.close();

    }
}