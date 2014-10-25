package com.eswaraj.tasks.bolt.processors.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;

import com.eswaraj.cache.CommentCache;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.PersonService;
import com.eswaraj.core.service.StormCacheAppServices;
import com.eswaraj.tasks.bolt.processors.AbstractBoltProcessor;
import com.eswaraj.tasks.spout.mesage.RefreshCommentMessage;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;

@Component
public class RefreshCommentBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private StormCacheAppServices stormCacheAppServices;
    @Autowired
    private AppKeyService appKeyService;
    @Autowired
    private AppService appService;
    @Autowired
    private PersonService personService;
    @Autowired
    private CommentCache commentCache;
    @Override
    public Result processTuple(Tuple inputTuple) {
        RefreshCommentMessage refreshCommentMessage = (RefreshCommentMessage) inputTuple.getValue(0);
        Long commentId = refreshCommentMessage.getCommentId();
        Long complaintId = refreshCommentMessage.getComplaintId();
        logDebug("Got Comment {} to refresh", commentId);
        try {
            commentCache.refreshComplaintComment(complaintId, commentId);
        } catch (ApplicationException ex) {
            logError("Unable to refresh comment " + commentId + " for compalint " + complaintId, ex);
        }
        return Result.Success;
    }

}
