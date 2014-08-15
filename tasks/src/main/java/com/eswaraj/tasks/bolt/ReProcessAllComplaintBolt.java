package com.eswaraj.tasks.bolt;

import java.util.List;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.tasks.topology.EswarajBaseBolt;
import com.eswaraj.web.dto.ComplaintDto;

public class ReProcessAllComplaintBolt extends EswarajBaseBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public Result processTuple(Tuple input) {
		try{
            Long start = 0L;
            Long pageSize = 100L;
            List<ComplaintDto> complaints;
            while (true) {
                complaints = getComplaintService().getAllComplaints(start, pageSize);
                if (complaints == null || complaints.isEmpty()) {
                    break;
                }
                for (ComplaintDto oneComplaint : complaints) {
                    logInfo("     oneComplaint : " + oneComplaint);
                    if (oneComplaint != null) {
                        writeToStream(input, new Values(oneComplaint.getId()));
                    }
                }
                start = start + complaints.size();
            }
            return Result.Success;
		}catch(Exception ex){
            logError("Unable to process", ex);
        }
        return Result.Failed;
	}


}
