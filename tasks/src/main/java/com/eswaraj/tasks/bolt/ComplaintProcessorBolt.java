package com.eswaraj.tasks.bolt;

import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.core.service.ComplaintService;
import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.tasks.topology.EswarajBaseBolt;

public class ComplaintProcessorBolt extends EswarajBaseBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private ComplaintService complaintService;
    private ClassPathXmlApplicationContext applicationContext;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        super.prepare(stormConf, context, collector);
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("task-spring-context.xml");
        complaintService = applicationContext.getBean(ComplaintService.class);
    }



	@Override
    public Result processTuple(Tuple input) {
		try{
            Long complaintId = null;
            Object value = input.getValue(0);
            if (value instanceof ComplaintMessage) {
                ComplaintMessage complaint = (ComplaintMessage) value;
                complaintId = complaint.getId();
            } else {
                complaintId = (Long) value;
            }

            ComplaintMessage updatedComplaintMessage = complaintService.updateLocationAndAdmins(complaintId);
            writeToStream(input, new Values(updatedComplaintMessage));
            return Result.Success;
		}catch(Exception ex){
            logError("Unable to process", ex);
        }
        return Result.Failed;
	}

    @Override
    public void cleanup() {
        super.cleanup();
        try {
            applicationContext.close();
        } catch (Exception ex) {
            logError("Unable to close application context", ex);
        }

    }

}
