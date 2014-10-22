package com.eswaraj.tasks.bolt.processors.comment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.PersonService;
import com.eswaraj.core.service.StormCacheAppServices;
import com.eswaraj.messaging.dto.CommentSavedMessage;
import com.eswaraj.tasks.bolt.processors.AbstractBoltProcessor;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.eswaraj.web.dto.DeviceDto;
import com.eswaraj.web.dto.PersonDto;
import com.eswaraj.web.dto.PoliticalBodyAdminDto;
import com.eswaraj.web.dto.PoliticalBodyTypeDto;
import com.eswaraj.web.dto.device.NotificationMessage;
import com.google.gson.JsonObject;

@Component
public class CommentSavedBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private StormCacheAppServices stormCacheAppServices;
    @Autowired
    private AppKeyService appKeyService;
    @Autowired
    private AppService appService;
    @Autowired
    private PersonService personService;
    @Override
    public Result processTuple(Tuple inputTuple) {
        CommentSavedMessage commentSavedMessage = (CommentSavedMessage) inputTuple.getValue(0);
        logDebug("Got CommentSavedMessage : {}", commentSavedMessage);
        try {
            // Save Comment to redis
            JsonObject commentJsonObject = stormCacheAppServices.getComment(commentSavedMessage.getCommentId());
            String commentKey = appKeyService.getCommentIdKey(commentSavedMessage.getCommentId());
            String commentListKeyForComplaint = appKeyService.getCommentListIdForComplaintKey(commentSavedMessage.getComplaintId());

            writeToMemoryStoreValue(commentKey, commentJsonObject.toString());
            Long creationTime = commentJsonObject.get("creationTime").getAsLong();
            writeToMemoryStoreSortedSet(commentListKeyForComplaint, commentSavedMessage.getCommentId().toString(), creationTime);

            boolean adminComment = commentJsonObject.get("adminComment").getAsBoolean();
            if (adminComment) {
                String adminOnlyCommentListForComplaint = appKeyService.getAdminCommentListIdForComplaintKey(commentSavedMessage.getComplaintId());
                writeToMemoryStoreSortedSet(adminOnlyCommentListForComplaint, commentSavedMessage.getCommentId().toString(), creationTime);
                // then Send mobile Notifications
                List<DeviceDto> devices = appService.getDevicesForComplaint(commentSavedMessage.getComplaintId());
                if (devices == null || devices.isEmpty()) {
                    logInfo("No devices available for complaint : {}, so not sending any message", commentSavedMessage.getComplaintId());
                    return Result.Success;
                }
                List<String> deviceList = new ArrayList<>();
                for (DeviceDto oneDeviceDto : devices) {
                    if (!StringUtils.isEmpty(oneDeviceDto.getGcmId())) {
                        deviceList.add(oneDeviceDto.getGcmId());
                    }
                }
                PoliticalBodyAdminDto politicalBodyAdminDto = appService.getPoliticalBodyAdminById(commentSavedMessage.getPoliticalAdminId());
                PersonDto person = personService.getPersonById(commentSavedMessage.getPersonId());
                PoliticalBodyTypeDto politicalBodyTypeDto = appService.getPoliticalBodyTypeById(politicalBodyAdminDto.getPoliticalBodyTypeId());
                String message = "A Comment made by " + politicalBodyTypeDto.getShortName() + " - " + person.getName() + " on your complaint";
                if (!person.getId().equals(politicalBodyAdminDto.getPersonId())) {
                    PersonDto politicalPerson = personService.getPersonById(politicalBodyAdminDto.getPersonId());
                    message = "A Comment made by " + person.getName() + " on behalf of " + politicalBodyTypeDto.getShortName() + " - " + politicalPerson.getName() + " on your complaint";
                }

                writeToStream(inputTuple, new Values(message, NotificationMessage.POLITICAL_ADMIN_COMMENTED_MESSAGE_TYPE, deviceList));
            }

        } catch (Exception ex) {
            logError("Unable to send message to devices ", ex);
        }
        return Result.Success;
    }

}
