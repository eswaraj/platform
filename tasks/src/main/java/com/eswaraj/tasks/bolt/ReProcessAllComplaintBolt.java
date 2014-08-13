package com.eswaraj.tasks.bolt;

import java.util.Iterator;

import org.springframework.data.neo4j.conversion.EndResult;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.tasks.topology.EswarajBaseBolt;

public class ReProcessAllComplaintBolt extends EswarajBaseBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public Result processTuple(Tuple input) {
		try{
            EndResult<Complaint> allComplaints = findAll(Complaint.class);
            Iterator<Complaint> complaintIterator = allComplaints.iterator();
            Complaint oneComplaint;
            logInfo("Reprocessing All Complaints after getting message : " + input.getMessageId());
            while (complaintIterator.hasNext()) {
                oneComplaint = complaintIterator.next();
                logInfo("     oneComplaint : " + oneComplaint);
                writeToStream(input, new Values(oneComplaint.getId()));
            }

            return Result.Success;
		}catch(Exception ex){
            logError("Unable to process", ex);
        }
        return Result.Failed;
	}


}
