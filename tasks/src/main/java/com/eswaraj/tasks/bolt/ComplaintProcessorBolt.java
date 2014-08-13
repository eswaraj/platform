package com.eswaraj.tasks.bolt;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.tasks.topology.EswarajBaseBolt;

public class ComplaintProcessorBolt extends EswarajBaseBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        super.prepare(stormConf, context, collector);
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

            ComplaintMessage updatedComplaintMessage = getComplaintService().updateLocationAndAdmins(complaintId);
            writeToStream(input, new Values(updatedComplaintMessage));
            return Result.Success;
		}catch(Exception ex){
            logError("Unable to process", ex);
        }
        return Result.Failed;
	}

}
