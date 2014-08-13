package com.eswaraj.tasks.bolt;

import java.util.List;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.tasks.topology.EswarajBaseBolt;

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
            List<Long> ids;
            while (true) {
                ids = getComplaintService().getAllComplaintIds(start, pageSize);
                if (ids == null || ids.isEmpty()) {
                    break;
                }
                for (long oneId : ids) {
                    logInfo("     oneComplaint : " + oneId);
                    writeToStream(input, new Values(oneId));

                }
                start = start + ids.size();
            }
            return Result.Success;
		}catch(Exception ex){
            logError("Unable to process", ex);
        }
        return Result.Failed;
	}


}
