package com.eswaraj.tasks.producer;

import java.io.IOException;

import kafka.message.Message;
import kafka.serializer.Decoder;
import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;

import org.codehaus.jackson.map.ObjectMapper;

import com.eswaraj.web.dto.ComplaintDto;

public  class ComplaintSerializer implements Encoder<ComplaintDto>, Decoder<ComplaintDto> {
	
	public ComplaintSerializer(VerifiableProperties vp){}
	
	public ComplaintDto fromBytes(byte[] message) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(message, ComplaintDto.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public byte[] toBytes(ComplaintDto complaint) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsBytes(complaint);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
