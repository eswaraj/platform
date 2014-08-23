package com.eswaraj.tasks.bolt.processors;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.core.service.ComplaintService;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.eswaraj.web.dto.ComplaintDto;

@Component
public class ReProcessAllComplaintBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private ComplaintService complaintService;

	@Override
    public Result processTuple(Tuple input) {
		try{
            Long start = 0L;
            Long pageSize = 100L;
            List<ComplaintDto> complaints;
            while (true) {
                complaints = complaintService.getAllComplaints(start, pageSize);
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
