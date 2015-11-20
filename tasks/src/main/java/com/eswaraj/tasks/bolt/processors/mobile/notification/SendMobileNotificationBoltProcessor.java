package com.eswaraj.tasks.bolt.processors.mobile.notification;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;

import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.StormCacheAppServices;
import com.eswaraj.tasks.bolt.processors.AbstractBoltProcessor;
import com.eswaraj.tasks.spout.mesage.SendMobileNotificationMessage;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.eswaraj.web.dto.device.NotificationMessage;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Message.Builder;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

@Component
public class SendMobileNotificationBoltProcessor extends AbstractBoltProcessor {

    @Value("${gcm_api_key}")
    private String googleApiKey;
    @Autowired
    private StormCacheAppServices stormCacheAppServices;
    @Autowired
    private AppKeyService appKeyService;
    @Autowired
    private AppService appService;
    @Override
    public Result processTuple(Tuple inputTuple) {
        logInfo("Got message");
        SendMobileNotificationMessage sendMobileNotificationMessage = (SendMobileNotificationMessage) inputTuple.getValue(0);
        String message = sendMobileNotificationMessage.getMessage();
        String messageType = sendMobileNotificationMessage.getMessageType();
        List<String> deviceList = sendMobileNotificationMessage.getDeviceList();
        logInfo("Got message : {}, messageType :{} and deviceList :{}", message, messageType, deviceList);
        try {
            sendMessage(message, messageType, deviceList);
        } catch (Exception ex) {
            logError("Unable to send message to devices ", ex);
        }
        return Result.Success;
    }

    public <T> void sendMessage(String msg, String messageType, List<String> deviceList) throws IOException {
        if (deviceList == null || deviceList.isEmpty()) {
            return;
        }
        Sender sender = new Sender(googleApiKey);
        logInfo("Sending message : {},{}", messageType, msg);

        Builder builder = new Message.Builder();
        builder.addData(NotificationMessage.MESSAGE, msg);
        builder.addData(NotificationMessage.MESSAGE_TYPE, messageType);
        Message message = builder.build();

        // List<String> deviceList = new ArrayList<String>();
        // deviceList.add("APA91bE5AdKI_oOJiSfn031gBzYVHo7V823zDEi0OulfLrBka80WQs3dFTnr1mPhAL_qQ9CmZznLQJRDm27XVNOcC0ppSDmmLvTsMvVimgVUlgEViBSz2ajpJalemp4wf9jcV1Z4WOAoI2xlSaeBq0r6zkFKo-ZRyg");
        MulticastResult result = sender.send(message, deviceList, 5);

        logInfo("Message Send Result = {} ", result);

    }

}
