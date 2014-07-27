package com.eswaraj.core.util;

public interface QueueProducer {

    public abstract void sendMessage(String queueName, String messageBody);

}