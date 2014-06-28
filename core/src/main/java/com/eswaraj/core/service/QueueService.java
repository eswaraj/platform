package com.eswaraj.core.service;

import java.io.Serializable;

import com.eswaraj.core.exceptions.ApplicationException;

public interface QueueService<KeyType,ValueType extends Serializable> {

	void sendMessage(String topicName, ValueType value) throws ApplicationException;
	
	void sendMessage(String topicName, KeyType key, ValueType value) throws ApplicationException;
}
