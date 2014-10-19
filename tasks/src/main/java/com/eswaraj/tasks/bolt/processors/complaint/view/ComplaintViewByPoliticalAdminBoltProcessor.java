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
import com.eswaraj.core.service.StormCacheAppServices;
import com.eswaraj.messaging.dto.ComplaintViewedByPoliticalAdminMessage;
import com.eswaraj.tasks.bolt.processors.AbstractBoltProcessor;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.eswaraj.web.dto.DeviceDto;
import com.eswaraj.web.dto.device.NotificationMessage;

@Component
public class ComplaintViewByPoliticalAdminBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private StormCacheAppServices stormCacheAppServices;
    @Autowired
    private AppKeyService appKeyService;
    @Autowired
    private AppService appService;
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
            String message = "";
            writeToStream(inputTuple, new Values(message, NotificationMessage.POLITICAL_ADMIN_VIEW_MESSAGE_TYPE, deviceList));
        } catch (Exception ex) {
            logError("Unable to send message to devices ", ex);
        }
        return Result.Success;
    }

}
