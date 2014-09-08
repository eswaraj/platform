package com.eswaraj.tasks.bolt.processors;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.core.service.ComplaintService;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.eswaraj.web.dto.ComplaintDto;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class ReProcessAllComplaintBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private ComplaintService complaintService;
    private JsonParser jsonParser;

	@Override
    public Result processTuple(Tuple input) {
		try{
            String jsonMessage = input.getString(0);
            JsonObject jsonObject = getJsonObject(jsonMessage);

            boolean processAllComplaints = false;
            Long locationId = 0L;
            if (jsonObject == null) {
                // Means process all complaints
                processAllComplaints = true;
            } else {
                locationId = jsonObject.get("locationId").getAsLong();
            }
            Long start = 0L;
            Long pageSize = 100L;
            List<ComplaintDto> complaints;
            while (true) {
                if (processAllComplaints) {
                    complaints = complaintService.getAllComplaints(start, pageSize);
                } else {
                    complaints = complaintService.getAllComplaintsOfLocation(locationId, start, pageSize);
                }

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

    private JsonObject getJsonObject(String jsonString) {
        try {
            return (JsonObject) jsonParser.parse(jsonString);
        } catch (Exception ex) {
            logWarning("Not a valid Json : {}", jsonString);
        }
        return null;
    }

}
