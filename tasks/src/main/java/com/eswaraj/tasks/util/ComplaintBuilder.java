package com.eswaraj.tasks.util;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import com.eswaraj.web.dto.ComplaintDto;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;

public class ComplaintBuilder extends BaseFunction {

		private transient ObjectMapper mapper ;

		private static final long serialVersionUID = 1L;

		public void execute(TridentTuple tuple, TridentCollector collector) {
			ComplaintDto comp = null;
			try {
				comp = mapper.readValue(tuple.getString(0), ComplaintDto.class);
				collector.emit(new Values(comp));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private ObjectMapper getMapper() {
			if (mapper == null) {
				mapper = new ObjectMapper();
			}
			return mapper;
		}

}
