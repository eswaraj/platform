package com.eswaraj.tasks.bolt.processors.comment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.core.service.ComplaintService;
import com.eswaraj.core.service.StormCacheAppServices;
import com.eswaraj.tasks.bolt.processors.AbstractBoltProcessor;
import com.eswaraj.tasks.spout.mesage.RefreshCommentMessage;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.eswaraj.web.dto.CommentComplaintDto;

@Component
public class RefreshAllCommentsBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private StormCacheAppServices stormCacheAppServices;
    @Autowired
    private ComplaintService complaintService;

    @Override
    public Result processTuple(Tuple inputTuple) {
        try {
            int page = 0;
            int size = 100;
            while (true) {

                List<CommentComplaintDto> list = complaintService.getCodmplaintComments(page, size);
                if (list.isEmpty()) {
                    break;
                }
                for (CommentComplaintDto oneCommentComplaintDto : list) {
                    RefreshCommentMessage refreshCommentMessage = new RefreshCommentMessage(oneCommentComplaintDto.getCommentId(), oneCommentComplaintDto.getComplaintId());
                    writeToParticularStream(inputTuple, new Values(refreshCommentMessage), "CommentRefreshStream");
                }
                page++;
            }
        } catch (Exception ex) {
            logError("Unable to send message to devices ", ex);
        }
        return Result.Success;
    }

}
