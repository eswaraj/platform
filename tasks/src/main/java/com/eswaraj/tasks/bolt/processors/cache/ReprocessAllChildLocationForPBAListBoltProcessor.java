package com.eswaraj.tasks.bolt.processors.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.core.service.StormCacheAppServices;
import com.eswaraj.queue.service.QueueService;
import com.eswaraj.tasks.bolt.processors.AbstractBoltProcessor;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.eswaraj.web.dto.LocationDto;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class ReprocessAllChildLocationForPBAListBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private StormCacheAppServices stormCacheAppServices;
    @Autowired
    private AppKeyService appKeyService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private QueueService queueService;
    @Autowired
    private AppService appService;
    @Autowired
    private LocationService locationService;
    private JsonParser jsonParser = new JsonParser();



    @Override
    public Result processTuple(Tuple inputTuple) {
        try{
            String message = (String) inputTuple.getValue(0);
            logInfo("Received Message : {}", message);
            JsonObject jsonObject = (JsonObject) jsonParser.parse(message);
            Long locationId = jsonObject.get("locationId").getAsLong();
            processLocation(inputTuple, locationId);

            return Result.Success;
        }catch(Exception ex){
            logError("Unable to refresh Political Body Admin ", ex);
        }
        return Result.Failed;
    }

    private void processLocation(Tuple inputTuple, Long locationId) throws ApplicationException {
        logInfo("Processing Location : " + locationId);
        writeToStream(inputTuple, new Values(locationId));
        List<LocationDto> locations = locationService.getChildLocationsOfParent(locationId);
        if (locations == null || locations.isEmpty()) {
            return;
        }
        for (LocationDto oneLocation : locations) {
            processLocation(inputTuple, oneLocation.getId());
        }
    }

}
