package com.eswaraj.tasks.bolt.processors.cache;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.cache.PersonCache;
import com.eswaraj.core.service.AppService;
import com.eswaraj.tasks.bolt.processors.AbstractBoltProcessor;
import com.eswaraj.tasks.spout.mesage.SendMobileNotificationMessage;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.eswaraj.web.dto.DeviceDto;
import com.eswaraj.web.dto.device.NotificationMessage;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class RefreshPersonBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private PersonCache personCache;
    @Autowired
    private AppService appService;
    @Override
    public Result processTuple(Tuple inputTuple) throws Exception {
        JsonObject jsonObject = (JsonObject) new JsonParser().parse((String) inputTuple.getValue(0));
        Long personId = jsonObject.get("personId").getAsLong();
        String system = null;
        if (jsonObject.get("system") != null) {
            system = jsonObject.get("system").getAsString();
        }

        logDebug("Got personId : {} to refresh from System {}", personId, system);
        personCache.refreshPerson(personId);
        if ("web".equalsIgnoreCase(system)) {
            // Send message to Mobile
            List<DeviceDto> devices = appService.getAllDevicesForPerson(personId);
            if (devices != null && !devices.isEmpty()) {
                List<String> deviceList = new ArrayList<>();
                for (DeviceDto oneDeviceDto : devices) {
                    if (!StringUtils.isEmpty(oneDeviceDto.getGcmId())) {
                        deviceList.add(oneDeviceDto.getGcmId());
                    }
                }
                JsonObject messageJson = new JsonObject();
                messageJson.addProperty("personId", personId);
                SendMobileNotificationMessage sendMobileNotificationMessage = new SendMobileNotificationMessage(messageJson.toString(), NotificationMessage.USER_UPDATED_ON_WEB, deviceList);
                writeToStream(inputTuple, new Values(sendMobileNotificationMessage));
            }
        }
        return Result.Success;
    }

}
