package com.eswaraj.tasks.bolt.processors.complaint.view;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.core.service.PersonService;
import com.eswaraj.core.service.StormCacheAppServices;
import com.eswaraj.messaging.dto.ComplaintViewedByPoliticalAdminMessage;
import com.eswaraj.tasks.bolt.processors.AbstractBoltProcessor;
import com.eswaraj.tasks.spout.mesage.SendMobileNotificationMessage;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.eswaraj.web.dto.DeviceDto;
import com.eswaraj.web.dto.LocationDto;
import com.eswaraj.web.dto.PersonDto;
import com.eswaraj.web.dto.PoliticalBodyAdminDto;
import com.eswaraj.web.dto.PoliticalBodyTypeDto;
import com.eswaraj.web.dto.device.NotificationMessage;
import com.google.gson.JsonObject;

@Component
public class ComplaintViewByPoliticalAdminBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private StormCacheAppServices stormCacheAppServices;
    @Autowired
    private AppKeyService appKeyService;
    @Autowired
    private AppService appService;
    @Autowired
    private PersonService personService;
    @Autowired
    private LocationService locationService;
    @Override
    public Result processTuple(Tuple inputTuple) {
        ComplaintViewedByPoliticalAdminMessage complaintViewByPoliticalAdminMessage = (ComplaintViewedByPoliticalAdminMessage) inputTuple.getValue(0);
        logDebug("Got complaintViewByPoliticalAdminMessage : {}", complaintViewByPoliticalAdminMessage);
        try {
            List<DeviceDto> devices = appService.getDevicesForComplaint(complaintViewByPoliticalAdminMessage.getComplaintId());
            if (devices == null || devices.isEmpty()) {
                logInfo("No devices available for complaint : {}, so not sending any message", complaintViewByPoliticalAdminMessage.getComplaintId());
                return Result.Success;
            }
            List<String> deviceList = new ArrayList<>();
            for (DeviceDto oneDeviceDto : devices) {
                if (!StringUtils.isEmpty(oneDeviceDto.getGcmId())) {
                    deviceList.add(oneDeviceDto.getGcmId());
                }
            }
            PoliticalBodyAdminDto politicalBodyAdminDto = appService.getPoliticalBodyAdminById(complaintViewByPoliticalAdminMessage.getPoliticalAdminId());
            PersonDto person = personService.getPersonById(complaintViewByPoliticalAdminMessage.getPersonId());
            PoliticalBodyTypeDto politicalBodyTypeDto = appService.getPoliticalBodyTypeById(politicalBodyAdminDto.getPoliticalBodyTypeId());
            LocationDto location = locationService.getLocationById(politicalBodyAdminDto.getLocationId());
            String message = "Your complaint #" + complaintViewByPoliticalAdminMessage.getComplaintId() + " has been viewed by " + person.getName() + " " + politicalBodyTypeDto.getShortName()
                    + " of " + location.getName();
            PersonDto politicalPerson = person;
            boolean self = true;
            if (!person.getId().equals(politicalBodyAdminDto.getPersonId())) {
                politicalPerson = personService.getPersonById(politicalBodyAdminDto.getPersonId());
                message = "Your complaint #" + complaintViewByPoliticalAdminMessage.getComplaintId() + " has been viewed by " + person.getName() + " on behalf of " + politicalPerson.getName() + "("
                        + politicalBodyTypeDto.getShortName() + " of " + location.getName() + ")";
                self = false;
            }
            JsonObject messageJson = new JsonObject();
            messageJson.addProperty("message", message);
            messageJson.addProperty("complaintId", complaintViewByPoliticalAdminMessage.getComplaintId());
            JsonObject pbaJsonObject = new JsonObject();
            pbaJsonObject.addProperty("id", politicalBodyAdminDto.getId());
            pbaJsonObject.addProperty("name", politicalPerson.getName());
            pbaJsonObject.addProperty("profilePhoto", politicalPerson.getProfilePhoto());
            pbaJsonObject.addProperty("position", politicalBodyTypeDto.getShortName());
            pbaJsonObject.addProperty("self", self);
            messageJson.add("viewedBy", pbaJsonObject);

            SendMobileNotificationMessage sendMobileNotificationMessage = new SendMobileNotificationMessage(messageJson.toString(), NotificationMessage.POLITICAL_ADMIN_VIEW_MESSAGE_TYPE, deviceList);
            writeToParticularStream(inputTuple, new Values(sendMobileNotificationMessage), "SendMobileNotificationStream");
        } catch (Exception ex) {
            logError("Unable to send message to devices ", ex);
        }
        return Result.Success;
    }

}
