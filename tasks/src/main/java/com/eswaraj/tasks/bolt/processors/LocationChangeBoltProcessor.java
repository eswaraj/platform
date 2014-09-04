package com.eswaraj.tasks.bolt.processors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;

import com.eswaraj.core.service.LocationKeyService;
import com.eswaraj.core.service.StormCacheAppServices;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class LocationChangeBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private LocationKeyService locationKeyService;
    @Autowired
    private StormCacheAppServices stormCacheAppServices;
    private JsonParser jsonParser = new JsonParser();


	@Override
    public Result processTuple(Tuple input) {
		try{
            String message = (String) input.getValue(0);
            JsonObject jsonObject = (JsonObject)jsonParser.parse(message);
            Long locationId = jsonObject.get("LocationId").getAsLong();
            String urlId = jsonObject.get("url").getAsString();
            JsonObject outputJsonObject = stormCacheAppServices.getCompleteLocationInfo(locationId);
            String redisKey = locationKeyService.getLocationInformationKey(locationId);

            String locationInfo = outputJsonObject.toString();
            logInfo("Writing Key {} to redis with Value as {}", redisKey, locationInfo);
            writeToMemoryStoreValue(redisKey, locationInfo);
            writeToMemoryStoreValue(urlId, locationId);//save url v/s location id in redis
            return Result.Success;
		}catch(Exception ex){

        }
        return Result.Failed;
	}

}
